package byow.Core;

import byow.TileEngine.Tileset;

/**
 * position class
 */
public class Position {
    int x;

    int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public static Position generatePositionForRoom() {

        return new Position(RandomUtils.uniform(randomWorld.RANDOM, Engine.HEIGHT - 8), RandomUtils.uniform(randomWorld.RANDOM, Engine.WIDTH));
    }


}
