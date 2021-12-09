package com.dhbw.MorseProject.send.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for {@link EncoderFinishedEvent}-Object. Used for telling the GUI sending finished.
 *
 * @author @author Mark Mühlenbarg & Frederik Wolter
 */
public class EncoderFinishedEvent {
    private final List<IEncoderFinishedListener> listeners = new ArrayList<>();

    /**
     * Add a listener to the event which should be notified.
     *
     * @param listener to be added.
     */
    public void addListener(IEncoderFinishedListener listener) {
        listeners.add(listener);
    }

    /**
     * Used to call the event and notify the listeners.
     */
    public void alert() {
        for (IEncoderFinishedListener listener : listeners) {
            listener.run();
        }
    }
}
