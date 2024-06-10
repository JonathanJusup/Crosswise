package logic;

/**
 * GameBoard Class which contains the GameBoard Array. Theoretically
 * the GameBoard can be any Size except 0 x 0
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class GameBoard {
    public GameTiles[][] gameBoard = new GameTiles[6][6];
    private final GameTiles[] gameTiles = GameTiles.values();

    /**
     * GameBoard Constructor with preset EMPTY GameTiles (none.png)
     */
    public GameBoard() {
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                this.gameBoard[x][y] = GameTiles.EMPTY;
            }
        }
    }

    /**
     * GameBoard Constructor with given board array, when loading
     * existing game
     *
     * @param board existing board array
     */
    public GameBoard(int[][] board) {
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                this.gameBoard[x][y] = ordinalToGameTile(board[x][y]);
            }
        }
    }

    /**
     * Converts ordinal value to GameTile. Checks, if there exists a
     * GameTile corresponding to the given ordinal value
     *
     * @param ordinal Ordinal value
     * @return Corresponding GameTile
     */
    private GameTiles ordinalToGameTile(int ordinal) {
        assert ordinal >= 0 && ordinal < gameTiles.length;

        return gameTiles[ordinal];
    }

    /**
     * Check if GameBoard is filled with non-EMPTY tiles
     *
     * @return GameBoard full -> TRUE ; else FALSE
     */
    public boolean isFull() {
        boolean isFull = true;

        for (int x = 0; x < gameBoard.length && isFull; x++) {
            for (int y = 0; y < gameBoard[x].length && isFull; y++) {
                isFull = gameBoard[x][y] != GameTiles.EMPTY;
            }
        }

        return isFull;
    }

    /**
     * Checks if given position is free (EMPTY field on GameBoard cell)
     *
     * @param position Position on GameBoard
     * @return If cell is empty -> TRUE ; else FALSE
     */
    public boolean isFree(Position position) {
        return gameBoard[position.getRow()][position.getColumn()] == GameTiles.EMPTY;
    }
}
