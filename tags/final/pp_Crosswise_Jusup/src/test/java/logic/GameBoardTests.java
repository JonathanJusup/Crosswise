package logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static logic.GameTiles.EMPTY;
import static logic.GameTiles.T_CROSS;
import static logic.GameTiles.T_PENTAGON;
import static logic.GameTiles.T_SQUARE;
import static logic.GameTiles.T_STAR;
import static logic.GameTiles.T_SUN;
import static logic.GameTiles.T_TRIANGLE;
import static logic.GameTiles.WC_MOVER;
import static logic.GameTiles.WC_REMOVER;
import static logic.GameTiles.WC_SWAPONBOARD;
import static logic.GameTiles.WC_SWAPWITHHAND;
import static logic.Utilities.transposeBoard;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GameBoardTests {

    @Test
    public void boardConstructor_different_sizes_test() {
        GameBoard board = new GameBoard(3);

        //Note: Rows and Columns are swapped
        GameTiles[][] expected = new GameTiles[][] {
                {EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY}
        };

        assertArrayEquals("Test empty GameBoard", expected, board.getGameBoard());
        Assert.assertEquals(3, board.getGameBoard().length);

        board = new GameBoard(6);
        Assert.assertEquals(6, board.getGameBoard().length);
        Assert.assertEquals("All EMPTY test (6 x 6)", 6 * 6, Arrays.stream(board.getGameBoard())
                .mapToInt(deepLayer -> (int) Arrays.stream(deepLayer)
                        .filter(tile -> tile.equals(EMPTY)).count()).sum());

        board = new GameBoard(50);
        Assert.assertEquals(50, board.getGameBoard().length);
        Assert.assertEquals("All EMPTY test (50 x 50)", 50 * 50, Arrays.stream(board.getGameBoard())
                .mapToInt(deepLayer -> (int) Arrays.stream(deepLayer)
                        .filter(tile -> tile.equals(EMPTY)).count()).sum());

        board = new GameBoard(100);
        Assert.assertEquals(100, board.getGameBoard().length);
        Assert.assertEquals("All EMPTY test (100 x 100)", 100 * 100, Arrays.stream(board.getGameBoard())
                .mapToInt(deepLayer -> (int) Arrays.stream(deepLayer)
                        .filter(tile -> tile.equals(EMPTY)).count()).sum());

    }

    @Test
    public void boardConstructor_loadedPreset_test() {

        int[][] input = new int[][] {
                {1, 4, 0},
                {2, 5, 1},
                {0, 4, 0}
        };

        GameBoard board = new GameBoard(input);

        GameTiles[][] expected = new GameTiles[][] {
                {T_SUN, T_SQUARE, EMPTY},
                {T_CROSS, T_PENTAGON, T_SUN},
                {EMPTY, T_SQUARE, EMPTY}
        };

        assertArrayEquals("Test custom GameBoard", expected, board.getGameBoard());
    }

    @Test
    public void getGameBoard_test() {

        int[][] input = new int[][]{
                {2, 4, 0},
                {2, 5, 1},
                {2, 4, 0}
        };

        GameBoard board = new GameBoard(input);


        GameTiles[][] expected = new GameTiles[][]{
                {T_CROSS, T_SQUARE, EMPTY},
                {T_CROSS, T_PENTAGON, T_SUN},
                {T_CROSS, T_SQUARE, EMPTY}
        };

        Assert.assertArrayEquals(expected, board.getGameBoard());
    }

    @Test
    public void getGameTileAt_test() {
        int[][] input = new int[][]{
                {2, 4, 0},
                {2, 5, 1},
                {1, 3, 0}
        };
        GameBoard board = new GameBoard(transposeBoard(input));

        Assert.assertEquals(T_CROSS, board.getGameTileAt(new Position(0, 0)));
        Assert.assertEquals(EMPTY, board.getGameTileAt(new Position(2, 2)));
        Assert.assertEquals(T_TRIANGLE, board.getGameTileAt(new Position(1, 2)));
        Assert.assertEquals(T_SUN, board.getGameTileAt(new Position(0, 2)));

    }

    @Test
    public void placeTileOnBoard_test() {
        int[][] input = new int[][]{
                {2, 4, 0},
                {0, 5, 1},
                {0, 4, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(input));

        board.placeTileOnBoard(T_SUN, new Position(0, 1));
        board.placeTileOnBoard(T_CROSS, new Position(2, 0));
        board.placeTileOnBoard(T_PENTAGON, new Position(2, 2));
        board.placeTileOnBoard(EMPTY, new Position(1, 1));

        int[][] expected = new int[][]{
                {2, 4, 2},
                {1, 0, 1},
                {0, 4, 5},
        };

        GameBoard expected_board = new GameBoard(transposeBoard(expected));
        Assert.assertArrayEquals(expected_board.getGameBoard(), board.getGameBoard());
    }

    @Test
    public void isFull_test() {
        int[][] input = new int[][]{
                {2, 4, 0, 5, 0, 0},
                {2, 5, 1, 0, 0, 4},
                {2, 4, 0, 4, 3, 0},
                {2, 2, 0, 2, 3, 0},
                {2, 2, 1, 5, 0, 5},
                {0, 1, 0, 4, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(input));
        Assert.assertFalse(board.isFull());

        input = new int[][]{
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1}
        };

        board = new GameBoard(transposeBoard(input));
        Assert.assertTrue(board.isFull());


        input = new int[][]{
                {1, 1},
                {1, 1}
        };

        board = new GameBoard(transposeBoard(input));
        Assert.assertTrue(board.isFull());


        input = new int[][]{
                {1}
        };

        board = new GameBoard(transposeBoard(input));
        Assert.assertTrue(board.isFull());
    }

    @Test
    public void isEmpty_test() {
        int[][] input = new int[][]{
                {2, 4, 0, 5, 0, 0},
                {2, 5, 1, 0, 0, 4},
                {2, 4, 0, 4, 3, 0},
                {2, 2, 0, 2, 3, 0},
                {2, 2, 1, 5, 0, 5},
                {0, 1, 0, 4, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(input));
        Assert.assertFalse(board.isEmpty());


        input = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        board = new GameBoard(transposeBoard(input));
        Assert.assertTrue(board.isEmpty());


        input = new int[][]{
                {0}
        };

        board = new GameBoard(transposeBoard(input));
        Assert.assertTrue(board.isEmpty());
    }

    @Test
    public void usedSpaces_test() {
        int[][] input = new int[][]{
                {2, 4, 0},
                {2, 5, 1},
                {2, 4, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(input));

        Assert.assertEquals(7, board.usedSpaces());
    }

    @Test
    public void isFreeAt_test() {
        int[][] input = new int[][]{
                {2, 4, 0, 5, 0, 0},
                {2, 5, 1, 0, 0, 4},
                {2, 4, 0, 4, 3, 0},
                {2, 2, 0, 2, 3, 0},
                {2, 2, 1, 5, 0, 5},
                {0, 1, 0, 4, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(input));

        Assert.assertTrue(board.isFreeAt(new Position(2, 0)));
        Assert.assertTrue(board.isFreeAt(new Position(2, 2)));
        Assert.assertTrue(board.isFreeAt(new Position(4, 1)));
        Assert.assertTrue(board.isFreeAt(new Position(5, 3)));

        Assert.assertFalse(board.isFreeAt(new Position(0, 0)));
        Assert.assertFalse(board.isFreeAt(new Position(4, 2)));
        Assert.assertFalse(board.isFreeAt(new Position(2, 1)));
        Assert.assertFalse(board.isFreeAt(new Position(3, 5)));
    }

    @Test
    public void getGameTileOccurrencesAtSegment_Columns_test() {
        int[][] input = new int[][]{
                {2, 4, 3},
                {3, 5, 6},
                {0, 1, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(input));
        Map<GameTiles, Integer> occurrences;

        occurrences= board.getGameTileOccurrencesAtSegment(0, true);

        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_SUN));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_CROSS));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_TRIANGLE));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_SQUARE));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_PENTAGON));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_STAR));

        occurrences = board.getGameTileOccurrencesAtSegment(1, true);

        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_SUN));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_CROSS));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_TRIANGLE));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_SQUARE));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_PENTAGON));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_STAR));

        occurrences = board.getGameTileOccurrencesAtSegment(2, true);

        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_SUN));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_CROSS));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_TRIANGLE));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_SQUARE));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_PENTAGON));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_STAR));
    }

    @Test
    public void getGameTileOccurrencesAtSegment_Rows_test() {
        int[][] input = new int[][]{
                {2, 4, 5},
                {3, 6, 1},
                {4, 0, 5}
        };

        GameBoard board = new GameBoard(transposeBoard(input));

        Map<GameTiles, Integer> occurrences = board.getGameTileOccurrencesAtSegment(0, false);

        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_SUN));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_CROSS));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_TRIANGLE));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_SQUARE));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_PENTAGON));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_STAR));

        occurrences = board.getGameTileOccurrencesAtSegment(1, false);

        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_SUN));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_CROSS));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_TRIANGLE));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_SQUARE));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_PENTAGON));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_STAR));

        occurrences = board.getGameTileOccurrencesAtSegment(2, false);

        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_SUN));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_CROSS));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_TRIANGLE));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_SQUARE));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(T_PENTAGON));
        Assert.assertEquals(Integer.valueOf(0), occurrences.get(T_STAR));
    }

    @Test
    public void gameTileOccurrences_test() {
        int[][] input = new int[][]{
                {1, 4, 0},
                {2, 5, 1},
                {0, 4, 0}
        };

        GameBoard board = new GameBoard(input);
        Map<GameTiles, Integer> expectedOccurrences = new HashMap<>();
        expectedOccurrences.put(T_SUN, 2);
        expectedOccurrences.put(T_CROSS, 1);
        expectedOccurrences.put(T_TRIANGLE, 0);
        expectedOccurrences.put(T_SQUARE, 2);
        expectedOccurrences.put(T_PENTAGON, 1);
        expectedOccurrences.put(T_STAR, 0);


        Map<GameTiles, Integer> gameTileOccurrences = new HashMap<>();
        gameTileOccurrences.put(T_SUN, board.getGameTileOccurrences(T_SUN));
        gameTileOccurrences.put(T_CROSS, board.getGameTileOccurrences(T_CROSS));
        gameTileOccurrences.put(T_TRIANGLE, board.getGameTileOccurrences(T_TRIANGLE));
        gameTileOccurrences.put(T_SQUARE, board.getGameTileOccurrences(T_SQUARE));
        gameTileOccurrences.put(T_PENTAGON, board.getGameTileOccurrences(T_PENTAGON));
        gameTileOccurrences.put(T_STAR, board.getGameTileOccurrences(T_STAR));

        for (Map.Entry<GameTiles, Integer> occurrence : expectedOccurrences.entrySet()) {
            int gameTileCount = gameTileOccurrences.get(occurrence.getKey());
            assertEquals(gameTileCount, occurrence.getValue().intValue());
        }
    }

    @Test
    public void gameTileOccurrences_wildcards_test() {
        int[][] input = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        GameBoard board = new GameBoard(input);
        Map<GameTiles, Integer> expectedOccurrences = new HashMap<>();
        expectedOccurrences.put(WC_REMOVER, 0);
        expectedOccurrences.put(WC_MOVER, 0);
        expectedOccurrences.put(WC_SWAPONBOARD, 0);
        expectedOccurrences.put(WC_SWAPWITHHAND, 0);

        Map<GameTiles, Integer> gameTileOccurrences = new HashMap<>();
        gameTileOccurrences.put(WC_REMOVER, board.getGameTileOccurrences(WC_REMOVER));
        gameTileOccurrences.put(WC_MOVER, board.getGameTileOccurrences(WC_MOVER));
        gameTileOccurrences.put(WC_SWAPONBOARD, board.getGameTileOccurrences(WC_SWAPONBOARD));
        gameTileOccurrences.put(WC_SWAPWITHHAND, board.getGameTileOccurrences(WC_SWAPWITHHAND));

        for (Map.Entry<GameTiles, Integer> occurrence : expectedOccurrences.entrySet()) {
            int gameTileCount = gameTileOccurrences.get(occurrence.getKey());
            assertEquals(gameTileCount, occurrence.getValue().intValue());
        }
    }
}
