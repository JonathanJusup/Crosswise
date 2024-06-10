package logic;

/**
 * Enumeration for Game Evaluation States.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public enum GameStates {
    /**
     * Game is still ongoing
     */
    ONGOING_GAME,
    /**
     * Draw
     */
    DRAW,
    /**
     * Vertical Team wins
     */
    TEAM_VERTICAL,
    /**
     * Horizontal Team wins
     */
    TEAM_HORIZONTAL
}
