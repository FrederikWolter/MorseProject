package com.dhbw.MorseProject.send;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Encoder {

    private static Encoder instance;

    private Encoder() {

    }

    public static Encoder getInstance() {
        if (instance==null) {
            instance  = new Encoder();
        }
        return instance;
    }

    public void send(String morse, Melody melody, int timeUnit) {
        char[] signals = morse.toCharArray();
        int frequency = 500;
        for (char x: signals) {
            switch (x) {
                case ' ': //TODO Change to global static variable
                    wait(2*timeUnit);
                    break;
                case '/':
                    wait(6*timeUnit);
                    break;
                case '.':
                    signal(1, timeUnit, frequency);
                    break;
                case '-':
                    signal(3, timeUnit, frequency);
                    break;
                default:
                    //TODO Throw Exception
                    break;
            }
        }
    }

    private void wait(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();//TODO Exception handling
        }
    }

    private void signal(int duration, int timeUnit, int frequency) {
        SourceDataLine sourceDataLine;
        try {
            sourceDataLine = AudioSystem.getSourceDataLine(new AudioFormat(8000F, 8, 1, true, false));
            sourceDataLine.open(sourceDataLine.getFormat());
            sourceDataLine.start();

            for (int u = 0; u < (duration*timeUnit*8); u++) {
                sourceDataLine.write(new byte[]{(byte) (Math.sin(u / (8000F / frequency) * 2.0 * Math.PI) * 127.0)}, 0, 1);
            }
            sourceDataLine.drain();
            wait(timeUnit);
        } catch (LineUnavailableException e) {
            e.printStackTrace(); //TODO Exception handling
        }

    }



}
