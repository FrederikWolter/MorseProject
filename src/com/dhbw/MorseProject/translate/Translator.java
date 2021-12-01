package com.dhbw.MorseProject.translate;

import java.util.HashMap;


/**
 * @author Hassan El-Khalil
 * This Class is responsible for translating Strings into Morse-Codes and v.v.
 */
public class Translator {
    static String L = "-"; // Long Morse Signal
    static String S = "."; // Short Morse Signal
    static String W = "/"; // Word end
    static String C = " "; // Char end

    private static final HashMap<Character, String> CharToMorse = new HashMap<>();
    private static final HashMap<String, Character> MorseToChar = new HashMap<>();
    static{
        CharToMorse.put('A', S+L);
        CharToMorse.put('B', L+S+S+S);
        CharToMorse.put('C', L+S+L+S);
        CharToMorse.put('D', L+S+S);
        CharToMorse.put('E', S);
        CharToMorse.put('F', S+S+L+S);
        CharToMorse.put('G', L+L+S);
        CharToMorse.put('H', S+S+S+S);
        CharToMorse.put('I', S+S);
        CharToMorse.put('J', S+L+L+L);
        CharToMorse.put('K', L+S+L);
        CharToMorse.put('L', S+L+S+S);
        CharToMorse.put('M', L+L);
        CharToMorse.put('N', L+S);
        CharToMorse.put('O', L+L+L);
        CharToMorse.put('P', S+L+L+S);
        CharToMorse.put('Q', L+L+S+L);
        CharToMorse.put('R', S+L+S);
        CharToMorse.put('S', S+S+S);
        CharToMorse.put('T', L);
        CharToMorse.put('U', S+S+L);
        CharToMorse.put('V', S+S+S+L);
        CharToMorse.put('W', S+L+L);
        CharToMorse.put('X', L+S+S+L);
        CharToMorse.put('Y', L+S+L+L);
        CharToMorse.put('Z', L+L+S+S);
        CharToMorse.put('0', L+L+L+L+L);
        CharToMorse.put('1', S+L+L+L+L);
        CharToMorse.put('2', S+S+L+L+L);
        CharToMorse.put('3', S+S+S+L+L);
        CharToMorse.put('4', S+S+S+S+L);
        CharToMorse.put('5', S+S+S+S+S);
        CharToMorse.put('6', L+S+S+S+S);
        CharToMorse.put('7', L+L+S+S+S);
        CharToMorse.put('8', L+L+L+S+S);
        CharToMorse.put('9', L+L+L+L+S);
        CharToMorse.put('.', W+S+L+S+L+S+L);
        CharToMorse.put(',', W+L+L+S+S+L+L);
        CharToMorse.put('\'', W+S+L+L+L+L+S);
        CharToMorse.put(':', W+L+L+L+S+S+S);
        CharToMorse.put('-', W+L+S+S+S+S+L);
        CharToMorse.put(' ', W);
        reverse();
    }

    /**
     *This Method reverts the CharToMorse Hashmap
     */
    public static void reverse(){
        for(HashMap.Entry<Character, String> entry : CharToMorse.entrySet()){
            MorseToChar.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * For a single Char-Input this method returns the associated Morse-String as defined in the {@link #CharToMorse Hashmap}
     * For an unrecognized char it will return null
     * @param l Char-input
     * @return Morse-String or null
     */
    public static String toMorse(char l){
        l = Character.toUpperCase(l);
        return CharToMorse.getOrDefault(l, null);
    }

    /**
     * For a single Morse-Code this method returns the associated Char as defined in the {@link #MorseToChar Hashmap}
     * For an unrecognized char it will return the null char
     * @param Morsecode Morse-Code Input in the defined Morse-alphabet
     * @return translated Char
     */
    public static char toChar(String Morsecode){
        return MorseToChar.getOrDefault(Morsecode, (char) 0);
    }

    /**
     * For an input Morse-Code it returns the translated String
     * @param morse Enter a String containing multiple single Morse-Codes recognized in {@link #MorseToChar}
     * @return The translated String
     */
    public static String morseToText(String morse){
        // Split each Morse code by separating them at the whitespace
        String[] morseChar = morse.split(" ");
        StringBuilder x = new StringBuilder();
        for (String s : morseChar) {
            char c = toChar(s);
            if(c == 0)
                return null;
            x.append(c);
        }
        return x.toString();
    }

    /**
     * For an input String it returns the translated Morse-Code
     * @param Text Enter a String with containing the Recognized Chars in {@link #CharToMorse}
     * @return The translated Morse-code
     */
    public static String textToMorse(String Text){
        // Creating array of string length
        char[] ch = Text.toCharArray();
        StringBuilder x = new StringBuilder();
        for (char c : ch) {
            String s = toMorse(c);
            if(s == null)
                return null;
            x.append(s);
            x.append(C);
        }
        return x.toString();
    }
}
