package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Player Class for the game Crosswise. Each player has a name, a value if
 * it's active, it's an AI player and a playerHand.
 * <p>
 * The player can interact with the game Crosswise by performing actions such
 * as playing GameTiles from its own hand or using wildcards.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class Player {

    /**
     * player Name
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
     * playerHand List of GameTiles
     */
    private final List<GameTiles> hand;


    /**
     * Player Constructor for new Game. Also starts with empty playerHand.
     *
     * @param name     player name
     * @param isActive is active player
     * @param isAI     is AI player
     */
    public Player(String name, boolean isActive, boolean isAI) {
        this.name = name;
        this.isActive = isActive;
        this.isAI = isAI;
        this.hand = new ArrayList<>();
    }

    /**
     * Player Constructor for loading existing player with existing playerHand.
     * Specified hand gets converted from Array of GameTile ordinal values to
     * ArrayList of GameTiles. This constructor is only called in logic package.
     *
     * @param name     player name
     * @param isActive is active player
     * @param isAI     is AI player
     * @param hand     exiting hand
     */
    Player(String name, boolean isActive, boolean isAI, int[] hand) {
        this.name = name;
        this.isActive = isActive;
        this.isAI = isAI;
        this.hand = new ArrayList<>(IntStream.of(hand).mapToObj(Utilities::ordinalToGameTiles).toList());
    }

    /**
     * name Getter.
     *
     * @return name
     */
    String getName() {
        return name;
    }

    /**
     * isActive Getter.
     *
     * @return isActive
     */
    boolean isActive() {
        return isActive;
    }

    /**
     * hand Getter.
     *
     * @return hand Copy
     */
    List<GameTiles> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * isAI Getter.
     *
     * @return isAI
     */
    boolean isAI() {
        return isAI;
    }


    /**
     * Removes GameTile from playerHand, normally after GameTile was used by Player.
     *
     * @param handSlot handSlot Index, where to remove
     */
    void removeGameTileAt(int handSlot) {
        hand.remove(handSlot);
    }

    /**
     * Add GameTile to playerHand. Normally after player has played
     * GameTile from hand. Also used, when playerHand gets initialized.
     *
     * @param tile     GameTile to add
     * @param handSlot handSlot Index, where to add
     */
    void addGameTileAt(GameTiles tile, int handSlot) {
        hand.add(handSlot, tile);
    }

    /**
     * Gets Number of Standard GameTile in playerHand.
     * <p>
     * E.g. (1, 2, 3, 4) -> 4 | (1, 2, 3, 10) -> 1 | (1, 2, 0, 0) -> 2
     *
     * @return number of standard GameTiles
     */
    int getNumberOfStandardGameTiles() {
        return (int) hand.stream().filter(GameTiles::isStandardGameTile).count();
    }

    /**
     * Looks for first occurrence of specified GameTile and returns
     * the handSlot Index. Terminates early if gameTile is found.
     * Mainly used for looking up wildcards in playerHand.
     *
     * @param gameTile GameTile to look for
     * @return Index of first occurrence
     */
    int indexOfGameTile(GameTiles gameTile) {
        int result = -1;

        for (int i = 0; i < hand.size() && result == -1; i++) {
            if (hand.get(i).equals(gameTile)) {
                result = i;
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (isActive != player.isActive) return false;
        if (isAI != player.isAI) return false;
        if (!Objects.equals(name, player.name)) return false;

        return Objects.equals(hand, player.hand);
    }

}
