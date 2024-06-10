package logic;

import java.util.*;

/**
 * Bag of GameTiles contains 42 GameTiles containing 6 GameTile Types, 12 Wildcards
 * containing 4 Wildcard Types. Contains fixed amount of GameTiles and Wildcards, but
 * at random positions in the queue.
 *
 * This Class is a Singleton, whose instance is shared by all player instances.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public final class TileBag {
    //42 Game Tiles (7 * 6 Tile Types) + 12 Wildcards (3 * 4 Wildcard Types)
    private final int GAMETILE_COUNT = 42;
    private final int WILDCARD_COUNT = 12;


    //Singleton Instance
    private static final TileBag tileBagInstance = new TileBag();
    private final Queue<GameTiles> tileBag = new LinkedList<>();

    /**
     * Private TileBag Constructor. Singleton Instance creates itself
     */
    private TileBag() {
        List<GameTiles> allTiles = initAllTilesList();
        Random randomPicker = new Random();
        GameTiles randomTile;

        for (int i = 0; i < GAMETILE_COUNT + WILDCARD_COUNT; i++) {
            int idx = randomPicker.nextInt(allTiles.size());

            randomTile = allTiles.get(idx);
            allTiles.remove(idx);

            this.tileBag.add(randomTile);
        }

        //System.out.println("tileBag: " + Arrays.toString(this.tileBag.toArray()));
    }

    /**
     * Gets TileBag Singleton Instance
     * @return TileBag Instance
     */
    public static TileBag getInstance() {
        return tileBagInstance;
    }

    /**
     * Initializes an ArrayList with all 42 GameTiles and 12 Wildcard in
     * predictable order, from which random elements can get picked later
     *
     * @return GameTile List
     */
    private List<GameTiles> initAllTilesList() {
        List<GameTiles> allTiles = new ArrayList<>();

        for (int i = 0; i < GAMETILE_COUNT; i++) {
            int gameTileTypes = 6;
            switch (i % gameTileTypes) {
                case 0 -> allTiles.add(GameTiles.T_SUN);
                case 1 -> allTiles.add(GameTiles.T_CROSS);
                case 2 -> allTiles.add(GameTiles.T_TRIANGLE);
                case 3 -> allTiles.add(GameTiles.T_SQUARE);
                case 4 -> allTiles.add(GameTiles.T_PENTAGON);
                case 5 -> allTiles.add(GameTiles.T_STAR);
                default -> System.err.println("ERROR: Couldn't find <GAMETILE> to add to list");
            }
        }

        for (int i = 0; i < WILDCARD_COUNT; i++) {
            int wildcardTypes = 4;
            switch (i % wildcardTypes) {
                case 0 -> allTiles.add(GameTiles.WC_REMOVER);
                case 1 -> allTiles.add(GameTiles.WC_MOVER);
                case 2 -> allTiles.add(GameTiles.WC_SWAPONBOARD);
                case 3 -> allTiles.add(GameTiles.WC_SWAPWITHHAND);
                default -> System.err.println("ERROR: Couldn't find <WILDCARD> to add to list");
            }
        }

        //System.out.println(allTiles.toString());
        return allTiles;
    }

    /**
     * Gets first GameTile or Wildcard of randomized tileBag Queue
     * @return randomized GameTile / Wildcard
     */
    public GameTiles getTile() {
        return this.tileBag.poll();
    }

    /**
     * Returns the whole tileBag Queue
     * @return tileBag
     */
    public Queue<GameTiles> getTileBag() {
        return this.tileBag;
    }

    /**
     * Getter for TileBag Size
     * @return TileBag Size Integer
     */
    public int getTileBagSize() {
        return this.tileBag.size();
    }
}
