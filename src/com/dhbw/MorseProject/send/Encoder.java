package com.dhbw.MorseProject.send;

import javax.sound.sampled.*;

// todo comments; author; javadoc


public class Encoder {

    public static final int timeUnit = 100;

    private Thread encoderThread;
    private volatile boolean isPlaying;
    private static Encoder instance;


    private Encoder() {

    }


    public static Encoder getInstance() {
        if (instance == null) {
            instance = new Encoder();
        }
        return instance;
    }

    public void send(String morse, Melody melody) {
        stopPlaying();
        isPlaying = true;
        encoderThread = new Thread(() -> sending(morse, melody));
        encoderThread.start();
    }

    private void sending(String morse, Melody melody) {
        char[] signals = morse.toCharArray();
        int frequency = 500;

        try{
            SourceDataLine sourceDataLine;
            sourceDataLine = AudioSystem.getSourceDataLine(new AudioFormat(8000F, 8, 1, true, false));
            sourceDataLine.open(sourceDataLine.getFormat());
            sourceDataLine.start();

            for (char x: signals) {
                switch (x) {
                    case ' ' -> wait(2 * timeUnit); //TODO Change to global static variable
                    case '/' -> wait(6 * timeUnit);
                    case '.' -> signal2(1 * timeUnit, frequency, sourceDataLine);
                    case '-' -> signal2(3 * timeUnit, frequency, sourceDataLine);
                    default -> { } //TODO Throw Exception
                }
                if(!isPlaying)
                    break;
            }

            sourceDataLine.close();
        }catch (LineUnavailableException e){
            e.printStackTrace(); //TODO Exception handling
        }
    }

    public synchronized boolean stopPlaying() {
        isPlaying = false;
        try {       // todo nesessary?
            encoderThread.join();
            return true;
        } catch (Exception e) {
            if(encoderThread!=null)
                e.printStackTrace(); //TODO Exception Handling
            return false;
        }
    }


    private void wait(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();//TODO Exception handling
        }
    }

    //see https://rosettacode.org/wiki/Sine_wave
    private void signal2(int duration, int frequency, SourceDataLine dataLine) {

        try {
            int sampleRate = 44000;
            byte[] buffer = sineWave(frequency, duration, sampleRate);
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);

            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();

            wait(timeUnit);                         // 1TU pause after each signal
        }catch(LineUnavailableException e){
            e.printStackTrace(); //TODO Exception handling
        }
    }

    //todo introduce variables for magic numbers

    private byte[] sineWave(int frequency, int duration, int sampleRate){
        int samples = (duration * sampleRate) / 1000;
        byte[] result = new byte[samples];
        double interval = (double) sampleRate / frequency;

        for(int i = 0; i < samples; i++){
            double angle = 2.0 * Math.PI * i / interval;
            result[i] = (byte)(Math.sin(angle) * 70); // todo volume
        }
        return result;
    }
}
