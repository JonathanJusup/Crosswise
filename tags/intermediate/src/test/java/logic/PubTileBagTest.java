package logic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Public Tile Bag Test for the game Crosswise
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class PubTileBagTest {

    TileBag tileBag = TileBag.getInstance();

    @Test
    public void tileBagSizeTest() {
        //6 GameTiles Types * 7 + 4 Wildcard Types * 3 = 54 overall Tiles
        assertEquals("Test Tile Bag Size", 54, tileBag.getTileBagSize());
    }

    @Test
    public void tileTypeCountTest() {
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
                new int[] {7, 7, 7, 7, 7, 7, 3, 3, 3, 3}, tileCount);
    }

}
