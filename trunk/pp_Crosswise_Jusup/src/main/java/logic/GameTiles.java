package logic;

/**
 * GameTiles and Wildcard Enumeration for the game Crosswise.
 * Contains static helper Methods to identify given GameTiles
 * and constants to grasp GameTiles structure.
 * <p>
 * GameTiles with Prefix "T_" are Standard GameTiles.
 * GameTiles with Prefix "WC_" are Wildcards.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public enum GameTiles {
    EMPTY,
    T_SUN,
    T_CROSS,
    T_TRIANGLE,
    T_SQUARE,
    T_PENTAGON,
    T_STAR,
    WC_REMOVER,
    WC_MOVER,
    WC_SWAPONBOARD,
    WC_SWAPWITHHAND;

    /**
     * Number of standard GameTile types
     */
    static final int STANDARD_GAMETILES_TYPES = 6;

    /**
     * Number of standard GameTiles per type
     */
    static final int STANDARD_GAMETILES_COUNT = 7;

    /**
     * Number of wildcard types
     */
    static final int WILDCARDS_TYPES = 4;

    /**
     * Number of wildcards per type
     */
    static final int WILDCARDS_COUNT = 3;

    /**
     * Checks, if the given GameTile is a Wildcard
     *
     * @param tile GameTile to check
     * @return GameTile is wildcard -> TRUE
     */
    public static boolean isWildcard(GameTiles tile) {
        return tile == GameTiles.WC_MOVER || tile == GameTiles.WC_REMOVER
                || tile == GameTiles.WC_SWAPONBOARD || tile == GameTiles.WC_SWAPWITHHAND;
    }

    /**
     * Checks, if given GameTile is a standard GameTile
     *
     * @param tile GameTile to check
     * @return GameTile is standard GameTile -> TRUE
     */
    public static boolean isStandardGameTile(GameTiles tile) {
        return tile != null
                && tile.ordinal() >= GameTiles.T_SUN.ordinal()
                && tile.ordinal() <= GameTiles.T_STAR.ordinal();
    }
}
