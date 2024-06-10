package logic;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Utility test class for testing Utility class in logic package.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class UtilitiesTests {

    @Test
    public void numberToGameTiles_test() {
        GameTiles[] gameTiles = GameTiles.values();

        for (int i = 0; i < gameTiles.length; i++) {
            assertEquals(Utilities.ordinalToGameTiles(i), gameTiles[i]);
        }
    }

    @Test
    public void isVerticalTeam_test() {
        Assert.assertTrue(Utilities.isVerticalTeam(0));
        Assert.assertTrue(Utilities.isVerticalTeam(2));

        Assert.assertFalse(Utilities.isVerticalTeam(1));
        Assert.assertFalse(Utilities.isVerticalTeam(3));
    }

    @Test
    public void colorGameTiles() {
        Assert.assertEquals("\u001B[43m" + 1 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.T_SUN));
        Assert.assertEquals("\u001B[44m" + 2 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.T_CROSS));
        Assert.assertEquals("\u001B[42m" + 3 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.T_TRIANGLE));
        Assert.assertEquals("\u001B[41m" + 4 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.T_SQUARE));
        Assert.assertEquals("\u001B[47m" + 5 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.T_PENTAGON));
        Assert.assertEquals("\u001B[45m" + 6 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.T_STAR));

        Assert.assertEquals("\u001B[49m" + 0 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.EMPTY));
        Assert.assertEquals("\u001B[49m" + 7 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.WC_REMOVER));
        Assert.assertEquals("\u001B[49m" + 8 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.WC_MOVER));
        Assert.assertEquals("\u001B[49m" + 9 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.WC_SWAPONBOARD));
        Assert.assertEquals("\u001B[49m" + 10 + "\u001B[0m",
                Utilities.colorGameTileMsg(GameTiles.WC_SWAPWITHHAND));

    }


}
