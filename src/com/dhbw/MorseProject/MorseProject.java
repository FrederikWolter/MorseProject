package com.dhbw.MorseProject;

import com.dhbw.MorseProject.send.Encoder;
import com.dhbw.MorseProject.send.Melody;
import com.dhbw.MorseProject.translate.Translator;

// todo comment ids from Pflichtenheft ;) where these are implemented
// todo setup jar export

/***
 * Main entry point to application - providing the main method.
 * @author Frederik Wolter
 */
public class MorseProject {

    /***
     * MAIN entry point of application.
     * @param args currently not used.
     */
    public static void main(String[] args) {
        //todo initialize central data here
        initMelodies();

        // todo remove test code
        Encoder e = Encoder.getInstance();

        //e.send("....--....- ....- ....--....-", Melody.getMelodyList().get(0));
        e.send(Translator.textToMorse("abcd"), Melody.getMelodyList().get(4));
        System.out.println(Translator.textToMorse("abcd"));


        /*e.send(".", new Melody("test", new int[1]));
        String input="end";
        Scanner s = new Scanner(System.in);
        System.out.println("\n\n++ Only input '.', '-', ' ' and '/' ++");
        do {
            System.out.print("\nInput: ");
            input=s.next();
            if(input.equalsIgnoreCase("stop"))
                e.stopPlaying();
            else if(!input.equalsIgnoreCase("end"))
                e.send(input, new Melody("test", new ArrayList<>()));

        } while(!input.equalsIgnoreCase("end"));*/
    }

    /***
     * Initialize the Melodies.
     */
    private static void initMelodies(){
        new Melody("Fest", new int[]
                {550});
        new Melody("Test", new int[]
                {550, 440, 330});
        new Melody("Linear Steigend", new int[]
                {200, 300, 400, 500, 600, 700, 800, 900, 1000});
        new Melody("Linear Fallend", new int[]
                {1000, 900, 800, 700, 600, 500, 400, 300, 200});
        new Melody("Zickzack", new int[]
                {200, 1000, 300, 900, 400, 800, 500, 700, 600});
        new Melody("Alle meine Entchen", new int[]
                {523, 587, 659, 698, 784, 784, 880, 880, 880, 880, 784, 880, 880, 880, 880, 784, 689, 689, 689, 689, 659, 659, 587, 587, 587, 587, 523});
    }
}
