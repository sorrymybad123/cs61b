package capers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import static capers.Utils.*;

/** A repository for Capers 
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers"); // TODO Hint: look at the `join`

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() throws IOException {
        File capers = new File(CWD, ".capers");
        capers.mkdir();
        File dogs = new File(capers, "dogs");
        dogs.mkdir();
        File story = new File(capers, "story.txt");
        story.createNewFile();
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) throws IOException {
        File story = Utils.join(CAPERS_FOLDER, "story.txt");
        String lastText = null;
        if (story.exists()) {
            lastText = Utils.readContentsAsString(story);
        }
        Utils.writeContents(story, lastText + text + "\n");
        text = Utils.readContentsAsString(story);
        System.out.println(text);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) throws IOException {
       Dog newDog = new Dog(name, breed, age);
       // save the dog to the file
       newDog.saveDog();

       System.out.println(newDog.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) throws IOException {
        // TODO
        Dog thatDog = Dog.fromFile(name);
        thatDog.haveBirthday();
        thatDog.saveDog();

    }
}
