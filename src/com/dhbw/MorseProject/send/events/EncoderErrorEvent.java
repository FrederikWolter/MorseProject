package com.dhbw.MorseProject.send.events;

import java.util.ArrayList;
import java.util.List;

public class EncoderErrorEvent {
    private List<IEncoderErrorListener> listeners = new ArrayList<IEncoderErrorListener>();

    public void addListener(IEncoderErrorListener listener){
        listeners.add(listener);
    }

    public void alert(String message){
        for (IEncoderErrorListener listener:
                listeners) {
            listener.run(message);
        }
    }
}
