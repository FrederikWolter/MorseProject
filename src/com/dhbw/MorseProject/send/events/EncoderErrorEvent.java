package com.dhbw.MorseProject.send.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for {@link EncoderErrorEvent}-Object. Used for notify the GUI if an error accused.
 *
 * @author @author Mark Mühlenbarg & Frederik Wolter
 */
public class EncoderErrorEvent {
    private final List<IEncoderErrorListener> listeners = new ArrayList<>();

    /**
     * Add a listener to the event which should be notified.
     *
     * @param listener to be added.
     */
    public void addListener(IEncoderErrorListener listener) {
        listeners.add(listener);
    }

    /**
     * Used to call the event and notify the listeners.
     */
    public void alert(String message) {
        for (IEncoderErrorListener listener : listeners) {
            listener.run(message);
        }
    }
}
