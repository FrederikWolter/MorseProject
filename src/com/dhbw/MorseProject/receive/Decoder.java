package com.dhbw.MorseProject.receive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static void main(String[] args) throws IOException {
        Decoder.getInstance().startRecording();
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        String name = reader.readLine();
        Decoder.getInstance().stopRecording();
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
            //TODO notify Decoder und dort die sperre für die auswertung damit aushebeln (über isListening)
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

                analyzeSamples(samples);
                if (timeStamps.size() > 2)
                    analyzeTimeStamps();
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

    private void analyzeTimeStamps() {
        for (int i = 0; i < timeStamps.size()-1; i++) {

            int between = timeStamps.get(i+1).getIndex()-timeStamps.get(i).getIndex();

            //long duration = timeStamps.get(i+1).getTimestamp().toEpochMilli() - timeStamps.get(i).getTimestamp().toEpochMilli();

            //System.out.println(between + " " + timeStamps.size() + " " + i + " " + timeStamps.get(i).isQuiet());

            //TODO vlt die werte nicht statisch sonder berechnen lassen, indem ein cache benutzt wird und jedes mal ausgwertet wird wie lange was sein soll.

            if (timeStamps.get(i).isQuiet()) {
                //2=>' ', 6=> '/'

                if (350 <= between && between < 500) {       //Its a ' '
                    lastSignal.append("cs");
                } else if (850 <= between && between < 1200) {                           //Its a '/'
                    lastSignal.append("/");
                }

/*
                if (7500 <= between && between < 9500) {       //Its a ' '
                    lastSignal.append("cs");
                } else if (12000 <= between && between < 23000) {                           //Its a '/'
                    lastSignal.append("/");
                }*/
            } else {
                //1=>'.', 3=>'-'

                if (27 <= between && between < 100) {       //Its a '.'
                    lastSignal.append(".");
                } else if (190 <= between && between < 300) { //Its a '-'
                    lastSignal.append("-");
                }

                /*if (650 <= between && between < 2800) {       //Its a '.'
                    lastSignal.append(".");
                } else if (4500 <= between && between < 5500) { //Its a '-'
                    lastSignal.append("-");
                }*/

            }

        }
        Noise last = timeStamps.get(timeStamps.size()-1);
        timeStamps.clear();
        timeStamps.add(last);
    }

    private void analyzeSamples(List<Noise> samples) {

        if (timeStamps.size() == 0 || timeStamps.get(timeStamps.size() - 1).isQuiet() != samples.get(0).isQuiet())
            timeStamps.add(samples.get(0));

        for (int i = 0; i < samples.size(); i++) {
            //TODO Simplify (these objects are currently only for an overview)

            Noise start = samples.get(i);

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
