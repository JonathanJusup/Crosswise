package logic;

/**
 * TODO: Maybe put this whole thing in the game class?!
 *
 * Logic for the game Crosswise. It handles the point calculation of the game
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public final class Logic {

    /**
     * Calculates GameTile Count per Type of given segment Row / Column
     *
     * @param gameBoard current gameBoard
     * @param segment segment of Row / Column
     * @param isRow is Row or Column
     * @return GameTile Count per Type
     */
    public static int[] getTilesPerSegment(GameTiles[][] gameBoard, int segment, boolean isRow) {
        int boardSize = gameBoard.length; //Always square size row-wise and col-wise
        int[] tileCount = new int[6]; //6 Game Tiles + EMPTY

        for (int i = 0; i < boardSize; i++) {
            switch (gameBoard[isRow ? i: segment][isRow ? segment : i]) {
                case T_SUN      -> tileCount[0]++;
                case T_CROSS    -> tileCount[1]++;
                case T_TRIANGLE -> tileCount[2]++;
                case T_SQUARE   -> tileCount[3]++;
                case T_PENTAGON -> tileCount[4]++;
                case T_STAR     -> tileCount[5]++;
            }
        }

        return tileCount;
    }

    /**
     * Calculates points of given GameTiles Array based on Tile combination point rules
     *      All different = 6 Points
     *      2x same tiles = 1 Point
     *      3x same tiles = 3 Points
     *      4x same tiles = 5 Points
     *      5x same tiles = 7 Points
     *      6x same tiles = WIN
     *
     * @param gameTiles GameTiles of given Row / Column
     * @return points
     */
    public static int getPoints(int[] gameTiles) {
        int points = 0;

        //6 Points for all different Game Tiles
        boolean allDifferent = true;
        for (int i = 0; i < gameTiles.length && allDifferent; i++) {
            allDifferent = gameTiles[i] == 1;
        }

        if (allDifferent) {
            points = 6;
        } else {
            for (int gameTile : gameTiles) {
                switch (gameTile) {
                    case 2 -> points += 1;                  //2x same tiles
                    case 3 -> points += 3;                  //3x same tiles
                    case 4 -> points += 5;                  //4x same tiles
                    case 5 -> points += 7;                  //5x same tiles
                    case 6 -> points = Integer.MAX_VALUE;   //6x same tiles
                }
            }
        }

        return points;
    }

    /**
     * Calculates Team points by considering all Rows / Columns.
     * TODO: Integer.MAX_VALUE as Instant Win value good or bad?
     *
     * @param gameBoard Current gameBoard
     * @param vertical Team which points to calculate
     * @return Team Points | Integer.MAX_VALUE if Instant Win
     */
    public static int getTeamPoints(GameTiles[][] gameBoard, boolean vertical) {
        int pointSum = 0;

        for (int i = 0; i < gameBoard.length && pointSum != Integer.MAX_VALUE; i++) {
            int segmentPoints = getPoints(getTilesPerSegment(gameBoard, i, vertical));
            pointSum = segmentPoints != Integer.MAX_VALUE ? pointSum + segmentPoints : segmentPoints;
        }
        
        return pointSum;
    }
}
