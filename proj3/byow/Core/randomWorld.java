package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

/**
 * Draws a world that contains RANDOM tiles.
 */
public class randomWorld {
    private static final int WIDTH = Engine.WIDTH;
    private static final int HEIGHT = Engine.HEIGHT;

    private static final int partitionWidth = WIDTH - 1 / Room.MAXLength;

    private static final int partitionHeight = HEIGHT - 1 / Room.MAXLength;

    public  List<Position> partitionsPositions;

    private long SEED;
    public  Random RANDOM;
    public TETile[][] randomTiles;



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
    private static TETile getTile(Position p, TETile[][] randomTiles) {
        return randomTiles[p.x][p.y];
    }

    /**
     * change it to wall
     */
    private static void changeToWall(Position p, TETile[][] randomTiles) {
        if (getTile(p, randomTiles).equals(Tileset.NOTHING)) {
            // change that tile in that position
            randomTiles[p.x][p.y] = Tileset.WALL;
        }
    }


    /**
     * fill the wall around
     */
    private static void fillWall(Position p, TETile[][] randomTiles) {
        Position left = new Position(p.x - 1, p.y);
        Position right = new Position(p.x + 1, p.y);
        Position up = new Position(p.x, p.y + 1);
        Position down = new Position(p.x, p.y - 1);
        Position leftUp = new Position(p.x - 1, p.y + 1);
        Position rightUp = new Position(p.x + 1, p.y + 1);
        Position LeftDown = new Position(p.x - 1, p.y - 1);
        Position rightDown = new Position(p.x + 1, p.y - 1);
        changeToWall(left, randomTiles);
        changeToWall(right, randomTiles);
        changeToWall(up, randomTiles);
        changeToWall(down, randomTiles);
        changeToWall(leftUp, randomTiles);
        changeToWall(rightUp, randomTiles);
        changeToWall(LeftDown, randomTiles);
        changeToWall(rightDown, randomTiles);


    }


    /**
     * if a position is nothing add it with wall
     */
    private static void addWall(TETile[][] randomTiles) {
        for (int x = 0; x < WIDTH - 1; x += 1) {
            for (int y = 0; y < HEIGHT - 1; y += 1) {
                if (randomTiles[x][y].equals(Tileset.FLOOR)) {
                    // fill the wall around for this floor
                    Position p = new Position(x, y);
                    fillWall(p, randomTiles);
                }
            }
        }
    }

    /**
     * create room
     */
    public void createRoom(Position p, TETile[][] randomTiles) {
        Room roomConner = new Room(p, SEED, randomTiles);
        roomConner.saveRoomToRooms();
    }

    /**
     * partition
     */
    public void setPartitionsPositions(int width, int height) {
        partitionsPositions = new ArrayList<>();
        int addWidth =  Math.round(width / partitionWidth);
        int addHeight = Math.round(height / partitionHeight);
        for (int x = 1; x < WIDTH; x+=addWidth) {
            for (int y = 1; y < HEIGHT; y+=addHeight) {
                Position p = new Position(x, y);
                partitionsPositions.add(p);
            }
        }
    }

    /**
     * generate Position For Room
     */
    private Position generatePositionForRoom() {
        int x = RandomUtils.uniform(RANDOM, 1, WIDTH - 9);
        int y = RandomUtils.uniform(RANDOM,1, HEIGHT - 9);
        return new Position(x, y);
    }

    /**
     * create the world
     */
    public randomWorld(long seed, TETile[][] randomTiles) {

        SEED = seed;
        this.randomTiles = randomTiles;
        this.RANDOM = new Random(SEED);
        // create the world with nothing
        // TERenderer ter = new TERenderer();
        // ter.initialize(WIDTH, HEIGHT);
        fillWithNothingTiles(randomTiles);
        // TODO partition this world
        setPartitionsPositions(WIDTH, HEIGHT);

        for (int i = 0; i < 30; i++) {
            int randomIndex = RandomUtils.uniform(RANDOM, 0 , partitionsPositions.size());
            Position p = partitionsPositions.remove(randomIndex);
            Room room = new Room(p, seed, randomTiles);
            room.saveRoomToRooms();
        }


        // create a room in right up conner
//        Position pConner = new Position(71, 21);
//        Room roomConner = new Room(pConner, SEED, randomTiles);
//        roomConner.saveRoomToRooms();
//        for (int i = 0; i < RandomUtils.uniform(RANDOM, 34, 35); i++) {
//            // generate random position
//            Position p = roomConner.generatePositionForRoom();
//            for (Room room : Room.rooms) {
//                while (Position.getDistanceByPosition(p, room.p) < 15) {
//                   p = generatePositionForRoom();
//                }
//            }
//            // create random room
//            Room room = new Room(p, seed, randomTiles);
//            // save the room
//            room.saveRoomToRooms();
//        }


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
                // add edge to the graph
                Graph.addEdge(e);
                //Room.edgeTo(e.p1, e.p);

            }
        }


        // use Kruskal to generate spt
        TreeSet<edge> edges = Graph.KruskalToGenerateMst();
        for (edge e : edges) {
            Room.edgeTo(e.p, e.p1, randomTiles);
        }

        addWall(randomTiles);
        //ter.renderFrame(randomTiles);
    }


}

