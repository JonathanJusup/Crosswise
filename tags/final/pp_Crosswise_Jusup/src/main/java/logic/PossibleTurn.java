package logic;

/**
 * Record for Possible Turns proposed by AI_Player.
 * Contains all important parameters to make every turn possible
 * (Basic Turns & Wildcard Turns (1 Phase & 2 Phase)).
 * <p>
 * Contains overwritten Getter Methods. Otherwise, this
 * record only contains data, no other functionality.
 *
 * @param pos              Position
 * @param toPlace          GameTile to place
 * @param handSlot         HandSlot, from where tile is played
 * @param lastPosition     Last Position in case of tiles being switched
 * @param toSwap           GameTile to swap in case of swapping
 * @param points_team      Team points
 * @param points_opponent  Opponent points
 * @param points_team_gain Team points gain
 * @param hasSecondPhase   If second Phase wildcard is played
 * @param wildcard         Played Wildcard
 * @author Jonathan El Jusup (cgt104707)
 */
record PossibleTurn(Position pos, GameTiles toPlace, Integer handSlot, Position lastPosition,
                    GameTiles toSwap, int points_team, int points_opponent, int points_team_gain,
                    boolean hasSecondPhase, GameTiles wildcard) {

    /**
     * Pos Getter.
     *
     * @return pos
     */
    @Override
    public Position pos() {
        return pos;
    }

    /**
     * toPlace Getter.
     *
     * @return toPlace
     */
    @Override
    public GameTiles toPlace() {
        return toPlace;
    }

    /**
     * handSlot Getter.
     *
     * @return handSlot
     */
    @Override
    public Integer handSlot() {
        return handSlot;
    }

    /**
     * lastPosition Getter.
     *
     * @return lastPosition
     */
    @Override
    public Position lastPosition() {
        return lastPosition;
    }

    /**
     * toSwap Getter.
     *
     * @return toSwap
     */
    @Override
    public GameTiles toSwap() {
        return toSwap;
    }

    /**
     * points_team Getter.
     *
     * @return points_team
     */
    @Override
    public int points_team() {
        return points_team;
    }

    /**
     * points_opponent Getter.
     *
     * @return points_opponent
     */
    @Override
    public int points_opponent() {
        return points_opponent;
    }

    /**
     * hasSecondPhase Getter.
     *
     * @return hasSecondPhase
     */
    @Override
    public boolean hasSecondPhase() {
        return hasSecondPhase;
    }
}
