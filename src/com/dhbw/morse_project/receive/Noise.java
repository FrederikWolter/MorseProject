package com.dhbw.morse_project.receive;

/**
 * This (record)class is used as a POJO for the representation of one Sample.
 * @param isQuiet Ture if this sample is silence. (Smaller than the threshold (see {@link AudioListener}))
 * @param index The index of this Sample.
 *
 * @author Daniel Czeschner
 * @see AudioListener
 * @see <a href="https://jax.de/blog/datenklassen-in-java-einfuehrung-in-java-records/">jax.de - Einfuehrung in Java records</a>
 */
public record Noise(boolean isQuiet, int index) {
    @Override
    public String toString() {
        return "Quiet: " + isQuiet + " | Index:" + index;
    }
}
