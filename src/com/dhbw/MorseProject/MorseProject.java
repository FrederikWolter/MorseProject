package com.dhbw.MorseProject;

import com.dhbw.MorseProject.send.Encoder;
import com.dhbw.MorseProject.send.Melody;

import java.util.ArrayList;

public class MorseProject {

    public static void main(String[] args) {
        Encoder e = Encoder.getInstance();
        e.send("... --- ...", new Melody("test", new ArrayList<>()));
    }
}
