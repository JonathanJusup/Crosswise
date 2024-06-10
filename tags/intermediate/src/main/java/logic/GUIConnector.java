package logic;

/**
 * Interface used for the logic for the game Crosswise to communicate
 * with the GUI
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public interface GUIConnector {

    //Handle Tile Placement on Board::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Places GameTile on GameBoard and shows it on the GUI
     *
     * @param pos Position on GameBoard
     * @param gameTile GameTile to place
     */
    void placeTileOnBoard(Position pos, GameTiles gameTile);

    /**
     * Removes GameTile from the GameBoard
     *
     * @param pos Position on GameBoard
     * @param gameTile GameTile to remove
     */
    void removeTileFromBoard(Position pos, GameTiles gameTile);

    //Handle Tile Placement on Player Hand::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Removes specific GameTile from playerHand and shows it on the GUI
     *
     * @param player player
     * @param idx index of GameTile to remove
     */
    void removeTileFromHand(Player player, int idx/*, GameTiles tile*/);

    /**
     * Adds GameTile to playerHand and shows it to the GUI
     *
     * @param player player
     * @param tile tile to add
     */
    void addTileToHand(Player player, GameTiles tile);

    //Handle Optional Panels (Point Helpers & Point Table)::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Toggles Point Helpers to show or hide on GUI
     */
    void togglePointHelpers();

    /**
     * Toggles PointTable to show or hide on GUI
     */
    void togglePointTable();

    //Handle Gameplay:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Sets the name (and team) of the current player on the GUI
     *
     * @param name player name
     */
    void setCurrentPlayer(String name);

    /**
     * Called when the game is won by a team. Needs to display both names
     * of the winner team on the GUI. If there is no winner, but there are
     * also no empty fields left, no player has won. Result is a draw (which
     * should also be displayed on the GUI)
     *
     * Also has to ensure, that the user cannot continue playing the ended
     * game (e.g. by disabling components)
     *
     * @param winnerNames Both team members of winning team | Or no winner
     */
    void onGameEnd(String[] winnerNames);
}
