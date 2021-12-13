package com.dhbw.morse_project;

import com.dhbw.morse_project.gui.GUI;
import com.dhbw.morse_project.send.Melody;

import javax.swing.*;

/**
 * Main entry point to application - providing the main method.
 *
 * @author Frederik Wolter
 */
public class MorseProject {

    /**
     * MAIN entry point of application.
     *
     * @param args currently not used.
     */
    public static void main(String[] args) {
        initMelodies();

        // Start GUI
        SwingUtilities.invokeLater(GUI::new);
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
                {400, 450, 500, 550, 600, 650, 700, 750, 800, 850});
        new Melody("Linear Fallend", new int[]
                {850, 800, 750, 700, 650, 600, 550, 500, 450, 400});
        new Melody("Zickzack", new int[]
                {850, 400, 800, 450, 750, 500, 700, 550, 650, 600});
        new Melody("Alle meine Entchen", new int[]
                {523, 587, 659, 698, 784, 784, 880, 880, 880, 880, 784, 880, 880, 880, 880, 784, 689, 689, 689, 689, 659, 659, 587, 587, 587, 587, 523});
        new Melody("Handyklingelton", new int[]
                {1319, 1175, 740, 831, 1047, 988, 587, 659, 988, 880, 523, 659, 440});
    }
}
