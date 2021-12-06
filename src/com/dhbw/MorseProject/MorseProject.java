package com.dhbw.MorseProject;

import com.dhbw.MorseProject.send.Encoder;
import com.dhbw.MorseProject.send.Melody;
import com.dhbw.MorseProject.translate.Translator;

// todo comments
// todo comment ids from Pflichtenheft ;) where these are implemented
// todo setup jar export

/***
 * Main entry point to application - providing the main method.
 * @author Frederik Wolter
 */
public class MorseProject {

    public static void main(String[] args) {
        //todo initialize central data here: e.g. melodies

        // todo remove test code
        Encoder e = Encoder.getInstance();


        //int[] freq = {550, 440, 330};
        int[] freq = {550};
        Melody melody = new Melody("Test", freq);
        e.send(Translator.textToMorse("abcd"), melody);
        System.out.println(Translator.textToMorse("abcd"));

        int[] freq2 = {523, 587, 659, 698, 784, 784, 880, 880, 880, 880, 784, 880, 880, 880, 880, 784, 689, 689, 689, 689, 659, 659, 587, 587, 587, 587, 523};
        Melody melody2 = new Melody("Test", freq);
        e.send("....--....- ....- ....--....-", melody2);


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
}
