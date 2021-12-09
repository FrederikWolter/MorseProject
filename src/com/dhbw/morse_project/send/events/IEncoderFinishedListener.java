package com.dhbw.morse_project.send.events;

/**
 * Interface used for Listener to 'encoder finished'-event.
 * @author Mark Mühlenbarg & Frederik Wolter
 */
public interface IEncoderFinishedListener {
    /**
     * Method executed when event is triggered.
     */
    void run();
}
