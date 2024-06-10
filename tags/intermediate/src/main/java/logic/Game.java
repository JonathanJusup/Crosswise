package logic;

/**
 * Game Crosswise holds (most) of the logic of the game. Can be played and
 * accessed without UserInterface.
 *
 * Crosswise contains a 6 x 6 (standard) field, which can be accessed and
 * modified by the current player. If the player makes a turn and places his
 * GameTile / Wildcard the GameBoard updates to the players turn.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class Game {
    public GameBoard board;
    public Player[] players;
    public Logic logic;
    public TileBag tileBag;
    public GUIConnector gui;

    public int currentPlayer;


    /**
     * Test Constructor with preset empty tiles, 4 active Players
     */
    public Game(GUIConnector gui) {
        this.board = new GameBoard();
        this.players = new Player[4];
        this.tileBag = TileBag.getInstance();
        this.currentPlayer = 0;
        this.gui = gui;
    }

    /**
     * Game Constructor for loading existing games
     */
    public Game(int[][] board, Player[] players, TileBag tileBag, int currentPlayer, GUIConnector gui) {
        this.board = new GameBoard(board);
        this.players = players;
        this.tileBag = tileBag;
        this.currentPlayer = currentPlayer;
        this.gui = gui;
    }

    public boolean proposeTurn(GameTiles tile, Position pos) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public boolean proposeTurn(GameTiles tile, Position pos1, Position pos2) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public void playerTurn(GameTiles tile, Position pos) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public void playerTurn(GameTiles tile, Position pos1, Position pos2) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Gets previous player
     *
     * @return index if previous player
     */
    private int getPrevPlayer() {
        return (currentPlayer % 2) == 1 ? 0 : 1;
    }

    private boolean placeGameTile(Player player, GameTiles tile, Position pos) {
        assert tile.ordinal() >= GameTiles.T_SUN.ordinal() && tile.ordinal() <= GameTiles.T_STAR.ordinal();

        if (board.isFree(pos)) {
            board.gameBoard[pos.getRow()][pos.getColumn()] = tile;
        } else {
            return false;
        }
        return true;
    }

    private boolean useWildcard(Player player, GameTiles wildcard, Position pos1, Position pos2) {
        assert wildcard.ordinal() >= GameTiles.WC_MOVER.ordinal() &&
               wildcard.ordinal()<= GameTiles.WC_SWAPWITHHAND.ordinal();

        boolean validTurn;
        /*
        switch (wildcard) {
            case WC_MOVER -> {
                validTurn = !board.isFree(pos1) && board.isFree(pos2);
                if (validTurn) {
                    GameTiles toMove = board.gameBoard[pos1.getRow()][pos1.getColumn()];
                    board.gameBoard[pos1.getRow()][pos2.getColumn()] = GameTiles.EMPTY;
                    board.gameBoard[pos2.getRow()][pos2.getColumn()] = toMove;
                    player.pollGameTile(wildcard);
                    player.addGameTile(tileBag.getTile());
                }
            }
            case WC_REMOVER -> {
                validTurn = !board.isFree(pos1) && pos2 == null;
                if (validTurn) {
                    board.gameBoard[pos1.getRow()][pos1.getColumn()] = GameTiles.EMPTY;
                    player.pollGameTile(wildcard);
                    player.addGameTile(tileBag.getTile());
                }
            }
            case WC_SWAPONBOARD -> {
                throw new UnsupportedOperationException("Not implemented yet!");

                validTurn = !board.isFree(pos1) && !board.isFree(pos2);
                if (validTurn) {
                    GameTiles toSwap = board.gameBoard[pos1.getRow()][pos1.getColumn()];
                    board.gameBoard[pos1.getRow()][pos1.getColumn()] = board.gameBoard[pos2.getRow()][pos2.getColumn()];
                    board.gameBoard[pos2.getRow()][pos2.getColumn()] = toSwap;
                    player.removeGameTile(wildcard);
                    player.addGameTile(wildcard);
                }
                break;

            }

            case WC_SWAPWITHHAND -> {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
        }
        */

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Evaluates Current State of the game. Determines Winner Team based on team
     * points. If one team achieves an early win of sixes, the can end, if there
     * is no win of sixes, it's still an ongoing game.
     *
     * Edgecase when both teams achieve a win of sixes, which should be impossible,
     * except, manipulated board is loaded. Team which got their row/ column of
     * sixes first, wins.
     *
     * @return Evaluated Game State
     */
    public GameEvaluationStates evaluateGame() {
        int[] teamPoints = new int[2];
        GameEvaluationStates evaluationState = null;

        teamPoints[0] = Logic.getTeamPoints(board.gameBoard, true);
        teamPoints[1] = Logic.getTeamPoints(board.gameBoard, false);

        //Early instant Win while ongoing game
        if (teamPoints[0] == Integer.MAX_VALUE || teamPoints[1] == Integer.MAX_VALUE) {
            if (teamPoints[0] > teamPoints[1]) {
                evaluationState = GameEvaluationStates.TEAM_VERTICAL;
            } else if (teamPoints[0] < teamPoints[1]) {
                evaluationState = GameEvaluationStates.TEAM_HORIZONTAL;
            } else {
                //This shouldn't be possible -> Edge case when loading
                //First Team which scored 6x same GameTiles wins
                evaluationState = getPrevPlayer() == 0 ?
                        GameEvaluationStates.TEAM_VERTICAL : GameEvaluationStates.TEAM_HORIZONTAL;
            }
        }

        //Check for ongoing game
        if (evaluationState == null) {
            if (!board.isFull()) {
                evaluationState = GameEvaluationStates.ONGOING_GAME;
            } else {
                if (teamPoints[0] > teamPoints[1]) {
                    evaluationState = GameEvaluationStates.TEAM_VERTICAL;
                } else if (teamPoints[0] < teamPoints[1]) {
                    evaluationState = GameEvaluationStates.TEAM_HORIZONTAL;
                } else {
                    evaluationState = GameEvaluationStates.DRAW;
                }
            }
        }

        return evaluationState;
    }
}
