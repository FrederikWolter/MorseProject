package com.dhbw.MorseProject.send;

import com.dhbw.MorseProject.translate.Translator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

// todo comments

/**
 * Class responsible for encoding morse-signals to audio and therefor generating and sending the audio signal.
 * @author Lucas Schaffer & Frederik Wolter
 */
public class Encoder {
    // region public static final
    // active division to make these public static due to possible use by other classes as part of encoder 'interface'
    /**
     * Global time unit in ms used by all morse-code timing related code
     */
    public static final int TIME_UNIT = 100;
    /**
     * Defined sampleRate used to generate the signal in samples/s
     */
    public static final int SAMPLE_RATE = 44000;
    /**
     * Volume between 0 and 127 in which signal is played
     */
    public static final byte VOLUME = 60;
    /**
     * Damp factor for sine wave. Value between 0 and 1 representing percentage of samples modified.
     */
    public static final double DAMP_FACTOR = 0.98;
    // endregion

    /**
     * Singleton like for the MAIN-thread version of {@link Encoder}
     */
    private static Encoder INSTANCE;

    /**
     * Thread used for actual timing relevant sending work.
     */
    private Thread encoderThread;
    /**
     * Variable accessed by sending-thread and main-thread (volatile) signaling if the next tone should be played.
     * Could be used to stop playback through stopPlaying.
     */
    private volatile boolean isPlaying;

    // empty private constructor hence the main-thread variant of Encoder does nothing
    private Encoder() { }

    /**
     * Implementation of singleton to access the existing object or create one.
     * @return only instance of {@link Encoder} in main-thread mode.
     */
    public static Encoder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Encoder();
        }
        return INSTANCE;
    }

    /**
     * main send method used to send morse-signal with a defined melody.
     * @param morse  to be sent.
     * @param melody in which to send.
     */
    public void send(String morse, Melody melody) {
        stopPlaying();                                              // prevent encoder from playing multiple signals at once
        isPlaying = true;
        encoderThread = new Thread(() -> sending(morse, melody));   // create a new sending thread executing the sending method
        encoderThread.start();                                      // start created thread
    }

    /**
     * private helper method sending the given morse code with the given melody.
     * Called by sending-thread version of {@link Encoder}
     * @param morse  to be sent.
     * @param melody in which to send.
     */
    @SuppressWarnings("PointlessArithmeticExpression")
    private void sending(String morse, Melody melody) {
        char[] signals = morse.toCharArray();
        int[] freqList = melody.getFreqList();
        int freq_length = freqList.length;
        int freq_index = 0;

        for (char x : signals) {
            switch (String.valueOf(x)) {
                case Translator.C -> wait(2 * TIME_UNIT);
                case Translator.W -> wait(6 * TIME_UNIT);
                case Translator.S -> signal(1 * TIME_UNIT, freqList[(freq_index++) % freq_length]);
                case Translator.L -> signal(3 * TIME_UNIT, freqList[(freq_index++) % freq_length]);
                default -> {
                } //TODO Throw Exception
            }
            if (!isPlaying)     // stop playing next tone if isPlaying is false
                break;
        }
    }

    /**
     * public method to stop playing the current morse-signal.
     */
    public synchronized void stopPlaying() {
        isPlaying = false;
        try {       // todo necessary?
            encoderThread.join();
        } catch (Exception e) {
            if (encoderThread != null)
                e.printStackTrace(); //TODO Exception Handling
        }
    }

    /**
     * Helper method for waiting a defined duration on the sending thread.
     * Used to implement silence between tones.
     * @param duration to wait in ms
     */
    private void wait(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();    //TODO Exception handling
        }
    }

    /**
     * Helper method for playing a tone for a given duration and with a given frequency.
     * Inspired by (among others) <a href="https://rosettacode.org/wiki/Sine_wave">rosettacode.org</a>
     * @param duration  of the tone.
     * @param frequency of the tone.
     */
    private void signal(int duration, int frequency) {
        try {
            byte[] buffer = sineWave(frequency, duration);
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);

            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();

            wait(TIME_UNIT);                         // 1 time unit pause after each signal
        } catch (LineUnavailableException e) {
            e.printStackTrace(); //TODO Exception handling
        }
    }

    /**
     * private helper method generating a sine wave with given frequency and duration.
     * public static volume ist used for the amplitude.
     * @param frequency of sine wave
     * @param duration  of sine wave
     * @return byte array of sine wave
     */
    private byte[] sineWave(int frequency, int duration) {
        int samples = (duration * SAMPLE_RATE) / 1000;               // convert samples/s to samples/(duration in ms)
        byte[] result = new byte[samples];
        double interval = (double) SAMPLE_RATE / frequency;

        for (int i = 0; i < samples; i++) {
            double angle = 2.0 * Math.PI * i / interval;
            result[i] = (byte) (Math.sin(angle) * VOLUME);
        }
        return fadeOutSineWave(result);
    }

    /**
     * Damp created sine wave in array.
     * Solution to 'pop' sound especially at the end of tone.
     * @param buffer sine wave to be dampened
     * @return dampened sine wave
     */
    private byte[] fadeOutSineWave(byte[] buffer) {
        int len = buffer.length;

        // damping of sine wave start
        for (int i = 0; i < (int) (len * (1 - DAMP_FACTOR)); i++) {
            buffer[i] = (byte) (buffer[i] * Math.exp((i - (len * (1 - DAMP_FACTOR))) * 1 / (len * (1 - DAMP_FACTOR) / 3)));
        }
        // damping of sine wave end
        for (int i = (int) (len * DAMP_FACTOR); i < len; i++) {
            buffer[i] = (byte) (buffer[i] * Math.exp(-(i - (len * DAMP_FACTOR)) * 1 / (len * (1 - DAMP_FACTOR) / 4)));
        }
        return buffer;
    }
}
