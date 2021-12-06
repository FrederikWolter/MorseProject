package com.dhbw.MorseProject.send;

import com.dhbw.MorseProject.translate.Translator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

// todo comments; author; javadoc

public class Encoder {

    public static final int timeUnit = 100;
    public static final int sampleRate = 44000;
    public static final byte volume = 70;

    private static Encoder instance;

    private Thread encoderThread;
    private volatile boolean isPlaying;


    private Encoder() { }

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
        int[] freqList = melody.getFreqList();
        int freq_length = freqList.length;
        int freq_index = 0;

        for (char x : signals) {
            switch (String.valueOf(x)) {
                case Translator.C -> wait(2 * timeUnit);
                case Translator.W -> wait(6 * timeUnit);
                case Translator.S -> signal2(1 * timeUnit, freqList[(freq_index++) % freq_length]);
                case Translator.L -> signal2(3 * timeUnit, freqList[(freq_index++) % freq_length]);
                default -> { } //TODO Throw Exception
            }
            if (!isPlaying)
                break;
        }
    }

    public synchronized void stopPlaying() {
        isPlaying = false;
        try {       // todo nesessary?
            encoderThread.join();
        } catch (Exception e) {
            if (encoderThread != null)
                e.printStackTrace(); //TODO Exception Handling
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
    private void signal2(int duration, int frequency) {

        try {
            byte[] buffer = sineWave(frequency, duration);
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);

            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();

            wait(timeUnit);                         // 1TU pause after each signal
        } catch (LineUnavailableException e) {
            e.printStackTrace(); //TODO Exception handling
        }
    }

    //todo introduce variables for magic numbers
    private byte[] sineWave(int frequency, int duration) {
        int samples = (duration * sampleRate) / 1000;
        byte[] result = new byte[samples];
        double interval = (double) sampleRate / frequency;

        for (int i = 0; i < samples; i++) {
            double angle = 2.0 * Math.PI * i / interval;
            result[i] = (byte) (Math.sin(angle) * volume);
        }
        return result;
    }
}
