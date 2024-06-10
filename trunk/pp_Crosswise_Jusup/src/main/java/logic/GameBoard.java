package logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * GameBoard Class which contains the GameBoard Array.
 * GameBoard can be any Size except 0 x 0. Contains helpful
 * methods to analyze the gameBoard.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
class GameBoard {

    /**
     * GameTiles Array for efficient traversal
     */
    private final GameTiles[][] gameBoard;


    /**
     * GameBoard Constructor, which takes gameBoard size.
     * Initializes each gameBoard cell with EMPTY GameTile
     *
     * @param size gameBoard size
     */
    GameBoard(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("GameBoard Size cannot be 0");
        }

        gameBoard = new GameTiles[size][size];
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                this.gameBoard[x][y] = GameTiles.EMPTY;
            }
        }
    }

    /**
     * GameBoard Constructor. For loading an existing board from a loaded game.
     * Loaded board Array needs to be converted from ordinal-value to GameTile.
     *
     * @param board existing board array
     */
    GameBoard(int[][] board) {
        gameBoard = new GameTiles[board.length][board.length];

        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                this.gameBoard[x][y] = Utilities.ordinalToGameTiles(board[x][y]);
            }
        }
    }


    /**
     * Getter for gameBoard. Returns copy of gameBoard.
     *
     * @return copy of gameBoard
     */
    GameTiles[][] getGameBoard() {
        return Arrays.stream(gameBoard).map(GameTiles[]::clone).toArray(GameTiles[][]::new);
    }

    /**
     * Gets GameTile at specified Position on gameBoard.
     *
     * @param pos Position
     * @return GameTile at pos
     */
    GameTiles getGameTileAt(Position pos) {
        return gameBoard[pos.column()][pos.row()];
    }

    /**
     * Places specified GameTile on specified Position on gameBoard.
     *
     * @param tile GameTile to place
     * @param pos  Position where to place
     */
    void placeTileOnBoard(GameTiles tile, Position pos) {
        gameBoard[pos.column()][pos.row()] = tile;
    }

    /**
     * Check if GameBoard is filled with non-EMPTY GameTiles.
     * Terminates early, if EMPTY GameTile is found.
     *
     * @return No EMPTY GameTiles left -> TRUE
     */
    boolean isFull() {
        boolean isFull = true;

        for (int x = 0; x < gameBoard.length && isFull; x++) {
            for (int y = 0; y < gameBoard[x].length && isFull; y++) {
                isFull = gameBoard[x][y] != GameTiles.EMPTY;
            }
        }

        return isFull;
    }

    /**
     * Check if GameBoard is filled with EMPTY GameTiles.
     * Terminates early, if non-EMPTY GameTile is found
     *
     * @return All EMPTY GameTiles -> TRUE
     */
    boolean isEmpty() {
        boolean isEmpty = true;

        for (int x = 0; x < gameBoard.length && isEmpty; x++) {
            for (int y = 0; y < gameBoard[x].length && isEmpty; y++) {
                isEmpty = gameBoard[x][y] == GameTiles.EMPTY;
            }
        }

        return isEmpty;
    }

    /**
     * Counts non-EMPTY GameTiles on gameBoard.
     *
     * @return number of non-EMPTY GameTiles
     */
    int usedSpaces() {
        int usedSpaces = 0;

        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                if (!isFreeAt(new Position(x, y))) {
                    usedSpaces++;
                }
            }
        }

        return usedSpaces;
    }

    /**
     * Checks if given position is free (EMPTY field on GameBoard cell)
     *
     * @param position Position on GameBoard
     * @return Cell has EMPTY GameTile -> TRUE
     */
    boolean isFreeAt(Position position) {
        return gameBoard[position.column()][position.row()] == GameTiles.EMPTY;
    }

    /**
     * Counts occurrences of specified GameTile on whole gameBoard.
     *
     * @param toCount tile to count occurrences
     * @return Map of GameTiles and their occurrence
     */
    int getGameTileOccurrences(GameTiles toCount) {
        return Arrays.stream(gameBoard)
                .map(deepLayer -> Arrays.stream(deepLayer)
                        .filter(tile -> tile.equals(toCount))
                        .count())
                .mapToInt(Long::intValue).sum();
    }

    /**
     * Counts all GameTile occurrences on gameBoard for a specified segment.
     * A segment an Index of either a Row or Column. Which one it is, is
     * specified by isVertical Flag
     *
     * @param segment    segment Index
     * @param isVertical isVertical Flag, if Column or Row
     * @return Map of GameTiles and their occurrence
     */
    Map<GameTiles, Integer> getGameTileOccurrencesAtSegment(int segment, boolean isVertical) {

        //Initialize Map of GameTiles with their occurrences on specified segment
        Map<GameTiles, Integer> result = new HashMap<>();
        GameTiles[] gameTiles = GameTiles.values();
        for (int i = 1; i < gameTiles.length; i++) {
            result.put(gameTiles[i], 0);
        }

        //Increment result map entry at occurring tile
        for (int i = 0; i < gameBoard.length; i++) {
            GameTiles tile = gameBoard[isVertical ? segment : i][isVertical ? i : segment];
            if (result.containsKey(tile)) {
                result.replace(tile, result.get(tile) + 1);
            }
        }

        return result;
    }

    /**
     * Custom toString Method for the gameBoard with optional coloring of GameTiles.
     * Represents each GameTile as its ordinal value for better readability.
     * <p>
     * Coloring messes up Log File. If Log File is of interest, don't use coloring.
     *
     * @return GameBoard as String representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < gameBoard.length; x++) {
            sb.append("[");
            for (int y = 0; y < gameBoard[x].length; y++) {

                //Append with optional Coloring
                sb.append(Utilities.LOG_IN_COLOR
                        ? Utilities.colorGameTileMsg(gameBoard[y][x])
                        : gameBoard[y][x].ordinal());

                //Only append Space, if not last element
                if (y < gameBoard[x].length - 1) {
                    sb.append(" ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
