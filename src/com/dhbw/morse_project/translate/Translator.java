package com.dhbw.morse_project.translate;

import java.util.HashMap;

/**
 * This Class is responsible for translating Strings into Morse-Codes and v.v.
 *
 * @author Hassan El-Khalil, supported by Frederik Wolter
 */
@SuppressWarnings("unused")
public abstract class Translator {
    /**
     * Long Morse Signal
     */
    public static final String L = "-";
    /**
     * Short Morse Signal
     */
    public static final String S = ".";
    /**
     * Word-End Morse Signal
     */
    public static final String W = "/";
    /**
     * Char-End Morse Signal
     */
    public static final String C = " ";

    private static final HashMap<Character, String> CharToMorse = new HashMap<>();
    private static final HashMap<String, Character> MorseToChar = new HashMap<>();

    static {
        // region initialize CharToMorse
        CharToMorse.put('A', S + L);
        CharToMorse.put('B', L + S + S + S);
        CharToMorse.put('C', L + S + L + S);
        CharToMorse.put('D', L + S + S);
        CharToMorse.put('E', S);
        CharToMorse.put('F', S + S + L + S);
        CharToMorse.put('G', L + L + S);
        CharToMorse.put('H', S + S + S + S);
        CharToMorse.put('I', S + S);
        CharToMorse.put('J', S + L + L + L);
        CharToMorse.put('K', L + S + L);
        CharToMorse.put('L', S + L + S + S);
        CharToMorse.put('M', L + L);
        CharToMorse.put('N', L + S);
        CharToMorse.put('O', L + L + L);
        CharToMorse.put('P', S + L + L + S);
        CharToMorse.put('Q', L + L + S + L);
        CharToMorse.put('R', S + L + S);
        CharToMorse.put('S', S + S + S);
        CharToMorse.put('T', L);
        CharToMorse.put('U', S + S + L);
        CharToMorse.put('V', S + S + S + L);
        CharToMorse.put('W', S + L + L);
        CharToMorse.put('X', L + S + S + L);
        CharToMorse.put('Y', L + S + L + L);
        CharToMorse.put('Z', L + L + S + S);
        CharToMorse.put('0', L + L + L + L + L);
        CharToMorse.put('1', S + L + L + L + L);
        CharToMorse.put('2', S + S + L + L + L);
        CharToMorse.put('3', S + S + S + L + L);
        CharToMorse.put('4', S + S + S + S + L);
        CharToMorse.put('5', S + S + S + S + S);
        CharToMorse.put('6', L + S + S + S + S);
        CharToMorse.put('7', L + L + S + S + S);
        CharToMorse.put('8', L + L + L + S + S);
        CharToMorse.put('9', L + L + L + L + S);
        CharToMorse.put('.', S + L + S + L + S + L);
        CharToMorse.put(',', L + L + S + S + L + L);
        CharToMorse.put('\'', S + L + L + L + L + S);
        CharToMorse.put(':', L + L + L + S + S + S);
        CharToMorse.put('-', L + S + S + S + S + L);
        CharToMorse.put(' ', W);
        // endregion

        reverse();  // initialize MorseToChar by reversing CharToMorse
    }

    // region getter & setter
    /**
     * Getter for CharToMorse
     *
     * @return CharToMorse Map
     */
    public static HashMap<Character, String> getCharToMorse() {
        return CharToMorse;
    }
    /**
     * Getter for MorseToChar
     *
     * @return MorseToChar Map
     */
    public static HashMap<String, Character> getMorseToChar() {
        return MorseToChar;
    }
    // endregion

    /**
     * This Method reverses the CharToMorse Hashmap and saves it in MorseToChar.
     */
    private static void reverse() {
        for (HashMap.Entry<Character, String> entry : CharToMorse.entrySet())
            MorseToChar.put(entry.getValue(), entry.getKey());
    }

    /**
     * For a single Char-Input this method returns the associated Morse-String as defined in {@link #CharToMorse}.
     * For an unrecognized char it will return null.
     *
     * @param input Char-input
     * @return Morse-String or null
     */
    public static String toMorse(char input) {
        input = Character.toUpperCase(input);
        return CharToMorse.getOrDefault(input, null);       // return looked up morse code or NULL
    }

    /**
     * For a single Morse-Code this method returns the associated Char as defined in the {@link #MorseToChar Hashmap}.
     * For an unrecognized char it will return the null char.
     *
     * @param morseCode Morse-Code Input in defined Morse-alphabet
     * @return translated Char or 0 char
     */
    public static char toChar(String morseCode) {
        return MorseToChar.getOrDefault(morseCode, (char) 0);           // return looked up char or the 0 char
    }

    /**
     * For an input Morse-Code it returns the translated String
     * [ID: F-LOG-20.2, F-LOG-20.2.1]
     *
     * @param morse Enter a String containing multiple single Morse-Codes recognized in {@link #MorseToChar}
     * @return translated String
     */
    public static String morseToText(String morse) {
        String[] morseChar = morse.split(" ");  // split each Morse code by separating at blank (char end)
        StringBuilder x = new StringBuilder();
        for (String s : morseChar) {
            char c = toChar(s);
            if (c == 0)                               // char not found
                x.append('?');
            x.append(c);
        }
        return x.toString();
    }

    /**
     * For an input String it returns the translated Morse-Code.
     * [ID: F-LOG-20.1, F-LOG-20.1.1]
     *
     * @param text Enter a String with containing the Recognized Chars in {@link #CharToMorse}
     * @return translated Morse-code
     */
    public static String textToMorse(String text) {
        char[] ch = text.toCharArray();         // Creating array of string length
        StringBuilder x = new StringBuilder();
        for (char c : ch) {
            String s = toMorse(c);
            if (s == null)                      // char not found
                return null;
            x.append(s).append(C);
        }
        return x.toString();
    }
}