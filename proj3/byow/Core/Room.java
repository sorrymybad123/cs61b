package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

/**
 *  create room for the world
 */
public class Room {
    private static final int MAXLength = 4;
    private static final int MINLength = 2;

    private static final int thresholdOfAdjacentRooms = (MINLength + MINLength) * 2;
    private static final int thresholdOfAdjacentRooms_no =  100*thresholdOfAdjacentRooms;
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
    public Room(Position p, long seed, TETile[][] randomTiles) {
        RANDOM = new Random(seed);
        boolean roomCreated = false;
        // determine whether we need to do it again
        while (!roomCreated) {
            roomCreated = createRandomRectangle(p, randomTiles);
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
            if (distance < thresholdOfAdjacentRooms_no && distance != 0) {
                // not itself
                adjacentRooms.add(otherRoom);
            }
        }
        return adjacentRooms;
    }


    /**
     * 生成远离现有房间的随机位置
     */
    public Position generateFarPositionForRoom() {
        Position position;
        boolean isFarEnough;
        do {
            position = generatePositionForRoom();
            isFarEnough = checkIfFarEnough(position, rooms);
        } while (!isFarEnough);
        return position;
    }

    /**
     * 检查新生成的房间是否远离现有房间
     */
    private static boolean checkIfFarEnough(Position newPosition, List<Room> existingRooms) {
        for (Room room : existingRooms) {
            if (Position.getDistanceByPosition(newPosition, room.p) < thresholdOfAdjacentRooms) {
                return false;
            }
        }
        return true;
    }


    /**
     * generate a point
     */
    public  Position generatePositionForRoom() {
        return new Position(RandomUtils.uniform(RANDOM,1 + MINLength,  Engine.WIDTH - MAXLength - 1), RandomUtils.uniform(RANDOM,1 + MINLength,  Engine.HEIGHT - MAXLength - 1));
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
    public static void makeHallwayLiner(Position p, Position p1, TETile[][] randomTiles) {
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
            randomTiles[i][p.y] = Tileset.FLOOR;
        }

    }





    /**
     * make a vertical hallWay
     */
    public static void makeVerticalHallway(Position p, Position p1, TETile[][] randomTiles) {
        if (!(p.x == p1.x)) {
            return;
        }
        int rightY;
        int originY;
        int originX = p.x;

        rightY = Math.abs(p.y - p1.y);
        originY = Math.min(p.y, p1.y);

        for (int i = originY; i < rightY + originY + 1; i++) {
            randomTiles[originX][i] = Tileset.FLOOR;
        }
    }


    /**
     * make a bias hallWay
     */
    public static void makeBiasHallWay(Position p, Position p1, TETile[][] randomTiles) {
        Position intersection = new Position(p.x, p1.y);
        makeVerticalHallway(p, intersection, randomTiles);
        makeHallwayLiner(p1, intersection, randomTiles);
    }

    /**
     * link the edge from
     */



    /**
     * room to other room
     */
    public static void edgeTo(Position thisPosition, Position otherPosition, TETile[][] randomTiles) {

        // it is vertical
        if (thisPosition.x == otherPosition.x && thisPosition.y != otherPosition.y) {
            makeVerticalHallway(thisPosition, otherPosition, randomTiles);
        // it is linear
        } else if (thisPosition.x != otherPosition.x && thisPosition.y == otherPosition.y) {
            makeHallwayLiner(thisPosition, otherPosition, randomTiles);
        // it is bias
        } else {
           makeBiasHallWay(thisPosition, otherPosition, randomTiles);
        }



    }


    public boolean createRandomRectangle(Position p, TETile[][] randomTiles) {
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
                randomTiles[i][j] = Tileset.FLOOR;
                Position position = new Position(i, j);
                roomsPositions.add(position);
            }
        }
        return true;
    }

}
