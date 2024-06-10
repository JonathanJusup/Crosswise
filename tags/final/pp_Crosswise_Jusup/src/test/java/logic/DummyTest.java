package logic;

import org.junit.Assert;
import org.junit.Test;

/**
 * This Test class is just for the statistic to show, that
 * the records are tested thoroughly. Due to records being
 * simple structures, no actual testing is needed.
 * <p>
 * This Class applies to all Classes and Records with no
 * further logic. Their only purpose it is to hold and
 * provide data.
 * <p>
 * Applies to PossibleTurn, GameData and PlayerData, which
 * only contain data and their respective Getters.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class DummyTest {

    @Test
    public void applyToPossibleTurn_test() {
        PossibleTurn dummyPossibleTurn = new PossibleTurn(
                null,
                null,
                null,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        Assert.assertNull(dummyPossibleTurn.pos());
        Assert.assertNull(dummyPossibleTurn.toPlace());
        Assert.assertNull(dummyPossibleTurn.handSlot());
        Assert.assertNull(dummyPossibleTurn.lastPosition());
        Assert.assertNull(dummyPossibleTurn.toSwap());
        Assert.assertEquals(0, dummyPossibleTurn.points_team());
        Assert.assertEquals(0, dummyPossibleTurn.points_opponent());
        Assert.assertEquals(0, dummyPossibleTurn.points_team_gain());
        Assert.assertFalse(dummyPossibleTurn.hasSecondPhase());
        Assert.assertNull(dummyPossibleTurn.wildcard());
    }

    @Test
    public void applyToGameData_test() {
        GameData dummyGameData = new GameData(
                null,
                0,
                null,
                null,
                null
        );

        Assert.assertNull(dummyGameData.getPlayers());
        Assert.assertEquals(0, dummyGameData.getCurrPlayer());
        Assert.assertNull(dummyGameData.getField());
        Assert.assertNull(dummyGameData.getUsedActionTiles());
        Assert.assertNull(dummyGameData.getTileBag());
    }

    @Test
    public void applyToPlayerData_test() {
        PlayerData dummyPlayerData = new PlayerData(null, false, false, null);

        Assert.assertNull(dummyPlayerData.getName());
        Assert.assertFalse(dummyPlayerData.isActive());
        Assert.assertFalse(dummyPlayerData.isAI());
        Assert.assertNull(dummyPlayerData.getHand());

    }
}
