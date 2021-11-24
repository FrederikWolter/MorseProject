package com.dhbw.MorseProject.send;

public class Encoder {

    private static Encoder instance = new Encoder();

    private Encoder() {

    }

    public static Encoder getInstance() {
        return instance;
    }

    public void send(String morse, Melody melody) {
        Char[] signals = morse.toCharArray();

        for (char x in signals) {

        }
    }

    private void shortSignal() {

    }

    private void longSignal() {

    }


}
