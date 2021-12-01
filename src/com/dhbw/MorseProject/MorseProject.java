package com.dhbw.MorseProject;

import com.dhbw.MorseProject.send.Encoder;
import com.dhbw.MorseProject.send.Melody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MorseProject {

    public static void main(String[] args) {

        int[] freq = {550, 440, 330};
        Melody melody = new Melody("Test", freq);

        Encoder e = Encoder.getInstance();

        /*e.send(".", new Melody("test", new int[1]));
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

        } while(!input.equalsIgnoreCase("end"));*/

        e.send("-- --", melody);
    }
}
