package com.dhbw.MorseProject;

import com.dhbw.MorseProject.gui.GUI;
import com.dhbw.MorseProject.send.Encoder;
import com.dhbw.MorseProject.send.Melody;
import com.dhbw.MorseProject.translate.Translator;

import javax.swing.*;

// todo comment ids from Pflichtenheft ;) where these are implemented
// todo setup jar export

/**
 * Main entry point to application - providing the main method.
 * @author Frederik Wolter
 */
public class MorseProject {

    /**
     * MAIN entry point of application.
     * @param args currently not used.
     */
    public static void main(String[] args) {
        //todo initialize central data here
        initMelodies();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });

    }

    /**
     * Initialize the Melodies. [ID: F-TEC-10.4.1]
     */
    private static void initMelodies() {
        new Melody("Fest", new int[]
                {550});
        new Melody("Triton", new int[]
                {550, 440, 330});
        new Melody("Linear Steigend", new int[]
                {200, 300, 400, 500, 600, 700, 800, 900, 1000});
        new Melody("Linear Fallend", new int[]
                {1000, 900, 800, 700, 600, 500, 400, 300, 200});
        new Melody("Zickzack", new int[]
                {200, 1000, 300, 900, 400, 800, 500, 700, 600});
        new Melody("Alle meine Entchen", new int[]
                {523, 587, 659, 698, 784, 784, 880, 880, 880, 880, 784, 880, 880, 880, 880, 784, 689, 689, 689, 689, 659, 659, 587, 587, 587, 587, 523});
        new Melody("Handyklingelton", new int[]
                {1319, 1175, 740, 831, 1047, 988, 587, 659, 988, 880, 523, 659, 440});
    }
}
