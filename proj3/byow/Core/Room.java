package byow.Core;

import byow.TileEngine.Tileset;

import java.util.*;

/**
 *  create room for the world
 */
public class Room {
    private static final int MAXLength = 8;
    private static final int MINLength = 2;

    private static final int thresholdOfAdjacentRooms = 30;
    private static HashSet<Position> roomsPositions = new HashSet<>();
    public static List<Room> rooms = new ArrayList<>();


    public Random RANDOM;


    int width;
    int height;
    Position p;
    Position door;


    /**
     * room constructor
     * @param p
     */
    public Room(Position p, long seed) {
        RANDOM = new Random(seed);
        boolean roomCreated = false;
        // determine whether we need to do it again
        while (!roomCreated) {
            roomCreated = createRandomRectangle(p);
            if (!roomCreated) {
                p = generatePositionForRoom();
            }
        }
        this.p = p;
    }

    /**
     * add room to rooms
     */
    public void saveRoomToRooms() {
        rooms.add(this);
    }

    /**
     * get adjacent rooms
     */
    public List<Room> getAdjacentRooms() {
        List<Room> adjacentRooms = new ArrayList<>();

        for (Room otherRoom : rooms) {
            double distance = this.getDistanceBetween(otherRoom);
            if (distance < thresholdOfAdjacentRooms && distance != 0) {
                // not itself
                adjacentRooms.add(otherRoom);
            }
        }
        return adjacentRooms;
    }



    /**
     * generate a point
     */
    public  Position generatePositionForRoom() {
        return new Position(RandomUtils.uniform(RANDOM,1,  Engine.WIDTH - 9), RandomUtils.uniform(RANDOM,1,  Engine.HEIGHT - 9));
    }


    /**
     * get the distance between two rooms
     */
    public double getDistanceBetween(Room room) {
        return Math.sqrt(Math.pow(this.door.x - room.door.x, 2) + Math.pow(this.door.y - room.door.y, 2));
    }



    /**
     * make a hallway of liner
     */
    public static void makeHallwayLiner(Position p, Position p1) {
        if (p.y != p1.y) {
            return;
        }

        int linerDistance;
        int rightX;
        int x;

        if (p.x > p1.x) {
            linerDistance = p.x - p1.x;
            rightX = p1.x + linerDistance;
            x = p1.x;
        } else {
            linerDistance = p1.x - p.x;
            rightX = p.x + linerDistance;
            x = p.x;
        }


        for (int i = x; i < rightX; i++) {
        // put the floor in the randomTiles
            randomWorld.randomTiles[i][p.y] = Tileset.FLOOR;
        }

    }





    /**
     * make a vertical hallWay
     */
    public static void makeVerticalHallway(Position p, Position p1) {
        if (!(p.x == p1.x)) {
            return;
        }
        int rightY;
        int originY;
        int originX = p.x;

        rightY = Math.abs(p.y - p1.y);
        originY = Math.min(p.y, p1.y);

        for (int i = originY; i < rightY + originY + 1; i++) {
            randomWorld.randomTiles[originX][i] = Tileset.FLOOR;
        }
    }


    /**
     * make a bias hallWay
     */
    public static void makeBiasHallWay(Position p, Position p1) {
        Position intersection = new Position(p.x, p1.y);
        makeVerticalHallway(p, intersection);
        makeHallwayLiner(p1, intersection);
    }

    /**
     * link the edge from
     */



    /**
     * room to other room
     */
    public static void edgeTo(Position thisPosition, Position otherPosition) {

        // it is vertical
        if (thisPosition.x == otherPosition.x && thisPosition.y != otherPosition.y) {
            makeVerticalHallway(thisPosition, otherPosition);
        // it is linear
        } else if (thisPosition.x != otherPosition.x && thisPosition.y == otherPosition.y) {
            makeHallwayLiner(thisPosition, otherPosition);
        // it is bias
        } else {
           makeBiasHallWay(thisPosition, otherPosition);
        }



    }


    public boolean createRandomRectangle(Position p) {
        width = RandomUtils.uniform(RANDOM, MINLength, MAXLength);
        height = RandomUtils.uniform(RANDOM, MINLength, MAXLength);

        int rightX = p.x + width - 1;
        int bottomY = p.y + height - 1;

        door = new Position(RandomUtils.uniform(RANDOM, p.x, rightX),RandomUtils.uniform(RANDOM, p.y, bottomY));
        // test the last rectangle if it is overlap
        for (int i = p.x; i <=  rightX + 8; i++) {
            for (int j = p.y; j <= bottomY + 8; j++) {
                Position position = new Position(i, j);
                if (roomsPositions.contains(position)) {
                    return false;
                }
            }
        }

        for (int i = p.x; i <  rightX; i++) {
            for (int j = p.y; j < bottomY; j++) {
                randomWorld.randomTiles[i][j] = Tileset.FLOOR;
                Position position = new Position(i, j);
                roomsPositions.add(position);
            }
        }
        return true;
    }

}
