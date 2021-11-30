package com.dhbw.MorseProject.receive;

import java.util.ArrayList;
import java.util.List;

/**
 * In this class, the samples are evaluated in order to record the Morse code from the audio input.
 * Audio input from the {@link AudioListener} class.
 * @author Daniel Czeschner, Supported by: Mark Mühlenberg
 */
public class Decoder {
    //TODO comments
    private List<Token> tokenList;
    private List<MorseSignal> signalList;
    private AudioListener audioListener;
    private volatile boolean isRecording = false;
    private Thread decoderThread;

    private static Decoder instance = null;
    private Thread ui_update_thread;

    public static void main(String[] args) {
        Decoder.getInstance().startRecording();
    }

    private Decoder(){

    }

    public boolean startRecording(/* TODO Thread ui_update_thread */){
        //TODO this.ui_update_thread = ui_update_thread;

        decoderThread = new Thread(decoderRunnable); //Creating new Thread because you can only call .start on Thread once

        audioListener = new AudioListener(/*decoderThread*/); //creating new audioListener
        isRecording = audioListener.startListening(); //start listening on audioListener

        if(isRecording)
            decoderThread.start();

        return isRecording;
    }

    public boolean stopRecording(){

        try{
            isRecording = false;    //setting boolean to false for graceful finish of decoderThread
            decoderThread.join();   //joining thread to wait until finish
            return true;    //return true if success
        } catch (InterruptedException ie){
            return false;   //return false if failed
        }
    }

    private final Runnable decoderRunnable = () -> {
        while (isRecording) {
            //Thread.onSpinWait();
            try {
                List<Double> samples = new ArrayList<>();
                synchronized (audioListener.synchronizedBuffer) {
                    audioListener.synchronizedBuffer.wait(); //wait until decoder is notified with new sample from audioListener
                    samples.addAll(audioListener.getNewSample()); //getting new Sample from audioListener
                }

                System.out.println("Start of Decoder");

                for(int i = 0; i < 100000000; i++){
                    if(i == 99999999){
                        System.out.println("finished long task");
                        System.out.println(samples);
                    }
                }

                //TODO: analyze sample

                //TODO ui_update_thread.notify();  //notify ui_update_thread about new signal


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        audioListener.stopListening();  //stop audioListener when decoder is finished
    };

    public boolean isRecording(){
        return this.isRecording;
    }

    public String getLastSignal(){
        return "";
    }

    public static Decoder getInstance(){
        if(instance == null){
            instance = new Decoder();
        }
        return  instance;
    }

}
