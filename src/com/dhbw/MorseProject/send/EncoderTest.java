package com.dhbw.MorseProject.send;

import java.util.ArrayList;
import java.util.Scanner;

public class EncoderTest {
    public static void main(String[] args) {
        Encoder e = Encoder.getInstance();
        e.send(".", new Melody("test", new ArrayList<>()));
        String input="end";
        Scanner s = new Scanner(System.in);
        System.out.println("\n\n++ Only input '.', '-', ' ' and '/' ++");
        do {
            System.out.print("\nInput: ");
            input=s.next();
            if(input.equalsIgnoreCase("stop"))
                e.stopPlaying();
            else if(!input.equalsIgnoreCase("end"))
                e.send(input, new Melody("test", new ArrayList<>()));

        } while(!input.equalsIgnoreCase("end"));
    }
}
