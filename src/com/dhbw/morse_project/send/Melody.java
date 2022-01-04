package com.dhbw.morse_project.send;

import java.util.ArrayList;

/**
 * This class is used as a POJO for the representation of a melody object. [ID: F-TEC-10.4]
 *
 * @author Frederik Wolter
 * @see Encoder
 */
@SuppressWarnings("unused")
public class Melody {
    /**
     * Kind of static singleton ArrayList of all melodies created in runtime.
     */
    private static ArrayList<Melody> melodyList;        // Alternative to Singleton: own Wrapper Class or making it public

    // region POJO attributes
    private String name;            // displayed name in gui
    private int[] freqList;         // array of integer frequencies, later played in loop when selected
    // endregion

    /**
     * Constructor of {@link Melody} Class.
     *
     * @param name     to be displayed in GUI
     * @param freqList used to play signal. If len(freqList) = 1 -> constant frequency.
     */
    public Melody(String name, int[] freqList) {
        this.name = name;
        this.freqList = freqList;

        if (melodyList == null)         // part of 'singleton' implementation
            melodyList = new ArrayList<>();

        melodyList.removeIf(melody -> melody.equals(this)); // remove old melody if same name
        Melody.melodyList.add(this);    // automatically add created object to list
    }

    // region getter & setter
    public static ArrayList<Melody> getMelodyList() {
        return melodyList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getFreqList() {
        return freqList;
    }

    public void setFreqList(int[] freqList) {
        this.freqList = freqList;
    }
    // endregion

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Melody)
            return this.name.equals(((Melody) obj).name);

        return false;
    }
}
