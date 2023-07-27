package game2048;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author lv ze huan
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */


    public boolean tilt(Side side) {
        boolean changed;
        changed = false;
        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        boolean add_score = false;
        boolean same_colum = false;
        int size = board.size();
        HashMap hashmap = new HashMap<>();
        Board temp;
        // up
        if (side == Side.NORTH){
            for (int c = size - 1; c >= 0; c -= 1){
                for (int r = size - 1; r >= 0; r -= 1){
                    add_score = false;
                    Tile t = board.tile(c, r);
                    if(t != null){
                        int num_of_null = checkupisnull(board, c, r + 1);
                        int row = num_of_null + r;
                        // check the above board is exist
                        if (row + 1 < size) {
                            if (t.value() == board.tile(c, row + 1).value() && !(hashmap.get(board.tile(c, row + 1)) == "true")) {
                                row = row + 1;
                                add_score = true;
                            }
                        }
                        if (row != r){
                            board.move(c, row, t);
                            changed = true;

                        }
                        if (add_score == true ){
                            score += board.tile(c, row).value();
                            hashmap.put(board.tile(c, row), "true");
                        }
                    }

                }
            }
        } else if (side == Side.WEST) {
            for (int r = 0; r < size; r += 1) {
                for (int c = 0; c < size; c += 1) {
                    add_score = false;
                    Tile t = board.tile(c, r);
                    if (t != null) {
                        int num_of_null = checkleftisnull(board, c - 1, r);
                        int col = c - num_of_null;
                        // check the above board is exist
                        if (col - 1  >=  0 ){
                            if (t.value() == board.tile(col-1, r).value() && !(hashmap.get(board.tile(col - 1, r)) == "true")) {
                                col -= 1;
                                add_score = true;
                            }
                        }
                        if (col != c) {
                            board.move(col, r, t);
                            changed = true;
                        }
                        if (add_score == true) {
                            score += board.tile(col, r).value();
                            hashmap.put(board.tile(col, r), "true");
                        }

                    }
                }
            }
        }
        else if (side == Side.EAST) {
            for (int r = size - 1; r >= 0; r -= 1) {
                same_colum = false;
                for (int c = size - 1; c >= 0; c -= 1) {
                    add_score = false;
                    Tile t = board.tile(c, r);
                    if (t != null) {
                        int num_of_null = checkrightisnull(board, c + 1, r);
                        int col = num_of_null + c;
                        // check the above board is exist
                        if (col + 1 <= size - 1) {
                            if (t.value() == board.tile(col + 1, r).value() && !(hashmap.get(board.tile(col + 1, r)) == "true") ) {
                                col += 1;
                                add_score = true;
                                same_colum = true;
                            }
                        }
                        if (col != c) {
                            board.move(col, r, t);
                            changed = true;
                        }
                        if (add_score) {
                            score += board.tile(col, r).value();
                            hashmap.put(board.tile(col, r), "true");
                        }
                    }
                }
            }
        } else if (side == Side.SOUTH) {
            for (int c = 0; c < size; c += 1) {
                for (int r = 0; r < size; r += 1) {
                    add_score = false;
                    Tile t = board.tile(c, r);
                    if (t != null) {
                        int num_of_null = checkdownisnull(board, c, r - 1);
                        int row = r - num_of_null;
                        // check the above board is exist
                        if (row - 1 >= 0) {
                            if (t.value() == board.tile(c, row - 1).value() && !(hashmap.get(board.tile(c, row - 1)) == "true") ) {
                                row = row - 1;
                                add_score = true;
                            }
                        }
                        if (row != r) {
                            board.move(c, row, t);
                            changed = true;
                        }
                        if (add_score == true) {
                            score += board.tile(c, row).value();
                            hashmap.put(board.tile(c, row), "true");
                        }
                    }
                }
            }

        }

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }



    public static int checkrightisnull(Board b, int c, int r){
        if (c > b.size() - 1){
            return 0;
        } else if (b.tile(c, r) != null) {
            return 0;

        } else{
            return 1 + checkrightisnull(b, c + 1, r);
        }
    }


    public static  int checkleftisnull(Board b, int c, int r){
        if (c < 0){
            return 0;
        } else if (b.tile(c, r) != null) {
            return 0;

        }else{
            return 1 + checkleftisnull(b, c - 1, r);

        }
    }




    public static int checkdownisnull(Board b, int c, int r){
        if (r < 0){
            return 0;
        } else if (b.tile(c, r) != null) {
            return 0;
        }else{
            return 1 + checkdownisnull(b, c, r - 1);
        }
    }
    public static int checkupisnull(Board b, int c, int r){
        if (r > b.size() - 1){
            return 0;
        } else if (b.tile(c, r) != null) {
            return 0;
        } else{
            return 1 + checkupisnull(b, c, r + 1);
        }
    }



    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        int size_b = b.size();
        for (int col = 0; col < size_b; col += 1) {
            for (int row = 0; row < size_b; row += 1) {
                Tile tile_value = b.tile(col, row);
                if (tile_value == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int size_b = b.size();
        for (int col = 0; col < size_b; col += 1) {
            for (int row = 0; row < size_b; row += 1) {
                Tile tile_value = b.tile(col, row);
                if (tile_value == null){
                    continue;
                }
                int x = tile_value.value();
                if (x == MAX_PIECE) {
                    return true;
                }

            }
        }
        return false;
    }


    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        int size_b = b.size();
        for (int col = 0; col < size_b; col += 1) {
            for (int row = 0; row < size_b; row += 1) {
                Tile tile_value = b.tile(col, row);
                if (tile_value == null){
                    return true;
                }
                int x = tile_value.value();

                // test the right value
                if(col < size_b - 1){
                    Tile value_right = b.tile(col + 1, row );
                    if (value_right != null){
                        int y = value_right.value();
                        if (y == x){
                            return true;
                        }
                    }

                }
          // test the down value
                if (row < size_b - 1){
                    if (b.tile(col, row + 1) != null){
                        int value_down = b.tile(col, row + 1).value();
                        if (value_down == x){
                            return true;
                        }

                    }
                }



            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
