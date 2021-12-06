package com.dhbw.MorseProject.send;

import com.dhbw.MorseProject.translate.Translator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

// todo comments

/***
 * Class responsible for encoding morse-signals to audio and therefor generating and sending the audio signal.
 * @author Lucas Schaffer & Frederik Wolter
 */
public class Encoder {
    // region public static final
    /***
     * Global time unit in ms used by all morse-code timing related code
     */
    public static final int timeUnit = 100;
    /***
     * Defined sampleRate used to generate the signal in samples/s
     */
    public static final int sampleRate = 44000;
    /***
     * Volume between 0 and 127 in which signal is played
     */
    public static final byte volume = 60;
    public static final double DAMP_FACTOR = 0.95;
    // endregion

    /***
     * Singleton for the MAIN-thread version of {@link Encoder}
     */
    private static Encoder INSTANCE;

    /***
     * Thread used for actual timing relevant sending work.
     */
    private Thread encoderThread;
    /***
     * Variable accessed by sending-thread and main-thread (volatile) signaling if the next tone should be played.
     * Could be used to stop playback through stopPlaying.
     */
    private volatile boolean isPlaying;

    // empty constructor hence the main-thread variant of Encoder does nothing
    private Encoder() { }

    /***
     * Implementation of singleton to access the existing object or create one.
     * @return only instance of {@link Encoder} in main-thread mode.
     */
    public static Encoder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Encoder();
        }
        return INSTANCE;
    }

    /***
     *  main send method used to send morse-signal with a defined melody.
     * @param morse to be sent.
     * @param melody in which to send.
     */
    public void send(String morse, Melody melody) {
        stopPlaying();                                              // prevent encoder from playing multiple signals at once
        isPlaying = true;
        encoderThread = new Thread(() -> sending(morse, melody));   // create a new sending thread executing the sending method
        encoderThread.start();                                      // start created thread
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
                default -> {
                } //TODO Throw Exception
            }
            if (!isPlaying)     // stop playing next tone if isPlaying is false
                break;
        }
    }

    /***
     * public method to stop playing the current morse-signal.
     */
    public synchronized void stopPlaying() {
        isPlaying = false;
        try {       // todo nesessary?
            encoderThread.join();
        } catch (Exception e) {
            if (encoderThread != null)
                e.printStackTrace(); //TODO Exception Handling
        }
    }

    /***
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
        int samples = (duration * sampleRate) / 1000;               // convert samples/s to samples/(duration in ms)
        byte[] result = new byte[samples];
        double interval = (double) sampleRate / frequency;

        for (int i = 0; i < samples; i++) {
            double angle = 2.0 * Math.PI * i / interval;
            result[i] = (byte) (Math.sin(angle) * volume);
        }
        return result;
    }

    /*private byte[] fadeOutSineWave(byte[] buffer){
        int len = buffer.length;

        for (int i = 0; i < (int) (len * (1 - DAMP_FACTOR)); i++) {
            buffer[i] = (byte) (buffer[i] * Math.exp((i - (len * (1-DAMP_FACTOR))) * 1/(len * (1 - DAMP_FACTOR) / 3)));
        }

        for (int i = (int) (len * DAMP_FACTOR); i < len; i++) {
            buffer[i] = (byte) (buffer[i] * Math.exp(-(i - (len * DAMP_FACTOR)) * 1/(len * (1 - DAMP_FACTOR) / 4)));
        }
        return buffer;
    }*/
}
