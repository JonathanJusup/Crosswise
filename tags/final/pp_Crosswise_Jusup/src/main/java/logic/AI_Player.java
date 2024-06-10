package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AI Player Class extends from Player. Represents AI Player, which
 * emulates a human player. It abides to following rules:
 * <p>
 * -Achieve "Win of Sixes" (also via wildcards if possible)
 * -Prevent opponent "Win of Sixes" (also via wildcards if possible)
 * -AI cannot see other playerHands
 * -Achieve most points possible
 * <p>
 * -If there are multiple equally ranked possible turns:
 * -Standard GameTile if preferred over Wildcard
 * -Use most occurring GameTile in playerHand
 * -Use least occurring GameTile on gameBoard
 * <p>
 * -If there are still multiple possible GameTiles to play:
 * -Use GameTile with the smallest ordinal value
 * <p>
 * -If there are multiple possible Positions:
 * -Place on smallest possible position (top-left)
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class AI_Player extends Player {

    /**
     * Team Flag for the AI to know, which team it's assigned to.
     */
    private final boolean isVerticalTeam;
    /**
     * GameBoard Instance, only for analyzing the gameBoard.
     * Never manipulates the gameBoard by setting or removing
     * a gameTile. All board manipulation happens locally on
     * board copies.
     */
    private GameBoard board;
    /**
     * Second Phase Flag for 2-Phase Wildcard turns.
     * 2-Phase Wildcard turns are 2 separate turns and
     * must be returned separately.
     */
    private PossibleTurn secondPhaseTurn;


    /**
     * AI Player constructor.
     *
     * @param name           AI_Player name
     * @param isActive       isActive Flag
     * @param isAI           isAI Flag
     * @param isVerticalTeam corresponding team Flag
     */
    public AI_Player(String name, boolean isActive, boolean isAI, boolean isVerticalTeam) {
        super(name, isActive, isAI);
        this.isVerticalTeam = isVerticalTeam;
    }

    /**
     * AI Player constructor for loading an existing AI Player.
     * Initializes with existing playerHand.
     *
     * @param name           AI_Player name
     * @param isActive       isActive Flag
     * @param isAI           isAI Flag
     * @param isVerticalTeam corresponding team Flag
     */
    public AI_Player(String name, boolean isActive, boolean isAI, int[] hand, boolean isVerticalTeam) {
        super(name, isActive, isAI, hand);
        this.isVerticalTeam = isVerticalTeam;
    }

    /**
     * board Setter. Just for testing purposes.
     *
     * @param board board to set
     */
    void setBoard(GameBoard board) {
        this.board = board;
    }

    /**
     * secondPhaseTurn Getter.
     *
     * @return secondPhaseTurn
     */
    PossibleTurn getSecondPhaseTurn() {
        return secondPhaseTurn;
    }

    /**
     * Calculates all possible Turns and evaluates them to one single optimal
     * AI Turn to make. Only one optimal possible Turn can be returned.
     * <p>
     * Its possible, that the AI cannot make a turn due to its deterministic
     * rules. (E.g. If AI had only one possible turn to make, which is to achieve
     * a winOfSixes for the opponent team, it would not play). Therefore, only one
     * optimal turn or <null> will be returned.
     *
     * @param gameBoard gameBoard Instance to evaluate best turn on
     * @return Single optimal turn | If not possible -> null
     */
    PossibleTurn evaluateToBestTurn(GameBoard gameBoard) {
        //Early returns, if secondPhaseTurn exists or gameBoard is already full
        if (secondPhaseTurn != null) {
            PossibleTurn secondPhase = secondPhaseTurn;
            secondPhaseTurn = null;
            return secondPhase;
        } else if (gameBoard.isFull()) {
            return null;
        }

        this.board = gameBoard;
        List<PossibleTurn> possibleTurns = new ArrayList<>();

        //Get optimal Turn of each GameTile in AI playerHand::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        //Iterate through own playerHand & reduce to 4 optimal Turns
        //There is only one optimal turn per handItem
        List<GameTiles> playerHand = this.getHand();
        for (int i = 0; i < playerHand.size(); i++) {
            GameTiles tile = playerHand.get(i);

            //In lateGame, there can be EMPTY GameTiles in hand
            if (tile != GameTiles.EMPTY) {
                if (GameTiles.isStandardGameTile(tile)) {
                    possibleTurns.addAll(getAllTurns_basic(tile, i));
                } else if (GameTiles.isWildcard(tile)) {
                    possibleTurns.addAll(getTurns_wildcard(tile, i));
                }
            }
        }

        PossibleTurn sixesTurn = null;
        List<PossibleTurn> bestTurns = new ArrayList<>();

        //Check if "Sixes Turn" is possible:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //A Sixes Turn will be preferred over all other possible turns

        //If WinOfSixes Turn can be achieved / prevented, then prefer over every other turn possibility
        for (int i = 0; i < possibleTurns.size() && sixesTurn == null; i++) {
            PossibleTurn possibleTurn = possibleTurns.get(i);
            if (possibleTurn.points_team() == Integer.MAX_VALUE) {

                //If WinOfSixes can be achieved
                sixesTurn = possibleTurn;

            } else {

                //If WinOfSixes can be prevented
                if (possibleTurn.wildcard() == GameTiles.WC_MOVER) {
                    //Special Case: MOVER
                    //Consider Points of Position, where Tile is moved from, NOT where it's moved to

                    //Detect opponent WinOfSixes Hazard
                    if (Game.calculatePoints(getTilesPerSegment(
                            isVerticalTeam ? possibleTurn.lastPosition().row() : possibleTurn.lastPosition().column(),
                            !isVerticalTeam, gameBoard.getGameBoard())) == 7) {

                        //Prevent Win on Sixes
                        if (possibleTurn.points_opponent() <= 7) {
                            //Possible Win of Sixes for opponent Team was prevented due to points staying the same
                            //Segment of opponent is now full of GameTiles

                            if (board.getGameTileOccurrencesAtSegment(
                                    isVerticalTeam ? possibleTurn.pos().row() : possibleTurn.pos().column(),
                                    !isVerticalTeam).get(possibleTurn.toPlace()) == 0) {

                                sixesTurn = possibleTurn;

                            }
                        }
                    }
                } else {

                    //Detect opponent WinOfSixes Hazard
                    if (Game.calculatePoints(getTilesPerSegment(
                            isVerticalTeam ? possibleTurn.pos().row() : possibleTurn.pos().column(),
                            !isVerticalTeam, gameBoard.getGameBoard())) == 7) {

                        //Prevent Win on Sixes
                        if (possibleTurn.points_opponent() <= 7) {
                            //Possible Win of Sixes for opponent Team was prevented due to points staying the same
                            //Segment of opponent is now full of GameTiles

                            if (board.getGameTileOccurrencesAtSegment(
                                    isVerticalTeam ? possibleTurn.pos().row() : possibleTurn.pos().column(),
                                    !isVerticalTeam).get(possibleTurn.toPlace()) == 0) {

                                sixesTurn = possibleTurn;

                            }
                        }
                    }
                }
            }

            //Always add possible turns next to possible sixes turn
            bestTurns.add(possibleTurn);
        }

        //Reduce to a few final Turns:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        List<PossibleTurn> finalTurns = new ArrayList<>();

        if (sixesTurn == null) {
            ArrayList<PossibleTurn> turns_mostPoints = new ArrayList<>(bestTurns);
            if (bestTurns.size() > 1) {
                //Get Possible Turn with most points
                PossibleTurn maxGainTurn = Collections.max(
                        bestTurns, Comparator.comparing(PossibleTurn::points_team_gain));

                //Adding all possible Turns which have same points as maxPointTurn including maxPointTurn;
                turns_mostPoints = new ArrayList<>(bestTurns.stream()
                        .filter(c -> c.points_team_gain() == maxGainTurn.points_team_gain()).toList());

            }

            //Reduce mostPoints Turns (only applies to moistPointTurns)
            finalTurns = reduceToFinalTurns(turns_mostPoints);


        } else {
            //If Sixes Turn exists, prefer it over any further turns
            finalTurns.add(sixesTurn);
        }


        //Handle Possibility, that no turn is possible or that there are multiple final Turns
        PossibleTurn finalTurn = finalTurns.isEmpty() ? null : finalTurns.get(0);

        //Differentiate between 1- and 2-Phase Turns
        if (finalTurn != null && finalTurn.hasSecondPhase()) {
            //2-Phase Turn

            boolean keepInHand = finalTurn.wildcard().equals(GameTiles.WC_MOVER) ||
                    finalTurn.wildcard().equals(GameTiles.WC_SWAPONBOARD);

            //Store 2. Phase use it next time
            secondPhaseTurn = new PossibleTurn(
                    finalTurn.pos(),
                    finalTurn.toPlace(),
                    keepInHand ? null : finalTurn.handSlot(),
                    finalTurn.lastPosition(),
                    finalTurn.toSwap(),
                    finalTurn.points_team(),
                    finalTurn.points_opponent(),
                    finalTurn.points_team_gain(),
                    false,
                    null
            );

            //Return 1. Phase Turn
            GameTiles tile = this.getHand().get(finalTurn.handSlot());
            return new PossibleTurn(
                    finalTurn.pos(),
                    finalTurn.wildcard() == null ? tile : finalTurn.wildcard(),
                    finalTurn.handSlot(),
                    finalTurn.lastPosition(),
                    finalTurn.toSwap(),
                    finalTurn.points_team(),
                    finalTurn.points_opponent(),
                    finalTurn.points_team_gain(),
                    finalTurn.hasSecondPhase(),
                    finalTurn.wildcard());
        } else {
            //1-Phase Turn
            return finalTurn;
        }
    }


    /**
     * Gets all basic non-wildcard AI-Turns by potentially placing the specified
     * gameTile on every possible location on board and calculating the Gain in points
     * for the team.
     *
     * @param tile     GameTile to get the best Turn with
     * @param handSlot handSlot, where tile is played from
     * @return Optimal basic AI Turns
     */
    List<PossibleTurn> getAllTurns_basic(GameTiles tile, int handSlot) {
        GameTiles[][] actualBoard = board.getGameBoard();
        ArrayList<PossibleTurn> possibleTurns = new ArrayList<>();


        //First get all possible Turns
        for (int x = 0; x < actualBoard.length; x++) {
            for (int y = 0; y < actualBoard[x].length; y++) {
                Position pos = new Position(x, y);

                if (board.isFreeAt(pos)) {
                    GameTiles[][] potentialBoard = board.getGameBoard();
                    potentialBoard[pos.column()][pos.row()] = tile;

                    int points_team_old = Game.calculatePoints(
                            getTilesPerSegment(isVerticalTeam ? x : y, isVerticalTeam, actualBoard));
                    int points_team_new = Game.calculatePoints(
                            getTilesPerSegment(isVerticalTeam ? x : y, isVerticalTeam, potentialBoard));
                    int points_opponent = Game.calculatePoints(
                            getTilesPerSegment(!isVerticalTeam ? x : y, !isVerticalTeam, potentialBoard));

                    int points_team_gain = points_team_new - points_team_old;
                    possibleTurns.add(new PossibleTurn(pos, tile, handSlot, null, null,
                            points_team_new, points_opponent, points_team_gain, false, null));
                }
            }
        }

        return possibleTurns;
    }

    /**
     * Gets the best Wildcard Turns by potentially playing specified wildcard in every
     * possible constellation. Calculates the Gain in points for the team and evaluates
     * the best wildcard turns to return. If WinOfSixes can be achieved or prevented,
     * this turn will be the only one best turn.
     *
     * @param tile     Wildcard to get the best Turn with
     * @param handSlot handSlot, where wildcard is played from
     * @return Optimal wildcard AI Turns
     */
    private List<PossibleTurn> getTurns_wildcard(GameTiles tile, int handSlot) {
        GameTiles[][] actualBoard = board.getGameBoard();

        ArrayList<PossibleTurn> possibleTurns = new ArrayList<>();
        if (board.usedSpaces() == 0) {
            return new ArrayList<>();
        }

        switch (tile) {
            case WC_REMOVER -> possibleTurns.addAll(allWildcardTurns_REMOVER(handSlot, actualBoard));
            case WC_MOVER -> possibleTurns.addAll(allWildcardTurns_MOVER(handSlot, actualBoard));
            case WC_SWAPONBOARD -> possibleTurns.addAll(allWildcardTurns_SWAPONBOARD(handSlot, actualBoard));
            case WC_SWAPWITHHAND -> possibleTurns.addAll(allWildcardTurns_SWAPWITHHAND(actualBoard));
        }

        PossibleTurn sixesTurn = null;

        //Then evaluate each possible Turn
        for (int i = 0; i < possibleTurns.size() && sixesTurn == null; i++) {
            PossibleTurn turn = possibleTurns.get(i);
            if (turn.points_team() == Integer.MAX_VALUE) {

                //If WinOfSixes can be achieved
                sixesTurn = turn;

            } else {

                //If WinOfSixes can be prevented
                if (turn.wildcard() == GameTiles.WC_MOVER) {
                    //Special Case: MOVER
                    //Consider Points of Position, where Tile is moved from, NOT where it's moved to
                    //AI ideally want to eliminate the WinOfSixes Hazard by moving a part of it to somewhere else

                    //Detect opponent WinOfSixes Hazard
                    if (Game.calculatePoints(getTilesPerSegment(
                            isVerticalTeam ? turn.lastPosition().row() : turn.lastPosition().column(),
                            !isVerticalTeam, actualBoard)) == 7) {

                        sixesTurn = getBestWinOfSixesPrevention(turn, possibleTurns);
                    }
                } else {
                    //Detect opponent WinOfSixes Hazard
                    if (Game.calculatePoints(getTilesPerSegment(
                            isVerticalTeam ? turn.pos().row() : turn.pos().column(),
                            !isVerticalTeam, actualBoard)) == 7) {

                        sixesTurn = getBestWinOfSixesPrevention(turn, possibleTurns);
                    }
                }
            }
        }

        //If Sixes Turns exists, it shall be the only Turn to return
        return sixesTurn == null ? possibleTurns : List.of(sixesTurn);
    }

    /**
     * Calculates out of all possible Wildcard Turns the best one to block an opponent
     * WinOfSixes by either blocking it so the points of the opponent segment stay the
     * same or ideally the points lower in value.
     *
     * @param turn     turn to check if it's the best one
     * @param allTurns all possible wildcard Turns
     * @return Best WinOfSixes blocking wildcard Turn
     */
    private PossibleTurn getBestWinOfSixesPrevention(PossibleTurn turn, List<PossibleTurn> allTurns) {
        PossibleTurn result = null;

        if (turn.points_opponent() <= 7) {

            //Blocking GameTile cannot be the same GameTile the WinOfSixes Hazard of opponent is made of
            if (board.getGameTileOccurrencesAtSegment(
                    isVerticalTeam ? turn.pos().row() : turn.pos().column(),
                    !isVerticalTeam).get(turn.toPlace()) == 0) {

                //Get Turn, that results in most point loss for opponent
                OptionalInt mostLossPoints = allTurns.stream()
                        .mapToInt(PossibleTurn::points_opponent).min();

                if (mostLossPoints.isPresent() && turn.points_opponent() == mostLossPoints.getAsInt()) {
                    result = turn;
                }
            }
        }

        return result;
    }


    /**
     * Gets all possible Turns for the wildcard REMOVER. This method is used by
     * getBestTurn_wildcard.
     *
     * @param handSlot    handSlot, where wildcard is played from
     * @param actualBoard actualBoard
     * @return all possible Turns for Wildcard REMOVER
     */
    List<PossibleTurn> allWildcardTurns_REMOVER(int handSlot, GameTiles[][] actualBoard) {
        GameTiles[][] potentialBoard;
        List<PossibleTurn> result = new ArrayList<>();

        //Iterate through whole gameBoard
        for (int x = 0; x < actualBoard.length; x++) {
            for (int y = 0; y < actualBoard[x].length; y++) {
                potentialBoard = board.getGameBoard();
                Position pos = new Position(x, y);

                //REMOVER can only be placed on non-EMPTY gameTiles
                if (!board.isFreeAt(pos)) {
                    potentialBoard[pos.column()][pos.row()] = GameTiles.EMPTY;

                    int points_team_old = Game.calculatePoints(
                            getTilesPerSegment(isVerticalTeam ? x : y, isVerticalTeam, actualBoard));
                    int points_team_new = Game.calculatePoints(
                            getTilesPerSegment(isVerticalTeam ? x : y, isVerticalTeam, potentialBoard));
                    int points_opponent_new = Game.calculatePoints(
                            getTilesPerSegment(!isVerticalTeam ? x : y, !isVerticalTeam, potentialBoard));

                    int points_team_gain = points_team_new - points_team_old;

                    //Add new possible turn
                    result.add(new PossibleTurn(pos, GameTiles.WC_REMOVER, handSlot,
                            null, null, points_team_new, points_opponent_new,
                            points_team_gain, false, GameTiles.WC_REMOVER));
                }
            }
        }

        return result;
    }

    /**
     * Gets all possible Turns for the wildcard MOVER. This method is used by
     * getBestTurn_wildcard.
     *
     * @param handSlot    handSlot, where wildcard is played from
     * @param actualBoard actualBoard
     * @return all possible Turns for Wildcard MOVER
     */
    List<PossibleTurn> allWildcardTurns_MOVER(int handSlot, GameTiles[][] actualBoard) {
        GameTiles[][] potentialBoard;
        List<PossibleTurn> result = new ArrayList<>();

        //Iterate through whole gameBoard
        for (int x = 0; x < actualBoard.length; x++) {
            for (int y = 0; y < actualBoard[x].length; y++) {
                Position lastPosition = new Position(x, y);

                //MOVER can only be played on non-EMPTY GameTile
                if (!board.isFreeAt(lastPosition)) {
                    GameTiles toPlace = board.getGameTileAt(lastPosition);

                    //All free Positions to place "placeTo" GameTile on
                    for (int x2 = 0; x2 < actualBoard.length; x2++) {
                        for (int y2 = 0; y2 < actualBoard[x2].length; y2++) {
                            potentialBoard = board.getGameBoard();
                            Position newPos = new Position(x2, y2);

                            //MOVER can only move GameTile to new Position, which is free on gameBoard
                            if (!newPos.equals(lastPosition) && board.isFreeAt(newPos)) {
                                potentialBoard[lastPosition.column()][lastPosition.row()] = GameTiles.EMPTY;
                                potentialBoard[newPos.column()][newPos.row()] = toPlace;

                                int points_team_old = Game.calculatePoints(getTilesPerSegment(
                                        isVerticalTeam ? x2 : y2, isVerticalTeam, actualBoard));
                                int points_team_new = Game.calculatePoints(getTilesPerSegment(
                                        isVerticalTeam ? x2 : y2, isVerticalTeam, potentialBoard));
                                int points_opponent_new = Game.calculatePoints(getTilesPerSegment(
                                        !isVerticalTeam ? x : y, !isVerticalTeam, potentialBoard));

                                //Add new possible Turn
                                int points_team_gain = points_team_new - points_team_old;
                                result.add(new PossibleTurn(newPos, toPlace, handSlot, lastPosition,
                                        GameTiles.EMPTY, points_team_new, points_opponent_new, points_team_gain,
                                        true, GameTiles.WC_MOVER));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Gets all possible Turns for the wildcard SWAPONBOARD. This method is used by
     * getBestTurn_wildcard.
     *
     * @param handSlot    handSlot, where wildcard is played from
     * @param actualBoard actualBoard
     * @return all possible Turns for Wildcard SWAPONBOARD
     */
    List<PossibleTurn> allWildcardTurns_SWAPONBOARD(int handSlot, GameTiles[][] actualBoard) {
        GameTiles[][] potentialBoard;
        List<PossibleTurn> result = new ArrayList<>();

        //In order to swapOnBoard, at least 2 GameTiles must be on gameBoard
        if (board.usedSpaces() >= 2) {

            //Iterate through whole gameBoard
            for (int x = 0; x < actualBoard.length; x++) {
                for (int y = 0; y < actualBoard[x].length; y++) {
                    Position lastPosition = new Position(x, y);

                    //SWAPONBOARD can only be played on non-EMPTY GameTile on gameBoard
                    if (!board.isFreeAt(lastPosition)) {
                        GameTiles toPlace = board.getGameTileAt(lastPosition);

                        //All used Positions to swap "placeTo" GameTile with "swapWith"
                        for (int x2 = 0; x2 < actualBoard.length; x2++) {
                            for (int y2 = 0; y2 < actualBoard[x2].length; y2++) {
                                potentialBoard = board.getGameBoard();
                                Position newPos = new Position(x2, y2);

                                //Can only swap with non-EMPTY GameTile on another Position than self on gameBoard
                                if (!newPos.equals(lastPosition) && !board.isFreeAt(newPos)) {
                                    GameTiles toSwap = board.getGameTileAt(newPos);
                                    potentialBoard[newPos.column()][newPos.row()] = toPlace;
                                    potentialBoard[lastPosition.column()][lastPosition.row()] = toSwap;

                                    int points_team_old = Game.calculatePoints(getTilesPerSegment(
                                            isVerticalTeam ? x2 : y2, isVerticalTeam, actualBoard));
                                    int points_team_new = Game.calculatePoints(getTilesPerSegment(
                                            isVerticalTeam ? x2 : y2, isVerticalTeam, potentialBoard));
                                    int points_opponent_new = Game.calculatePoints(getTilesPerSegment(
                                            !isVerticalTeam ? x2 : y2, !isVerticalTeam, potentialBoard));

                                    //Add new possible Turn
                                    int points_team_gain = points_team_new - points_team_old;
                                    result.add(new PossibleTurn(newPos, toPlace, handSlot, lastPosition,
                                            toSwap, points_team_new, points_opponent_new, points_team_gain,
                                            true, GameTiles.WC_SWAPONBOARD));
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Gets all possible Turns for the wildcard SWAPWITHHAND. This method is used by
     * getBestTurn_wildcard.
     *
     * @param actualBoard actualBoard
     * @return all possible Turns for Wildcard SWAPWITHHAND
     */
    List<PossibleTurn> allWildcardTurns_SWAPWITHHAND(GameTiles[][] actualBoard) {
        GameTiles[][] potentialBoard;
        List<PossibleTurn> result = new ArrayList<>();

        //There must be at least one standard GameTile in playerHand to swap with
        if (getNumberOfStandardGameTiles() >= 1) {

            //Iterate through whole gameBoard
            for (int x = 0; x < actualBoard.length; x++) {
                for (int y = 0; y < actualBoard[x].length; y++) {
                    Position pos = new Position(x, y);

                    //SWAPWITHHAND can only be played on non-EMPTY GameTile on gameBoard
                    if (!board.isFreeAt(pos)) {
                        GameTiles toSwap = board.getGameTileAt(pos);

                        //Iterate through whole hand
                        for (int i = 0; i < this.getHand().size(); i++) {

                            //Can only swap with standard GameTile in playerHand
                            if (GameTiles.isStandardGameTile(this.getHand().get(i))) {
                                potentialBoard = board.getGameBoard();

                                GameTiles toPlace = this.getHand().get(i);
                                potentialBoard[pos.column()][pos.row()] = toPlace;
                                //Irrelevant what happens, when placer gets GameTile in its own hand

                                int points_team_old = Game.calculatePoints(getTilesPerSegment(
                                        isVerticalTeam ? x : y, isVerticalTeam, actualBoard));
                                int points_team_new = Game.calculatePoints(getTilesPerSegment(
                                        isVerticalTeam ? x : y, isVerticalTeam, potentialBoard));
                                int points_opponent_new = Game.calculatePoints(getTilesPerSegment(
                                        !isVerticalTeam ? x : y, !isVerticalTeam, potentialBoard));

                                //Add new possible Turn
                                int points_team_gain = points_team_new - points_team_old;
                                result.add(new PossibleTurn(pos, toPlace, i, null, toSwap,
                                        points_team_new, points_opponent_new, points_team_gain,
                                        true, GameTiles.WC_SWAPWITHHAND));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }


    /**
     * Reduce given List of possibleTurns to (ideally) one single optimal turn.
     * Returns List, just in case, that the reduction let multiple Turns pass through.
     * <p>
     * 1. Prefer Standard GameTiles over Wildcards
     * 2. Prefer most occurring GameTiles in playerHand
     * 3. Prefer least occurring GameTiles on gameBoard
     * 4. Prefer GameTile with the lowest ordinal value
     * 5. Prefer the lowest possible Turn
     *
     * @param possibleTurns List of possible Turns
     * @return List of reduced possible Turns
     */
    List<PossibleTurn> reduceToFinalTurns(List<PossibleTurn> possibleTurns) {

        //Prefer Standard GameTiles over Wildcards if possible
        ArrayList<PossibleTurn> turns_OnlyStandardGameTiles = new ArrayList<>(possibleTurns);
        if (possibleTurns.size() > 1) {
            List<PossibleTurn> wildcardTurns = possibleTurns.stream().filter(turn -> turn.wildcard() != null).toList();
            List<PossibleTurn> basicTurns = possibleTurns.stream().filter(turn -> turn.wildcard() == null).toList();

            //If there are Basic turns, just take them, if there are none then take all wildcard Turns
            turns_OnlyStandardGameTiles = new ArrayList<>(basicTurns.size() > 0 ? basicTurns : wildcardTurns);
        }


        //Get Turns with GameTile, the player has the most of in its hand
        ArrayList<PossibleTurn> turns_mostOccurring_hand = new ArrayList<>(turns_OnlyStandardGameTiles);
        if (turns_OnlyStandardGameTiles.size() > 1) {
            turns_mostOccurring_hand = new ArrayList<>();
            Set<GameTiles> relevantGameTiles = turns_OnlyStandardGameTiles.stream()
                    .map(turn -> turn.wildcard() == null ? turn.toPlace() : turn.wildcard())
                    .collect(Collectors.toSet());

            Set<GameTiles> mostOccurringTileInHand = this.getMostOccurringGameTiles(relevantGameTiles);

            //Add reduced leftover Turns, if they contain most occurring GameTile in hand
            for (PossibleTurn turn : turns_OnlyStandardGameTiles) {
                if (mostOccurringTileInHand.contains(turn.wildcard() == null ? turn.toPlace() : turn.wildcard())) {
                    turns_mostOccurring_hand.add(turn);
                }
            }
        }

        //Get Turns with GameTile, which is the least occurring on board
        ArrayList<PossibleTurn> turns_leastOccurring_board = new ArrayList<>(turns_mostOccurring_hand);
        if (turns_mostOccurring_hand.size() > 1) {
            turns_leastOccurring_board = new ArrayList<>();
            List<GameTiles> relevantGameTiles = turns_mostOccurring_hand.stream()
                    .map(turn -> turn.wildcard() == null ? turn.toPlace() : turn.wildcard()).toList();

            Set<GameTiles> tiles = getLeastOccurringGameTiles_board(board, relevantGameTiles);

            //Add reduced leftover Turns, if they contain least occurring GameTiles on gameBoard
            for (PossibleTurn turn : turns_mostOccurring_hand) {
                if (tiles.contains(turn.wildcard() == null ? turn.toPlace() : turn.wildcard())) {
                    turns_leastOccurring_board.add(turn);
                }
            }
        }

        //Get Turns with GameTile, which has the smallest ordinal value
        ArrayList<PossibleTurn> turns_smallestOrdinal = new ArrayList<>(turns_leastOccurring_board);
        if (turns_leastOccurring_board.size() > 1) {
            turns_smallestOrdinal = new ArrayList<>();
            List<GameTiles> relevantGameTiles = turns_leastOccurring_board.stream()
                    .map(turn -> turn.wildcard() == null ? turn.toPlace() : turn.wildcard()).toList();

            GameTiles lowestGameTile = getMinOrdinalGameTiles(relevantGameTiles);

            //Add reduced leftover Turns, if they contain the lowest ordinal value GameTile
            for (PossibleTurn turn : turns_leastOccurring_board) {
                if (turn.wildcard() == null
                        ? turn.toPlace().equals(lowestGameTile)
                        : turn.wildcard().equals(lowestGameTile)) {
                    turns_smallestOrdinal.add(turn);
                }
            }
        }

        //Get Turns with the lowest Position
        ArrayList<PossibleTurn> turns_lowestPosition = new ArrayList<>(turns_smallestOrdinal);
        if (turns_smallestOrdinal.size() > 1) {
            turns_lowestPosition = new ArrayList<>();
            Position lowestPosition = Collections.min(turns_smallestOrdinal.stream().map(PossibleTurn::pos).toList());

            //Add reduced leftover Turns, if they contain the lowest position
            for (PossibleTurn turn : turns_smallestOrdinal) {
                if (turn.pos().equals(lowestPosition)) {
                    turns_lowestPosition.add(turn);
                }
            }
        }

        return turns_lowestPosition;
    }

    /**
     * Calculates most occurring GameTile on playerHand. This method is
     * specially used by AI_Player. Only considers specified relevant
     * GameTiles. There can be multiple "most occurring" GameTiles in
     * playerHand.
     * <p>
     * E.g. (1, 1, 1, 3) -> 1 | (1, 1, 3, 3) -> 1, 3 | (1, 2, 3, 4) -> 1, 2, 3, 4
     *
     * @param relevantGameTiles GameTiles to be considered
     * @return Set of most occurring GameTiles
     */
    Set<GameTiles> getMostOccurringGameTiles(Set<GameTiles> relevantGameTiles) {

        //Initializes Map occurrences of relevant GameTiles
        Map<GameTiles, Integer> occurrences = new HashMap<>();
        for (GameTiles tile : relevantGameTiles) {
            occurrences.put(tile, 0);
        }

        //Get all occurrences of relevant GameTiles in playerHand
        for (GameTiles tile : this.getHand()) {
            if (occurrences.containsKey(tile)) {
                occurrences.replace(tile, occurrences.get(tile) + 1);
            }
        }

        //Get the highest occurrence
        OptionalInt optional = occurrences.values().stream().mapToInt(o -> o).max();
        int highestOccurrence;

        Set<GameTiles> result = new HashSet<>();
        if (optional.isPresent()) {
            highestOccurrence = optional.getAsInt();

            //Add all GameTiles, whose occurrence is equal to the highestOccurrence
            for (GameTiles tile : relevantGameTiles) {
                if (occurrences.get(tile) == highestOccurrence) {
                    result.add(tile);
                }
            }
        }

        return result;
    }

    /**
     * Gets least occurring gameTiles on gameBoard. Only for given List
     * of relevant gameTiles.
     *
     * @param board         gameBoard to count occurrences on
     * @param relevantTiles List of GameTiles to look for on gameBoard
     * @return Set of least occurring GameTiles on gameBoard
     */
    Set<GameTiles> getLeastOccurringGameTiles_board(GameBoard board, List<GameTiles> relevantTiles) {
        //Store all occurrences of each GameTile in playerHand
        List<Integer> occurrences = new ArrayList<>();
        for (GameTiles tile : relevantTiles) {
            occurrences.add(board.getGameTileOccurrences(tile));
        }

        //Get least occurring amount
        OptionalInt optional = occurrences.stream().mapToInt(o -> o).min();

        Set<GameTiles> result = new HashSet<>();
        if (optional.isPresent()) {
            int minOccurrence = optional.getAsInt();

            //Add all GameTiles with occurrence, that is equal to the min occurrence amount
            for (int i = 0; i < occurrences.size(); i++) {
                Integer occurrence = occurrences.get(i);
                if (occurrence == minOccurrence) {
                    result.add(relevantTiles.get(i));
                }
            }
        }

        return result;
    }

    /**
     * Gets GameTile with the single lowest ordinal value out of given List
     * of relevant gameTiles.
     *
     * @param relevantTiles List of relevant gameTiles
     * @return GameTile with the lowest ordinal value
     */
    GameTiles getMinOrdinalGameTiles(List<GameTiles> relevantTiles) {
        //Get all ordinal Values of each GameTile in playerHand
        List<Integer> ordinals = this.getHand().stream()
                .filter(relevantTiles::contains).mapToInt(Enum::ordinal).boxed().toList();

        //Get minimum ordinal Value (only from relevant tiles)
        OptionalInt optional = ordinals.stream().mapToInt(o -> o).min();

        GameTiles result = null;
        if (optional.isPresent()) {
            for (int i = 0; i < ordinals.size() && result == null; i++) {
                int minOrdinal = optional.getAsInt();

                //Add all GameTiles, whose ordinal values equal the minimum ordinal value
                if (ordinals.get(i).equals(minOrdinal)) {
                    result = relevantTiles.get(i);
                }
            }
        }

        return result;
    }

    //Helper Methods::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Resets secondPhaseTurn to null. Used for checking, if next AI Turn
     * is possible. In case of wildcard Turn, secondPhaseTurn shouldn't be
     * stored.
     */
    void resetSecondPhaseTurn() {
        secondPhaseTurn = null;
    }

    /**
     * Calculates GameTile Count per Type for a given segment (Row / Column)
     *
     * @param segment        segment of Row / Column
     * @param isVerticalTeam is Row or Column
     * @return GameTile Count per Type
     */
    private int[] getTilesPerSegment(int segment, boolean isVerticalTeam, GameTiles[][] board) {
        int[] gameTileTypes = new int[GameTiles.STANDARD_GAMETILES_TYPES];

        for (int i = 0; i < board.length; i++) {
            switch (board[isVerticalTeam ? segment : i][isVerticalTeam ? i : segment]) {
                case T_SUN -> gameTileTypes[0]++;
                case T_CROSS -> gameTileTypes[1]++;
                case T_TRIANGLE -> gameTileTypes[2]++;
                case T_SQUARE -> gameTileTypes[3]++;
                case T_PENTAGON -> gameTileTypes[4]++;
                case T_STAR -> gameTileTypes[5]++;
            }
        }

        return gameTileTypes;
    }
}
