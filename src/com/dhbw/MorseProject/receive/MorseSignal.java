package com.dhbw.MorseProject.receive;

/**
 * @author Daniel Czehscner
 */
public class MorseSignal {

    private boolean quiet;
    private long lengthInMs;

    public MorseSignal(boolean quiet, long lengthInMs) {
        this.quiet = quiet;
        this.lengthInMs = lengthInMs;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public long getLengthInMs() {
        return lengthInMs;
    }

    public void setLengthInMs(long lengthInMs) {
        this.lengthInMs = lengthInMs;
    }
}
