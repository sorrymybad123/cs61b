package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarString[] stringSound = new GuitarString[37];
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        for(int i = 0; i < stringSound.length; i++){
            stringSound[i] = new GuitarString(440 * Math.pow(2, (i-24)/12));
        }

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);

                if (index >= 0 && index <  37) {
                    stringSound[index].pluck();
                }


            }

            double sample = 0;
            /* compute the superposition of samples */
            for (int i = 0; i < 37; i++){
                sample += stringSound[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < 37; i++){
                stringSound[i].tic();
            }
        }
    }
}
