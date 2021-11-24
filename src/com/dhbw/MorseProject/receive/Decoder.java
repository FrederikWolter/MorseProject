package com.dhbw.MorseProject.receive;

import java.util.List;

public class Decoder {
    List<Token> tokenList;
    List<MorseSignal> signalList;
    AudioListener audioListener;
    private boolean isRecording;

    public boolean startRecording(Thread ui_update_thread){
        isRecording = true;

        return false;
    }

    public boolean stopRecording(){
        isRecording = false;
        return false;
    }

    public boolean isRecording(){
        return this.isRecording;
    }

    public String getLastSignal(){
        return "";
    }
}
