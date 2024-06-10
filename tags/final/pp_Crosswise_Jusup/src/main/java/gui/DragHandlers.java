package gui;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import logic.Game;
import logic.GameTiles;
import logic.Position;

import static gui.Utilities.imageURLToGameTile;

/**
 * DragHandlers static Helper Class with custom Drag & Drop Handlers,
 * which can be assigned to nodes.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public final class DragHandlers {

    /**
     * handSlot, from where tile is played
     */
    private static Integer handSlot = null;
    /**
     * lastPosition, if 2 positions are of interest.
     */
    private static Position lastPosition = null;
    /**
     * tileToSwap, if 2 gameTiles are of interest.
     */
    private static GameTiles tileToSwap = null;

    //Drag Handler::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Standard onDragDetected Handler for making it possible to drag GameTiles
     * and wildcards from playerHand around. Stores handSlot, where sourceNode
     * is dragged from.
     *
     * @param sourceNode Node dragged form playerHand
     */
    static void setOnDragDetected(ImageView sourceNode) {
        sourceNode.setOnDragDetected((MouseEvent event) -> {
            if (!imageURLToGameTile(sourceNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                Dragboard db = sourceNode.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                Image image = sourceNode.getImage();

                //Resize Image to fit handSlot Dimensions
                content.putImage(new Image(
                        image.getUrl(),
                        sourceNode.getFitWidth(),
                        sourceNode.getFitHeight(),
                        false,
                        true)
                );

                content.putUrl(image.getUrl());
                db.setContent(content);

                //Propagate to game Instance handSlot, from where GameTile is played from
                GridPane parent = (GridPane) sourceNode.getParent();
                boolean isVertical = parent.getRowCount() > parent.getColumnCount();
                handSlot = isVertical ? GridPane.getRowIndex(sourceNode) : GridPane.getColumnIndex(sourceNode);
            }
            event.consume();
        });
    }

    /**
     * onDragOver Handler for setting acceptTransferModes, when Node is dragged over
     * targetNode. targetNode accepts based on which GameTile is dragged over it.
     *
     * @param targetNode targetNode on which something is dragged over
     * @param isSwapping isSwapping Flag for Wildcard 2. Phase
     */
    static void setOnDragOver(ImageView targetNode, boolean isSwapping) {
        targetNode.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != targetNode && event.getDragboard().hasImage()) {

                GameTiles tile = imageURLToGameTile(event.getDragboard().getUrl());

                if (!isSwapping) {
                    //Basic Turn / Wildcard 1. Phase
                    if (tile == GameTiles.WC_REMOVER) {
                        //REMOVER -> Only non-EMPTY GameTiles
                        if (!imageURLToGameTile(targetNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                    } else if (tile == GameTiles.WC_MOVER || tile == GameTiles.WC_SWAPONBOARD
                            || tile == GameTiles.WC_SWAPWITHHAND) {
                        //MOVER, SWAPONBOARD, SWAPWITHHAND -> Anywhere
                        event.acceptTransferModes(TransferMode.MOVE);
                    } else {
                        //BASIC TURN -> Only EMPTY GameTiles
                        if (imageURLToGameTile(targetNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                    }
                } else {
                    //Wildcard 2. Phase
                    if (!GameTiles.isWildcard(tile)) {
                        //SWAPPING -> Only non-EMPTY GameTiles
                        if (!imageURLToGameTile(targetNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                    }
                }
            }

            event.consume();
        });
    }

    /**
     * Standard onDragDropped Handler for triggering a playerTurn. Resets all
     * PlayerTurnParameters after.
     *
     * @param targetNode targetNode on which something was dropped
     * @param game       game Instance to call playerTurn()
     */
    static void setOnDragDropped(ImageView targetNode, Game game) {
        targetNode.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage() && db.hasUrl()) {

                //Reset all Effects (targetNode + GridPane parent)
                targetNode.getParent().setEffect(null);
                targetNode.setEffect(null);

                Position pos = new Position(GridPane.getColumnIndex(targetNode), GridPane.getRowIndex(targetNode));
                GameTiles tile = imageURLToGameTile(db.getUrl());

                //Trigger PlayerTurn + Reset Turn Parameters
                game.playerTurn(tile, pos, handSlot, tileToSwap, lastPosition);
                resetPlayerTurnParameters();

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Standard onDragEntered Handler for setting effects to targetNode, when something is
     * dragged over it. Basically same condition as setOnDragOver Handler, with small
     * adjustments.
     *
     * @param targetNode targetNode on which something is dragged over
     * @param isSwapping isSwapping Flag for Wildcard 2. Phase
     */
    static void setOnDragEntered(ImageView targetNode, boolean isSwapping) {
        targetNode.setOnDragEntered((DragEvent event) -> {
            if (event.getGestureSource() != targetNode && event.getDragboard().hasImage()) {
                GameTiles tile = imageURLToGameTile(event.getDragboard().getUrl());

                if (!isSwapping) {
                    //Basic Turn / Wildcard 1. Phase
                    if (GameTiles.isWildcard(tile)) {
                        //WILDCARD -> Only non-EMPTY GameTiles
                        //Note: MOVER, SWAPONBOARD, SWAPWITHHAND doesn't need this, because whole board lights up
                        if (!imageURLToGameTile(targetNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                            setDragEffect(targetNode);
                        }
                    } else {
                        //BASIC TURN -> Only EMPTY GameTiles
                        if (imageURLToGameTile(targetNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                            setDragEffect(targetNode);
                        }
                    }
                } else {
                    //Wildcard 2. Phase
                    if (!GameTiles.isWildcard(tile)) {
                        //SWAPPING -> Only non-EMPTY GameTiles
                        if (!imageURLToGameTile(targetNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                            setDragEffect(targetNode);
                        }
                    }
                }
            }
        });
    }

    /**
     * Standard setOnDragExited Handler for resetting all effects on specified targetNode.
     *
     * @param targetNode to reset effects on
     */
    static void setOnDragExited(ImageView targetNode) {
        targetNode.setOnDragExited((DragEvent event) -> {
            if (!event.isDropCompleted() && event.getGestureSource() != targetNode
                    && event.getDragboard().hasImage()) {

                targetNode.setEffect(null);
            }
        });
    }

    //Drag Handlers for GameBoard GridPane::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //For 2 Phase Wildcard Turns

    /**
     * Special onDragDetected Handler for the whole board. As long as sourceNode
     * is non-EMPTY GameTile, it can be dragged. Used for Wildcard 2. Phase Turn.
     *
     * @param sourceNode sourceNode to drag around
     */
    static void setOnDragDetected_board(ImageView sourceNode) {
        sourceNode.setOnDragDetected((MouseEvent event) -> {
            if (!imageURLToGameTile(sourceNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                Dragboard db = sourceNode.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                Image image = sourceNode.getImage();

                //Resize Image to fit handSlot Dimensions
                content.putImage(new Image(
                        image.getUrl(),
                        sourceNode.getFitWidth(),
                        sourceNode.getFitHeight(),
                        false,
                        false)
                );

                content.putUrl(image.getUrl());
                db.setContent(content);

                //Propagate to game Instance Position, from where GameTile is played from
                lastPosition = new Position(GridPane.getColumnIndex(sourceNode), GridPane.getRowIndex(sourceNode));

            }
            event.consume();
        });
    }

    /**
     * Sets onDragEntered to whole gameBoard GridPane for it to show the dragEffect on
     * the whole board.
     * <p>
     * This is specially used for 2-Phase Wildcards (MOVER, SWAPONBOARD, SWAPWITHHAND),
     * because they need to be activated in order to be used. They can be dropped
     * anywhere on board, therefore the entire board is glowing.
     *
     * @param board gameBoard GridPane
     */
    static void setOnDragEntered_board(GridPane board) {
        board.setOnDragEntered((DragEvent event) -> {
            if (event.getGestureSource() != board && event.getDragboard().hasImage()) {

                GameTiles tile = imageURLToGameTile(event.getDragboard().getUrl());
                if (tile == GameTiles.WC_MOVER || tile == GameTiles.WC_SWAPONBOARD
                        || tile == GameTiles.WC_SWAPWITHHAND) {

                    setDragEffect(board);
                }
            }
        });
    }

    /**
     * Complimentary Method to setOnDragEntered_board to reset all effects, when dragged
     * Node exits the board.
     *
     * @param board gameBoard GridPane
     */
    static void setOnDragExited_board(GridPane board) {
        board.setOnDragExited((DragEvent event) -> {
            if (!event.isDropCompleted() && event.getGestureSource()
                    != board && event.getDragboard().hasImage()) {

                board.setEffect(null);
            }
        });
    }

    //Drag and Drop Handler only for SWAP ON BOARD::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //At this point, only standard GameTiles will be moved on gameBoard

    /**
     * Special onDragOver Handler for the 2. Phase of wildcard turn
     * In this Phase, only non-EMPTY GameTiles can accept something.
     *
     * @param targetNode targetNode to accept
     */
    static void setOnDragOver_swap(ImageView targetNode) {
        targetNode.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != targetNode && event.getDragboard().hasImage()) {
                if (!imageURLToGameTile(targetNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }

            event.consume();
        });
    }

    /**
     * Special onDragDropped Handler for the 2. Phase Wildcard Turn. Additionally,
     * stores tileToSwap GameTile for SWAPONBOARD Wildcard 2. Phase Turn. Triggers
     * playerTurn.
     *
     * @param targetNode targetNode on which something was dropped
     * @param game       game Instance to call playerTurn()
     */
    static void setOnDragDropped_swap(ImageView targetNode, Game game) {
        targetNode.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage() && db.hasUrl()) {

                //Reset all effects
                targetNode.setEffect(null);

                Position pos = new Position(GridPane.getColumnIndex(targetNode), GridPane.getRowIndex(targetNode));
                GameTiles tile = imageURLToGameTile(db.getUrl());
                tileToSwap = imageURLToGameTile(targetNode.getImage().getUrl());

                //Trigger PlayerTurn + Reset Turn Parameters
                game.playerTurn(tile, pos, handSlot, tileToSwap, lastPosition);
                resetPlayerTurnParameters();

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Special onDragEntered Handler for setting effects to targetNode.
     * Same condition as setOnDragOver_swap Handler.
     *
     * @param targetNode targetNode to set effect on
     */
    static void setOnDragEntered_swap(ImageView targetNode) {
        targetNode.setOnDragEntered((DragEvent event) -> {
            if (event.getGestureSource() != targetNode && event.getDragboard().hasImage()) {
                if (!imageURLToGameTile(targetNode.getImage().getUrl()).equals(GameTiles.EMPTY)) {
                    setDragEffect(targetNode);
                }
            }
        });
    }

    //Helper Methods::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Sets a green glow effect to a specified node.
     *
     * @param targetNode node to set effect on
     */
    private static void setDragEffect(Node targetNode) {
        int depth = 10;

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.LIMEGREEN);
        borderGlow.setOffsetX(0);
        borderGlow.setOffsetY(0);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);

        borderGlow.setSpread(6);

        targetNode.setEffect(borderGlow);
    }

    /**
     * Resets all playerTurn Parameters handSlot, tileToSwap, lastPosition.
     * Normally happens after playerTurn is over.
     */
    private static void resetPlayerTurnParameters() {
        handSlot = null;
        tileToSwap = null;
        lastPosition = null;
    }
}
