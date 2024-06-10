package logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static logic.Utilities.transposeBoard;
import static org.junit.Assert.assertEquals;

public class AIPlayerTests {

    @Test
    public void allBasicTurns_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 1},
                {0, 1, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 2, 3, 4};

        AI_Player player = new AI_Player("AI", true, true, hand, true);
        player.setBoard(board);



        List<PossibleTurn> allPossibleTurns = player.getAllTurns_basic(GameTiles.T_SUN, 0);
        assertEquals(7, allPossibleTurns.size());
    }

    //Test all possible Wildcard Turns::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void allWildcardTurns_REMOVER_test() {
        int[][] testBoard = new int[][]{
                {2, 2, 0},
                {0, 2, 0},
                {0, 0, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_REMOVER(0, board.getGameBoard());
        assertEquals(4, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_REMOVER(0, board.getGameBoard());
        assertEquals(4, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_REMOVER_emptyGameBoard_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_REMOVER(0, board.getGameBoard());
        assertEquals(0, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_REMOVER(0, board.getGameBoard());
        assertEquals(0, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_MOVER_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 1, 0},
                {0, 2, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);

        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_MOVER(1, board.getGameBoard());
        assertEquals(18, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_MOVER(1, board.getGameBoard());
        assertEquals(18, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_MOVER_emptyGameBoard_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_MOVER(1, board.getGameBoard());
        assertEquals(0, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_MOVER(1, board.getGameBoard());
        assertEquals(0, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_MOVER_fullGameBoard_test() {
        int[][] testBoard = new int[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_MOVER(1, board.getGameBoard());
        assertEquals(0, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_MOVER(1, board.getGameBoard());
        assertEquals(0, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_SWAPONBOARD_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 1, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_SWAPONBOARD(2, board.getGameBoard());
        assertEquals(2, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_SWAPONBOARD(2, board.getGameBoard());
        assertEquals(2, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_SWAPONBOARD_emptyGameBoard_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_SWAPONBOARD(2, board.getGameBoard());
        assertEquals(0, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_SWAPONBOARD(2, board.getGameBoard());
        assertEquals(0, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_SWAPONBOARD_noOneToSwapWith_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 1}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_SWAPONBOARD(2, board.getGameBoard());
        assertEquals(0, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_SWAPONBOARD(2, board.getGameBoard());
        assertEquals(0, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_SWAPWITHHAND_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 1, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 2, 3, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_SWAPWITHHAND(board.getGameBoard());
        assertEquals(6, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_SWAPWITHHAND(board.getGameBoard());
        assertEquals(6, allPossibleTurnsH.size());
    }

    @Test
    public void allWildcardTurns_SWAPWITHHAND_noStandardGameTileInHand_test() {
        int[][] testBoard = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 1, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 8, 9, 10};

        AI_Player playerV = new AI_Player("AI", true, true, hand, true);
        playerV.setBoard(board);

        AI_Player playerH = new AI_Player("AI", true, true, hand, false);
        playerH.setBoard(board);


        List<PossibleTurn> allPossibleTurnsV = playerV.allWildcardTurns_SWAPWITHHAND(board.getGameBoard());
        assertEquals(0, allPossibleTurnsV.size());

        List<PossibleTurn> allPossibleTurnsH = playerH.allWildcardTurns_SWAPWITHHAND(board.getGameBoard());
        assertEquals(0, allPossibleTurnsH.size());
    }

    //Achieve WinOfSixes::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void AI_Vertical_WinOfSixes_StandardGameTile() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{4, 3, 2, 1};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 5),
                GameTiles.T_CROSS,
                2,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Horizontal_WinOfSixes_StandardGameTile() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{4, 3, 2, 1};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(5, 0),
                GameTiles.T_CROSS,
                2,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Vertical_WinOfSixes_MOVER() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 1, 1, 8};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 5),
                GameTiles.T_CROSS,
                null,
                new Position(5, 5),
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Horizontal_WinOfSixes_MOVER() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 1, 1, 8};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(5, 0),
                GameTiles.T_CROSS,
                null,
                new Position(5, 5),
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Vertical_WinOfSixes_SWAPONBOARD() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 1, 1, 9};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 5),
                GameTiles.T_CROSS,
                null,
                new Position(5, 5),
                GameTiles.T_SUN,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Horizontal_WinOfSixes_SWAPONBOARD() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 1},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 1, 1, 9};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(5, 0),
                GameTiles.T_CROSS,
                null,
                new Position(5, 5),
                GameTiles.T_SUN,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Vertical_WinOfSixes_SWAPWITHHAND() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 2, 1, 10};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 5),
                GameTiles.T_CROSS,
                1,
                null,
                GameTiles.T_SUN,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
        assertEquals(expectedTurn.toSwap(), turn.toSwap());
    }

    @Test
    public void AI_Horizontal_WinOfSixes_SWAPWITHHAND() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 1},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 2, 1, 10};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(5, 0),
                GameTiles.T_CROSS,
                1,
                null,
                GameTiles.T_SUN,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
        assertEquals(expectedTurn.toSwap(), turn.toSwap());
    }

    //Prevent WinOfSixes::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void AI_Vertical_prevent_WinOfSixes_StandardGameTile() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{4, 3, 2, 1};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(5, 0),
                GameTiles.T_SQUARE,
                0,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Horizontal_prevent_WinOfSixes_StandardGameTile() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{4, 3, 2, 1};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 5),
                GameTiles.T_SQUARE,
                0,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Vertical_prevent_WinOfSixes_REMOVER() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.WC_REMOVER,
                0,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Horizontal_prevent_WinOfSixes_REMOVER() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.WC_REMOVER,
                0,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Vertical_prevent_WinOfSixes_MOVER() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{8, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 1),
                GameTiles.T_CROSS,
                null,
                new Position(0, 0),
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Horizontal_prevent_WinOfSixes_MOVER() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{8, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(1, 0),
                GameTiles.T_CROSS,
                null,
                new Position(0, 0),
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Vertical_prevent_WinOfSixes_OnlyOneFreeSpace_MOVER() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 0},
                {1, 3, 4, 5, 6, 1},
                {1, 3, 4, 5, 6, 1},
                {1, 3, 4, 5, 6, 1},
                {1, 3, 4, 5, 6, 1},
                {1, 3, 4, 5, 6, 1}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{8, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(5, 0),
                GameTiles.T_SUN,
                null,
                new Position(0, 1),
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Horizontal_prevent_WinOfSixes_OnlyOneFreeSpace_MOVER() {
        int[][] testBoard = new int[][]{
                {2, 1, 1, 1, 1, 1},
                {2, 3, 3, 3, 3, 3},
                {2, 4, 4, 4, 4, 4},
                {2, 5, 5, 5, 5, 5},
                {2, 6, 6, 6, 6, 6},
                {0, 1, 1, 1, 1, 1}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{8, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 5),
                GameTiles.T_SUN,
                null,
                new Position(1, 0),
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Vertical_prevent_WinOfSixes_SWAPONBOARD() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 3},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{9, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.T_SUN,
                null,
                new Position(5, 5),
                GameTiles.T_CROSS,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
        assertEquals(expectedTurn.toSwap(), turn.toSwap());
    }

    @Test
    public void AI_Horizontal_prevent_WinOfSixes_SWAPONBOARD() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 1}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{9, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.T_SUN,
                null,
                new Position(5, 5),
                GameTiles.T_CROSS,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
        assertEquals(expectedTurn.toSwap(), turn.toSwap());
    }

    @Test
    public void AI_Vertical_prevent_WinOfSixes_SWAPWITHHAND() {
        int[][] testBoard = new int[][]{
                {2, 2, 2, 2, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{10, 2, 1, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.T_SUN,
                2,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Horizontal_prevent_WinOfSixes_SWAPWITHHAND() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{10, 2, 1, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.T_SUN,
                2,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Vertical_prevent_WinOfSixes_fullSegment_REMOVER() {
        int[][] testBoard = new int[][]{
                {1, 2, 2, 2, 2, 2},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(1, 0),
                GameTiles.WC_REMOVER,
                0,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Horizontal_prevent_WinOfSixes_fullSegment_REMOVER() {
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 1),
                GameTiles.WC_REMOVER,
                0,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Vertical_prevent_WinOfSixes_fullSegment_MOVER() {
        int[][] testBoard = new int[][]{
                {1, 2, 2, 2, 2, 2},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{8, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 1),
                GameTiles.T_CROSS,
                null,
                new Position(1, 0),
                GameTiles.EMPTY,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Horizontal_prevent_WinOfSixes_fullSegment_MOVER() {
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{8, 2, 2, 2};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        player.evaluateToBestTurn(board);
        PossibleTurn turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(1, 0),
                GameTiles.T_CROSS,
                null,
                new Position(0, 1),
                GameTiles.EMPTY,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    //Various Scenario Tests::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void AI_onlyWildcards_nonEmptyBoard() {
        int[][] testBoard = new int[][]{
                {5, 3, 5, 3, 3, 0},
                {5, 3, 0, 0, 0, 0},
                {0, 3, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 10, 9, 7};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        PossibleTurn turn = player.evaluateToBestTurn(board);
        turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.T_TRIANGLE,
                2,
                new Position(1, 1),
                GameTiles.T_PENTAGON,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
        assertEquals(expectedTurn.toSwap(), turn.toSwap());


    }

    @Test
    public void AI_Scenario_test_01() {
        int[][] testBoard = new int[][]{
                {0, 2, 1, 2, 2, 0},
                {0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{8, 6, 9, 3};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        PossibleTurn turn = player.evaluateToBestTurn(board);
        turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.T_CROSS,
                0,
                new Position(1, 1),
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.lastPosition(), turn.lastPosition());
    }

    @Test
    public void AI_Scenario_test_02() {
        int[][] testBoard = new int[][]{
                {2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{1, 3, 3, 4};
        AI_Player player = new AI_Player("AI", true, true, hand, false);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(1, 0),
                GameTiles.T_TRIANGLE,
                1,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Scenario_test_03() {
        int[][] testBoard = new int[][]{
                {3, 2, 1, 2, 2, 2},
                {4, 3, 1, 3, 3, 4},
                {3, 3, 1, 5, 0, 0},
                {4, 0, 1, 0, 0, 0},
                {4, 0, 0, 0, 0, 0},
                {4, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{6, 2, 5, 5};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(3, 3),
                GameTiles.T_PENTAGON,
                2,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Scenario_test_04() {
        int[][] testBoard = new int[][]{
                {5, 4, 4, 5, 4, 0},
                {3, 5, 5, 5, 5, 2},
                {3, 3, 6, 5, 2, 2},
                {3, 4, 6, 2, 4, 2},
                {3, 4, 6, 6, 1, 6},
                {1, 1, 6, 5, 1, 2}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{10, 7, 0, 0};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        PossibleTurn turn = player.evaluateToBestTurn(board);

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 0),
                GameTiles.WC_REMOVER,
                1,
                null,
                null,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
    }

    @Test
    public void AI_Scenario_test_05() {
        int[][] testBoard = new int[][]{
                {1, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{10, 10, 10, 1};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        PossibleTurn turn = player.evaluateToBestTurn(board);
        turn = player.getSecondPhaseTurn();

        PossibleTurn expectedTurn = new PossibleTurn(
                new Position(0, 5),
                GameTiles.T_SUN,
                3,
                null,
                GameTiles.T_CROSS,
                0,
                0,
                0,
                false,
                null
        );

        assertEquals(expectedTurn.pos(), turn.pos());
        assertEquals(expectedTurn.toPlace(), turn.toPlace());
        assertEquals(expectedTurn.handSlot(), turn.handSlot());
        assertEquals(expectedTurn.toSwap(), turn.toSwap());
    }

    @Test
    public void AI_Scenario_test_06() {
        int[][] testBoard = new int[][]{
                {2, 4, 4, 2, 2, 2},
                {6, 6, 4, 6, 4, 6},
                {1, 3, 3, 3, 3, 1},
                {2, 6, 3, 5, 5, 5},
                {2, 6, 3, 4, 5, 1},
                {1, 6, 3, 1, 2, 1}
        };

        GameBoard board = new GameBoard(transposeBoard(testBoard));
        int[] hand = new int[]{7, 0, 2, 3};
        AI_Player player = new AI_Player("AI", true, true, hand, true);

        PossibleTurn turn = player.evaluateToBestTurn(board);
        Assert.assertNull(turn);
    }

    //Turn Reduction Methods Test:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    //Get Minimum Ordinal GameTiles Tests:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Test
    public void getMinOrdinalGameTiles_normalHand_test() {
        int[] hand = new int[]{6, 7, 4, 8};
        AI_Player player = new AI_Player("", false, false, hand, true);

        GameTiles result = player.getMinOrdinalGameTiles(player.getHand());
        Assert.assertEquals(result, Utilities.ordinalToGameTiles(4));
    }

    @Test
    public void getMinOrdinalGameTiles_bigHand_test() {
        int[] hand = new int[]{5, 1, 4, 6, 9, 4, 1, 2, 10, 6, 7, 1, 8};
        AI_Player player = new AI_Player("", false, false, hand, true);

        GameTiles result = player.getMinOrdinalGameTiles(player.getHand());
        Assert.assertEquals(result, Utilities.ordinalToGameTiles(1));
    }

    @Test
    public void getMinOrdinalGameTiles_empty_test() {
        int[] hand = new int[]{};
        AI_Player player = new AI_Player("", false, false, hand, true);

        GameTiles result = player.getMinOrdinalGameTiles(player.getHand());
        Assert.assertNull(result);
    }

    @Test
    public void leastOccurrences_test() {
        int[] hand = new int[]{1, 6, 2, 3};
        AI_Player player = new AI_Player("player", true, true, hand, true);

        int[][] input = new int[][]{
                {1, 4, 0, 5, 0, 6},
                {2, 5, 1, 0, 0, 4},
                {0, 4, 0, 4, 3, 0},
                {5, 2, 0, 2, 3, 0},
                {2, 2, 1, 5, 6, 5},
                {0, 1, 0, 4, 0, 0}
        };

        GameBoard board = new GameBoard(input);

        List<GameTiles> leastOccurring_expected = new ArrayList<>(
                List.of(GameTiles.T_TRIANGLE, GameTiles.T_STAR));

        Set<GameTiles> leastOccurring = player.getLeastOccurringGameTiles_board(board, player.getHand());

        Assert.assertEquals(2, leastOccurring.size());
        for (GameTiles tile : leastOccurring_expected) {
            Assert.assertTrue(leastOccurring.contains(tile));
        }
    }
}
