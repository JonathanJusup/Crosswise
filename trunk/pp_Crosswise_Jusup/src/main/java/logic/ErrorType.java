package logic;

/**
 * ErrorType Enum for possible Error Situations while
 * Application Runs.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public enum ErrorType {
    /**
     * No Error
     */
    NONE,

    /**
     * Invalid Player Name / Duplicate Names
     */
    INVALID_PLAYER_NAME,

    /**
     * Invalid File to save/ load
     */
    INVALID_SAVE_FILE,

    /**
     * Impossible to play Wildcard
     */
    UNABLE_TO_PLAY_WILDCARD,

    /**
     * Critical Errors, that -theoretically- cannot happen.
     * If occurring, thrown Exception should break the game.
     */
    LOGIC_GAME_BREAK,

    /**
     * Small chance, that first player only gets wildcards,
     * which he cannot play on empty gameBoard.
     */
    ONLY_WILDCARDS_ALLOCATED
}
