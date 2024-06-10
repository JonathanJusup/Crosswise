package logic;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Public Tile Bag Test for the game Crosswise.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class TileBagTest {
    @Test
    public void tileBagSize_test() {
        TileBag tileBag = new TileBag();

        //6 GameTiles Types * 7 + 4 Wildcard Types * 3 = 54 overall Tiles
        assertEquals("Test Tile Bag Size", 54, tileBag.getTileBagSize());
    }

    @Test
    public void getTileBag_test() {
        List<GameTiles> toFillWith = List.of(
                GameTiles.T_SQUARE, GameTiles.T_TRIANGLE, GameTiles.T_PENTAGON, GameTiles.T_STAR);

        TileBag tileBag = new TileBag(toFillWith, false);

        assertEquals("Test TileBag with fill list", tileBag.getTileBag().stream().toList(), toFillWith);
    }

    @Test
    public void tileTypeCount_test() {
        TileBag tileBag = new TileBag();

        int[] tileCount = new int[10]; // 6 GameTile Types + 4 Wildcard Types
        while (tileBag.getTileBagSize() > 0) {
            switch (tileBag.getTile()) {
                case T_SUN -> tileCount[0]++;
                case T_CROSS -> tileCount[1]++;
                case T_TRIANGLE -> tileCount[2]++;
                case T_SQUARE -> tileCount[3]++;
                case T_PENTAGON -> tileCount[4]++;
                case T_STAR -> tileCount[5]++;
                case WC_MOVER -> tileCount[6]++;
                case WC_REMOVER -> tileCount[7]++;
                case WC_SWAPONBOARD -> tileCount[8]++;
                case WC_SWAPWITHHAND -> tileCount[9]++;
            }
        }

        assertArrayEquals("Test for correct Tile count",
                new int[]{7, 7, 7, 7, 7, 7, 3, 3, 3, 3}, tileCount);
    }

    @Test
    public void tileBagTestConstructor_test() {
        List<GameTiles> toFillWith = List.of(
                GameTiles.T_SQUARE, GameTiles.T_TRIANGLE, GameTiles.T_PENTAGON, GameTiles.T_STAR,
                GameTiles.WC_REMOVER, GameTiles.WC_MOVER, GameTiles.WC_SWAPONBOARD, GameTiles.WC_SWAPWITHHAND);

        TileBag tileBag = new TileBag(toFillWith, true);

        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_SQUARE)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_TRIANGLE)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_PENTAGON)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_STAR)).count());

        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_REMOVER)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_MOVER)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_SWAPONBOARD)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_SWAPWITHHAND)).count());

        assertEquals(46, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_CROSS)).count());
        assertEquals("Test Tile Bag Size", 54, tileBag.getTileBagSize());
    }

    @Test
    public void tileBagTestConstructor_noAutoFill_test() {
        List<GameTiles> toFillWith = List.of(
                GameTiles.T_SQUARE, GameTiles.T_TRIANGLE, GameTiles.T_PENTAGON, GameTiles.T_STAR,
                GameTiles.WC_REMOVER, GameTiles.WC_MOVER, GameTiles.WC_SWAPONBOARD, GameTiles.WC_SWAPWITHHAND);

        TileBag tileBag = new TileBag(toFillWith, false);

        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_SQUARE)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_TRIANGLE)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_PENTAGON)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_STAR)).count());

        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_REMOVER)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_MOVER)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_SWAPONBOARD)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_SWAPWITHHAND)).count());

        assertEquals("Test Tile Bag Size", 8, tileBag.getTileBagSize());
    }

    @Test
    public void existing_tileBagConstructor_test() {
        int[] toFillWith = new int[] {3, 4, 5, 6, 7, 8, 9, 10};

        TileBag tileBag = new TileBag(toFillWith, false);

        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_SQUARE)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_TRIANGLE)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_PENTAGON)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.T_STAR)).count());

        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_REMOVER)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_MOVER)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_SWAPONBOARD)).count());
        assertEquals(1, tileBag.getTileBag().stream().filter(tile -> tile.equals(GameTiles.WC_SWAPWITHHAND)).count());

        assertEquals("Test Tile Bag Size", 8, tileBag.getTileBagSize());
    }

    @Test
    public void tileBagGetTile_test() {
        List<GameTiles> toFillWith = List.of(
                GameTiles.T_SQUARE, GameTiles.T_TRIANGLE, GameTiles.T_PENTAGON, GameTiles.T_STAR,
                GameTiles.WC_REMOVER, GameTiles.WC_MOVER, GameTiles.WC_SWAPONBOARD, GameTiles.WC_SWAPWITHHAND);

        TileBag tileBag = new TileBag(toFillWith, false);

        assertEquals("Before Poll", 8, tileBag.getTileBagSize());

        assertEquals(GameTiles.T_SQUARE, tileBag.getTile());
        assertEquals(GameTiles.T_TRIANGLE, tileBag.getTile());
        assertEquals(GameTiles.T_PENTAGON, tileBag.getTile());
        assertEquals(GameTiles.T_STAR, tileBag.getTile());

        assertEquals(GameTiles.WC_REMOVER, tileBag.getTile());
        assertEquals(GameTiles.WC_MOVER, tileBag.getTile());
        assertEquals(GameTiles.WC_SWAPONBOARD, tileBag.getTile());
        assertEquals(GameTiles.WC_SWAPWITHHAND, tileBag.getTile());

        assertEquals("After Poll", 0, tileBag.getTileBagSize());
    }
}
