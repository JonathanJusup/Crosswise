package logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static logic.Utilities.transposeBoard;
import static org.junit.Assert.assertEquals;

/**
 * Public Logic Test for the game Crosswise
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class GameTest {

    @Test
    public void gameTestConstructor_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        Player[] players = new Player[4];
        players[0] = new Player("dummyPlayer1", true, false);
        players[1] = new Player("dummyPlayer2", true, false);
        players[2] = new Player("dummyPlayer3", false, false);
        players[3] = new Player("dummyPlayer4", false, false);

        Game game = new Game(testBoard, players, new FakeGUI());
        Assert.assertEquals(2, game.getActivePlayers());
    }

    @Test
    public void getTeamNames_test() {
        int[][] testBoard = new int[][] {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        Player[] players = new Player[4];
        players[0] = new Player("dummyPlayer1", true, false);
        players[1] = new Player("dummyPlayer2", true, false);
        players[2] = new Player("dummyPlayer3", false, false);
        players[3] = new Player("dummyPlayer4", false, false);


        Game game = new Game(testBoard, players, new FakeGUI());
        Assert.assertEquals(2, game.getActivePlayers());

        String[] team1Names = game.getTeamNames(0);
        String[] team2Names = game.getTeamNames(1);

        Assert.assertEquals(1, team1Names.length);
        Assert.assertEquals(1, team2Names.length);

        Assert.assertArrayEquals(new String[] {"dummyPlayer1"}, team1Names);
        Assert.assertArrayEquals(new String[] {"dummyPlayer2"}, team2Names);

    }

    @Test
    public void toGameStructure_test() {
        int[][] testBoard = new int[][] {
                {1, 2, 3, 4, 5, 6},
                {0, 2, 3, 4, 5, 6},
                {0, 0, 3, 4, 5, 6},
                {0, 0, 0, 4, 5, 6},
                {0, 0, 0, 0, 5, 6},
                {0, 0, 0, 0, 0, 6}
        };
        Player[] players = new Player[4];
        players[0] = new Player("dummyPlayer1", true, false);
        players[1] = new Player("dummyPlayer2", true, false);
        players[2] = new Player("dummyPlayer3", false, false);
        players[3] = new Player("dummyPlayer4", false, false);

        Game game = new Game(testBoard, players, new FakeGUI());
        GameData gameData = game.toGameData();

        Assert.assertEquals(4, gameData.getPlayers().length);
        Assert.assertEquals(0, gameData.getCurrPlayer());
        Assert.assertArrayEquals(testBoard, gameData.getField());
        Assert.assertArrayEquals(new int[4], gameData.getUsedActionTiles());
        Assert.assertEquals(54, gameData.getTileBag().length);
    }

    //Evaluate Rows & Columns:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void evaluateRow_test() {
        int[][] testBoard = new int[][] {
                {1, 2, 3, 4, 5, 6},
                {0, 2, 3, 4, 5, 6},
                {0, 0, 3, 4, 5, 6},
                {0, 0, 0, 4, 5, 6},
                {0, 0, 0, 0, 5, 6},
                {0, 0, 0, 0, 0, 6}
        };

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());

        int[] tilesOfSegment = game.getTilesPerSegment(0, true);
        int points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Row 1", 0, points);

        tilesOfSegment = game.getTilesPerSegment(1, true);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Row 2", 1, points);

        tilesOfSegment = game.getTilesPerSegment(2, true);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Row 3", 3, points);

        tilesOfSegment = game.getTilesPerSegment(3, true);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Row 4", 5, points);

        tilesOfSegment = game.getTilesPerSegment(4, true);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Row 5", 7, points);

        tilesOfSegment = game.getTilesPerSegment(5, true);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Row 6", Integer.MAX_VALUE, points);
    }

    @Test
    public void evaluateColumn_test() {
        int[][] testBoard = new int[][] {
                {1, 1, 2, 2, 0, 0},
                {3, 3, 4, 4, 5, 5},
                {5, 5, 5, 6, 6, 6},
                {2, 2, 2, 2, 3, 3},
                {3, 3, 3, 3, 3, 4},
                {1, 2, 3, 4, 5, 6}
        };

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());
        int[] tilesOfSegment = game.getTilesPerSegment(0, false);
        int points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Col 1", 2, points);

        tilesOfSegment = game.getTilesPerSegment(1, false);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Col 2", 3, points);

        tilesOfSegment = game.getTilesPerSegment(2, false);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Col 3", 6, points);

        tilesOfSegment = game.getTilesPerSegment(3, false);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Col 4", 6, points);

        tilesOfSegment = game.getTilesPerSegment(4, false);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Col 5", 7, points);

        tilesOfSegment = game.getTilesPerSegment(5, false);
        points = Game.calculatePoints(tilesOfSegment);
        assertEquals("Test Points for Col 6", 6, points);
    }

    //Evaluation::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Test
    public void getTeamPointsHorizontal_test() {
        int[][] testBoard = new int[][] {
                {1, 0, 0, 0, 0, 0},     //0 Points
                {1, 1, 0, 0, 0, 0},     //1 Points
                {1, 1, 1, 0, 0, 0},     //3 Points
                {1, 1, 1, 1, 0, 0},     //5 Points
                {1, 1, 1, 1, 1, 0},     //7 Points
                {1, 2, 3, 4, 5, 6}      //6 Points
        };

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());
        int points = game.getTeamPoints(false);
        assertEquals("Test horizontal Team Points", 22, points);
    }

    @Test
    public void getTeamPointsVertical_test() {
        int[][] testBoard = new int[][] {
                {1, 1, 1, 4, 5, 0},     //1 Points
                {1, 1, 1, 4, 5, 0},     //2 Points
                {0, 2, 2, 4, 5, 1},     //3 Points
                {0, 2, 2, 4, 6, 1},     //6 Points
                {0, 0, 3, 5, 6, 1},     //6 Points
                {0, 0, 3, 5, 6, 1}      //5 Points
        };

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());
        int points = game.getTeamPoints(true);
        assertEquals("Test vertical Team Points", 23, points);
    }


    //Winner Evaluation:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void evaluate_winnerVertical_test() {
        //Test Board (Vertical Team 12 | Horizontal Team 36)
        int[][] testBoard = new int[][]{
                {1, 1, 2, 2, 3, 3}, //3
                {2, 1, 2, 2, 6, 3}, //3
                {3, 1, 2, 2, 6, 3}, //2
                {4, 1, 2, 5, 6, 4}, //1
                {5, 2, 2, 5, 6, 4}, //2
                {6, 2, 3, 5, 5, 4}  //1
              // 6, 6, 7, 6, 5, 6
        };

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());

        int points_TeamV = game.getTeamPoints(true);
        int points_TeamH = game.getTeamPoints(false);
        GameStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", 36, points_TeamV);
        assertEquals("Horizontal Team Points Test", 12, points_TeamH);
        assertEquals("Vertical Winner Test", GameStates.TEAM_VERTICAL, result);
    }

    @Test
    public void evaluate_winnerHorizontal_test() {
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

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());

        int points_TeamV = game.getTeamPoints(true);
        int points_TeamH = game.getTeamPoints(false);
        GameStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", 12, points_TeamV);
        assertEquals("Horizontal Team Points Test", 36, points_TeamH);
        assertEquals("Vertical Winner Test", GameStates.TEAM_HORIZONTAL, result);
    }

    @Test
    public void evaluate_draw_test() {
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

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());

        int points_TeamV = game.getTeamPoints(true);
        int points_TeamH = game.getTeamPoints(false);
        GameStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", 41, points_TeamV);
        assertEquals("Horizontal Team Points Test", 41, points_TeamH);
        assertEquals("Vertical Winner Test", GameStates.DRAW, result);
    }

    @Test
    public void evaluate_ongoingGame_test() {
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

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());

        int points_TeamV = game.getTeamPoints(true);
        int points_TeamH = game.getTeamPoints(false);
        GameStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", 10, points_TeamV);
        assertEquals("Horizontal Team Points Test", 10, points_TeamH);
        assertEquals("Vertical Winner Test", GameStates.ONGOING_GAME, result);
    }

    //WinOfSixes Tests::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

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

        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());

        int points_TeamV = game.getTeamPoints(true);
        int points_TeamH = game.getTeamPoints(false);
        GameStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", Integer.MAX_VALUE, points_TeamV);
        assertEquals("Horizontal Team Points Test", 36, points_TeamH);
        assertEquals("Vertical Winner Test", GameStates.TEAM_VERTICAL, result);
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
        Game game = new Game(transposeBoard(testBoard), new Player[4], new FakeGUI());

        int points_TeamV = game.getTeamPoints(true);
        int points_TeamH = game.getTeamPoints(false);
        GameStates result = game.evaluateGame();

        assertEquals("Vertical Team Points Test", Integer.MAX_VALUE, points_TeamV);
        assertEquals("Horizontal Team Points Test", 0, points_TeamH);
        assertEquals("Vertical Winner Test", GameStates.TEAM_VERTICAL, result);
    }



    //Turn Evaluation:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void playerTurn_standardGameTile_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        Player[] players = new Player[4];
        players[0] = new Player("dummyPlayer1", true, false);
        players[1] = new Player("dummyPlayer2", true, false);
        players[2] = new Player("dummyPlayer3", false, false);
        players[3] = new Player("dummyPlayer4", false, false);

        List<GameTiles> tilesToFill = List.of(GameTiles.T_SUN);
        TileBag tileBag = new TileBag(tilesToFill, false);

        Game game = new Game(testBoard, players, 0, tileBag, new FakeGUI());
        game.playerTurn(GameTiles.T_SUN, new Position(0, 2), 0, null, null);


        int[][] expectedBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {1, 0, 0}
        };
        Assert.assertArrayEquals(transposeBoard(expectedBoard), game.toGameData().getField());
    }

    @Test
    public void playerTurn_REMOVER_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 1},
                {0, 0, 0}
        };
        Player[] players = new Player[4];
        players[0] = new Player("dummyPlayer1", true, false);
        players[1] = new Player("dummyPlayer2", true, false);
        players[2] = new Player("dummyPlayer3", false, false);
        players[3] = new Player("dummyPlayer4", false, false);

        List<GameTiles> tilesToFill = List.of(
                GameTiles.WC_REMOVER, GameTiles.WC_MOVER, GameTiles.WC_SWAPONBOARD, GameTiles.WC_SWAPWITHHAND);
        TileBag tileBag = new TileBag(tilesToFill, false);

        Game game = new Game(transposeBoard(testBoard), players, 0, tileBag, new FakeGUI());
        game.playerTurn(GameTiles.WC_REMOVER, new Position(2, 1), 0, null, null);


        int[][] expectedBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        Assert.assertArrayEquals(transposeBoard(expectedBoard), game.toGameData().getField());
    }

    @Test
    public void playerTurn_MOVER_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        Player[] players = new Player[4];
        players[0] = new Player("dummyPlayer1", true, false);
        players[1] = new Player("dummyPlayer2", true, false);
        players[2] = new Player("dummyPlayer3", false, false);
        players[3] = new Player("dummyPlayer4", false, false);

        List<GameTiles> tilesToFill = List.of(
                GameTiles.WC_REMOVER, GameTiles.WC_MOVER, GameTiles.WC_SWAPONBOARD, GameTiles.WC_SWAPWITHHAND);
        TileBag tileBag = new TileBag(tilesToFill, false);

        Game game = new Game(transposeBoard(testBoard), players, 0, tileBag, new FakeGUI());
        game.playerTurn(GameTiles.WC_MOVER, new Position(1, 1), 1,
                null, null);
        game.playerTurn(GameTiles.T_SUN, new Position(1, 2), null,
                null, new Position(1, 1));


        int[][] expectedBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 1, 0}
        };
        Assert.assertArrayEquals(transposeBoard(expectedBoard), game.toGameData().getField());
    }

    @Test
    public void playerTurn_SWAPONBOARD_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 1, 2},
                {0, 0, 0}
        };
        Player[] players = new Player[4];
        players[0] = new Player("dummyPlayer1", true, false);
        players[1] = new Player("dummyPlayer2", true, false);
        players[2] = new Player("dummyPlayer3", false, false);
        players[3] = new Player("dummyPlayer4", false, false);

        List<GameTiles> tilesToFill = List.of(
                GameTiles.WC_REMOVER, GameTiles.WC_MOVER, GameTiles.WC_SWAPONBOARD, GameTiles.WC_SWAPWITHHAND);
        TileBag tileBag = new TileBag(tilesToFill, false);

        Game game = new Game(transposeBoard(testBoard), players, 0, tileBag, new FakeGUI());
        game.playerTurn(GameTiles.WC_SWAPONBOARD, new Position(2, 1), 2,
                null, null);
        game.playerTurn(GameTiles.T_SUN, new Position(2, 1), null, GameTiles.T_CROSS,
                new Position(1, 1));


        int[][] expectedBoard = new int[][]{
                {0, 0, 0},
                {0, 2, 1},
                {0, 0, 0}
        };
        Assert.assertArrayEquals(transposeBoard(expectedBoard), game.toGameData().getField());
    }

    @Test
    public void playerTurn_SWAPWITHHAND_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 2},
                {0, 0, 0}
        };
        Player[] players = new Player[4];
        players[0] = new Player("dummyPlayer1", true, false);
        players[1] = new Player("dummyPlayer2", true, false);
        players[2] = new Player("dummyPlayer3", false, false);
        players[3] = new Player("dummyPlayer4", false, false);

        List<GameTiles> tilesToFill = List.of(
                GameTiles.WC_SWAPWITHHAND, GameTiles.T_SQUARE);
        TileBag tileBag = new TileBag(tilesToFill, false);

        Game game = new Game(transposeBoard(testBoard), players, 0, tileBag, new FakeGUI());
        game.playerTurn(GameTiles.WC_SWAPWITHHAND, new Position(2, 1), 0,
                null, null);
        game.playerTurn(GameTiles.T_SQUARE, new Position(2, 1), 1,
                null, null);


        int[][] expectedBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 4},
                {0, 0, 0}
        };
        Assert.assertArrayEquals(transposeBoard(expectedBoard), game.toGameData().getField());
    }


    @Test
    public void loadGame_reconstructTileBag_test() {
        GameData gameData = GameFileManager.loadGame("src/test/SaveFiles/1.json");
        Game game;

        try {
            game = new Game(gameData, new FakeGUI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Integer[] reconstructed = game.getRemainingGameTiles();
        Arrays.sort(reconstructed);

        Integer[] expected = Arrays.stream(gameData.getTileBag()).sorted().boxed().toArray(Integer[]::new);
        Arrays.sort(expected);

        Assert.assertArrayEquals(expected, reconstructed);
    }


}
