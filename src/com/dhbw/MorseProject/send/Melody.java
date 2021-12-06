package com.dhbw.MorseProject.send;

import java.util.ArrayList;

/***
 * This class is used as a POJO for the representation of a melody object.
 * @author Frederik Wolter
 */
public class Melody {
    /***
     * Kind of static singleton ArrayList of all melodies created in runtime.
     */
    private static ArrayList<Melody> melodyList;

    // region POJO attributes
    private String name;            // displayed name in gui
    private int[] freqList;         // array of integer frequencies, later played in loop when selected
    // endregion

    /***
     * Constructor of {@link Melody} Class.
     * @param name to be displayed in GUI
     * @param freqList used to play signal. If len(freqList) = 1 -> constant frequency.
     */
    public Melody(String name, int[] freqList) {
        this.name = name;
        this.freqList = freqList;

        if (melodyList == null) {        // part of singleton implementation
            melodyList = new ArrayList<>();
        }

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
}
