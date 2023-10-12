package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Draws a world that contains RANDOM tiles.
 */
public class randomWorld {
    private static final int WIDTH = Engine.WIDTH;
    private static final int HEIGHT = Engine.HEIGHT;

    private long SEED;
    public  Random RANDOM;
    public static TETile[][] randomTiles;



    /**
     * Fills the given 2D array of tiles with Nothing tiles.
     * @param tiles
     */
    public static void fillWithNothingTiles(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
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
     * get the tile in this position
     */
    private static TETile getTile(Position p) {
        return randomTiles[p.x][p.y];
    }

    /**
     * change it to wall
     */
    private static void changeToWall(Position p) {
        if (getTile(p).equals(Tileset.NOTHING)) {
            // change that tile in that position
            randomTiles[p.x][p.y] = Tileset.WALL;
        }
    }


    /**
     * fill the wall around
     */
    private static void fillWall(Position p) {
        Position left = new Position(p.x - 1, p.y);
        Position right = new Position(p.x + 1, p.y);
        Position up = new Position(p.x, p.y + 1);
        Position down = new Position(p.x, p.y - 1);
        Position leftUp = new Position(p.x - 1, p.y + 1);
        Position rightUp = new Position(p.x + 1, p.y + 1);
        Position LeftDown = new Position(p.x - 1, p.y - 1);
        Position rightDown = new Position(p.x + 1, p.y - 1);
        changeToWall(left);
        changeToWall(right);
        changeToWall(up);
        changeToWall(down);
        changeToWall(leftUp);
        changeToWall(rightUp);
        changeToWall(LeftDown);
        changeToWall(rightDown);


    }


    /**
     * if a position is nothing add it with wall
     */
    private static void addWall() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (randomTiles[x][y].equals(Tileset.FLOOR)) {
                    // fill the wall around for this floor
                    Position p = new Position(x, y);
                    fillWall(p);
                }
            }
        }
    }

    /**
     * generate Position For Room
     */
    private Position generatePositionForRoom() {
        return new Position(RandomUtils.uniform(RANDOM,1,  Engine.WIDTH - 9), RandomUtils.uniform(RANDOM,1,  Engine.HEIGHT - 9));
    }

    /**
     * create the world
     */
    public randomWorld(long seed) {
        this.RANDOM = new Random(SEED);
        // create the world with nothing
        SEED = seed;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        randomTiles = new TETile[WIDTH][HEIGHT];
        fillWithNothingTiles(randomTiles);


        for (int i = 0; i < RandomUtils.uniform(RANDOM, 30, 40); i++) {
            // generate random position
            Position p = generatePositionForRoom();
            for (Room room : Room.rooms) {
                while (Position.getDistanceByPosition(p, room.p) < 15) {
                   p = generatePositionForRoom();
                }
            }
            // create random room
            Room room = new Room(p, seed);
            // save the room
            room.saveRoomToRooms();
        }


//        Position p = new Position(3, 3);
//        Room room2 = new Room(p);
//        room2.saveRoomToRooms();
//
//        p = new Position(10, 11);
//        Room room1 = new Room(p);
//        room1.saveRoomToRooms();
//
//        // generate random position
//        Position p3 = Room.generatePositionForRoom();
//        // create random room
//        Room room3 = new Room(p);
//        // save the room
//        room3.saveRoomToRooms();

        List<Room> alreadyUsedRoom = new ArrayList<>();
        for (Room room : Room.rooms) {
            List<Room> adjacentRooms = room.getAdjacentRooms();
            alreadyUsedRoom.add(room);

            // make them connected
            for (Room adjacentRoom : adjacentRooms) {
                edge e = new edge(room.door, adjacentRoom.door);
                if (alreadyUsedRoom.contains(adjacentRoom)) {
                    continue;
                }
                alreadyUsedRoom.contains(adjacentRoom);
                // add edge to the graph
                Graph.addEdge(e);
                //Room.edgeTo(e.p1, e.p);

            }
        }


        // use Kruskal to generate spt
        TreeSet<edge> edges = Graph.KruskalToGenerateMst();
        for (edge e : edges) {
            Room.edgeTo(e.p, e.p1);
        }

        addWall();
        ter.renderFrame(randomTiles);
    }


}

