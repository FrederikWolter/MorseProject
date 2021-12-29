package com.dhbw.morse_project.receive;

/**
 * This class is used as a POJO for the representation of one Sample.
 *
 * @author Mark Mühlenberg, mininaml changes by Daniel Czeschner
 * @see AudioListener
 */
public class Noise {
    // TODO convert to java record recommend; similar to data class in kotlin https://jax.de/blog/datenklassen-in-java-einfuehrung-in-java-records/
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
