package logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gui.UserInterfaceController.HAND_SIZE;
import static gui.UserInterfaceController.MAX_PLAYER_COUNT;
import static logic.ErrorType.LOGIC_GAME_BREAK;

/**
 * Game Crosswise holds (most) of the logic for the game. Can be used and
 * accessed without UserInterface.
 * <p>
 * Crosswise contains a 6 x 6 (standard) field, which can be accessed and
 * modified by the current player. If the player makes a turn and places his
 * GameTile / Wildcard the GameBoard updates to the players turn.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class Game {

    /**
     * gameBoard Instance
     */
    private final GameBoard board;
    /**
     * player Instance Array
     */
    private final Player[] players;
    /**
     * tileBag Instance
     */
    private final TileBag tileBag;
    /**
     * gui Instance for communicating with the GUI
     */
    private final GUIConnector gui;

    /**
     * Number of active players
     */
    private final int activePlayers;
    /**
     * currentPlayer Index. Must be Integer in order to
     * be null, if invalid SaveFile is loaded and does
     * not contain currentPlayer.
     */
    private int currentPlayer;

    /**
     * usedWildcards int-Array
     */
    private int[] usedWildcards = new int[GameTiles.WILDCARDS_TYPES];
    /**
     * Flag for blocking any further gameplay, if game has already ended
     */
    private boolean gameEnd = false;


    /**
     * Game Test constructor. Uses predefined gameBoard
     * and FakeGUI. Use only for testing purposes.
     *
     * @param board   predefined gameBoard
     * @param players Predefined players
     * @param gui     FakeGUI
     */
    Game(int[][] board, Player[] players, GUIConnector gui) {
        this.board = new GameBoard(board);
        this.players = players;
        this.tileBag = new TileBag();
        this.gui = gui;
        this.currentPlayer = 0;

        //Arbitrarily initialize Players, if not initialized by test
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player("test_" + i, true, false, new int[4]);
            }
        }

        this.activePlayers = getActivePlayers();
    }

    /**
     * Game Test constructor. Uses predefined gameBoard, currentPlayer, TileBag
     * and FakeGUI. Use only for testing purposes.
     *
     * @param board         Predefined gameBoard
     * @param players       Predefined players
     * @param currentPlayer currentPlayer
     * @param tileBag       Predefined TileBag
     * @param gui           FakeGUI
     */
    Game(int[][] board, Player[] players, int currentPlayer, TileBag tileBag, GUIConnector gui) {
        this.board = new GameBoard(board);
        this.players = players;
        this.tileBag = tileBag;
        this.gui = gui;
        this.currentPlayer = currentPlayer;

        //Arbitrarily initialize Players, if not initialized by test
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player("test_" + i, true, false, new int[4]);
            }
        }

        this.activePlayers = getActivePlayers();

        initPlayerHands();
    }

    /**
     * Game Constructor for new game. Normally Called by UserInterfaceController.
     */
    public Game(int size, Player[] players, GUIConnector gui) {
        this.board = new GameBoard(size);
        this.players = players;
        this.usedWildcards = new int[4];
        this.currentPlayer = 0;
        this.gui = gui;
        this.activePlayers = getActivePlayers();
        this.tileBag = new TileBag();


        //Update Announcement Labels & GameBoard
        gui.setCurrentPlayer(players[currentPlayer].getName());
        showBoardGameTiles();

        //Initialize PlayerHands, show and enable current playerHand
        initPlayerHands();
        gui.hideAllPlayerHands();

        gui.showPlayerHand(currentPlayer, players[currentPlayer].getHand(), players[currentPlayer].isAI());
        gui.enableAndDisablePlayerHands(currentPlayer, players[currentPlayer].isAI());

        //Update usedWildcards- & point-Labels
        gui.updateUsedWildcards(usedWildcards);
        updateAllPointLabels();

        //Initial Game Logging
        removeOldTxtLog();
        Utilities.logEntry(getInitGameLog(), ErrorType.NONE);

        //If first player only contains Wildcards
        if (players[currentPlayer].getHand().stream().allMatch(GameTiles::isWildcard)) {
            String message = "Game Over - First player only contains Wildcards! \nPlease start a new game";
            gui.setAnnouncementLabel("Game Over");
            gui.createAlert(ErrorType.ONLY_WILDCARDS_ALLOCATED, message);
        } else {

            //If first player is AI, force AI Turn
            if (players[currentPlayer].isAI()) {
                forceTurnAI();
            }
        }
    }

    /**
     * Game Constructor for loading existing game. Based on given gameData
     * a new Game Instance is created. If gameData is invalid, throws
     * Exception. This method is normally called by UserInterfaceController.
     * <p>
     * This game Instance can be a freshly initialized game, a
     * running one and even an already ended game.
     *
     * @param gameData gameData
     * @param gui      JavaFXGUI
     * @throws Exception Exception, if gameData invalid
     */
    public Game(GameData gameData, GUIConnector gui) throws Exception {
        Player[] players = new Player[gameData.getPlayers().length];

        //Try to instantiate Game Instance, based on gameData
        try {
            //Create new Player Instance from PlayerData
            PlayerData[] playerData = gameData.getPlayers();
            for (int i = 0; i < players.length; i++) {
                if (playerData[i].isAI()) {
                    //AI Player
                    players[i] = new AI_Player(
                            playerData[i].getName(),
                            playerData[i].isActive(),
                            playerData[i].isAI(),
                            playerData[i].getHand(),
                            Utilities.isVerticalTeam(i));

                } else {
                    //Human Player
                    players[i] = new Player(
                            playerData[i].getName(),
                            playerData[i].isActive(),
                            playerData[i].isAI(),
                            playerData[i].getHand());
                }
            }

            this.players = players;
            this.board = new GameBoard(gameData.getField());
            this.currentPlayer = gameData.getCurrPlayer();
            this.usedWildcards = gameData.getUsedActionTiles();

            TileBag potentialTileBag;
            try {
                potentialTileBag = new TileBag(gameData.getTileBag(), false);
            } catch (Exception e) {
                potentialTileBag = new TileBag(Arrays.stream(getRemainingGameTiles())
                        .mapToInt(Integer::intValue).toArray(), false);
            }
            this.tileBag = potentialTileBag;
            this.gui = gui;
            this.activePlayers = getActivePlayers();

        } catch (Exception e) {
            throw new IllegalArgumentException("Couldn't load save File!");
        }

        //Update Announcement Labels & GameBoard
        gui.setCurrentPlayer(players[currentPlayer].getName());
        showBoardGameTiles();

        //Show and enable only current playerHand
        gui.hideAllPlayerHands();
        gui.showPlayerHand(currentPlayer, players[currentPlayer].getHand(), players[currentPlayer].isAI());
        gui.enableAndDisablePlayerHands(currentPlayer, players[currentPlayer].isAI());

        //Update usedWildcards- & point-Labels
        gui.updateUsedWildcards(usedWildcards);
        updateAllPointLabels();

        //Initial Game Logging
        removeOldTxtLog();
        Utilities.logEntry(getInitGameLog(), ErrorType.NONE);

        //If Game is already over, disable current playerHand & show GameEnd Popup
        GameStates gameState = evaluateGame();
        if (gameState != GameStates.ONGOING_GAME) {
            int[] teamPoints = new int[]{getTeamPoints(true), getTeamPoints(false)};
            gui.onGameEnd(getTeamNames(0), getTeamNames(1), teamPoints, gameState);
        }

        //If first player is AI, force AI Turn
        if (players[currentPlayer].isAI()) {
            forceTurnAI();
        }
    }


    /**
     * Gets Player Names of specified team.
     * With 4 active players, each team has 2 players;
     * With 2 active players, each team has only 1 player.
     * <p>
     * Player Indices are assigned the following:
     * player [0,2] / player[0] = Team 1
     * player [1,3] / player[1] = Team 2
     *
     * @param teamIdx Team Index
     * @return String Array of player Names
     */
    String[] getTeamNames(int teamIdx) {
        String[] playerNames;

        playerNames = new String[activePlayers == MAX_PLAYER_COUNT ? 2 : 1];
        playerNames[0] = players[teamIdx == 0 ? 0 : 1].getName();

        //If 4 players, there are 2 Names per Team
        if (activePlayers == MAX_PLAYER_COUNT) {
            playerNames[1] = players[teamIdx == 0 ? 2 : 3].getName();
        }

        return playerNames;
    }

    /**
     * Initialize GameBoard by setting each ImageView to an EMPTY GameTile.
     */
    private void showBoardGameTiles() {
        for (int i = 0; i < board.getGameBoard().length; i++) {
            for (int j = 0; j < board.getGameBoard().length; j++) {
                gui.placeTileOnBoard(board.getGameBoard()[i][j], new Position(i, j), false);
            }
        }
    }

    /**
     * Initializes Player Hands with random GameTiles, polled
     * from TileBag Instance.
     */
    private void initPlayerHands() {
        for (int i = 0; i < activePlayers; i++) {
            for (int j = 0; j < HAND_SIZE; j++) {
                GameTiles tileToAdd = tileBag.getTile();
                players[i].addGameTileAt((tileToAdd != null ? tileToAdd : GameTiles.EMPTY), j);
            }
        }
    }

    /**
     * Player Turn by playing GameTiles and/or Wildcards. Both Logic and GUI are
     * getting updated together.
     * <p>
     * There are 2 different player Turns:
     * 1. Basic PlayerTurn
     * -Places specified GameTile on GameBoard
     * -Removes used GameTiles from playerHand
     * -Update all PointLabels
     * -Prepare for next playerTurn
     * <p>
     * 2. Wildcard PlayerTurn
     * -2 Different Wildcard Turn Types (1 Phase | 2 Phase):
     * -REMOVE (1 Phase) | MOVE, SWAPONBOARD, SWAPWITHHAND (2 Phases)
     * -Second Phase of 2-Phase-Wildcards will be executed here
     *
     * @param tile         GameTile to place on GameBoard
     * @param pos          Position on GameBoard
     * @param handSlot     handSlot, from where GameTile ist played
     * @param tileToSwap   GameTile to swap with
     * @param lastPosition source Position on GameBoard (relevant for 2 Phase Wildcards)
     */
    public void playerTurn(GameTiles tile, Position pos, Integer handSlot, GameTiles tileToSwap, Position lastPosition) {
        boolean swapWithHand = false;

        //Reset Board Drag Handler instantly after GameTile is played
        gui.resetBoardDragHandler(this, false);

        //Differentiate, if Wildcard is played or Standard GameTile
        if (!GameTiles.isWildcard(tile) && !gameEnd) {

            //WILDCARD: MOVE / SWAPONBOARD/ SWAPWITHHAND (2. Phase)
            if (pos != null && lastPosition != null) {
                if (tileToSwap != null && tileToSwap != GameTiles.EMPTY) {
                    //SWAPONBOARD (2/2 Phase)

                    //Animate, if Turn was made by AI
                    if (players[currentPlayer].isAI()) {
                        gui.createAndPlayAnimation(tileToSwap, currentPlayer, null,
                                pos, lastPosition, this, false);
                    }

                    //Place Target GameTile to Source Position
                    board.placeTileOnBoard(tileToSwap, lastPosition);
                    gui.placeTileOnBoard(tileToSwap, lastPosition, players[currentPlayer].isAI());

                    //Logging
                    Utilities.logEntry(String.format("%s | [%s] swapped %s with %s | new Hand : %s",
                            players[currentPlayer].getName(), tile.ordinal(), lastPosition, pos,
                            players[currentPlayer].getHand().stream().map(Enum::ordinal).collect(Collectors.toList())
                    ), ErrorType.NONE);
                } else {
                    //MOVE (2/2 Phase)

                    //Place EMPTY GameTile to Source Position
                    board.placeTileOnBoard(GameTiles.EMPTY, lastPosition);
                    gui.placeTileOnBoard(GameTiles.EMPTY, lastPosition, players[currentPlayer].isAI());

                    //Logging
                    Utilities.logEntry(String.format("%s | [%s] moved from %s to %s | new Hand : %s",
                            players[currentPlayer].getName(), tile.ordinal(), lastPosition, pos,
                            players[currentPlayer].getHand().stream().map(Enum::ordinal).collect(Collectors.toList())
                    ), ErrorType.NONE);
                }
            } else if (pos != null && !board.isFreeAt(pos)) {
                //SWAPWITHHAND (2/2 Phase)

                swapWithHand = true;
                GameTiles toSwapWith = board.getGameTileAt(pos);

                //Animate, if Turn was made by AI
                if (players[currentPlayer].isAI()) {
                    gui.createAndPlayAnimation(toSwapWith, currentPlayer, handSlot,
                            pos, null, this, false);
                }

                //Remove SWAPWITHHAND wildcard from playerHand
                players[currentPlayer].removeGameTileAt(handSlot);
                gui.removeTileFromHandAt(currentPlayer, handSlot);

                //Add stored GameTile from gameBoard to playerHand
                players[currentPlayer].addGameTileAt(toSwapWith, handSlot);
                gui.addTileToHandAt(currentPlayer, toSwapWith, handSlot, players[currentPlayer].isAI());

                //Logging
                Utilities.logEntry(String.format("%s | [%s] swapped from hand at [%s] to %s | new Hand : %s",
                        players[currentPlayer].getName(), tile.ordinal(), handSlot, pos,
                        players[currentPlayer].getHand().stream().map(Enum::ordinal).collect(Collectors.toList())
                ), ErrorType.NONE);
            }

            //Place Source GameTile to Target Position (after BASIC TURN, REMOVE, MOVE or SWAPONBOARD)
            if (pos != null) {
                if (players[currentPlayer].isAI()) {
                    //Animate, if Turn was made by AI

                    if (lastPosition != null) {
                        gui.createAndPlayAnimation(tile, currentPlayer, null,
                                lastPosition, pos, this, true);
                    } else {
                        gui.createAndPlayAnimation(tile, currentPlayer, handSlot,
                                null, pos, this, true);
                    }
                }

                //Place Standard GameTile on gameBoard and show it
                board.placeTileOnBoard(tile, pos);
                gui.placeTileOnBoard(tile, pos, players[currentPlayer].isAI());
            }

            //Updates Player Hand after BASIC TURN
            if (pos != null && handSlot != null && !swapWithHand) {
                updatePlayerHand(handSlot);

                //Logging
                Utilities.logEntry(String.format("%s | [%s] placed on %s | handSlot [%d] | new Hand : %s",
                        players[currentPlayer].getName(), tile.ordinal(), pos, handSlot,
                        players[currentPlayer].getHand().stream().map(Enum::ordinal).collect(Collectors.toList())
                ), ErrorType.NONE);
            }


            updateAllPointLabels();
            prepareNextTurn();

        } else if (!gameEnd) {

            //Player Turn, when Wildcard is played
            useWildcard(tile, pos, handSlot);
        }
    }

    /**
     * 1. Phase of using a Wildcard as part of a playerTurn.
     * There are 2 different Wildcard Types:
     * <p>
     * ----------1 Phase Wildcards (REMOVE)----------
     * <p>
     * -REMOVE: Player drags Wildcard on top of a non-EMPTY GameTile
     * to remove it from the GameBoard
     * -PlayerTurn completed
     * <p>
     * ----------2 Phase Wildcards (MOVE, SWAPONBOARD, SWAPWITHHAND)----------
     * <p>
     * -MOVE: Player drags Wildcard anywhere on board to "activate" the wildcard (1. Phase)
     * -Tells player via Announcement Label, what to do next
     * -Every GameBoard ImageView Cell are getting specialized Drag Handlers
     * -Ready for 2. Phase | Player Turn is not done
     * <p>
     * -SWAPONBOARD: Player drags Wildcard anywhere on board to "activate" the wildcard (1. Phase)
     * -Tells player via Announcement Label, what to do next
     * -Every GameBoard ImageView Cell are getting specialized Drag Handlers
     * -Ready for 2. Phase | Player Turn is not done
     * <p>
     * -SWAPWITHHAND
     * -Tells player voa Announcement Label, what to do next
     * -Every GameBoard ImageView Cell are getting specialized Drag Handlers
     * -Ready for 2. Phase | Player Turn is not done
     *
     * @param tile     wildcard used
     * @param pos      position, where GameTile is placed
     * @param handSlot handSlot, from where GameTile is played
     */
    private void useWildcard(GameTiles tile, Position pos, Integer handSlot) {
        boolean isEmpty = board.isEmpty();

        //There must be at least one GameTile on gameBoard in order to play wildcard
        if (!isEmpty) {

            switch (tile) {
                case WC_MOVER -> {
                    //2 Phase Turn (1/2 Phase)

                    //Animate AI Turn (1. Phase)
                    if (players[currentPlayer].isAI()) {
                        gui.createAndPlayAnimation(GameTiles.WC_MOVER, currentPlayer, handSlot,
                                null, pos, this, true);
                    }

                    //Tell player what to do next via Announcement Label
                    gui.setAnnouncementLabel("Move a GameTile on GameBoard");
                    gui.setBoardDragHandler(this, false);

                    //Update Used Wildcards Labels
                    incrementWildcardUsed(tile);
                    gui.updateUsedWildcards(usedWildcards);

                    //PlayerHand must be disabled for Phase 2
                    gui.disablePlayerHand(currentPlayer);

                    //When MOVER Wildcard is played (activated), replace it with new Tile
                    //This means player gets new GameTile before he moves a Tile on Board!
                    updatePlayerHand(handSlot);

                }
                case WC_REMOVER -> {
                    //1 Phase Turn

                    GameTiles toSwapWith = board.getGameTileAt(pos);

                    //Animate, if Turn was made by AI
                    if (players[currentPlayer].isAI()) {
                        gui.createAndPlayAnimation(GameTiles.WC_REMOVER, currentPlayer, handSlot,
                                null, pos, this, true);
                    }

                    //Place EMPTY GameTile on GameBoard
                    board.placeTileOnBoard(GameTiles.EMPTY, pos);
                    gui.placeTileOnBoard(GameTiles.EMPTY, pos, players[currentPlayer].isAI());

                    //Remove GameTile from hand (Logic & GUI)
                    players[currentPlayer].removeGameTileAt(handSlot);
                    gui.removeTileFromHandAt(currentPlayer, handSlot);

                    //Add removed GameTile to playerHand (Logic & GUI)
                    players[currentPlayer].addGameTileAt(toSwapWith, handSlot);
                    gui.addTileToHandAt(currentPlayer, toSwapWith, handSlot, players[currentPlayer].isAI());


                    //Update all PointLabels + prepare for next Turn
                    incrementWildcardUsed(tile);
                    gui.updateUsedWildcards(usedWildcards);
                    updateAllPointLabels();

                    //Logging
                    Utilities.logEntry(String.format("%s | [%s] removed at %s | handSlot [%d] | new Hand : %s",
                            players[currentPlayer].getName(), tile.ordinal(), pos, handSlot,
                            players[currentPlayer].getHand().stream().map(Enum::ordinal).collect(Collectors.toList())
                    ), ErrorType.NONE);

                    prepareNextTurn();
                }
                case WC_SWAPONBOARD -> {
                    //2 Phase Turn (1/2 Phase)

                    //There must at least be 2 GameTiles on Board in order to play wildcard
                    if (board.usedSpaces() >= 2) {

                        //Animate AI Turn (1. Phase)
                        if (players[currentPlayer].isAI()) {
                            gui.createAndPlayAnimation(GameTiles.WC_SWAPONBOARD, currentPlayer, handSlot,
                                    null, pos, this, true);
                        }

                        //Tell player what to do next via Announcement Label
                        gui.setAnnouncementLabel("Move a GameTile to another to swap them");
                        gui.setBoardDragHandler(this, true);

                        //Update Used Wildcards Labels
                        incrementWildcardUsed(tile);
                        gui.updateUsedWildcards(usedWildcards);

                        //PlayerHand must be disabled for Phase 2
                        gui.disablePlayerHand(currentPlayer);

                        //When MOVER Wildcard is played (activated), replace it with new Tile
                        //This means player gets new GameTile before he moves a Tile on Board!
                        updatePlayerHand(handSlot);

                    } else {
                        gui.setAnnouncementLabel("Cannot play SwapOnBoard - Less than 2 GameTiles on GameBoard!");
                    }
                }
                case WC_SWAPWITHHAND -> {
                    //2 Phase Turn (1/2 Phase)

                    //There mus be at least one standardGameTile in player Hand
                    //GameTile from TileBag which will be polled after playing wildcard is also possible
                    if (players[currentPlayer].getNumberOfStandardGameTiles() >= 1
                            || GameTiles.isStandardGameTile(tileBag.getTileBag().peek())) {

                        //Animate AI Turn (1. Phase)
                        if (players[currentPlayer].isAI()) {
                            //handSlot only contains Index of GameTile to swap with, but not the played wildcard
                            //Dev Note: This may not be optimal
                            int wildcardPlayedFrom = players[currentPlayer].indexOfGameTile(GameTiles.WC_SWAPWITHHAND);

                            //GameBreaking Error: Played Wildcard doesn't exist in hand
                            if (wildcardPlayedFrom == -1) {
                                String message = "Played Wildcard SWAPWITHHAND not in playerHand!";
                                Utilities.logEntry(message, LOGIC_GAME_BREAK);
                                throw new NoSuchElementException(message);
                            }

                            //Animate AI Turn (1. Phase)
                            gui.createAndPlayAnimation(GameTiles.WC_SWAPWITHHAND, currentPlayer, wildcardPlayedFrom,
                                    null, pos, this, true);

                            //Replace Wildcard with new Tile
                            updatePlayerHand(wildcardPlayedFrom);
                        }


                        gui.setAnnouncementLabel("Move GameTile from Hand to GameTile on GameBoard to swap");
                        gui.setBoardDragHandler(this, true);

                        //Update Used Wildcards Labels
                        incrementWildcardUsed(tile);
                        gui.updateUsedWildcards(usedWildcards);

                        gui.resetBoardDragHandler(this, true);


                        //When SWAPWITHHAND Wildcard is played (activated), replace it with new Tile
                        //This means player gets new GameTile before he moves a Tile on Board!
                        updatePlayerHand(handSlot);

                    } else {
                        String message = "Cannot play SwapWithHand - At least 1 standard GameTile in PlayerHand required!";
                        gui.setAnnouncementLabel(message);
                        Utilities.logEntry(message, ErrorType.UNABLE_TO_PLAY_WILDCARD);
                    }
                }
            }
        } else {
            String message = "Cannot play wildcard - Empty GameBoard!";
            gui.setAnnouncementLabel(message);
            Utilities.logEntry(message, ErrorType.UNABLE_TO_PLAY_WILDCARD);
        }
    }

    /**
     * Prepare next playerTurn by setting next player, hiding and disabling all
     * playerHands except currentPlayer playerHand. Evaluates game after
     * playerTurn. If result == ONGOING_GAME, game continues, else game ends.
     */
    private void prepareNextTurn() {
        //Logging
        Utilities.logEntry(board.toString(), ErrorType.NONE);

        //Evaluate Game if conditions are right
        GameStates state = evaluateGame();
        int prevPlayer = currentPlayer;

        switch (state) {
            case ONGOING_GAME -> {

                //Update currentPlayer
                currentPlayer = (currentPlayer + 1) % activePlayers;

                //Updates Label to current Player Name
                gui.setCurrentPlayer(players[currentPlayer].getName());

                //Update all playerHands (hide/show & disable/enable)
                if (players[prevPlayer].isAI()) {
                    //Delay all methods to execute, after AI is done with its AI Turn Animation
                    gui.hideAllPlayerHands_Delay();
                    gui.showPlayerHand_Delay(currentPlayer, players[currentPlayer].getHand(), players[currentPlayer].isAI());
                    gui.enableAndDisablePlayerHands(currentPlayer, players[currentPlayer].isAI());
                } else {
                    gui.hideAllPlayerHands();
                    gui.showPlayerHand(currentPlayer, players[currentPlayer].getHand(), players[currentPlayer].isAI());
                    gui.enableAndDisablePlayerHands(currentPlayer, players[currentPlayer].isAI());
                }

                //Force AI Turn, only if last player was also an AI_Player
                if (players[currentPlayer].isAI() && !players[(currentPlayer - 1 + activePlayers) % activePlayers].isAI()) {
                    forceTurnAI();
                }
            }
            case DRAW, TEAM_VERTICAL, TEAM_HORIZONTAL -> {
                //Trigger game end

                int[] teamPoints = new int[]{getTeamPoints(true), getTeamPoints(false)};

                //Delay onGameEnd, if last turn was made by AI
                if (players[prevPlayer].isAI()) {
                    gui.onGameEnd_Delay(getTeamNames(0), getTeamNames(1), teamPoints, state);
                } else {
                    gui.onGameEnd(getTeamNames(0), getTeamNames(1), teamPoints, state);
                }
            }
        }
    }

    /**
     * Gets number of active players. There must be 2 or 4 active players.
     *
     * @return Number of active players
     */
    public int getActivePlayers() {
        return (int) Stream.of(players).filter(Player::isActive).count();
    }

    /**
     * Evaluates Current State of the game. Determines Winner Team based on team
     * points. If one team achieves an early win of sixes, the game ends, if there
     * is no win of sixes, it's still an ongoing game. Game also ends, if next
     * AI_Player is unable to make a turn.
     *
     * @return Evaluated GameState
     */
    GameStates evaluateGame() {

        GameStates evaluationState = GameStates.ONGOING_GAME;

        //Get both team points to evaluate
        int[] teamPoints = new int[2];
        teamPoints[0] = getTeamPoints(true);
        teamPoints[1] = getTeamPoints(false);


        //Early Win while ongoing game (Win of sixes)
        if (teamPoints[0] == Integer.MAX_VALUE || teamPoints[1] == Integer.MAX_VALUE) {
            //Trigger Game Stop
            forceGameStop();

            //Evaluate
            if (teamPoints[0] > teamPoints[1]) {
                evaluationState = GameStates.TEAM_VERTICAL;
            } else if (teamPoints[0] < teamPoints[1]) {
                evaluationState = GameStates.TEAM_HORIZONTAL;
            } else {
                evaluationState = GameStates.DRAW;
            }

        }

        //Check if future AI Player is able to make a turn, if not, game also ends.
        int futurePlayer = (currentPlayer + 1) % activePlayers;
        PossibleTurn turn;
        if (players[futurePlayer].isAI() && !board.isFull()) {
            turn = ((AI_Player) players[futurePlayer]).evaluateToBestTurn(board);
            ((AI_Player) players[futurePlayer]).resetSecondPhaseTurn();
        } else {
            //Dummy Turn
            turn = new PossibleTurn(null, null, null, null,
                    null, 0, 0, 0, false, null);
        }

        //Winner evaluation, when GameBoard is Full, no early win is achieved and AI is able to make a turn
        if (evaluationState == GameStates.ONGOING_GAME && (board.isFull() || turn == null)) {
            //Trigger Game Stop
            forceGameStop();

            //Evaluate
            if (teamPoints[0] > teamPoints[1]) {
                evaluationState = GameStates.TEAM_VERTICAL;
            } else if (teamPoints[0] < teamPoints[1]) {
                evaluationState = GameStates.TEAM_HORIZONTAL;
            } else {
                evaluationState = GameStates.DRAW;
            }
        }

        if (gameEnd) {
            //Last Log Entry of gameBoard and playerHands
            Utilities.logEntry(getInitGameLog(), ErrorType.NONE);
            Utilities.logEntry(board.toString(), ErrorType.NONE);
        }

        //Log every Evaluation
        Utilities.logEntry("Evaluation State: " + evaluationState, ErrorType.NONE);

        return evaluationState;
    }

    /**
     * Calculates GameTile Count per Type for a given segment (Row / Column)
     *
     * @param segment        segment of Row / Column
     * @param isVerticalTeam is Row or Column
     * @return GameTile Count per Type
     */
    int[] getTilesPerSegment(int segment, boolean isVerticalTeam) {
        int boardSize = board.getGameBoard().length;
        int[] gameTileTypes = new int[GameTiles.STANDARD_GAMETILES_TYPES];

        for (int i = 0; i < boardSize; i++) {
            switch (board.getGameBoard()[isVerticalTeam ? segment : i][isVerticalTeam ? i : segment]) {
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

    /**
     * Calculates points of given GameTiles Array based on Tile combination point rules
     * All different = 6 Points
     * 2x same tiles = 1 Point
     * 3x same tiles = 3 Points
     * 4x same tiles = 5 Points
     * 5x same tiles = 7 Points
     * 6x same tiles = Integer.MAX_VALUE (Instant Win)
     *
     * @param gameTiles GameTiles of given Row / Column
     * @return points
     */
    static int calculatePoints(int[] gameTiles) {
        int points = 0;

        //6 Points for all different Game Tiles
        boolean allDifferent = true;
        for (int i = 0; i < gameTiles.length && allDifferent; i++) {
            allDifferent = gameTiles[i] == 1;
        }

        if (allDifferent) {
            points = 6;
        } else {
            for (int gameTile : gameTiles) {
                switch (gameTile) {
                    case 2 -> points += 1;                  //2x same tiles
                    case 3 -> points += 3;                  //3x same tiles
                    case 4 -> points += 5;                  //4x same tiles
                    case 5 -> points += 7;                  //5x same tiles
                    case 6 -> points = Integer.MAX_VALUE;   //6x same tiles
                }
            }
        }

        return points;
    }

    /**
     * Calculates Team points by considering all Rows / Columns.
     * Handles WinOfSixes points as Integer.MAX_VALUE.
     *
     * @param isVerticalTeam Team which points to calculate
     * @return Team Points | Integer.MAX_VALUE if Instant Win
     */
    int getTeamPoints(boolean isVerticalTeam) {
        int pointSum = 0;

        for (int i = 0; i < board.getGameBoard().length && pointSum != Integer.MAX_VALUE; i++) {
            int segmentPoints = calculatePoints(getTilesPerSegment(i, isVerticalTeam));
            pointSum = segmentPoints != Integer.MAX_VALUE ? pointSum + segmentPoints : segmentPoints;
        }

        return pointSum;
    }

    /**
     * Updates all (optional) Point Labels (Horizontal/Vertical Point Bars & Team Points).
     * Updates Label Text to current Points of each Segment (Column/ Row) and all Columns/ Rows
     * for each Team.
     */
    private void updateAllPointLabels() {
        int[] verticalPoints = new int[board.getGameBoard().length];
        int[] horizontalPoints = new int[board.getGameBoard().length];

        //Update horizontal/ vertical Point Bars
        for (int i = 0; i < board.getGameBoard().length; i++) {
            verticalPoints[i] = calculatePoints(getTilesPerSegment(i, true));
            horizontalPoints[i] = calculatePoints(getTilesPerSegment(i, false));
        }

        //Update Team Points Labels
        gui.updatePointBars(verticalPoints, horizontalPoints);
        gui.updateTeamPoints(
                getTeamPoints(true),
                getTeamPoints(false));
    }

    /**
     * Increases counter of usedWildcards Integer Array based on played Wildcard.
     *
     * @param wildcard Played Wildcard
     */
    private void incrementWildcardUsed(GameTiles wildcard) {
        assert GameTiles.isWildcard(wildcard);

        switch (wildcard) {
            case WC_REMOVER -> usedWildcards[0]++;
            case WC_MOVER -> usedWildcards[1]++;
            case WC_SWAPONBOARD -> usedWildcards[2]++;
            case WC_SWAPWITHHAND -> usedWildcards[3]++;
        }
    }

    /**
     * Updates the current playerHand by removing a GameTile at a specified
     * handSlot, polling a random GameTile from the tileBag and adding it to
     * the playerHand at the same handSlot.
     *
     * @param handSlot handSlot, where to remove / add
     */
    private void updatePlayerHand(int handSlot) {
        //Remove GameTile from hand (Logic & GUI)
        players[currentPlayer].removeGameTileAt(handSlot);
        gui.removeTileFromHandAt(currentPlayer, handSlot);

        //Get and add random GameTile from TileBag to hand (Logic & GUI)
        GameTiles newTile = tileBag.getTile();
        newTile = newTile == null ? GameTiles.EMPTY : newTile;

        players[currentPlayer].addGameTileAt(newTile, handSlot);
        gui.addTileToHandAt(currentPlayer, newTile, handSlot, players[currentPlayer].isAI());
    }

    /**
     * Gets String of initialized Game. Contains TileBag, and players.
     * Each active Player Entry contains its playerIdx, name, isAI and hand.
     * Inactive players are marked as such.
     *
     * @return Initial Log Entry
     */
    private String getInitGameLog() {

        //Calculate Padding for the longest playerName
        int padding = Stream.of(players).map(Player::getName).mapToInt(String::length).max().orElse(0);

        StringBuilder sb = new StringBuilder();
        sb.append("TileBag | ").append(tileBag.toString()).append("\n");

        for (int i = 0; i < players.length; i++) {
            if (players[i].isActive()) {
                //Active Players
                sb.append(String.format("%s | %" + padding + "s | %5s | %s%n",
                        "player" + i,
                        players[i].getName(),
                        players[i].isAI() ? "AI" : "Human",
                        Arrays.toString(players[i].getHand().stream().mapToInt(Enum::ordinal).toArray())));
            } else {
                //Inactive Players
                sb.append(String.format("player %d not active", i));
                sb.append(i < players.length - 1 ? "\n" : "");
            }
        }

        return sb.toString();
    }

    /**
     * Removes old logFile, if existing.
     */
    private void removeOldTxtLog() {
        String logFilePath = "./src/log/Log.txt";
        new File(logFilePath).delete();
    }

    /**
     * Converts current game-Instance Members to their representative Data-Classes.
     * GameBoard gets converted to 2D Array of ordinal-values of each of its GameTiles.
     * Player Instances get their own PlayerData Classes, where their playerHands are
     * converted to an Array of ordinal-values of each of its GameTiles. TileBag also
     * gets converted to an Array of ordinal-Values.
     *
     * @return GameData Structure of game
     */
    GameData toGameData() {

        //Map GameTiles of gameBoard to their ordinal values
        int[][] board_casted = Stream.of(board.getGameBoard())
                .map(firstLayer -> Stream.of(firstLayer)
                        .mapToInt(Enum::ordinal)
                        .toArray())
                .toArray(int[][]::new);

        //Create for each Player-Instance a separate PlayerData-Instance
        PlayerData[] playerData = new PlayerData[players.length];
        for (int i = 0; i < playerData.length; i++) {
            playerData[i] = new PlayerData(
                    players[i].getName(),
                    players[i].isActive(),
                    players[i].isAI(),

                    //Map GameTiles of playerHand to their ordinal values
                    players[i].getHand().stream().mapToInt(Enum::ordinal).toArray()
            );
        }

        //Create TileBag Array of ordinal Values of the corresponding GameTiles
        int[] tileBag_casted = tileBag.getTileBag().stream().mapToInt(Enum::ordinal).toArray();

        return new GameData(playerData, currentPlayer, board_casted, usedWildcards, tileBag_casted);
    }


    /**
     * Helper Method used for reconstructing a TileBag when loading an existing game.
     * Counts all Standard GameTiles and Wildcards, which were already on gameBoard,
     * used by players or in playerHand. Subtracts each result from maximum occurrence.
     *
     * @return Array of remaining GameTiles
     */
    Integer[] getRemainingGameTiles() {
        List<Integer> usedGameTiles = new ArrayList<>();

        //Count all GameTiles on gameBoard
        for (int i = 1; i < GameTiles.STANDARD_GAMETILES_TYPES + 1; i++) {
            GameTiles tile = Utilities.ordinalToGameTiles(i);
            int count = board.getGameTileOccurrences(tile);
            for (int j = 0; j < count; j++) {
                usedGameTiles.add(tile.ordinal());
            }
        }

        //Count all GameTiles and Wildcards of each playerHand
        for (Player player : players) {
            if (player.isActive()) {
                List<GameTiles> playerHand = player.getHand();

                for (GameTiles tile : playerHand) {
                    if (tile.ordinal() > 0) {
                        usedGameTiles.add(tile.ordinal());
                    }
                }
            }
        }

        //Count all previously used Wildcards
        for (int i = 0; i < usedWildcards.length; i++) {
            for (int j = 0; j < usedWildcards[i]; j++) {
                switch (i) {
                    case 0 -> usedGameTiles.add(GameTiles.WC_REMOVER.ordinal());
                    case 1 -> usedGameTiles.add(GameTiles.WC_MOVER.ordinal());
                    case 2 -> usedGameTiles.add(GameTiles.WC_SWAPONBOARD.ordinal());
                    case 3 -> usedGameTiles.add(GameTiles.WC_SWAPWITHHAND.ordinal());
                }
            }
        }

        //Calculate Remaining

        List<Integer> result = new ArrayList<>();
        int gameTilesCount = GameTiles.values().length;

        //Calculate remaining GameTiles
        for (int i = 1; i < GameTiles.STANDARD_GAMETILES_TYPES + 1; i++) {
            int ordinal = i;
            int remaining = GameTiles.STANDARD_GAMETILES_COUNT
                    - (int) usedGameTiles.stream().filter(value -> value.equals(ordinal)).count();

            for (int j = 0; j < remaining; j++) {
                result.add(ordinal);
            }
        }

        //Calculate remaining Wildcards
        for (int i = GameTiles.STANDARD_GAMETILES_TYPES + 1; i < gameTilesCount; i++) {
            int ordinal = i;
            int remaining = GameTiles.WILDCARDS_COUNT
                    - (int) usedGameTiles.stream().filter(value -> value.equals(ordinal)).count();

            for (int j = 0; j < remaining; j++) {
                result.add(ordinal);
            }
        }

        return result.toArray(Integer[]::new);
    }

    /**
     * Forces current AI_Player to make a turn. Must be public
     * for the JavaFXGUI to call it. This breaks the separation
     * of Logic and GUI.
     */
    public void forceTurnAI() {
        if (players[currentPlayer].isAI() && !gameEnd) {
            PossibleTurn turn = ((AI_Player) players[currentPlayer]).evaluateToBestTurn(board);
            this.playerTurn(turn.toPlace(), turn.pos(), turn.handSlot(), turn.toSwap(), turn.lastPosition());
        }

    }

    /**
     * Forces the game to stop. AI cannot order the next AI_Player
     * to make a turn anymore. Called, when game has ended.
     */
    public void forceGameStop() {
        gameEnd = true;
        gui.forceStop();
    }

    /**
     * Forces the game to stop. AI cannot order the next AI_Player
     * to make a turn anymore. Called, when new game is about to start.
     */
    public void stopOldGame() {
        gameEnd = true;
        gui.interrupt();

    }
}
