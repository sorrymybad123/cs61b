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

    /**
     * get distance by position
     * @param p
     * @param p1
     * @return
     */
    public static double getDistanceByPosition(Position p, Position p1) {
        return Math.sqrt(Math.pow(p.x - p1.x, 2) + Math.pow(p.y - p1.y, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position oas = (Position)o;
            if (this.y ==  oas.y && this.x == oas.x) {
                return true;
            }
            return false;
        }
        return false;
    }


}
