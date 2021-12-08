package com.dhbw.MorseProject.send;

import java.util.ArrayList;
import java.util.List;

public class EncoderFinishedEvent{
    private List<IEncoderFinishedListener> listeners = new ArrayList<IEncoderFinishedListener>();

    public void addListener(IEncoderFinishedListener listener){
        listeners.add(listener);
    }

    public void alert(){
        for (IEncoderFinishedListener listener:
             listeners) {
            listener.run();
        }
    }
}
