package logic;

/**
 * GameData Class for saving and loading an existing game. Contains Player-Instances
 * as PlayerData Instances. GameBoard and tileBag are converted to Integer Arrays
 * for better readability in save File.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class GameData {

    /**
     * PlayerData Array stores each Player-Instance.
     */
    private final PlayerData[] players;
    /**
     * Current Player of current/loaded Game.
     */
    private final Integer currPlayer;
    /**
     * GameBoard of current/loaded Game.
     */
    private final int[][] field;
    /**
     * Array of used Wildcards.
     */
    private final int[] usedActionTiles;
    /**
     * Array of each tileBag GameTile as its ordinal value.
     */
    private final int[] tileBag;


    /**
     * GameData constructor.
     *
     * @param players         PlayerData
     * @param currPlayer      currentPlayer
     * @param field           gameBoard
     * @param usedActionTiles usedWildcards
     * @param tileBag         tileBag
     */
    GameData(PlayerData[] players, int currPlayer, int[][] field,
             int[] usedActionTiles, int[] tileBag) {

        this.players = players;
        this.currPlayer = currPlayer;
        this.field = field;
        this.usedActionTiles = usedActionTiles;
        this.tileBag = tileBag;
    }

    /**
     * players Getter.
     *
     * @return players
     */
    PlayerData[] getPlayers() {
        return this.players;
    }

    /**
     * currentPlayer Getter.
     *
     * @return currentPlayer
     */
    int getCurrPlayer() {
        return this.currPlayer;
    }

    /**
     * gameBoard Getter.
     *
     * @return gameBoard
     */
    int[][] getField() {
        return this.field;
    }

    /**
     * usedWildcards Getter.
     *
     * @return usedWildcards
     */
    int[] getUsedActionTiles() {
        return this.usedActionTiles;
    }

    /**
     * tileBag Getter.
     *
     * @return tileBag
     */
    int[] getTileBag() {
        return this.tileBag;
    }

}
