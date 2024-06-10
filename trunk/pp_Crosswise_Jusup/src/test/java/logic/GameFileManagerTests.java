package logic;

import org.junit.Assert;
import org.junit.Test;

/**
 * GameFileManager Test class for the game Crosswise.
 * Contains a test for loading an existing game in .json File.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class GameFileManagerTests {
    @Test
    public void saveGame_test() {
        Player[] players = new Player[4];
        players[0] = new Player("PlayerName1", true, false, new int[] {1, 1, 1, 1});
        players[1] = new Player("PlayerName2", true, false, new int[] {2, 2, 2, 2});
        players[2] = new Player("PlayerName3", true, false, new int[] {3, 3, 3, 3});
        players[3] = new Player("PlayerName4", true, false, new int[] {4, 4, 4, 4});


        Game game = new Game(6, players, new FakeGUI());
        GameData gameData_expected = game.toGameData();
        GameFileManager.saveGame(game, "src/test/SaveFiles/testSaveFile.json");


        GameData gameData = GameFileManager.loadGame("src/test/SaveFiles/testSaveFile.json");

        PlayerData[] playerData = gameData.getPlayers();

        PlayerData[] playerData_expected = gameData_expected.getPlayers();
        Assert.assertEquals(playerData_expected[0], playerData[0]);
        Assert.assertEquals(playerData_expected[1], playerData[1]);
        Assert.assertEquals(playerData_expected[2], playerData[2]);
        Assert.assertEquals(playerData_expected[3], playerData[3]);

        int[][] expected_gameBoard = gameData_expected.getField();

        Assert.assertArrayEquals(expected_gameBoard, gameData.getField());
        Assert.assertArrayEquals(gameData_expected.getUsedActionTiles(), gameData.getUsedActionTiles());
        Assert.assertArrayEquals(gameData_expected.getTileBag(), gameData.getTileBag());
    }

    @Test
    public void loadGame_test() {
        GameData gameData = GameFileManager.loadGame("src/test/SaveFiles/1.json");
        PlayerData[] playerData = gameData.getPlayers();

        PlayerData expected_player1 = new PlayerData(
                "PlayerName1", true, false, new int[] {4, 3, 6, 3});
        PlayerData expected_player2 = new PlayerData(
                "PlayerName2", true, false, new int[] {3, 1, 2, 1});
        PlayerData expected_player3 = new PlayerData(
                "PlayerName3", true, false, new int[] {6, 5, 1, 1});
        PlayerData expected_player4 = new PlayerData(
                "PlayerName4", true, false, new int[] {1, 4, 6, 2});

        Assert.assertEquals(expected_player1, playerData[0]);
        Assert.assertEquals(expected_player2, playerData[1]);
        Assert.assertEquals(expected_player3, playerData[2]);
        Assert.assertEquals(expected_player4, playerData[3]);

        int[][] expected_gameBoard = new int[][]{
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 5, 0, 0},
                {0, 0, 5, 3, 0, 0},
                {0, 0, 3, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},

        };

        Assert.assertArrayEquals(expected_gameBoard, gameData.getField());
        Assert.assertArrayEquals(new int[] {3, 0, 2, 1}, gameData.getUsedActionTiles());
        Assert.assertArrayEquals(new int[]
                {9, 5, 1, 4, 4, 5, 4, 2, 6, 5, 8, 5, 6, 2, 4, 2, 8, 8, 2, 4, 1, 2, 10, 3, 6, 3, 10, 6},
                gameData.getTileBag());
    }

    @Test
    public void loadGame_invalidFile_test() {
        GameData gameData = GameFileManager.loadGame("src/test/SaveFiles/EMPTY_FILE.json");
        Assert.assertNull(gameData);
    }

}
