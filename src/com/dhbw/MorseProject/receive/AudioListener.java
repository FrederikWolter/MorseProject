package com.dhbw.MorseProject.receive;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioListener {



    private TargetDataLine line = null;
    private boolean isListening = false;

    public void startListening(){
        isListening = true; //boolean auf true setzen für schleifendurchlauf in listenerThread

        AudioFormat format = new AudioFormat(42000.0f, 16, 1, true, true); //Default Line
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if(!AudioSystem.isLineSupported(info)){
            System.out.println("DataLine not available.");
            return;
        }



        listenerThread.start(); //listenerThread in neuem Thread starten
    }

    public void stopListening(){
        isListening = false;    //boolean auf false setzen, damit schleife den durchlauf demnächst sauber beendet
        try {
            listenerThread.join();  //join, um zu warten bis listenerThread fertig ist.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Thread listenerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(isListening){
                //...
            }
        }
    });

    public double[] getNewSample() {
        double[] back = new double[0];
        return back;
    }


}
