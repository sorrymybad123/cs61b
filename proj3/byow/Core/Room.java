package byow.Core;

import byow.TileEngine.Tileset;

/**
 *  create room for the world
 */
public class Room {
    private static final int MAXLength = 8;

    private static final int MINLength = 1;


    public Room(Position p) {
        createRandomRectangle(p);
    }


    public void createRandomRectangle(Position p) {
        for (int i = p.x; i <  p.x + RandomUtils.uniform(randomWorld.RANDOM, MINLength, MAXLength); i++) {
            for (int j = p.y; j < p.y + RandomUtils.uniform(randomWorld.RANDOM, MINLength, MAXLength); j++) {
                randomWorld.randomTiles[i][j] = Tileset.FLOOR;
                // TODO add wall to the floor

                // TODO add four position of tile with wall
            }
        }
    }

}
