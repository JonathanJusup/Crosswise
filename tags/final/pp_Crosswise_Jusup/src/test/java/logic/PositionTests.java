package logic;

import org.junit.Assert;
import org.junit.Test;

/**
 * Public Position Test class for the game Crosswise.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class PositionTests {

    @Test
    public void compareTo_test() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(0, 1);
        Position pos3 = new Position(1, 0);
        Position pos4 = new Position(1, 1);
        Position origin = new Position(0, 0);

        Assert.assertEquals(0, pos1.compareTo(origin));

        Assert.assertTrue(pos1.compareTo(pos2) < 0);
        Assert.assertTrue(pos1.compareTo(pos3) < 0);
        Assert.assertTrue(pos1.compareTo(pos4) < 0);

        Assert.assertTrue(pos2.compareTo(pos1) > 0);
        Assert.assertTrue(pos2.compareTo(pos3) > 0);
        Assert.assertTrue(pos2.compareTo(pos4) < 0);

        Assert.assertTrue(pos3.compareTo(pos1) > 0);
        Assert.assertTrue(pos3.compareTo(pos2) < 0);
        Assert.assertTrue(pos3.compareTo(pos4) < 0);

        Assert.assertTrue(pos4.compareTo(pos1) > 0);
        Assert.assertTrue(pos4.compareTo(pos2) > 0);
        Assert.assertTrue(pos4.compareTo(pos3) > 0);

    }

    @Test
    public void toString_test() {
        Position pos = new Position(4, 10);
        String expected = "(4|10)";

        Assert.assertEquals(expected, pos.toString());
    }
}
