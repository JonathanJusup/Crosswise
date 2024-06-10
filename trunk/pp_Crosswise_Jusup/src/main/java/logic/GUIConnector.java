package logic;

import javafx.scene.image.ImageView;

import java.util.List;

/**
 * Interface used for the logic for the game Crosswise to communicate
 * with the GUI. Makes it possible for the logic to tell the GUI how
 * to react on certain actions made by the logic.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public interface GUIConnector {
    /**
     * Places GameTile on GameBoard and shows it on the GUI. If AI has
     * played, the gameTile gets placed with a certain delay specified
     * in implementing class.
     *
     * @param gameTile GameTile to place
     * @param pos      Position on GameBoard
     * @param isAITurn isAITurn, if it's an AI Turn
     */
    void placeTileOnBoard(GameTiles gameTile, Position pos, boolean isAITurn);

    /**
     * Disables all playerHands & enables specified playerHand
     * for User to be able to make a playerTurn. AI hands are always
     * disabled and not interactive.
     *
     * @param playerIdx player Index
     * @param isAI      isAI Flag, if to show
     */
    void enableAndDisablePlayerHands(int playerIdx, boolean isAI);

    /**
     * Disables playerHand with specified player Index
     *
     * @param playerIdx player Index
     */
    void disablePlayerHand(int playerIdx);

    /**
     * Hide all player Hands, by showing only empty GameTiles
     * regardless of what the player is actually holding.
     */
    void hideAllPlayerHands();

    /**
     * Does the same thing as "hideAllPlayerHands", but with a delay, so
     * all playerHands won't become hidden, while the AI is still playing.
     */
    void hideAllPlayerHands_Delay();

    /**
     * Shows current playerHand, by setting handSlot Images to actual tiles,
     * instead of keeping it hidden (EMPTY GameTile).
     * <p>
     * If playerHand belongs to an AI_Player, an additional condition,
     * specified by implementing class must be fulfilled, for the AI Hand
     * to be shown.
     *
     * @param playerIdx player Index
     * @param tiles     gameTiles to show
     * @param isAI      isAI Flag, if to show
     */
    void showPlayerHand(int playerIdx, List<GameTiles> tiles, boolean isAI);

    /**
     * Does the same thing as "showPlayerHand", but with a delay, so the next
     * playerHand won't be shown while the AI is still playing.
     *
     * @param playerIdx player Index
     * @param tiles     gameTiles to show
     * @param isAI      isAI Flag, if to show
     */
    void showPlayerHand_Delay(int playerIdx, List<GameTiles> tiles, boolean isAI);

    /**
     * Removes specific GameTile from playerHand and shows it on the GUI
     * as EMPTY GameTile. Usually the result is not seen, due to the
     * playerHand instantly getting another GameTile at the same Position.
     *
     * @param player   player
     * @param handSlot handSlot Index, where to remove
     */
    void removeTileFromHandAt(int player, int handSlot);

    /**
     * Adds GameTile to playerHand and shows it to the GUI. If playerHand
     * belongs to an AI_Player, additional conditions, specified by
     * implementing class must be fulfilled in order to show added Tile
     * on playerHand.
     *
     * @param player   player
     * @param tile     tile to add
     * @param handSlot handSlot Index, where to add
     * @param isAI     isAI Flag if to show
     */
    void addTileToHandAt(int player, GameTiles tile, int handSlot, boolean isAI);

    /**
     * Sets GameTile of playerHand at specified handSlot to a given GameTile
     * and shows it on the GUI.
     *
     * @param playerIdx   Player Index
     * @param handSlotIdx handSlot, where to set
     * @param tile        GameTile to set
     */
    void setGameTileOnHand(int playerIdx, int handSlotIdx, GameTiles tile);

    /**
     * Sets the name of the current player on the GUI.
     *
     * @param name player name
     */
    void setCurrentPlayer(String name);

    /**
     * Updates optional Point bars after each playerTurn via specified
     * point Arrays for both teams.
     *
     * @param verticalPoints   Segment Points of vertical Team
     * @param horizontalPoints Segment Points of horizontal Team
     */
    void updatePointBars(int[] verticalPoints, int[] horizontalPoints);

    /**
     * Updates optional Team Points Labels on Team Information Panels.
     *
     * @param teamPoints_1 Team 1 Points
     * @param teamPoints_2 Team 2 Points
     */
    void updateTeamPoints(int teamPoints_1, int teamPoints_2);

    /**
     * Updates usedWildcard Labels based on previously played wildcards.
     *
     * @param usedWildcards int-Array of all used Wildcards
     */
    void updateUsedWildcards(int[] usedWildcards);

    /**
     * Called when the game is over. A game is over, when a team has won,
     * both teams draw or player was unable to make a move. This last case
     * happens mostly at late stages of the game and is not an error.
     * <p>
     * Needs to display both names of the winner team on the GUI. If there
     * is no winner, but there are also no empty fields left, no player has
     * won. Result is a draw (which should also be displayed on the GUI)
     * <p>
     * Also has to ensure, that the user cannot continue playing the ended
     * game (e.g. by disabling components)
     *
     * @param playerNamesT1 PlayerNames of Team 1
     * @param playerNamesT2 PlayerNames of Team 2
     * @param teamPoints    winner team points
     * @param winnerTeam    winner Team / draw
     */
    void onGameEnd(String[] playerNamesT1, String[] playerNamesT2, int[] teamPoints, GameStates winnerTeam);

    /**
     * Does same thing as onGameEnd, but with a delay, so the GameEnd Popup won't
     * show up, while the AI is still playing the last move.
     *
     * @param playerNamesT1 PlayerNames of Team 1
     * @param playerNamesT2 PlayerNames of Team 2
     * @param teamPoints    winner team points
     * @param winnerTeam    winner Team / draw
     */
    void onGameEnd_Delay(String[] playerNamesT1, String[] playerNamesT2, int[] teamPoints, GameStates winnerTeam);

    /**
     * Sets a combination of DragAndDrop Handlers to specified ImageView Source.
     *
     * @param source ImageView to set DragAndDrop Handlers
     */
    void setSourceDragHandler(ImageView source);

    /**
     * Sets a combination of DragAndDrop Handlers to the whole gameBoard.
     * -Swap = FALSE, when Wildcard MOVE is used
     * -Swap = TRUE, when Wildcard SWAPONBOARD is used
     *
     * @param game game Instance
     * @param swap GameTile.MOVE -> FALSE | GameTile.SWAPONBOARD -> TRUE
     */
    void setBoardDragHandler(Game game, boolean swap);

    /**
     * Resets all DragAndDrop Handlers of the whole gameBoard.
     *
     * @param game       game Instance
     * @param isSwapping isSwapping Flag, if is swapping
     */
    void resetBoardDragHandler(Game game, boolean isSwapping);

    /**
     * Sets text of Announcement Label with specified message.
     *
     * @param msg message to set
     */
    void setAnnouncementLabel(String msg);

    /**
     * Closes all active Popup Windows, especially GameEndPopup
     * If no Popup Window is active, does nothing.
     */
    void closePopups();

    /**
     * Creates temporary ImageView of GameTile which should be animated.
     * ImageView gets translated via Path Transition from specified starting
     * Position from sourceNode to target Position at targetNode.
     * <p>
     * This animation Method is used for every single animation constellation.
     * Whether animating an ImageView from playerHand at specified handSlot
     * to gameBoard, the other way around, or animating ImageView from
     * gameBoard to another Position on gameBoard.
     * <p>
     * This Method must break separation of Logic & GUI in order to trigger
     * next AI Turn.
     *
     * @param tile          gameTile, from which imageView to animate is created
     * @param currentPlayer currentPlayer
     * @param handSlot      handSlot from where GameTile is played
     * @param sourcePos     Source Node Position
     * @param targetPos     Target Node Position
     * @param game          Game Instance
     * @param doNextTurn    doNextTurn Flag
     */
    void createAndPlayAnimation(GameTiles tile, int currentPlayer, Integer handSlot,
                                Position sourcePos, Position targetPos, Game game, boolean doNextTurn);

    /**
     * Stopps currently running animation by stopping PathTransition
     * and removing involved ImageView from animationPane. Played Pause
     * cannot be skipped though.
     */
    void stopAnimation();

    /**
     * Updates animation duration by reading, which RadioMenuItem is
     * currently selected. All 3 Animation durations may be changed
     * via Constants.
     */
    void updateAnimationDuration();

    /**
     * Sets internal Flag to false for completely halting all actions,
     * especially not calling nextTurn after PauseTransition is finished.
     * This method is called, when the game has ended.
     */
    void forceStop();

    /**
     * Sets internal Flag to false for completely halting all actions,
     * especially not calling nextTurn after PauseTransition is finished.
     * This method is called, when new game has started
     */
    void interrupt();

    /**
     * Creates new Alert with errorType and message.
     *
     * @param errorType errorType
     * @param msg       message
     */
    void createAlert(ErrorType errorType, String msg);
}
