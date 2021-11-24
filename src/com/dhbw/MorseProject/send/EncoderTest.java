package com.dhbw.MorseProject.send;

import java.util.ArrayList;

public class EncoderTest {
    public static void main(String[] args) {
        Encoder e = Encoder.getInstance();
        e.send("... --- ...", new Melody("test", new ArrayList<>()), 100);
    }
}
