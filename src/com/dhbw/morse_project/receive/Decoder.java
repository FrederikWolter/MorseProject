package com.dhbw.morse_project.receive;

import com.dhbw.morse_project.send.Encoder;

import java.util.ArrayList;
import java.util.List;

/**
 * In this class, the samples ({@link Noise}-Objects) are evaluated in order to generate the Morse-Code.
 * <p>
 * [ID: F-LOG-20.3.2, (F-LOG-20.3.3)]
 *
 * @author Daniel Czeschner, Supported by: Mark Mühlenberg
 * @see AudioListener
 */
@SuppressWarnings("unused")
public class Decoder {

    /**
     * The {@link AudioListener} from which new samples are read.
     */
    private AudioListener audioListener;

    /**
     * Defines if the {@link Decoder} and so the {@link AudioListener} is currently recording or not.
     */
    private volatile boolean isRecording = false;

    /**
     * Thread in which the samples are evaluated and the Morse-Code is build. (Decoding-Thread)
     */
    private Thread decoderThread;

    /**
     * List with the {@link Noise}-Objets which are different to the previous entry. So that on a quiet sample a loud one follows.
     */
    private final List<Noise> FILTERED_SAMPLES_LIST = new ArrayList<>();

    /**
     * StringBuilder in which the last found Morse-Signals are appended.
     */
    private final StringBuilder LAST_SIGNAL = new StringBuilder();

    /**
     * Defines if the last signal was silence. If so don't add another silence to the output after it. (Example: We don't want: ". / / -.". What we want is: ". / -.")
     */
    private boolean lastWasSilence = true;

    /**
     * Constructor
     */
    public Decoder(/* TODO GUI gui */) {
        //TODO this.gui = gui;
        audioListener = new AudioListener(/* TODO gui*/); //creating new audioListener
    }

    /**
     * Method to start capturing the microphone input through the {@link AudioListener} and start the Decoding-Thread ({@link #decoderThread}) if no error happened.
     * <p>
     * [ID: F-GUI-30.1.1]
     *
     * @return True if the listener started successfully and the Decoding-Thread is started.
     * @see AudioListener#startListening() ()
     */
    public boolean startRecording() {
        decoderThread = new Thread(decoderRunnable); //Creating new Thread because you can only call .start on Thread once

        isRecording = audioListener.startListening(); //start listening on audioListener

        //Reset
        LAST_SIGNAL.setLength(0);
        FILTERED_SAMPLES_LIST.clear();
        lastWasSilence = true;

        if (isRecording)
            decoderThread.start();

        return isRecording;
    }

    /**
     * This method stops the {@link #decoderThread} and so the {@link AudioListener}-Thread.
     * It waits until the last run of the {@link #decoderThread}-Thread finished.
     * <p>
     * [ID: F-GUI-30.1.4]
     *
     * @return True, if the Thread stopped without errors.
     */
    public boolean stopRecording() {
        try {
            if(decoderThread != null){
                isRecording = false;    //Setting boolean to false for graceful finish the decoderThread
                decoderThread.join();   //Joining thread to wait for it to finish the last run.
            }
            return true;    //return true if success
        } catch (InterruptedException ie) {
            return false;   //return false if failed
        }
    }

    /**
     * The Runnable for the {@link #decoderThread}-Thread.
     * In this Thread/Runnable the received {@link Noise}-Objets (from the {@link AudioListener}) are analyzed, filtered and the output Morse-Code is generated.
     * Everytime a new Morse-Signal was found the GUI is notified so that it can fetch the latest Morse-Signals.
     * <p>
     * Part of: [ID: F-TEC-10.1.1] (Other in {@link AudioListener})
     *
     * @see #analyzeInputSamples(List)
     * @see #analyzeFilteredSamples()
     */
    private final Runnable decoderRunnable = () -> {
        while (isRecording) {
            List<Noise> samples = null;
            try {
                synchronized (audioListener.getSynchronizedBuffer()) {
                    audioListener.getSynchronizedBuffer().wait(); //Wait until decoder is notified from AudioListener that there are new Samples.
                    samples = audioListener.getNewSamples();      //Getting new Samples from the AudioListener
                }
            } catch (InterruptedException e) {
                e.printStackTrace(); //This error is never going to happen, because we don't use interrupt() on this Thread. We need to catch it anyway.
            }

            if (samples != null) {
                analyzeInputSamples(samples);
                analyzeFilteredSamples();
                if (LAST_SIGNAL.length() > 0) {
                    /* TODO notify GUI
                    synchronized (gui.getGuiDecoderSynchronizeObject()){
                        gui.getGuiDecoderSynchronizeObject().notify(); //notify ui_update_thread about new signals
                    } */
                }
            }
        }
        audioListener.stopListening();  //Stop AudioListener when Decoder finished
    };

