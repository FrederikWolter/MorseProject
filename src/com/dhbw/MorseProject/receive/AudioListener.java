package com.dhbw.MorseProject.receive;

import javax.sound.sampled.TargetDataLine;

public class AudioListener {



    private TargetDataLine line = null;
    private boolean isListening = false;

    public void startListening(){
        isListening = true; //boolean auf true setzen für schleifendurchlauf in listenerThread
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


}
