package logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Player Class for the game Crosswise. Each player has a name, a value whether
 * it is active, whether it is AI player and a player hand.
 *
 * The player can perform actions such as using GameTiles from its own hand or
 * using wildcards.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class Player {
    private final String name;
    private final boolean isActive;
    private final boolean isAI;
    private final List<GameTiles> hand;

    private final int HANDSIZE = 4;         //Player Hand Size

    /**
     * Player Constructor for new Game
     *
     * @param name player name
     * @param isActive is active player
     * @param isAI is AI player
     */
    public Player(String name, boolean isActive, boolean isAI) {
        this.name = name;
        this.isActive = isActive;
        this.isAI = isAI;
        this.hand = initPlayerHand();
    }

    /**
     * Player Constructor for loading existing player
     *
     * @param name player name
     * @param isActive is active player
     * @param isAI is AI player
     * @param hand exiting hand
     */
    public Player(String name, boolean isActive, boolean isAI, List<GameTiles> hand) {
        this.name = name;
        this.isActive = isActive;
        this.isAI = isAI;
        this.hand = hand;
    }

    /**
     * Initializes Player Hand with random GameTiles
     *
     * @return playerHand with random GameTiles
     */
    private List<GameTiles> initPlayerHand() {
        assert TileBag.getInstance().getTileBagSize() > 4;

        List<GameTiles> playerHand = new ArrayList<>();
        for (int i = 0; i < HANDSIZE; i++) {
            playerHand.add(TileBag.getInstance().getTile());
        }

        return playerHand;
    }

    /**
     * Removes GameTile (normally used by Player) from player hand
     *
     * @param tile GameTile to remove
     */
    public GameTiles pollGameTile(GameTiles tile) {
        assert hand.size() == HANDSIZE;

        int idx = lookupGameTile(tile);
        assert idx >= 0 && idx < hand.size();

        GameTiles gameTile = hand.get(idx);
        hand.remove(idx);

        return gameTile;
    }

    /**
     * Add GameTile (normally after player has played GameTile from hand)
     * to player hand
     *
     * @param tile GameTile to add
     */
    public void addGameTile(GameTiles tile) {
        assert hand.size() == HANDSIZE - 1;

        hand.add(tile);
    }

    /**
     * Lookup for first index of GameTile in player hand
     *
     * @param tile GameTile to lookup for
     * @return Index of GameTile in player hand ; If not existing return -1
     */
    public int lookupGameTile(GameTiles tile) {
        return hand.indexOf(tile);
    }
}
