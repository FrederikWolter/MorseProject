package com.dhbw.MorseProject.send.events;

/**
 * Interface used for Listener to 'encoder error'-event.
 * @author Mark Mühlenbarg & Frederik Wolter
 */
public interface IEncoderErrorListener {
    /**
     * Method executed when event is triggered.
     * @param message of error.
     */
    void run(String message);
}
