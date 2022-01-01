package com.dhbw.morse_project.receive;

/**
 * This (record)class is used as a POJO for the representation of one Sample.
 *
 * @author Mark Mühlenberg, mininaml changes by Daniel Czeschner
 * @see AudioListener
 * @param quiet Ture if this sample is silence. (Smaller than the threshold (see {@link AudioListener}))
 * @param index The index of this Sample.
 */
public record Noise(boolean quiet, int index) {

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
