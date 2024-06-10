package logic;

/**
 * FakeGUI used for testing the logic of the game Crosswise. All methods
 * do nothing, but the logic works.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class FakeGUI implements GUIConnector{
    /**
     * Places GameTile on GameBoard and shows it on the GUI
     *
     * @param pos      Position on GameBoard
     * @param gameTile GameTile to place
     */
    @Override
    public void placeTileOnBoard(Position pos, GameTiles gameTile) {

    }

    /**
     * Removes GameTile from the GameBoard
     *
     * @param pos      Position on GameBoard
     * @param gameTile GameTile to remove
     */
    @Override
    public void removeTileFromBoard(Position pos, GameTiles gameTile) {

    }

    /**
     * Removes specific GameTile from playerHand and shows it on the GUI
     *
     * @param player player
     * @param idx    index of GameTile to remove
     */
    @Override
    public void removeTileFromHand(Player player, int idx) {

    }

    /**
     * Adds GameTile to playerHand and shows it to the GUI
     *
     * @param player player
     * @param tile   tile to add
     */
    @Override
    public void addTileToHand(Player player, GameTiles tile) {

    }

    /**
     * Toggles Point Helpers to show or hide on GUI
     */
    @Override
    public void togglePointHelpers() {

    }

    /**
     * Toggles PointTable to show or hide on GUI
     */
    @Override
    public void togglePointTable() {

    }

    /**
     * Sets the name (and team) of the current player on the GUI
     *
     * @param name player name
     */
    @Override
    public void setCurrentPlayer(String name) {

    }

    /**
     * Called when the game is won by a team. Needs to display both names
     * of the winner team on the GUI. If there is no winner, but there are
     * also no empty fields left, no player has won. Result is a draw (which
     * should also be displayed on the GUI)
     * <p>
     * Also has to ensure, that the user cannot continue playing the ended
     * game (e.g. by disabling components)
     *
     * @param winnerNames Both team members of winning team | Or no winner
     */
    @Override
    public void onGameEnd(String[] winnerNames) {

    }
}
