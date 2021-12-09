package com.dhbw.MorseProject.receive;

/**
 * This class is used as a POJO for the representation of one Sample.
 *
 * @author Mark Mühlenberg, mininaml changes by Daniel Czeschner
 * @see AudioListener
 */
public class Noise {

    // region POJO attributes
    private final boolean quiet;
    private final int index;
    //endregion

    /**
     * Constructor of {@link Noise} Class.
     *
     * @param quiet Ture if this sample is silence. (Smaller than the threshold (see {@link AudioListener}))
     * @param index The index of this Sample.
     */
    public Noise(boolean quiet, int index) {
        this.quiet = quiet;
        this.index = index;
    }

    // region getter
    public int getIndex() {
        return index;
    }

    public boolean isQuiet() {
        return quiet;
    }
    //endregion

    @Override
    public String toString() {
        return "Quiet: " + this.isQuiet() + " | Index:" + index;
    }
}
