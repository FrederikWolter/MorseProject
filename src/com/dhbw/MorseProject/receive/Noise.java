package com.dhbw.MorseProject.receive;

import java.time.Instant;
import java.util.Date;

public class Noise {



    private boolean quiet;
    private Instant timestamp;

    public Noise(boolean quiet, Instant timestamp) {
        this.quiet = quiet;
        this.timestamp = timestamp;
    }

    public Noise(boolean quiet, Date timestamp){
        this(quiet, (Instant) null);
        this.timestamp = timestamp.toInstant();
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

    public Date getTimestampAsDate(){
        return Date.from(timestamp);
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

}
