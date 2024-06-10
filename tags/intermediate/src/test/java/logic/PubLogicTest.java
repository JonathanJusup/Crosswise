package logic;

import org.junit.Test;
import static logic.GameTiles.*;
import static org.junit.Assert.*;

/**
 * Public Logic Test for the game Crosswise
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class PubLogicTest {

    @Test
    public void boardConstructor_standard_test() {
        GameBoard board = new GameBoard();

        //Note: Rows and Columns are swapped
        GameTiles[][] expected = new GameTiles[][] {
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY}
        };

        assertArrayEquals("Test empty GameBoard", expected, board.gameBoard);
    }

    @Test
    public void boardConstructor_custom_test() {

        int[][] input = new int[][] {
                {1, 4, 0, 5, 0, 0},
                {2, 5, 1, 0, 0, 4},
                {0, 4, 0, 4, 3, 0},
                {5, 2, 0, 2, 3, 0},
                {2, 2, 1, 5, 0, 5},
                {0, 1, 0, 4, 0, 0}
        };

        GameBoard board = new GameBoard(input);

        GameTiles[][] expected = new GameTiles[][] {
                {T_SUN, T_SQUARE, EMPTY, T_PENTAGON, EMPTY, EMPTY},
                {T_CROSS, T_PENTAGON, T_SUN, EMPTY, EMPTY, T_SQUARE},
                {EMPTY, T_SQUARE, EMPTY, T_SQUARE, T_TRIANGLE, EMPTY},
                {T_PENTAGON, T_CROSS, EMPTY, T_CROSS, T_TRIANGLE, EMPTY},
                {T_CROSS, T_CROSS, T_SUN, T_PENTAGON, EMPTY, T_PENTAGON},
                {EMPTY, T_SUN, EMPTY, T_SQUARE, EMPTY, EMPTY}
        };

        assertArrayEquals("Test custom GameBoard", expected, board.gameBoard);
    }


    //Evaluate Rows (6 Tests):::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Test
    public void evaluateRow01_test() {
        int[][] testBoard = new int[][] {
                {1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 0, true);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Row 1", 0, points);
    }

    @Test
    public void evaluateRow02_test() {
        int[][] testBoard = new int[][] {
                {0, 2, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 1, true);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Row 2", 1, points);
    }

    @Test
    public void evaluateRow03_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 3, 0, 0, 0},
                {0, 0, 3, 0, 0, 0},
                {0, 0, 3, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 2, true);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Row 3", 3, points);
    }

    @Test
    public void evaluateRow04_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 4, 0, 0},
                {0, 0, 0, 4, 0, 0},
                {0, 0, 0, 4, 0, 0},
                {0, 0, 0, 4, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 3, true);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Row 4", 5, points);
    }

    @Test
    public void evaluateRow05_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 5, 0},
                {0, 0, 0, 0, 5, 0},
                {0, 0, 0, 0, 5, 0},
                {0, 0, 0, 0, 5, 0},
                {0, 0, 0, 0, 5, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 4, true);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Row 5", 7, points);
    }

    @Test
    public void evaluateRow06_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 0, 6},
                {0, 0, 0, 0, 0, 6},
                {0, 0, 0, 0, 0, 6},
                {0, 0, 0, 0, 0, 6},
                {0, 0, 0, 0, 0, 6},
                {0, 0, 0, 0, 0, 6}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 5, true);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Row 6", Integer.MAX_VALUE, points);
    }

    //Evaluate Columns (6 Tests)::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Test
    public void evaluateColumn01_test() {
        int[][] testBoard = new int[][] {
                {1, 1, 2, 2, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 0, false);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Col 1", 2, points);
    }

    @Test
    public void evaluateColumn02_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 0, 0},
                {3, 3, 4, 4, 5, 5},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 1, false);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Col 2", 3, points);
    }

    @Test
    public void evaluateColumn03_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {5, 5, 5, 6, 6, 6},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 2, false);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Col 3", 6, points);
    }

    @Test
    public void evaluateColumn04_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 3, 3},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 3, false);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Col 4", 6, points);
    }

    @Test
    public void evaluateColumn05_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {3, 3, 3, 3, 3, 4},
                {0, 0, 0, 0, 0, 0}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 4, false);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Col 5", 7, points);
    }

    @Test
    public void evaluateColumn06_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {1, 2, 3, 4, 5, 6}
        };
        GameBoard gameBoard = new GameBoard(testBoard);
        int[] tilesOfSegment = Logic.getTilesPerSegment(gameBoard.gameBoard, 5, false);
        int points = Logic.getPoints(tilesOfSegment);
        assertEquals("Test Points for Col 6", 6, points);
    }

    //Horizontal Evaluation
    @Test
    public void evaluateHorizontal_test() {
        int[][] testBoard = new int[][] {
                {1, 0, 0, 0, 0, 0},     //0 Points
                {1, 1, 0, 0, 0, 0},     //1 Points
                {1, 1, 1, 0, 0, 0},     //3 Points
                {1, 1, 1, 1, 0, 0},     //5 Points
                {1, 1, 1, 1, 1, 0},     //7 Points
                {1, 2, 3, 4, 5, 6}      //6 Points
        };
        GameBoard gameBoard = new GameBoard(testBoard);

        int points = Logic.getTeamPoints(gameBoard.gameBoard, false);
        assertEquals("Test horizontal Team Points", 22, points);
    }

    //Vertical Evaluation
    @Test
    public void evaluateVertical_test() {
        int[][] testBoard = new int[][] {
                {1, 1, 1, 4, 5, 0},     //1 Points
                {1, 1, 1, 4, 5, 0},     //2 Points
                {0, 2, 2, 4, 5, 1},     //3 Points
                {0, 2, 2, 4, 6, 1},     //6 Points
                {0, 0, 3, 5, 6, 1},     //6 Points
                {0, 0, 3, 5, 6, 1}      //5 Points
        };
        GameBoard gameBoard = new GameBoard(testBoard);

        int points = Logic.getTeamPoints(gameBoard.gameBoard, true);
        assertEquals("Test vertical Team Points", 23, points);
    }

    //Winner Evaluation:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Test
    public void winnerVertical_test() {
        //Test Board (Vertical Team 36 | Horizontal Team 12)
        int[][] testBoard = new int[][]{
                {1, 1, 2, 2, 3, 3}, //3
                {2, 1, 2, 2, 6, 3}, //3
                {3, 1, 2, 2, 6, 3}, //2
                {4, 1, 2, 5, 6, 4}, //1
                {5, 2, 2, 5, 6, 4}, //2
                {6, 2, 3, 5, 5, 4}  //1
              // 6, 6, 7, 6, 5, 6
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();

        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        int points_TeamV = Logic.getTeamPoints(game.board.gameBoard, true);
        int points_TeamH = Logic.getTeamPoints(game.board.gameBoard, false);
        GameEvaluationStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", 36, points_TeamV);
        assertEquals("Horizontal Team Points Test", 12, points_TeamH);
        assertEquals("Vertical Winner Test", GameEvaluationStates.TEAM_VERTICAL, result);
    }


    @Test
    public void winnerHorizontal_test() {
        //Test Board (Vertical Team 12 | Horizontal Team 36)
        int[][] testBoard = new int[][]{
                {1, 2, 3, 4, 5, 6}, //6
                {1, 1, 1, 1, 2, 2}, //6
                {2, 2, 2, 2, 2, 3}, //7
                {2, 2, 2, 5, 5, 5}, //6
                {3, 6, 6, 6, 6, 5}, //5
                {3, 3, 3, 4, 4, 4}  //6
               //3, 3, 2, 1, 2, 1
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();

        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        int points_TeamV = Logic.getTeamPoints(game.board.gameBoard, true);
        int points_TeamH = Logic.getTeamPoints(game.board.gameBoard, false);
        GameEvaluationStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", 12, points_TeamV);
        assertEquals("Horizontal Team Points Test", 36, points_TeamH);
        assertEquals("Vertical Winner Test", GameEvaluationStates.TEAM_HORIZONTAL, result);
    }

    @Test
    public void draw_test() {
        //Test Board (Vertical Team 41 | Horizontal Team 41)
        int[][] testBoard = new int[][]{
                {1, 1, 1, 1, 1, 6}, //7
                {1, 1, 1, 1, 1, 5}, //7
                {1, 1, 1, 1, 1, 4}, //7
                {1, 1, 1, 1, 1, 3}, //7
                {1, 1, 1, 1, 1, 2}, //7
                {6, 5, 4, 3, 2, 1}  //6
               //7, 7, 7, 7, 7, 6
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();

        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        int points_TeamV = Logic.getTeamPoints(game.board.gameBoard, true);
        int points_TeamH = Logic.getTeamPoints(game.board.gameBoard, false);
        GameEvaluationStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", 41, points_TeamV);
        assertEquals("Horizontal Team Points Test", 41, points_TeamH);
        assertEquals("Vertical Winner Test", GameEvaluationStates.DRAW, result);
    }

    @Test
    public void winOfSixes_test() {
        //Test Board (Vertical Team WIN | Horizontal Team 36)
        int[][] testBoard = new int[][]{
                {1, 1, 1, 1, 1, 6}, //7
                {1, 1, 1, 1, 1, 5}, //7
                {1, 1, 1, 1, 1, 4}, //7
                {1, 1, 1, 1, 1, 3}, //7
                {1, 1, 1, 1, 1, 2}, //7
                {1, 5, 4, 3, 2, 1}  //1
               //W, 7, 7, 7, 7, 6
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();

        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        int points_TeamV = Logic.getTeamPoints(game.board.gameBoard, true);
        int points_TeamH = Logic.getTeamPoints(game.board.gameBoard, false);
        GameEvaluationStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", Integer.MAX_VALUE, points_TeamV);
        assertEquals("Horizontal Team Points Test", 36, points_TeamH);
        assertEquals("Vertical Winner Test", GameEvaluationStates.TEAM_VERTICAL, result);
    }

    @Test
    public void winOfSixes_ongoingGame_test() {
        //Test Board (Vertical Team WIN | Horizontal Team 0)
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0}, //0
                {1, 0, 0, 0, 0, 0}, //0
                {1, 0, 0, 0, 0, 0}, //0
                {1, 0, 0, 0, 0, 0}, //0
                {1, 0, 0, 0, 0, 0}, //0
                {1, 0, 0, 0, 0, 0}  //0
               //W, 0, 0, 0, 0, 0
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();

        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        int points_TeamV = Logic.getTeamPoints(game.board.gameBoard, true);
        int points_TeamH = Logic.getTeamPoints(game.board.gameBoard, false);
        GameEvaluationStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", Integer.MAX_VALUE, points_TeamV);
        assertEquals("Horizontal Team Points Test", 0, points_TeamH);
        assertEquals("Vertical Winner Test", GameEvaluationStates.TEAM_VERTICAL, result);
    }

    @Test
    public void ongoingGame_test() {
        //Test Board (Vertical Team WIN | Horizontal Team 0)
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0}, //0
                {2, 0, 0, 0, 0, 0}, //0
                {3, 0, 3, 3, 0, 0}, //3
                {4, 0, 4, 3, 0, 0}, //1
                {5, 0, 0, 0, 0, 0}, //0
                {6, 5, 4, 3, 2, 1}  //6
               //6, 0, 1, 3, 0, 0
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();

        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        int points_TeamV = Logic.getTeamPoints(game.board.gameBoard, true);
        int points_TeamH = Logic.getTeamPoints(game.board.gameBoard, false);
        GameEvaluationStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", 10, points_TeamV);
        assertEquals("Horizontal Team Points Test", 10, points_TeamH);
        assertEquals("Vertical Winner Test", GameEvaluationStates.ONGOING_GAME, result);
    }

    //Turn Evaluation:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void validTurnPropositionGameTile() {
        //Test Board
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0},
                {4, 0, 0, 0, 0, 0},
                {5, 0, 0, 0, 0, 0},
                {6, 5, 4, 3, 2, 1}
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();
        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        assertTrue(game.proposeTurn(T_STAR, new Position(5, 0)));
        assertTrue(game.proposeTurn(T_STAR, new Position(3, 3)));
    }

    @Test
    public void validTurnRemover_test() {
        //Test Board
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0},
                {4, 0, 0, 0, 0, 0},
                {5, 0, 0, 0, 0, 0},
                {6, 5, 4, 3, 2, 1}
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();
        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        assertTrue(game.proposeTurn(WC_REMOVER, new Position(0, 0)));
        assertTrue(game.proposeTurn(WC_REMOVER, new Position(0, 5)));
    }


    @Test
    public void validTurnMover_test() {
        //Test Board
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0},
                {4, 0, 0, 0, 0, 0},
                {5, 0, 0, 0, 0, 0},
                {6, 5, 4, 3, 2, 1}
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();
        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        assertTrue(game.proposeTurn(WC_MOVER, new Position(0, 5), new Position(5, 0)));
        assertTrue(game.proposeTurn(WC_MOVER, new Position(5, 5), new Position(1, 4)));
    }

    @Test
    public void validTurnSwapOnBoard_test() {
        //Test Board
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0},
                {4, 0, 0, 0, 0, 0},
                {5, 0, 0, 0, 0, 0},
                {6, 5, 4, 3, 2, 1}
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();
        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        assertTrue(game.proposeTurn(WC_SWAPONBOARD, new Position(0, 0), new Position(0, 5)));
        assertTrue(game.proposeTurn(WC_SWAPONBOARD, new Position(5, 5), new Position(0, 2)));
    }

    @Test
    public void validTurnSwapWithHand_test() {
        //Test Board
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0},
                {4, 0, 0, 0, 0, 0},
                {5, 0, 0, 0, 0, 0},
                {6, 5, 4, 3, 2, 1}
        };

        Player[] players = new Player[4];
        TileBag tileBag = TileBag.getInstance();
        Game game = new Game(testBoard, players, tileBag, 0, new FakeGUI());

        assertTrue(game.proposeTurn(WC_SWAPWITHHAND, new Position(0, 0)));
        assertTrue(game.proposeTurn(WC_SWAPWITHHAND, new Position(5, 5)));
    }
}
