package com.dhbw.MorseProject.receive;

import java.util.ArrayList;
import java.util.List;

/**
 * In this class, the samples are evaluated in order to record the Morse code from the audio input.
 * Audio input from the {@link AudioListener} class.
 *
 * @author Daniel Czeschner, Supported by: Mark Mühlenberg
 */
public class Decoder {
    //TODO comments
    private AudioListener audioListener;
    private volatile boolean isRecording = false;
    private Thread decoderThread;

    private static Decoder instance = null;

    private List<Noise> timeStamps = new ArrayList<>();

    private Thread ui_update_thread;

    StringBuilder lastSignal = new StringBuilder();

    public static void main(String[] args) {
        Decoder.getInstance().startRecording();
    }

    /**
     * Private constructor for the Singleton-Pattern
     */
    private Decoder() {

    }

    public boolean startRecording(/* TODO Thread ui_update_thread */) {
        //TODO this.ui_update_thread = ui_update_thread;

        decoderThread = new Thread(decoderRunnable); //Creating new Thread because you can only call .start on Thread once

        audioListener = new AudioListener(/*decoderThread*/); //creating new audioListener
        isRecording = audioListener.startListening(); //start listening on audioListener

        if (isRecording)
            decoderThread.start();

        return isRecording;
    }

    public boolean stopRecording() {
        try {
            isRecording = false;    //setting boolean to false for graceful finish of decoderThread
            decoderThread.join();   //joining thread to wait until finish
            return true;    //return true if success
        } catch (InterruptedException ie) {
            return false;   //return false if failed
        }
    }

    private final Runnable decoderRunnable = () -> {
        while (isRecording) {
            try {
                List<Noise> samples;
                synchronized (audioListener.synchronizedBuffer) {
                    audioListener.synchronizedBuffer.wait(); //wait until decoder is notified with new sample from audioListener
                    samples = audioListener.getNewSample(); //getting new Sample from audioListener
                }

                //TODO: analyze sample
                int next = timeStamps.size();
                analyzeSamples(samples);
                if (timeStamps.size() > 1)
                    analyzeTimeStamps(next);
                if (lastSignal.length() > 0) {
                    System.out.println(getLastSignal()); //TODO DELETE debug if no longer needed
                    //TODO ui_update_thread.notify();  //notify ui_update_thread about new signal
                    lastSignal.setLength(0); //Reset the StringBuilder
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        audioListener.stopListening();  //stop audioListener when decoder is finished
    };

    private void analyzeTimeStamps(int next) {
        for (int i = next; i < timeStamps.size(); i++) {


            //TODO NOISE umbauen auf index berechnung, da der letzt wert im einfach zu lange zurück liegt und deshalb der nächte laute ton immer ein - ist.
            //alternativ kann auch versucht werden, wenn die dafor leise waren, dass die einfach ignoriert werden. Jedoch dass das nicht immer so ist.

            long duration = timeStamps.get(i).getTimestamp().toEpochMilli() - timeStamps.get(i - 1).getTimestamp().toEpochMilli();

            //TODO get timeunits from encoder
            long timeUnit = 100;

            if (timeStamps.get(i).isQuiet()) {
                //2=>' ', 6=> '/'

                if (0 < duration && duration < 5 * timeUnit) { //Its a ' '
                    lastSignal.append(" ");
                } else if (0 < duration) {                           //Its a '/'
                    lastSignal.append("/");
                }
            } else {
                //1=>'.', 3=>'-'

                if (0 < duration && duration < 3 * timeUnit) { //Its a '.'
                    lastSignal.append(".");
                } else if (0 < duration) {                           //Its a '-'
                    lastSignal.append("-");
                }

            }
        }
    }

    private void analyzeSamples(List<Noise> samples) {

        if (timeStamps.size() == 0 || timeStamps.get(timeStamps.size() - 1).isQuiet() != samples.get(0).isQuiet())
            timeStamps.add(samples.get(0));

        for (int i = 0; i < samples.size(); i++) {
            //TODO Simplify (these objects are currently only for an overview)

            Noise start = samples.get(0);

            boolean quiet = start.isQuiet();

            for (int j = i + 1; j < samples.size(); j++) { //Check next Noise Object
                boolean test = samples.get(j).isQuiet();

                if (test != quiet) { //Next is loud and we were quiet; Next is quiet and we were loud
                    Noise end = samples.get(j);
                    timeStamps.add(end);
                    i = j;
                    break;
                }
            }
        }
    }


    public boolean isRecording() {
        return this.isRecording;
    }

    public String getLastSignal() {
        return lastSignal.toString();
    }

    public static Decoder getInstance() {
        if (instance == null) {
            instance = new Decoder();
        }
        return instance;
    }

}
