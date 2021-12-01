package com.dhbw.MorseProject.send;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Encoder {
    // todo implement threads
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
        for (char x: signals) {
            switch (x) {
                case ' ' -> wait(2 * timeUnit); //TODO Change to global static variable
                case '/' -> wait(6 * timeUnit);
                case '.' -> signal(1 * timeUnit, frequency);
                case '-' -> signal(3 * timeUnit, frequency);
                default -> { } //TODO Throw Exception
            }
            if(!isPlaying)
                break;
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

    private void signal(int duration, int frequency) {
        SourceDataLine sourceDataLine;
        //todo introduce variables for magic numbers
        try {
            sourceDataLine = AudioSystem.getSourceDataLine(new AudioFormat(8000F, 8, 1, true, false));
            sourceDataLine.open(sourceDataLine.getFormat());
            sourceDataLine.start();

            for (int u = 0; u < (duration*8); u++) {
                sourceDataLine.write(new byte[]{(byte) (Math.sin(u / (8000F / frequency) * 2.0 * Math.PI) * 127.0)}, 0, 1);
            }   // TODO: signal quality? some times cracking in signal
            sourceDataLine.drain();
            wait(timeUnit);                         // 1TU pause after each signal
            //sourceDataLine.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace(); //TODO Exception handling
        }
    }
}
