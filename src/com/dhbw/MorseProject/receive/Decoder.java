package com.dhbw.MorseProject.receive;

import java.util.List;

public class Decoder {
    List<Token> tokenList;
    List<MorseSignal> signalList;
    AudioListener audioListener;
    private volatile boolean isRecording;
    private Thread decoderThread;

    private static Decoder instance = null;
    private Thread ui_update_thread;

    private Decoder(){

    }

    public boolean startRecording(Thread ui_update_thread){
        if (isRecording){
            return false;
        } else{
            try {
                isRecording = true;

                this.ui_update_thread = ui_update_thread;

                decoderThread = new Thread(decoderRunnable);    //Creating new Thread because you can only call .start on Thread once
                decoderThread.start();

                return true;
            } catch (Exception e){
                return false;
            }
        }


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



    private final Runnable decoderRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                //Thread.onSpinWait();
                try {
                    wait(); //wait until decoder is notified with new sample from audioListener
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double sample[] = audioListener.getNewSample(); //getting new Sample from audioListener
                //TODO: analyze sample
                //... sample analysis

                ui_update_thread.notify();  //notify ui_update_thread about new signal

            }
        }
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
