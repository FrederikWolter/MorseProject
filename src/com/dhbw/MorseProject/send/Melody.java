package com.dhbw.MorseProject.send;

import java.util.ArrayList;

public class Melody {

    private static ArrayList<Melody> melodyList;

    private String name;
    private ArrayList<Integer> freqList;


    public Melody(String name, ArrayList<Integer> freqList) {
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

    public ArrayList<Integer> getFreqList() {
        return freqList;
    }

    public void setFreqList(ArrayList<Integer> freqList) {
        this.freqList = freqList;
    }

    public static ArrayList<Melody> getMelodyList() {
        return melodyList;
    }

}
