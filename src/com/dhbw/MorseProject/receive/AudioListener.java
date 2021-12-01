package com.dhbw.MorseProject.receive;

import javax.sound.sampled.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * In this class the input of the microphone is captured and the rms values of this input can be fetched.
 * @author Daniel Czeschner, Supported by: Mark Mühlenberg
 */
public class AudioListener {

    /**
     * Holds the current TargetDataLine from which this class reads the audio input.
     * @see TargetDataLine
     */
    private TargetDataLine line = null;

    /**
     * Is the {@link AudioListener} currently recording or not.
     */
    private boolean isListening = false;

    /**
     * The size of the array in which the data line's input is read. When size is reached a new {@link Noise} Object is created. See {@link #analyzeNoise(byte[])}}
     */
    private final int bufferSize = 200;

    /**
     * The minimum amount of calculated rms values before the {@link Decoder} is notified.
     */
    private final int minNewSamples = 10;

    /**
     * The calculated RMS values are added into this List and can be fetched with the {@link #getNewSample()} method.
     * This Object is used to synchronize this class and the {@link Decoder} class.
     * (On this object the {@link Decoder} and {@link #listenerThread} thread is synchronized)
     */
    public final List<Noise> synchronizedBuffer = new ArrayList<>();

    //private Thread decoderThread;
    /**
     * The thread for the audio input, in which the input of {@link #line} is read into the memory buffer and the rms being calculated.
     * Also, the notification of the {@link Decoder} is handheld here. (Also see {@link #minNewSamples})
     */
    private Thread listenerThread;

    /*public AudioListener(Thread decoderThread){
        this.decoderThread = decoderThread;
    }*/

    /**
     * Method to start listening on the microphone input.
     * The {@link #line} is initialized here with the default microphone of a computer on which this program is running.
     * @return Returns true if the listener started successfully.
     */
    public boolean startListening(){

        AudioFormat format = new AudioFormat(44000f, 16, 1, true, true); //Default Line
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if(!AudioSystem.isLineSupported(info)){
            System.out.println("DataLine not available.");
            return isListening;
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return isListening;
        }

        isListening = true;

        listenerThread = new Thread(listenerRunnable);

        listenerThread.start();

        return isListening;
    }

    /**
     * Returns the value of the filed {@link #isListening}.
     * @return True, if the AudioListener is currently recording.
     * @see #isListening
     */
    public boolean isListening(){
        return isListening;
    }

    /**
     * This Method stops the audio listener. It waits until the last run of the {@link #listenerThread} finished.
     */
    public void stopListening(){
        //Set boolean to false so that the loop finishes cleanly
        isListening = false;
        try {
            //join to wait for the listenerThread to finish.
            listenerThread.join();
            line.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * The runnable for the {@link #listenerThread} thread.
     * @see Runnable
     */
    private final Runnable listenerRunnable = () -> {
        int i = 1;
        byte[] memoryBuffer = new byte[bufferSize];
        while(isListening){
            if(line.read(memoryBuffer, 0, memoryBuffer.length)>0){

                //region TODO delete DEBUG if no longer necessary
                System.out.println(i + " "+rmsValue(memoryBuffer));
                i++;
                //endregion

                synchronized (synchronizedBuffer) {
                    Noise noise = analyzeNoise(memoryBuffer);
                    synchronizedBuffer.add(noise);

                    //TODO delete DEBUG if no longer necessary
                    System.out.println(String.format("Added %s", noise.toString()));

                    if(synchronizedBuffer.size()>=minNewSamples){
                        synchronizedBuffer.notify();
                        //TODO delete DEBUG if no longer necessary
                        System.out.println("Try");
                    }
                }
            }
        }
    };

    /**
     * Methode that Calculates the Root Mean Square of the audio input.
     * See <a href="https://en.wikipedia.org/wiki/Root_mean_square">Root Mean Square on Wikipedia</a>
     * @param x The memoryBuffer byte array of the Input-AudioSystem (TargetDataLine)
     * @return The RMS Value of the input byte array
     */
    private double rmsValue(byte[] x){
        if(x.length == 0)
            return 0.0;

        double rms = 0.0;
        for(int i : x){
            rms += i * i;
        }
        rms/=x.length;
        return Math.sqrt(rms);
    }

    /**
     * Creates a new {@link Noise} object for a corresponding audio input sample.
     * Uses {@link #rmsValue(byte[])} to calculate the RMS value for the byte array.
     * A signal is silence if this calculated RMS value is smaller than {@link #getNoiseThreshold()}
     * @param byteArray The last bytes recorded by the microphone in an array.
     * @return The {@link Noise} object
     */
    private Noise analyzeNoise(byte[] byteArray){
        Instant timestamp = Instant.now();
        double rms = rmsValue(byteArray);
        boolean quiet = rms < getNoiseThreshold();

        return new Noise(quiet, timestamp);
    }

    /**
     * Method to fetch the last recorded RMS values since the last fetch.
     * @return A List of double values with the last samples since last fetch.
     * @see List<Double>
     */
    public List<Noise> getNewSample() {
        //TODO test if this method can be updated to an array (Performance)
        synchronized (synchronizedBuffer){
            List<Noise> test = new ArrayList<>(synchronizedBuffer);
            synchronizedBuffer.clear();
            return test;
        }
    }

    /**
     * Get the current Threshold selected by the user from the GUI.
     * If a tone is louder than this threshold the value is identified as loud.
     * @return The threshold
     */
    private double getNoiseThreshold(){
        //TODO: get noise threshold from GUI
        return 35.0;
    }
}
