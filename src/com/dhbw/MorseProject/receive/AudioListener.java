package com.dhbw.MorseProject.receive;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class AudioListener {

    private TargetDataLine line = null;
    private boolean isListening = false;
    private final int bufferSize = 6000;
    private byte[] memoryBuffer = new byte[bufferSize];

    public static void main(String[] args) {
        AudioListener listener = new AudioListener();
        listener.startListening();
    }

    public void startListening(){
        System.out.println("Test");
        isListening = true; //boolean auf true setzen für schleifendurchlauf in listenerThread
        listenerThread.start(); //listenerThread in neuem Thread starten
    }

    public void stopListening(){
        isListening = false;    //boolean auf false setzen, damit schleife den durchlauf demnächst sauber beendet
        try {
            listenerThread.join();  //join, um zu warten bis listenerThread fertig ist.
            line.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Thread listenerThread = new Thread(() -> {

        AudioFormat format = new AudioFormat(16000.f, 16, 1, true, true); //Default Line
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if(!AudioSystem.isLineSupported(info)){
            System.out.println("DataLine not available.");
            return;
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        }

        while(isListening){
            if(line.read(memoryBuffer, 0, memoryBuffer.length)>0){
                //System.out.println(calculateLvl(memoryBuffer));
                System.out.println(rmsValue(memoryBuffer));
            }
        }
    });

    static float rmsValue(byte arr[])
    {
        int square = 0;
        float mean = 0;
        float root = 0;

        // Calculate square.
        for(int i = 0; i < arr.length; i++)
        {
            square += Math.pow(arr[i], 2);
        }

        // Calculate Mean.
        mean = (square / (float) (arr.length));

        // Calculate Root.
        root = (float)Math.sqrt(mean);

        return root;
    }

    public double[] getNewSample() {
        double[] back = new double[0];
        return back;
    }
}
