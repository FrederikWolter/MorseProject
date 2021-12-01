package com.dhbw.MorseProject.send;

import java.util.ArrayList;

// todo comments; author; javadoc

public class Melody {

    private static ArrayList<Melody> melodyList;

    private String name;
    private int[] freqList;


    public Melody(String name, int[] freqList) {
        this.name = name;
        this.freqList = freqList;

        if (melodyList == null){
            melodyList = new ArrayList<>();
        }

        Melody.melodyList.add(this);
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

    public static ArrayList<Melody> getMelodyList() {
        return melodyList;
    }
}
