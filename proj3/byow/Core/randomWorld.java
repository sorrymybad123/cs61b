package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world that contains RANDOM tiles.
 */
public class randomWorld {
    private static final int WIDTH = Engine.WIDTH;
    private static final int HEIGHT = Engine.HEIGHT;

    private long SEED;
    public static Random RANDOM;
    public static TETile[][] randomTiles;



    /**
     * Fills the given 2D array of tiles with Nothing tiles.
     * @param tiles
     */
    public static void fillWithNothingTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * TODO add the outside wall to the rectangle may be a room or hallway
     */

    /**
     * generate different shape
     */
    public void generateShape(Position p) {

    }

    /**
     * create a random rectangle
     */
    public void createRandomRectangle(Position p) {
        for (int i = p.x; i <  p.x + RandomUtils.uniform(RANDOM, 10); i++) {
            for (int j = p.y; j < p.y + RandomUtils.uniform(RANDOM, 10); j++) {
                randomTiles[i][j] = Tileset.FLOOR;
                // TODO add wall to the floor

                // TODO add four position of tile with wall
            }
        }
    }

    /**
     * if a position is nothing add it with wall
     */
    private void addWall(Position p) {

    }

    /**
     * create the world
     */
    public randomWorld(int seed) {
        this.RANDOM = new Random(SEED);
        // create the world with nothing
        SEED = seed;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        randomTiles = new TETile[WIDTH][HEIGHT];
        fillWithNothingTiles(randomTiles);
        Position p = new Position(10, 10);
        createRandomRectangle(p);
        //TODO add some random rectangle by seed
        ter.renderFrame(randomTiles);
    }


}

