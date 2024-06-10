package logic;

import java.util.Arrays;
import java.util.Objects;

/**
 * PlayerData Structure for representing a Player.
 * Used to save/load a Player for the game Crosswise.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
class PlayerData {

    /**
     * Player Name
     */
    private final String name;
    /**
     * isActive Flag
     */
    private final boolean isActive;
    /**
     * isAI Flag
     */
    private final boolean isAI;
    /**
     * playerHand (GameTile ordinal-values)
     */
    private final int[] hand;

    /**
     * PlayerData constructor.
     *
     * @param name     playerName
     * @param isActive isActive
     * @param isAI     isAI
     * @param hand     playerHand
     */
    PlayerData(String name, boolean isActive, boolean isAI, int[] hand) {
        this.name = name;
        this.isActive = isActive;
        this.isAI = isAI;
        this.hand = hand;
    }

    /**
     * Name Getter.
     *
     * @return name
     */
    String getName() {
        return name;
    }

    /**
     * isActive Getter.
     * Needs to be public for UserInterface to determine, how many players
     * are active and therefore creating a fitting User Interface with the
     * right amount of playerHands.
     *
     * @return isActive
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * isActive Getter.
     *
     * @return isActive
     */
    boolean isAI() {
        return isAI;
    }

    /**
     * hand Getter.
     *
     * @return hand
     */
    int[] getHand() {
        return hand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerData that = (PlayerData) o;
        return isActive == that.isActive
                && isAI == that.isAI
                && Objects.equals(name, that.name)
                && Arrays.equals(hand, that.hand);
    }
}
