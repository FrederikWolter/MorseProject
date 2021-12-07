package com.dhbw.MorseProject.receive;

import java.time.Instant;
import java.util.Date;

/**
 * @author Mark Mühlenberg, mininaml changes by Daniel Czeschner
 */
public class Noise {

    private boolean quiet;
    private Instant timestamp;
    private int index;

    public Noise(boolean quiet, Instant timestamp) {
        this.quiet = quiet;
        this.timestamp = timestamp;
    }

    public Noise(boolean quiet, Date timestamp) {
        this(quiet, (Instant) null);
        this.timestamp = timestamp.toInstant();
    }

    public Noise(boolean quiet, int index) {
        this.quiet = quiet;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Date getTimestampAsDate() {
        return Date.from(timestamp);
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        long ms;
        if(this.getTimestamp() != null)
            ms = getTimestamp().toEpochMilli();
        else
            ms = this.getIndex();
        return "Quiet: " + this.isQuiet() + " | time:" + ms;
    }
}
