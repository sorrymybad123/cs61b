package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) throws InterruptedException {


        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);

        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        StdDraw.text(20, 20, "Please input a string: ");
        StdDraw.show();

        // Initialize random number generator
        rand = new Random(seed);

    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        if (n <= 0) {
            return null;
        }

        String randomString = "";
        // get the char in CHARACTERS
        for (int i = 0; i < n; i++) {
            int randomCharSerial = RandomUtils.uniform(rand, CHARACTERS.length);
            randomString += CHARACTERS[randomCharSerial];
        }

        return randomString;
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        StdDraw.clear();
        StdDraw.text(20, 20, s);
        // If game is not over, display relevant game information at the top of the screen
        if (gameOver == false) {
           int index = RandomUtils.uniform(rand, 0, ENCOURAGEMENT.length);
           StdDraw.text(32, 38, ENCOURAGEMENT[index]);
           StdDraw.text(20, 38, "Round: " + round);
           StdDraw.text(3, 38, "Watch!");
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) throws InterruptedException {
        // Display each character in letters, making sure to blank the screen between letters
        for (char c : letters.toCharArray()) {
            StdDraw.pause(500);
            drawFrame(Character.toString(c));
            StdDraw.pause(1000);
        }
        StdDraw.clear(Color.BLACK);
    }
    public static String solicitStartNCharsInput(int n) {
        //Read n letters of player input
        StringBuilder inputBuilder = new StringBuilder();
        while (inputBuilder.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                inputBuilder.append(key);
            }
        }
        return inputBuilder.toString();
    }
    public String solicitNCharsInput(int n) {
        //Read n letters of player input
        StringBuilder inputBuilder = new StringBuilder();
        while (inputBuilder.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                inputBuilder.append(key);
            }
        }
        playerTurn = false;
        return inputBuilder.toString();
    }

    public void startGame() throws InterruptedException {
        //TODO: Set any relevant variables before the game starts
        round = 1;
        String randomString = "";
        String inputString = "";
        int numOfString;
        while (true) {
            gameOver = false;
            StdDraw.clear();
            StdDraw.text(20, 20, "Round: " + round);
            StdDraw.show();
            Thread.sleep(1000);
            StdDraw.clear();
            numOfString = RandomUtils.uniform(rand, 1, 5);
            // randomly generate a target string
            randomString = generateRandomString(numOfString);
            // display target string on screen one character at a time
            flashSequence(randomString);
            StdDraw.clear(Color.BLACK);
            // Wait for player input until they type in as many characters as there are in the target
            playerTurn = true;
            if (playerTurn == true) {
                inputString = solicitNCharsInput(numOfString);
            }
            System.out.println(inputString);
            System.out.println(randomString);

            if (!inputString.equals(randomString)) {
                gameOver = true;
                StdDraw.clear();
                StdDraw.text(20, 20, "Game Over You made it to round: " + round);
                StdDraw.show();
                break;
            }
            round++;
        }

        //TODO: Establish Engine loop
    }

}
