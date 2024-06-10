package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Bag of GameTiles contains 42 GameTiles containing 6 Standard GameTile Types, 12 Wildcards
 * containing 4 Wildcard Types. Contains fixed amount of GameTiles and Wildcards, but
 * at random positions in the queue.
 * <p>
 * 42 Game Tiles (7 * 6 Tile Types) + 12 Wildcards (3 * 4 Wildcard Types)
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class TileBag {
    /**
     * Number of gameTile, specified by gameRules
     */
    private final int GAMETILE_COUNT = GameTiles.STANDARD_GAMETILES_TYPES * GameTiles.STANDARD_GAMETILES_COUNT;
    /**
     * Number of wildcards, specified by gameRules
     */
    private final int WILDCARD_COUNT = GameTiles.WILDCARDS_TYPES * GameTiles.WILDCARDS_COUNT;
    /**
     * Randomizer Seed for deterministic TileBag Reconstruction.
     */
    private final int SEED = 42;
    /**
     * Queue of GameTiles
     */
    private final Queue<GameTiles> tileBag = new LinkedList<>();


    /**
     * TileBag Constructor.
     */
    TileBag() {
        List<GameTiles> allTiles = initAllTilesList();
        Random randomPicker = new Random();
        GameTiles randomTile;

        //Fill TileBag randomly
        for (int i = 0; i < GAMETILE_COUNT + WILDCARD_COUNT; i++) {
            int idx = randomPicker.nextInt(allTiles.size());

            randomTile = allTiles.get(idx);
            allTiles.remove(idx);

            this.tileBag.add(randomTile);
        }
    }

    /**
     * Test TileBag Constructor. Just for Testing purposes.
     * Fills TileBag with List of GameTiles. If not full,
     * fill with GameTiles.T_CROSS.
     */
    TileBag(List<GameTiles> tiles, boolean autoFill) {
        if (autoFill) {
            for (int i = 0; i < GAMETILE_COUNT + WILDCARD_COUNT; i++) {
                if (i < tiles.size()) {
                    this.tileBag.add(tiles.get(i));
                } else {
                    this.tileBag.add(GameTiles.T_CROSS);
                }
            }
        } else {
            this.tileBag.addAll(tiles);
        }
    }

    /**
     * TileBag Constructor. For loading an existing tileBag from Array of
     * int-values, representing the ordinal values of its corresponding GameTile.
     *
     * @param tiles     Array of GameTile ordinal values
     * @param randomize Flag, if TileBag should be randomized
     */
    TileBag(int[] tiles, boolean randomize) {
        if (randomize) {
            List<GameTiles> allTiles = new ArrayList<>(Arrays.stream(tiles).mapToObj(Utilities::ordinalToGameTiles).toList());
            Random randomPicker = new Random(SEED);
            GameTiles randomTile;

            //Fill TileBag randomly
            for (int i = 0; i < allTiles.size(); i++) {
                int idx = randomPicker.nextInt(allTiles.size());

                randomTile = allTiles.get(idx);
                allTiles.remove(idx);

                this.tileBag.add(randomTile);
            }

        } else {
            this.tileBag.addAll(Arrays.stream(tiles).mapToObj(Utilities::ordinalToGameTiles).toList());
        }
    }

    /**
     * Initializes an ArrayList with all 42 GameTiles and 12 Wildcard in
     * predictable order, from which random elements can get picked later on.
     *
     * @return GameTile List
     */
    private List<GameTiles> initAllTilesList() {
        List<GameTiles> allTiles = new ArrayList<>();

        //Fill with standard GameTiles
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

        //Fill with wildcards
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

        return allTiles;
    }


    /**
     * Gets first GameTile or Wildcard of randomized tileBag Queue
     *
     * @return randomized GameTile / Wildcard
     */
    GameTiles getTile() {
        return this.tileBag.poll();
    }

    /**
     * Returns the whole tileBag Queue.
     *
     * @return tileBag
     */
    Queue<GameTiles> getTileBag() {
        return this.tileBag;
    }

    /**
     * Getter for TileBag Size.
     *
     * @return TileBag Size Integer
     */
    int getTileBagSize() {
        return this.tileBag.size();
    }

    @Override
    public String toString() {
        return Arrays.toString(this.tileBag.toArray());
    }
}
