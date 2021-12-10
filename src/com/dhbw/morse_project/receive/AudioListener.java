package com.dhbw.morse_project.receive;

import com.dhbw.morse_project.gui.GUI;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * In this class the input of the microphone is captured into a buffer.
 * This buffer is then split into windowed-buffers with overlap (see {@link #analyzeBuffer(byte[])}).
 * For each windowed-buffer the RMS-Value is calculated and a new {@link Noise}-Object is generated. (also see {@link #calculateRMSValue(byte[])}).
 * That means the software recognizes Morse-Signals based on the RMS-Value (volume).
 * Evaluated are these Objets in the {@link Decoder} class.
 * <p>
 * ([ID: F-TEC-10.1.1, (F-LOG-20.3.2, F-LOG-20.3.3)])
 *
 * @author Daniel Czeschner, Supported by: Mark Mühlenberg
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class AudioListener {

    /**
     * Holds the current TargetDataLine from which this class reads the audio input.
     *
     * @see TargetDataLine
     */
    private TargetDataLine line = null;

    /**
     * Is the {@link AudioListener} currently recording or not.
     */
    private boolean isListening = false;

    /**
     * The size of the buffer-array in which the data line input is read.
     */
    private final int BUFFER_SIZE = 1000;

    /**
     * The size of one windowed-buffer.
     */
    private final int WINDOW_SIZE = BUFFER_SIZE / 5;

    /**
     * The size of the overlapped values between the windowed-buffer's.
     */
    private final int STEP_SIZE = WINDOW_SIZE / 2;

    /**
     * The index for the next {@link Noise} object.
     */
    private int buffersRead = 0;

    /**
     * The minimum amount of new {@link Noise}-Objects in the {@link #SYNCHRONIZED_BUFFER} before the {@link Decoder} is notified that there are new samples ({@link Noise}-Objets) to check.
     */
    private final int MIN_NEW_SAMPLES = 15;

    /**
     * Smooth factor for rms-values. This value defines the multiplier of how much the values of a list of rms values should be smooth depending on the average of this list.
     */
    private final float RMS_SMOOTH_AMOUNT = 0.85f;

    /**
     * In this List the created {@link Noise}-Objects are added.
     * This List is also used to synchronize the {@link Decoder}- and {@link #listenerThread}-Thread.
     * @see #analyzeBuffer(byte[])
     */
    private final List<Noise> SYNCHRONIZED_BUFFER = new ArrayList<>();

    /**
     * Thread for the audio input from {@link #line} in which everything is handled.
     * @see #listenerRunnable
     */
    private Thread listenerThread;

    /**
     * The GUI {@link GUI} instance
     */
    private final GUI gui;

    /**
     * Constructor
     * @param gui The {@link GUI} instance.
     */
    public AudioListener(GUI gui){
        this.gui = gui;
    }

    /**
     * Method to start capturing the microphone input.
     * The {@link #line} is initialized here with the default microphone of the computer on which this program is running.
     *
     * @return True if the listener started successfully.
     */
    public boolean startListening() {
        AudioFormat format = new AudioFormat(44000f, 16, 1, true, true); //Default Line
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);   // [ID: F-TEC-10.2.1]

        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("DataLine not available.");
            return false;
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return false;
        }

        isListening = true;

        //Reset Buffer
        SYNCHRONIZED_BUFFER.clear();

        listenerThread = new Thread(listenerRunnable); //Creating new Thread because you can only call .start on Thread once

        listenerThread.start();

        return isListening;
    }

    /**
     * Returns the value of the variable {@link #isListening}.
     *
     * @return True, if the AudioListener is currently recording.
     */
    public boolean isListening() {
        return isListening;
    }

    /**
     * This method stops the capturing of the microphone input.
     * It waits until the last run of the {@link #listenerThread}-Thread finished.
     *
     */
    public void stopListening() {
        isListening = false;        //Set boolean to false so that the loop finishes cleanly
        try {
            listenerThread.join();  //Join to wait for the listenerThread to finish.
            line.stop();
            line.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * The Runnable for the {@link #listenerThread}-Thread.
     * In this Thread/Runnable everything to generate new {@link Noise}-Objets and fill the {@link #SYNCHRONIZED_BUFFER} is done here.
     * The notification of the {@link Decoder} is also handheld here.
     *
     * @see #SYNCHRONIZED_BUFFER
     * @see #analyzeBuffer(byte[])
     * @see Decoder
     */
    private final Runnable listenerRunnable = () -> {
        byte[] memoryBuffer = new byte[BUFFER_SIZE];

        synchronized (SYNCHRONIZED_BUFFER) {
            try {
                SYNCHRONIZED_BUFFER.wait(1000);   //We wait 1 sec before we start listening to skip user mouse clicks, etc..
            } catch (InterruptedException e) {
                e.printStackTrace();    //This error is never going to happen, because we don't use interrupt() on this Thread. We need to catch it anyway.
            }
        }

        while (isListening) {
            if (line.read(memoryBuffer, 0, memoryBuffer.length) > 0) {
                synchronized (SYNCHRONIZED_BUFFER) {
                    SYNCHRONIZED_BUFFER.addAll(analyzeBuffer(memoryBuffer));

                    if (SYNCHRONIZED_BUFFER.size() >= MIN_NEW_SAMPLES) {
                        SYNCHRONIZED_BUFFER.notify();
                    }
                }
            }
        }
    };

    /**
     * This method is using windowed-buffers to go through the buffer-byte-array with x (see {@link #STEP_SIZE}) overlapping values.
     * Based on the values in each windowed-buffer an RMS-Value is calculated and smoothed out.
     * Then for each smoothed RMS-Value a new {@link Noise}-Object is created.
     * <p>
     * Part of: [ID: F-TEC-10.1.1] (Other in {@link Decoder})
     *
     * @param byteArray buffer with the recorded input from the microphone.
     * @return An ArrayList with the new {@link Noise}-Objects (Samples).
     * @see #calculateRMSValue(byte[])
     * @see #smoothRMSValues(List, float)
     */
    private List<Noise> analyzeBuffer(byte[] byteArray) {
        List<Noise> noiseList = new ArrayList<>();
        List<Double> rmsBuffer = new ArrayList<>();

        byte[] windowedBuffer = new byte[WINDOW_SIZE];

        //Windowed-Buffer creation and rms calculation
        for (int i = 0; i <= BUFFER_SIZE - WINDOW_SIZE; i += STEP_SIZE) {
            for (int j = 0, s = 0; j < WINDOW_SIZE; j++, s++) {
                windowedBuffer[s] = byteArray[j + i];
            }
            double rms = calculateRMSValue(windowedBuffer);

            rmsBuffer.add(rms);

        }

        List<Double> smoothed = smoothRMSValues(rmsBuffer, RMS_SMOOTH_AMOUNT); //Smooth the RMS-Values

        for (Double smoothedRMS : smoothed) {
            boolean quiet = smoothedRMS < getNoiseThreshold();
            Noise noise = new Noise(quiet, buffersRead++);
            noiseList.add(noise);
        }

        return noiseList;
    }

    /**
     * Methode that calculates the "Root Mean Square" of the input byte array.
     * See <a href="https://en.wikipedia.org/wiki/Root_mean_square">Root Mean Square on Wikipedia</a>
     *
     * @param byteArray The array from which the RMS should be calculated.
     * @return The RMS Value of the input byte array
     * @see #analyzeBuffer(byte[])
     */
    private double calculateRMSValue(byte[] byteArray) {
        if (byteArray.length == 0)
            return 0.0;

        double rms = 0.0;

        for (int i : byteArray) {
            rms += i * i;
        }

        rms /= byteArray.length;

        return Math.sqrt(rms);
    }

    /**
     * This Method is smoothing out the RMS-Values.
     * Based on <a href="https://stackoverflow.com/questions/32788836/smoothing-out-values-of-an-array">Stackoverflow question</a>
     *
     * @param rmsBuffer List with the RMS-values to smooth.
     * @param variance  Defines how much the values should be smoothed.
     * @return The smoothed RMS-Values in an ArrayList.
     */
    private List<Double> smoothRMSValues(List<Double> rmsBuffer, float variance) {
        double weighted = getListAverage(rmsBuffer) * variance;
        List<Double> smoothed = new ArrayList<>();

        for (int i = 0; i < rmsBuffer.size(); i++) {
            double prev = i > 0 ? smoothed.get(i - 1) : rmsBuffer.get(i);
            double next = rmsBuffer.get(i);

            smoothed.add(getListAverage(Arrays.asList(weighted, prev, next, next)));
        }

        return smoothed;
    }

    /**
     * Method to calculate the average (avg) value of a list.
     *
     * @param list List from which the avg should be calculated.
     * @return The average value
     */
    private double getListAverage(List<Double> list) {
        float sum = 0;

        for (double value : list) {
            sum += value;
        }

        return (sum / list.size());
    }

    /**
     * Method to fetch the latest {@link Noise}-Objects from the {@link #SYNCHRONIZED_BUFFER}-Buffer and clears this buffer after it.
     *
     * @return The latest {@link Noise}-Objets
     * @see #analyzeBuffer(byte[])
     */
    public List<Noise> getNewSamples() {
        synchronized (SYNCHRONIZED_BUFFER) {
            List<Noise> back = new ArrayList<>(SYNCHRONIZED_BUFFER);
            SYNCHRONIZED_BUFFER.clear();
            return back;
        }
    }

    /**
     * Get the current Threshold selected by the user from the GUI.
     * If a tone is louder than this threshold the value is identified as loud.
     *
     * @return The threshold
     */
    private double getNoiseThreshold() {
        return this.gui.getNoiseThreshold();
    }

    /**
     * Classic getter
     *
     * @return {@link #SYNCHRONIZED_BUFFER}
     */
    public List<Noise> getSynchronizedBuffer() {
        return SYNCHRONIZED_BUFFER;
    }
}
