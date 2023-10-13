package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;


    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * clear HexWorld to nothing
     */
    public static void clearHexWorldNothing(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * print something by what thing and how much in line
     */
    private static void printInWorld(TETile[][] world, TETile thing, int x, int y, int num) {
        int z = x;
        for (;z < num + x; z++) {
            if (z >= 0 && z < WIDTH && y >= 0 && y < HEIGHT) {
                world[z][y] = thing;
            } else {
                throw new RuntimeException();
            }
        }
    }
    /**
     * add a hexagon
     */
    public static void addHexagon(TETile[][] world, int size, TETile thing, int x, int y) {

        // 检查是否越界
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            throw new IllegalArgumentException("Invalid position for hexagon.");
        }
        // upside
        int numOfNothing = size - 1;
        int numOfThing = size;
        for (int i = 0; i < size; i++) {
            // print the line of thing
            printInWorld(world, thing, x + numOfNothing, y, numOfThing);
            y++;
            numOfNothing--;
            numOfThing += 2;
        }

        // control position
        numOfNothing = 0;
        numOfThing -= 2;

        for (int i = 0; i < size; i++) {
            printInWorld(world, thing, x + numOfNothing, y,  numOfThing);
            numOfNothing++;
            numOfThing -= 2;
            y++;
        }

    }

    /**
     * Picks a random tile
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.AVATAR;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.SAND;
            default: return Tileset.NOTHING;
        }
    }

    /**
     * get the upside neighbor
     */
    public static int getUpsideNeighbor(int y, int size) {
        return y + 2 * size;
    }

    /**
     * get the right neighbor
     */
    public static int getRightNeighborX(int x, int size) {
        return x + 2 * size - 1;
    }

    /**
     * get bottom of the neighbor
     */
    public static int getBottomRightNeighborY(int y, int size) {
        return y - size;
    }

    /**
     * get up right of the neighbor
     */
    private static int getRightUpNeighborY(int y, int size) {
       return y + size;
    }

    /**
     * draw colum hex
     */
    public static void drawColumHex(TETile[][] world, int num, int x, int y, int size) {
        int getupY = y;
        for (int i = 0; i < num; i++) {
            addHexagon(world, size, randomTile(), x, getupY);
            getupY = getUpsideNeighbor(getupY, size);
        }
    }

    /**
     * get the left neighbor
     * @param size
     */
    private static int getLeftNeighborX(int x, int size) {
        return x - 2 * size + 1;
    }

    public static void drawGraph(TETile[][] world, int size, int num) {
        int getX = size;
        int getY = size * 3;
        for (int i = 0; i < 3; i++) {
            drawColumHex(world, num, getX, getY, size);
            getX = getRightNeighborX(getX, size);
            getY = getBottomRightNeighborY(getY, size);
            num++;
        }

        getY = size * 3;
        num = num - 2;
        getY = getBottomRightNeighborY(getY, size);
        for (int i = 0; i < 2; i++) {
            drawColumHex(world, num, getX, getY, size);
            getX = getRightNeighborX(getX, size);
            getY = getRightUpNeighborY(getY, size);
            num--;
        }


    }




    public static void main(String[] args) {
        TERenderer ter = new TERenderer();

        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];

        clearHexWorldNothing(world);

        drawGraph(world, 3, 4);

        ter.renderFrame(world);
    }


}

