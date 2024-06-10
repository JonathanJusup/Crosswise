package logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Player Test class for testing the Player class for
 * the game Crosswise.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class PlayerTests {

    @Test
    public void constructor_test() {
        Player player = new Player("dummyName", true, false);

        Assert.assertEquals("dummyName", player.getName());
        Assert.assertTrue(player.isActive());
        Assert.assertFalse(player.isAI());
    }

    @Test
    public void constructor_loadingPlayer_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 2, 3, 4});
        List<GameTiles> expectedHand = new ArrayList<>(
                List.of(GameTiles.T_SUN, GameTiles.T_CROSS, GameTiles.T_TRIANGLE, GameTiles.T_SQUARE));

        Assert.assertEquals("dummyName", player.getName());
        Assert.assertTrue(player.isActive());
        Assert.assertFalse(player.isAI());
        Assert.assertEquals(expectedHand, player.getHand());
    }

    @Test
    public void removeGameTileAt_atStart_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 2, 3, 4});
        List<GameTiles> expectedHand = List.of(
                GameTiles.T_SUN, GameTiles.T_CROSS, GameTiles.T_TRIANGLE, GameTiles.T_SQUARE);

        Assert.assertEquals(expectedHand, player.getHand());
        player.removeGameTileAt(0);

        expectedHand = List.of(GameTiles.T_CROSS, GameTiles.T_TRIANGLE, GameTiles.T_SQUARE);
        Assert.assertEquals(expectedHand, player.getHand());
    }

    @Test
    public void removeGameTileAt_atEnd_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 2, 3, 4});
        List<GameTiles> expectedHand = List.of(
                GameTiles.T_SUN, GameTiles.T_CROSS, GameTiles.T_TRIANGLE, GameTiles.T_SQUARE);

        Assert.assertEquals(expectedHand, player.getHand());
        player.removeGameTileAt(3);

        expectedHand = List.of(GameTiles.T_SUN, GameTiles.T_CROSS, GameTiles.T_TRIANGLE);
        Assert.assertEquals(expectedHand, player.getHand());
    }

    @Test
    public void addGameTileAt_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 2, 3});
        List<GameTiles> expectedHand = List.of(
                GameTiles.T_SUN, GameTiles.T_CROSS, GameTiles.T_TRIANGLE);

        Assert.assertEquals(expectedHand, player.getHand());
        player.addGameTileAt(GameTiles.T_SQUARE, 3);

        expectedHand = List.of(GameTiles.T_SUN, GameTiles.T_CROSS, GameTiles.T_TRIANGLE, GameTiles.T_SQUARE);
        Assert.assertEquals(expectedHand, player.getHand());
    }

    @Test
    public void getNumberOfStandardGameTiles_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 2, 3, 10});
        Assert.assertEquals(3, player.getNumberOfStandardGameTiles());
    }

    @Test
    public void getNumberOfStandardGameTiles_bigHand_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 2, 3, 10, 5, 6, 7, 3, 4, 8});
        Assert.assertEquals(7, player.getNumberOfStandardGameTiles());
    }

    @Test
    public void indexOfGameTile_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 8, 4, 7});

        Assert.assertEquals(0, player.indexOfGameTile(GameTiles.T_SUN));
        Assert.assertEquals(1, player.indexOfGameTile(GameTiles.WC_MOVER));
        Assert.assertEquals(2, player.indexOfGameTile(GameTiles.T_SQUARE));
        Assert.assertEquals(3, player.indexOfGameTile(GameTiles.WC_REMOVER));
    }

    @Test
    public void indexOfGameTile_notExisting_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 2, 3, 10});

        Assert.assertEquals(-1, player.indexOfGameTile(GameTiles.T_SQUARE));
        Assert.assertEquals(-1, player.indexOfGameTile(GameTiles.T_PENTAGON));
        Assert.assertEquals(-1, player.indexOfGameTile(GameTiles.WC_REMOVER));
    }

    @Test
    public void indexOfGameTile_bigHand_test() {
        Player player = new Player("dummyName", true, false, new int[]{1, 2, 3, 10, 5, 6, 7, 3, 4, 8});

        Assert.assertEquals(0, player.indexOfGameTile(GameTiles.T_SUN));
        Assert.assertEquals(1, player.indexOfGameTile(GameTiles.T_CROSS));
        Assert.assertEquals(2, player.indexOfGameTile(GameTiles.T_TRIANGLE));
        Assert.assertEquals(-1, player.indexOfGameTile(GameTiles.WC_SWAPONBOARD));

    }


}
