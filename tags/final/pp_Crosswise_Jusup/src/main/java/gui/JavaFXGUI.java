package gui;

import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


/**
 * This class is responsible for changing the GUI when the logic deems
 * it necessary. Created by the GUI and then passed as a parameter into
 * the logic.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class JavaFXGUI implements GUIConnector {
    //GameBoard & PlayerHands:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * GameBoard GridPane to enable/disable whole board
     */
    private final GridPane gameBoardGrid;
    /**
     * All ImageViews of gameBoard.
     */
    private final ImageView[][] gameBoard;
    /**
     * Multiple Player Hands GridPane.
     * To enable/ disable as whole.
     */
    private final GridPane[] playerHandsGrids;
    /**
     * To modify placed GameTiles in ImageView, to enable/disable,
     * show/hide (actual-/ empty-GameTile).
     */
    private final ImageView[][] playerHands;

    //Optional Point Bars & Labels::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Optional Point Bar for Vertical Teams
     */
    private final Label[] verticalPoints;
    /**
     * Optional Point Bar for Horizontal Teams
     */
    private final Label[] horizontalPoints;
    /**
     * Optional TeamPoints Labels for SideContainer
     * in TeamInformation Grids for both teams.
     */
    private final Label[] teamPoints;

    //AI & Animation Related::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * CheckMenuItem for showing or hiding AI Hands
     */
    private final CheckMenuItem showIsAI;
    /**
     * Transparent Animation Pane Overlay, where the
     * temporary ImageViews for the Animation are placed and moved on.
     */
    private final Pane animationPane;
    /**
     * Animation Duration RadioMenuItems for determining
     * current Animation Duration via helper Method.
     */
    private final RadioMenuItem[] animationDuration;
    /**
     * Current Animation Duration, which is played.
     * Needed for optionally cancelling the running animation.
     * Gets reset and reassigned after each individual animation.
     */
    private PathTransition currentAnimation = null;
    /**
     * GameEnd Flag for blocking nextPlayerTurn after Pause
     * Transition finishes.
     */
    private boolean gameEnd = false;
    /**
     * Additional Interrupted Flag besides gameEnd. For allowing
     * placement of gameTiles after Animation finishes. Otherwise,
     * gameEnd blocks last GameTile placements.
     */
    private boolean interrupted = false;
    /**
     * Current Animation Duration. Will be calculated each time
     * before a new Animation is played.
     */
    private Duration duration;


    /**
     * Universal Announcement Label
     */
    private final Label announcementLbl;
    /**
     * Used Wildcards Labels Array
     */
    private final Label[] usedWildcardsLbl;
    /**
     * Popup Stage, when Game has ended
     */
    private Stage popupGameEnd = null;
    /**
     * Trophy String constant to be used, whenever a WinOfSixes is achieved.
     */
    private final String TROPHY = "\uD83C\uDFC6";

    /**
     * Private inner Enum for Animation Durations.
     * All values can be changed here.
     */
    private enum DURATIONS {
        SHORT(Duration.millis(500)),
        MEDIUM(Duration.millis(1000)),
        LONG(Duration.millis(2000));

        /**
         * Duration value.
         */
        final Duration value;

        /**
         * Constructor.
         *
         * @param duration duration
         */
        DURATIONS(Duration duration) {
            this.value = duration;
        }
    }


    /**
     * JavaFXGUI Constructor.
     *
     * @param gameBoard         All ImageViews of gameBoard
     * @param gameBoardGrid     gameBoard GridPane
     * @param playerHands       All ImageViews of playerHands
     * @param playerHandsGrids  playerHands GridPanes
     * @param verticalPoints    All vertical Point Labels
     * @param horizontalPoints  All horizontal Point Labels
     * @param teamPoints        teamPoint Label Containers of both teams
     * @param usedWildcardsLbl  usedWildcards Array
     * @param announcementLbl   AnnouncementLabel
     * @param animationPane     Animation Pane
     * @param showIsAI          Show/ Hide AI Hand Flag
     * @param animationDuration Animation Duration RadioMenuItems
     */
    public JavaFXGUI(
            ImageView[][] gameBoard, GridPane gameBoardGrid,
            ImageView[][] playerHands, GridPane[] playerHandsGrids,
            Label[] verticalPoints, Label[] horizontalPoints,
            HBox[] teamPoints, Label[] usedWildcardsLbl,
            Label announcementLbl, Pane animationPane,
            CheckMenuItem showIsAI, RadioMenuItem[] animationDuration) {

        this.gameBoard = gameBoard;
        this.gameBoardGrid = gameBoardGrid;

        this.playerHands = playerHands;
        this.playerHandsGrids = playerHandsGrids;

        this.verticalPoints = verticalPoints;
        this.horizontalPoints = horizontalPoints;
        this.teamPoints = new Label[]{
                (Label) teamPoints[0].getChildren().get(0),
                (Label) teamPoints[1].getChildren().get(0)
        };

        this.animationPane = animationPane;
        this.showIsAI = showIsAI;
        this.animationDuration = animationDuration;

        this.usedWildcardsLbl = usedWildcardsLbl;
        this.announcementLbl = announcementLbl;
    }

    @Override
    public void placeTileOnBoard(GameTiles gameTile, Position pos, boolean isAITurn) {
        updateAnimationDuration();

        if (isAITurn) {

            //If AI places Tile on Board, do it after Animation is over
            PauseTransition pause = new PauseTransition(duration);
            pause.setOnFinished(event -> {
                if (!gameEnd || !interrupted) {
                    //Don't place tile on Board, if game already ended
                    this.gameBoard[pos.column()][pos.row()].setImage(Utilities.gameTilesImageMap.get(gameTile));
                }
                event.consume();
            });
            pause.play();
        } else {
            this.gameBoard[pos.column()][pos.row()].setImage(Utilities.gameTilesImageMap.get(gameTile));
        }

    }

    @Override
    public void enableAndDisablePlayerHands(int playerIdx, boolean isAI) {

        //Disable all playerHands except specified player at playerIdx
        for (int i = 0; i < playerHands.length; i++) {
            playerHandsGrids[i].setDisable(i != playerIdx);
        }

        //If player is AI, then never enable its playerHand
        if (isAI) {
            playerHandsGrids[playerIdx].setDisable(true);
        }
    }

    @Override
    public void disablePlayerHand(int playerIdx) {
        playerHandsGrids[playerIdx].setDisable(true);
    }

    @Override
    public void hideAllPlayerHands() {
        for (int i = 0; i < playerHands.length; i++) {
            for (int j = 0; j < playerHands[i].length; j++) {
                setGameTileOnHand(i, j, GameTiles.EMPTY);
            }
        }
    }

    @Override
    public void hideAllPlayerHands_Delay() {
        PauseTransition pause = new PauseTransition(duration);
        pause.setOnFinished(event -> {
            hideAllPlayerHands();
            event.consume();
        });
        pause.play();
    }

    @Override
    public void showPlayerHand(int playerIdx, List<GameTiles> tiles, boolean isAI) {
        for (int i = 0; i < playerHands[playerIdx].length; i++) {
            if (isAI) {
                //Only show AI Hand, if showIsAI is selected
                if (showIsAI.isSelected()) {
                    setGameTileOnHand(playerIdx, i, tiles.get(i));
                    setSourceDragHandler(playerHands[playerIdx][i]);
                }
            } else {
                setGameTileOnHand(playerIdx, i, tiles.get(i));
                setSourceDragHandler(playerHands[playerIdx][i]);
            }
        }
    }

    @Override
    public void showPlayerHand_Delay(int playerIdx, List<GameTiles> tiles, boolean isAI) {
        PauseTransition pause = new PauseTransition(duration);
        pause.setOnFinished(event -> {
            if (!gameEnd || !interrupted) {
                //Don't place tile on Board, if game already ended
                showPlayerHand(playerIdx, tiles, isAI);
            }
            event.consume();
        });
        pause.play();
    }

    @Override
    public void removeTileFromHandAt(int playerIdx, int handSlot) {
        this.playerHands[playerIdx][handSlot].setImage(Utilities.gameTilesImageMap.get(GameTiles.EMPTY));
    }

    @Override
    public void addTileToHandAt(int playerIdx, GameTiles tile, int handSlot, boolean isAI) {
        if (isAI) {
            //Newly added GameTile only shown, if showIsAI is selected
            if (showIsAI.isSelected()) {
                playerHands[playerIdx][handSlot].setImage(Utilities.gameTilesImageMap.get(tile));
            }
        } else {
            playerHands[playerIdx][handSlot].setImage(Utilities.gameTilesImageMap.get(tile));
        }
    }

    @Override
    public void setGameTileOnHand(int playerIdx, int handSlotIdx, GameTiles tile) {
        playerHands[playerIdx][handSlotIdx].setImage(Utilities.gameTilesImageMap.get(tile));
    }

    @Override
    public void setCurrentPlayer(String name) {
        announcementLbl.setText(name + "'s Turn");
    }

    @Override
    public void updatePointBars(int[] verticalPoints, int[] horizontalPoints) {
        for (int i = 0; i < verticalPoints.length; i++) {
            //Vertical Team Points
            if (verticalPoints[i] == Integer.MAX_VALUE) {
                this.verticalPoints[i].setText(TROPHY);
            } else {
                this.verticalPoints[i].setText(String.valueOf(verticalPoints[i]));
            }

            //Horizontal Team Points
            if (horizontalPoints[i] == Integer.MAX_VALUE) {
                this.horizontalPoints[i].setText(TROPHY);
            } else {
                this.horizontalPoints[i].setText(String.valueOf(horizontalPoints[i]));
            }
        }
    }

    @Override
    public void updateTeamPoints(int teamPoints_1, int teamPoints_2) {
        this.teamPoints[0].setText(teamPoints_1 == Integer.MAX_VALUE ? TROPHY : String.valueOf(teamPoints_1));
        this.teamPoints[1].setText(teamPoints_2 == Integer.MAX_VALUE ? TROPHY : String.valueOf(teamPoints_2));
    }

    @Override
    public void updateUsedWildcards(int[] usedWildcards) {
        for (int i = 0; i < usedWildcardsLbl.length; i++) {
            usedWildcardsLbl[i].setText(usedWildcards[i] + "/3");
        }
    }

    @Override
    public void onGameEnd(String[] playerNamesT1, String[] playerNamesT2, int[] teamPoints, GameStates winnerTeam) {
        //Disable all Player Hands
        for (GridPane handGrid : playerHandsGrids) {
            handGrid.setDisable(true);
        }

        //Display Winner Team / Draw on main Window
        switch (winnerTeam) {
            case DRAW -> announcementLbl.setText("DRAW");
            case TEAM_VERTICAL, TEAM_HORIZONTAL -> announcementLbl.setText(
                    (winnerTeam == GameStates.TEAM_VERTICAL ? "VERTICAL TEAM" : "HORIZONTAL TEAM") + " WON");
        }

        //Game End Popup Window:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        final Point2D initSize = new Point2D(500, 600);
        final Point2D maxSize = new Point2D(800, 700);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameEndPopup.fxml"));
        GameEndPopupController gameEndPopupController;
        AnchorPane popupLayout;

        try {
            popupLayout = loader.load();
            popupLayout.getStyleClass().add("root");

            gameEndPopupController = new GameEndPopupController(
                    popupLayout,
                    playerNamesT1,
                    playerNamesT2,
                    teamPoints,
                    winnerTeam
            );
            loader.setController(gameEndPopupController);
            gameEndPopupController.initialize(null, null);

            Scene scene = new Scene(popupLayout, initSize.getX(), initSize.getY());
            scene.getStylesheets().add(Objects.requireNonNull(getClass()
                    .getResource("GameOverStyle.css")).toExternalForm());

            Stage popupStage = new Stage();
            this.popupGameEnd = popupStage;
            popupStage.setTitle("Game Over");
            popupStage.setScene(scene);
            //popupStage.setAlwaysOnTop(true);

            popupStage.setMinWidth(initSize.getX());
            popupStage.setMinHeight(initSize.getY());

            popupStage.setMaxWidth(maxSize.getX());
            popupStage.setMaxHeight(maxSize.getY());

            popupStage.show();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void onGameEnd_Delay(String[] playerNamesT1, String[] playerNamesT2, int[] teamPoints, GameStates winnerTeam) {
        PauseTransition pause = new PauseTransition(duration);
        pause.setOnFinished(event -> {
            onGameEnd(playerNamesT1, playerNamesT2, teamPoints, winnerTeam);
            event.consume();
        });
        pause.play();
    }

    @Override
    public void setSourceDragHandler(ImageView source) {
        Image image = source.getImage();
        GameTiles tile = Utilities.imageURLToGameTile(image.getUrl());

        if (tile.ordinal() > GameTiles.EMPTY.ordinal()) {
            DragHandlers.setOnDragDetected(source);
        }
    }

    @Override
    public void setBoardDragHandler(Game game, boolean swap) {
        for (ImageView[] imageViews : gameBoard) {
            for (ImageView imageView : imageViews) {
                if (GameTiles.isStandardGameTile(Utilities.imageURLToGameTile(imageView.getImage().getUrl()))) {
                    DragHandlers.setOnDragDetected_board(imageView);
                }

                if (swap) {
                    DragHandlers.setOnDragEntered_swap(imageView);
                    DragHandlers.setOnDragOver_swap(imageView);
                    DragHandlers.setOnDragDropped_swap(imageView, game);
                }
            }
        }
    }

    @Override
    public void resetBoardDragHandler(Game game, boolean isSwapping) {
        for (ImageView[] views : gameBoard) {
            for (ImageView view : views) {
                view.setOnDragDetected(null);
            }
        }

        //Reset gameBoard GridPane Handlers
        DragHandlers.setOnDragEntered_board(gameBoardGrid);
        DragHandlers.setOnDragExited_board(gameBoardGrid);

        if (isSwapping) {
            gameBoardGrid.setOnDragEntered(null);
            gameBoardGrid.setOnDragExited(null);
        }

        for (ImageView[] imageViews : gameBoard) {
            for (ImageView imageView : imageViews) {
                DragHandlers.setOnDragExited(imageView);
                DragHandlers.setOnDragEntered(imageView, isSwapping);
                DragHandlers.setOnDragDropped(imageView, game);
                DragHandlers.setOnDragOver(imageView, isSwapping);
            }
        }
    }

    @Override
    public void setAnnouncementLabel(String msg) {
        announcementLbl.setText(msg);
    }

    @Override
    public void closePopups() {
        if (popupGameEnd != null) {
            popupGameEnd.close();
        }
    }

    @Override
    public void createAndPlayAnimation(GameTiles tile, int currentPlayer, Integer handSlot, Position sourcePos,
                                       Position targetPos, Game game, boolean doNextTurn) {

        updateAnimationDuration();
        Point2D offset = animationPane.localToScene(0.0, 0.0);

        //Create Source ImageView
        ImageView source = sourcePos == null
                ? playerHands[currentPlayer][handSlot]
                : gameBoard[sourcePos.column()][sourcePos.row()];
        Bounds bound_source = source.getBoundsInLocal();
        Point2D position_source = source.localToScene(0.0, 0.0);

        //Create Target ImageView
        ImageView target = targetPos == null
                ? playerHands[currentPlayer][handSlot]
                : gameBoard[targetPos.column()][targetPos.row()];
        Bounds bound_target = target.getBoundsInLocal();
        Point2D position_target = target.localToScene(0.0, 0.0);


        ImageView image = new ImageView(Utilities.gameTilesImageMap.get(tile));
        image.setX(-500);
        image.setY(-500);
        image.setFitWidth(bound_target.getWidth());
        image.setFitHeight(bound_target.getHeight());
        animationPane.getChildren().add(image);

        //Create Starting Position
        Point2D from_01 = new Point2D(
                position_source.getX() - offset.getX() + (bound_source.getWidth() / 2),
                position_source.getY() - offset.getY() + (bound_source.getHeight() / 2)
        );

        //Create Target Position
        Point2D to_01 = new Point2D(
                position_target.getX() - offset.getX() + (bound_target.getWidth() / 2),
                position_target.getY() - offset.getY() + (bound_target.getHeight() / 2)
        );

        //Create Animation Path
        Path path = new Path();
        path.getElements().add(new MoveTo(from_01.getX(), from_01.getY()));
        path.getElements().add(new LineTo(to_01.getX(), to_01.getY()));

        //Initialize and Play Path Transition
        PathTransition pathTransition = new PathTransition(duration, path, image);
        currentAnimation = pathTransition;
        pathTransition.play();

        //Remove temporary ImageView from animation Pane & Reset current Animation
        pathTransition.setOnFinished(event -> {
            animationPane.getChildren().remove(image);
            currentAnimation = null;

            event.consume();
        });

        //If next Turn will be played by AI, force AI turn (also for 2 Phase AI Wildcard Turn)
        if (doNextTurn) {
            PauseTransition pause = new PauseTransition(duration);
            pause.setOnFinished(event -> {
                if (!gameEnd) {
                    game.forceTurnAI();
                    event.consume();
                }
            });
            pause.play();
        }
    }

    @Override
    public void stopAnimation() {
        if (currentAnimation != null) {
            currentAnimation.stop();
            animationPane.getChildren().clear();
        }
    }

    @Override
    public void updateAnimationDuration() {
        boolean updated = false;

        for (int i = 0; i < animationDuration.length && !updated; i++) {
            if (animationDuration[i].isSelected()) {
                switch (i) {
                    case 0 -> duration = DURATIONS.SHORT.value;
                    case 1 -> duration = DURATIONS.MEDIUM.value;
                    case 2 -> duration = DURATIONS.LONG.value;
                }
                updated = true;
            }
        }
    }

    @Override
    public void forceStop() {
        gameEnd = true;
        stopAnimation();
    }

    @Override
    public void interrupt() {
        interrupted = true;
        stopAnimation();
    }

    @Override
    public void createAlert(ErrorType errorType, String msg) {
        Utilities.createAlert(errorType, msg);
    }
}