    /**
     * In this Method the fetched {@link Noise}-Objects from the {@link AudioListener} are filtered (On a loud sample follows a silence and vice versa).
     *
     * @param samples List with the unfiltered {@link Noise}-Objects.
     */
    private void analyzeInputSamples(List<Noise> samples) {
        //We add the first sample to the filtered list and if the next value does not have the same identifier (quiet, loud) as the last entry in the last iteration.
        if (this.FILTERED_SAMPLES_LIST.size() == 0 || this.FILTERED_SAMPLES_LIST.get(this.FILTERED_SAMPLES_LIST.size() - 1).isQuiet() != samples.get(0).isQuiet())
            this.FILTERED_SAMPLES_LIST.add(samples.get(0));

        for (int i = 0; i < samples.size(); i++) {
            boolean isCurrentQuiet = samples.get(i).isQuiet();

            for (int j = i + 1; j < samples.size(); j++) { //Check next Noise Object
                if (samples.get(j).isQuiet() != isCurrentQuiet) { //Next is loud and prev. were quiet ; or ; Next is quiet and prev. were loud
                    this.FILTERED_SAMPLES_LIST.add(samples.get(j));
                    i = j;  //We don't want to check every sample more than once.
                    break;
                }
            }
        }
    }

    /**
     * Method to analyze the filtered Samples. This Method calculates the distance between two samples and based on this distance
     * and the type (quiet, lout) it is recognized which Morse code is involved.
     * The values used for the range for a Morse-Signal is based on the {@link Encoder#TIME_UNIT} divided by 100 and best practice values.
     * <br>(A calculation of the values are here not possible because we only have a small amount of Samples.
     * We would need to cache them or would have to start a synchronization with the transmitter beforehand.
     * Due to time constraints, however, this could not be implemented for the first version of this program.)
     *
     * @see #analyzeInputSamples(List)
     */
    private void analyzeFilteredSamples() {
        for (int i = 0; i < FILTERED_SAMPLES_LIST.size() - 1; i++) {

            //The amount of samples between
            int between = FILTERED_SAMPLES_LIST.get(i + 1).getIndex() - FILTERED_SAMPLES_LIST.get(i).getIndex();

            if (FILTERED_SAMPLES_LIST.get(i).isQuiet()) {
                //We don't want that a silence can follow on a silence.
                if (!lastWasSilence) {
                    if ((280 * Encoder.TIME_UNIT / 100) <= between && between < (500 * Encoder.TIME_UNIT / 100)) {         //Its a ' '
                        LAST_SIGNAL.append(" ");
                        lastWasSilence = true;
                    } else if ((780 * Encoder.TIME_UNIT / 100) <= between && between < (1200 * Encoder.TIME_UNIT / 100)) { //Its a '/'
                        LAST_SIGNAL.append(" / ");
                        lastWasSilence = true;
                    }
                }
            } else {
                if ((25 * Encoder.TIME_UNIT / 100) <= between && between < (100 * Encoder.TIME_UNIT / 100)) {         //Its a '.'
                    LAST_SIGNAL.append(".");
                    lastWasSilence = false;
                } else if ((180 * Encoder.TIME_UNIT / 100) <= between && between < (300 * Encoder.TIME_UNIT / 100)) { //Its a '-'
                    LAST_SIGNAL.append("-");
                    lastWasSilence = false;
                }
            }
        }
        //Delete everything in the list expect the last entry.
        Noise last = FILTERED_SAMPLES_LIST.get(FILTERED_SAMPLES_LIST.size() - 1);
        FILTERED_SAMPLES_LIST.clear();
        FILTERED_SAMPLES_LIST.add(last);
    }

    /**
     * Getter if the {@link Decoder} is currently decoding/recording or not.
     *
     * @return True, if decoding/recording
     * @see #isRecording
     */
    public boolean isRecording() {
        return this.isRecording;
    }

    /**
     * Getter for the GUI to get the last detected Morse-Signals.
     * After it the StringBuilder {@link #LAST_SIGNAL} is reset.
     *
     * @return String with the last Morse-Signals.
     */
    public String getLastSignal() {
        String back = LAST_SIGNAL.toString();
        LAST_SIGNAL.setLength(0); //Reset the StringBuilder
        return back;
    }
}
