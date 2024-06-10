package gui;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import logic.Game;
import logic.GameTiles;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Main class for the user interface.
 *
 * @author mjo, cei, Jonathan El Jusup (cgt104707)
 */
public class UserInterfaceController implements Initializable {

    private final int BOARDGRIDSIZE = 6;
    private final int HANDSIZE = 4;
    private final int CELLGAP = 5;

    @FXML
    private VBox root;

    @FXML
    private ImageView[][] gameBoardFXML;

    @FXML
    private Label announcementLabelFXML;

    @FXML
    private GridPane[] playerHandsFXML;

    //Instance of the game Crosswise
    Game game;

    /**
     * This is where you need to add code that should happen during
     * initialization and then change the java doc comment.
     *
     * @param location  probably not used
     * @param resources probably not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Menu Bar::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        MenuBar menuBar = createMenuBar();
        root.getChildren().add(menuBar);


        //Stack Pane for Overlay Prompts::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        StackPane stackPane = new StackPane();


        //Central Horizontal Alignment Container::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox alignmentContainerH = new HBox();
        root.getChildren().add(alignmentContainerH);
        alignmentContainerH.setAlignment(Pos.CENTER);


        //Global Container::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        GridPane globalContainer = createContainerGrid(new int[] {0, 0}, new int[] {0, 0});
        globalContainer.getStyleClass().add("global-container");

        alignmentContainerH.getChildren().add(stackPane);
        stackPane.getChildren().add(globalContainer);

        globalContainer.getColumnConstraints().get(0).prefWidthProperty().bind(alignmentContainerH.heightProperty());
        globalContainer.getColumnConstraints().get(1).setMinWidth(400);
        globalContainer.prefHeightProperty().bind(root.heightProperty());
        globalContainer.getRowConstraints().get(0).setMinHeight(100);


        //Announcement Label::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox labelContainer = new HBox();
        labelContainer.getStyleClass().add("announcement-label-container");

        globalContainer.add(labelContainer, 0, 0, 1, 1);

        labelContainer.setAlignment(Pos.CENTER);

        //TODO: Make GUI Controller set Label Text
        Label announcementLabel = new Label("<Label>Put these foolish ambitions to rest<Label>");
        announcementLabel.getStyleClass().add("announcement-label");

        announcementLabelFXML = announcementLabel;

        labelContainer.getChildren().add(announcementLabel);

        //TESTING DYNAMIC FONT SIZE
        /*
        announcementLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tmpText = new Text(newValue);
            tmpText.setFont(new Font(50));

            double textWidth = tmpText.getLayoutBounds().getWidth();
            if (textWidth <= labelContainer.getWidth() - PADDING) {
                announcementLabel.setFont(new Font(50));
            } else {
                double newFontSize = 30 * labelContainer.getWidth() / textWidth;
                announcementLabel.setFont(Font.font(Font.getDefault().getFamily(), newFontSize));
            }
        });
        */


        //Square Box Container for Main Container:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox alignmentMainContainerH = new HBox();
        VBox alignmentMainContainerV = new VBox();
        alignmentMainContainerH.getStyleClass().add("main-container-alignmentH");
        alignmentMainContainerV.getStyleClass().add("main-container-alignmentV");

        alignmentMainContainerH.getChildren().add(alignmentMainContainerV);
        globalContainer.add(alignmentMainContainerH, 0, 1, 1, 1);

        alignmentMainContainerV.prefWidthProperty().bind(alignmentMainContainerH.heightProperty());
        alignmentMainContainerV.prefHeightProperty().bind(alignmentMainContainerH.widthProperty());
        alignmentMainContainerH.alignmentProperty().set(Pos.CENTER);
        alignmentMainContainerV.alignmentProperty().set(Pos.TOP_CENTER);


        //Main Container Grid:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        GridPane mainContainer = createContainerGrid(new int[] {15, 70, 15}, new int[] {15, 70, 15});
        mainContainer.getStyleClass().add("main-container");

        alignmentMainContainerV.getChildren().add(mainContainer);

        mainContainer.prefWidthProperty().bind(alignmentMainContainerV.widthProperty());
        mainContainer.prefHeightProperty().bind(alignmentMainContainerV.widthProperty());


        //BoardContainer Grid:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        GridPane boardContainer = createContainerGrid(new int[] {90, 10}, new int[] {90, 10});
        boardContainer.getStyleClass().add("board-container");

        mainContainer.add(boardContainer, 1, 1, 1, 1);


        //Game Board Grid:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        GridPane gameBoard = createGameGrid(BOARDGRIDSIZE);
        gameBoard.getStyleClass().add("game-board");

        gameBoard.setPadding(new Insets(5, 5, 5, 5));
        boardContainer.add(gameBoard, 0, 0, 1, 1);


        //Initialize Grid with game tiles
        //TODO: Make logic determine tiles
        ImageView[][] imageViews = initImages(gameBoard);

        gameBoardFXML = imageViews;



        //Player Hand Grids:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        GridPane[] playerHands = createPlayerHands();
        for (GridPane playerHand : playerHands) {
            playerHand.getStyleClass().add("player-hands");

            playerHand.setAlignment(Pos.CENTER);

            //TODO: USE RETURN VALUE?!
            initHand(playerHand);
        }

        playerHandsFXML = playerHands;


        //Hand Containers which can either be cast to alignmentMainContainerV or alignmentMainContainerH
        Node[] handContainer = new Node[playerHands.length];
        for (int i = 0; i < handContainer.length; i++) {
            handContainer[i] = (i < 2) ? new HBox() : new VBox();
            handContainer[i].getStyleClass().add("playerHand-anchorPane");

            if (i < 2) {
                //Vertical Team Hands
                ((HBox) handContainer[i]).alignmentProperty().set(Pos.CENTER);
                ((HBox) handContainer[i]).getChildren().add(playerHands[i]);
            } else {
                //Horizontal Team Hands
                ((VBox) handContainer[i]).alignmentProperty().set(Pos.CENTER);
                ((VBox) handContainer[i]).getChildren().add(playerHands[i]);
            }
        }

        //Add Player Hands to mainContainer
        mainContainer.add(handContainer[0], 1, 0, 1, 1);
        mainContainer.add(handContainer[1], 1, 2, 1, 1);
        mainContainer.add(handContainer[2], 0, 1, 1, 1);
        mainContainer.add(handContainer[3], 2, 1, 1, 1);


        //Optional Panels:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //Point Grids (Horizontal & Vertical)

        GridPane horizontalPointsGrid = pointGridHorizontal();
        GridPane verticalPointsGrid = pointGridVertical();
        horizontalPointsGrid.getStyleClass().add("point-grid");
        verticalPointsGrid.getStyleClass().add("point-grid");

        horizontalPointsGrid.prefHeightProperty().bind(gameBoard.heightProperty());
        verticalPointsGrid.prefWidthProperty().bind(gameBoard.widthProperty());
        horizontalPointsGrid.setPadding(new Insets(5, 5, 5, 5));
        verticalPointsGrid.setPadding(new Insets(5, 5, 5, 5));

        boardContainer.add(horizontalPointsGrid, 1, 0, 1, 1);
        boardContainer.add(verticalPointsGrid, 0, 1, 1, 1);

        //Testing with no point Grid Helpers
        //boardContainer.getChildren().clear();
        //boardContainer.add(gameBoard, 0, 0, 2, 2);

        //Point Table
        HBox pointTableContainerH = new HBox();
        VBox pointTableContainerV = new VBox();

        pointTableContainerH.getStyleClass().add("point-table-container");
        pointTableContainerV.getStyleClass().add("point-table-container");

        pointTableContainerH.getChildren().add(pointTableContainerV);

        GridPane pointTable = createPointTable();
        pointTable.getStyleClass().add("point-table");

        pointTableContainerV.getChildren().add(pointTable);
        pointTableContainerV.prefWidthProperty().bind(pointTableContainerH.widthProperty());

        globalContainer.add(pointTableContainerV, 1, 1, 1, 1);

        //Bind 2. Column of Global Container to mainContainer so it grows with it
        globalContainer.getColumnConstraints().get(1).prefWidthProperty().bind(
                mainContainer.widthProperty().divide(2)
        );

        //Testing with no point table
        //globalContainer.getChildren().clear();
        //globalContainer.add(labelContainer, 0, 0, 2, 1);
        //globalContainer.add(alignmentMainContainerH, 0, 1, 2, 1);

        //Overlay for new Game::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox newGameMenu = createNewGameMenu(globalContainer.widthProperty());
        stackPane.getChildren().add(newGameMenu);

        //TODO: COMMENT OUT HERE FOR DISPLAYING NEW GAME OVERLAY
        //Toggle New Game Menu
        newGameMenu.toBack();
    }

    /**
     * Creates Menu Bar with 2 Main Menus <Game>, <Options>
     * <Game> Menu has MenuItems <New Game>, <Save>, <Load>, <Save & Quit>
     * <Option> Manu has CheckMenuItems <Show Point Bars>, <Show Point Table>
     *
     * @return MenuBar
     */
    private MenuBar createMenuBar() {
        //Overall Menu Bar::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        MenuBar menuBar = new MenuBar();
        Menu game = new Menu("Game");
        Menu options = new Menu("Options");
        menuBar.getMenus().addAll(game, options);

        //Menu Items::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        MenuItem newGame = new MenuItem("New Game");
        newGame.setId("MenuItem_NewGame");

        MenuItem saveGame = new MenuItem("Save");
        saveGame.setId("MenuItem_SaveGame");

        MenuItem loadGame = new MenuItem("Load");
        loadGame.setId("MenuItem_Load");

        MenuItem saveAndQuit = new MenuItem("Save & Quit");
        saveAndQuit.setId("MenuItem_SaveAndQuit");

        game.getItems().addAll(newGame, saveGame, loadGame, saveAndQuit);

        //Toggleable CheckMenu Items::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        CheckMenuItem pointBars = new CheckMenuItem("Show Point Bars");
        CheckMenuItem pointTable = new CheckMenuItem("Show Point Table");
        CheckMenuItem AIHand = new CheckMenuItem("Show AI Hand");

        Menu durationAITurn = new Menu("AI Turn Duration");
        CheckMenuItem duration_short = new CheckMenuItem("Short");
        CheckMenuItem duration_medium = new CheckMenuItem("Medium");
        CheckMenuItem duration_long = new CheckMenuItem("Long");

        durationAITurn.getItems().addAll(duration_short, duration_medium, duration_long);
        options.getItems().addAll(pointBars, pointTable, AIHand, durationAITurn);

        return menuBar;
    }

    /**
     * Creates generic GridContainer with custom ColumnConstraints and RowConstraints
     * If percentage value is 0, width / height of column / row is set to some arbitrary
     * small value.
     * @param colLayout Integer Array with percentage width for each Column, if 0
     *                  Column gets initialized with minWidth value
     * @param rowLayout Integer Array with percentage height for each Row, if 0
     *                  Row gets initialized with minHeight value
     *
     * @return gridPane with custom Column- & Row-Constraints
     */
    private GridPane createContainerGrid(int[] colLayout, int[] rowLayout) {
        GridPane gridPane = new GridPane();

        //Column Constraints
        for (int i : colLayout) {
            ColumnConstraints col = new ColumnConstraints();
            if (i != 0)
                col.setPercentWidth(i);
            else
                col.setMinWidth(5);

            gridPane.getColumnConstraints().add(col);
        }

        //Row Constraints
        for (int j : rowLayout) {
            RowConstraints row = new RowConstraints();
            if (j != 0)
                row.setPercentHeight(j);
            else
                row.setMinHeight(5);

            gridPane.getRowConstraints().add(row);
        }

        return gridPane;
    }

    /**
     * Creates square n x n Game GridPane with a gap between each column and row
     *
     * @return n x n GridPane
     */
    private GridPane createGameGrid(int gridSize) {
        GridPane gridPane = new GridPane();
        for (int x = 0; x < gridSize; x++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(2);
            gridPane.getColumnConstraints().add(col);

            RowConstraints row = new RowConstraints();
            row.setMinHeight(2);
            gridPane.getRowConstraints().add(row);
        }

        //Set Gap between Rows and Columns + Center Alignment
        gridPane.setHgap(CELLGAP);
        gridPane.setVgap(CELLGAP);
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    /**
     * Creates optional horizontal pointGrid Helper
     *
     * @return vertical point helper GridPane
     */
    private GridPane pointGridHorizontal() {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < BOARDGRIDSIZE; i++) {
            RowConstraints row = new RowConstraints();
            gridPane.getRowConstraints().add(row);

            row.setMinHeight(2);
            row.setVgrow(Priority.ALWAYS);
        }

        ColumnConstraints col = new ColumnConstraints();
        gridPane.getColumnConstraints().add(col);

        col.setMinWidth(2);
        col.setHgrow(Priority.ALWAYS);

        gridPane.setGridLinesVisible(true);
        gridPane.setHgap(CELLGAP);
        gridPane.setVgap(CELLGAP);
        gridPane.setAlignment(Pos.CENTER);

        //Point Labels
        for (int i = 0; i < BOARDGRIDSIZE; i++) {
            Label label = new Label("X");
            label.setFont(Font.font("Calibri", 20));

            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            gridPane.add(label, 0, i, 1, 1);
        }

        return gridPane;
    }

    /**
     * Creates optional vertical pointGrid Helper
     *
     * @return vertical point helper GridPane
     */
    private GridPane pointGridVertical() {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < BOARDGRIDSIZE; i++) {
            ColumnConstraints col = new ColumnConstraints();
            gridPane.getColumnConstraints().add(col);

            col.setMinWidth(2);
            col.setHgrow(Priority.ALWAYS);
        }

        RowConstraints row = new RowConstraints();
        gridPane.getRowConstraints().add(row);

        row.setMinHeight(2);
        row.setVgrow(Priority.ALWAYS);

        gridPane.setGridLinesVisible(true);
        gridPane.setHgap(CELLGAP);
        gridPane.setVgap(CELLGAP);
        gridPane.setAlignment(Pos.CENTER);

        //Point Labels
        for (int i = 0; i < BOARDGRIDSIZE; i++) {
            Label label = new Label("X");
            label.setFont(Font.font("Calibri", 20));

            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            gridPane.add(label, i, 0, 1, 1);
        }

        return gridPane;
    }

    /**
     * Creates GridPane Array of (4) PlayerHands
     * TODO: Dynamically create Hand for 2 or 4 Players (USE PARAMETER)
     *
     * @return GridPane Array of PlayerHands
     */
    private GridPane[] createPlayerHands() {
        int playerCount = 4;

        GridPane[] playerHands = new GridPane[playerCount];

        //Vertical Team Hands:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        for (int i = 0; i < playerCount / 2; i++) {
            playerHands[i] = new GridPane();

            for (int j = 0; j < HANDSIZE; j++) {
                ColumnConstraints col = new ColumnConstraints();
                col.setMinWidth(10);
                playerHands[i].getColumnConstraints().add(col);
            }
            RowConstraints row = new RowConstraints();
            row.setMinHeight(10);
            playerHands[i].getRowConstraints().add(row);

            playerHands[i].setHgap(CELLGAP);
            playerHands[i].prefWidthProperty().bind(playerHands[i].heightProperty().multiply(HANDSIZE).add(CELLGAP));
        }

        //Horizontal Team Hands:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        for (int i = playerCount / 2; i < playerCount; i++) {
            playerHands[i] = new GridPane();

            for (int j = 0; j < HANDSIZE; j++) {
                RowConstraints row = new RowConstraints();
                row.setMinHeight(10);
                playerHands[i].getRowConstraints().add(row);
            }
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(10);
            playerHands[i].getColumnConstraints().add(col);

            playerHands[i].setVgap(CELLGAP);
            playerHands[i].prefHeightProperty().bind(playerHands[i].widthProperty().multiply(HANDSIZE).add(CELLGAP));
        }

        return playerHands;
    }

    /**
     * Gets Grid of player hand and initializes it with ImageViews which
     * contain Empty Game Tiles (none.png)
     *
     * @param grdPn player hand gridPane
     */
    private void initHand(GridPane grdPn) {
        assert grdPn.getColumnCount() == HANDSIZE || grdPn.getRowCount() == HANDSIZE;

        int colCount = grdPn.getColumnCount();
        int rowCount = grdPn.getRowCount();
        int cellWidth = (int) grdPn.getWidth() / colCount;
        int cellHeight = (int) grdPn.getHeight() / rowCount;

        ImageView[] hand = new ImageView[HANDSIZE]; //Variable Hand Size?

        for (int i = 0; i < hand.length; i++) {
            hand[i] = new ImageView();

            hand[i].setFitWidth(cellWidth);
            hand[i].setFitHeight(cellHeight);
            hand[i].setPreserveRatio(false);
            hand[i].setSmooth(true);

            hand[i].setImage(new Image(tileToAddress(GameTiles.EMPTY)));

            //ImageView resizes when window resizes
            hand[i].fitWidthProperty().bind(
                    grdPn.widthProperty().divide(colCount).subtract(colCount > rowCount ? CELLGAP : 0));
            hand[i].fitHeightProperty().bind(
                    grdPn.heightProperty().divide(rowCount).subtract(colCount > rowCount ? 0 : CELLGAP));

            grdPn.add(hand[i], (colCount > rowCount ? i : 0), (colCount > rowCount ? 0 : i), 1, 1);
        }

    }

    /**
     * Creates PointTable Helper for showing corresponding Points to
     * given GameTile combinations
     *
     * @return pointTable GridPane
     */
    private GridPane createPointTable() {
        GridPane pointTable = createContainerGrid(new int[] {75, 25}, new int[] {0, 0, 0, 0, 0, 0, 0});

        Label tileCombination = new Label("Tile Combination");
        Label points = new Label("Points");

        pointTable.add(tileCombination, 0, 0, 1, 1);
        pointTable.add(points, 1, 0, 1, 1);


        GridPane[] combinations = new GridPane[6];
        for (int i = 0; i < combinations.length; i++) {

            //Combination Grid Container hBox
            HBox gridContainerH = new HBox();
            VBox gridContainerV = new VBox();

            gridContainerH.getChildren().add(gridContainerV);

            gridContainerH.getStyleClass().add("combination-grid-holder");
            gridContainerV.getStyleClass().add("combination-grid-holder");

            //6 x 1 Grid for holding GameTiles combinations
            combinations[i] = createContainerGrid(new int[] {0, 0, 0, 0, 0, 0}, new int[] {0});
            combinations[i].getStyleClass().add("combination-grid");

            gridContainerV.getChildren().add(combinations[i]);
            gridContainerV.alignmentProperty().set(Pos.CENTER);

            gridContainerV.prefWidthProperty().bind(gridContainerH.widthProperty());


            int colCount = combinations[i].getColumnCount();
            int rowCount = combinations[i].getRowCount();

            //Grid Layout Properties
            combinations[i].setHgap(CELLGAP);
            combinations[i].setAlignment(Pos.CENTER);

            combinations[i].prefWidthProperty().bind(gridContainerV.widthProperty().subtract(CELLGAP));
            combinations[i].prefHeightProperty().bind(combinations[i].widthProperty().divide(6));


            pointTable.add(gridContainerH, 0, i + 1, 1, 1);

            int cellWidth = (int) combinations[i].getWidth() / colCount;
            int cellHeight = (int) combinations[i].getHeight() / rowCount;

            //Initialize ImageViews
            for (int j = 0; j < colCount; j++) {
                ImageView imageView = new ImageView();

                //Image has to fit a cell and mustn't preserve ratio
                imageView.setFitWidth(cellWidth);
                imageView.setFitHeight(cellHeight);
                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);

                combinations[i].add(imageView, j, 0, 1, 1);

                //Image resizes, when window resizes
                imageView.fitWidthProperty().bind(combinations[i].widthProperty().divide(colCount).subtract(CELLGAP));
                imageView.fitHeightProperty().bind(combinations[i].heightProperty().divide(rowCount).subtract(CELLGAP));
            }
        }

        //Combination : <All GameTiles Different> : 6 Points
        setPointCombinationTilesImages(combinations[0], new GameTiles[] {
                GameTiles.T_SUN, GameTiles.T_CROSS, GameTiles.T_TRIANGLE,
                GameTiles.T_SQUARE, GameTiles.T_PENTAGON, GameTiles.T_STAR
        });

        //Combination : <2 same GameTiles> : 1 Points
        setPointCombinationTilesImages(combinations[1], new GameTiles[] {
                GameTiles.T_SQUARE, GameTiles.T_SQUARE
        });

        //Combination : <3 same GameTiles> : 3 Points
        setPointCombinationTilesImages(combinations[2], new GameTiles[] {
                GameTiles.T_SQUARE, GameTiles.T_SQUARE, GameTiles.T_SQUARE
        });

        //Combination : <4 same GameTiles> : 5 Points
        setPointCombinationTilesImages(combinations[3], new GameTiles[] {
                GameTiles.T_SQUARE, GameTiles.T_SQUARE, GameTiles.T_SQUARE,
                GameTiles.T_SQUARE
        });

        //Combination : <5 same GameTiles> : 7 Points
        setPointCombinationTilesImages(combinations[4], new GameTiles[] {
                GameTiles.T_SQUARE, GameTiles.T_SQUARE, GameTiles.T_SQUARE,
                GameTiles.T_SQUARE, GameTiles.T_SQUARE
        });

        //Combination : <6 same GameTiles> : Instant Win
        setPointCombinationTilesImages(combinations[5], new GameTiles[] {
                GameTiles.T_SQUARE, GameTiles.T_SQUARE, GameTiles.T_SQUARE,
                GameTiles.T_SQUARE, GameTiles.T_SQUARE, GameTiles.T_SQUARE
        });

        //Add Point Labels
        pointTable.add(new HBox(new Label("6")), 1, 1, 1, 1);
        pointTable.add(new HBox(new Label("1")), 1, 2, 1, 1);
        pointTable.add(new HBox(new Label("3")), 1, 3, 1, 1);
        pointTable.add(new HBox(new Label("5")), 1, 4, 1, 1);
        pointTable.add(new HBox(new Label("7")), 1, 5, 1, 1);
        pointTable.add(new HBox(new Label("WIN")), 1, 6, 1, 1);

        return pointTable;
    }

    /**
     * Allocates given combination of GameTiles Images to given Grid
     *
     * @param grid 6 x 1 GridPane
     * @param combination GameTiles Array with set combination
     */
    private void setPointCombinationTilesImages(GridPane grid, GameTiles[] combination) {
        assert combination.length <= 6;

        for (int i = 0; i < 6; i++) {
            ImageView image = (ImageView) grid.getChildren().get(i);

            //Fill with given Combination, fill rest with EMPTY
            if (i < combination.length) {
                image.setImage(new Image(tileToAddress(combination[i])));
            } else {
                image.setImage(new Image(tileToAddress(GameTiles.EMPTY)));
            }
        }
    }

    /**
     * Creates an array of imageViews corresponding to the gridPane.
     * Each imageView becomes a child of the gridPane and fills a cell.
     * For proper resizing it is bound to the gridPanes width and height.
     *
     * @return an array of imageViews added to the gridPane
     */
    private ImageView[][] initImages(GridPane grdPn) {
        int colCount = grdPn.getColumnCount();
        int rowCount = grdPn.getRowCount();

        ImageView[][] imageViews = new ImageView[colCount][rowCount];
        int cellWidth = (int) grdPn.getWidth() / colCount;
        int cellHeight = (int) grdPn.getHeight() / rowCount;

        //Bind each ImageView to a cell of the gridPane
        for (int x = 0; x < colCount; x++) {
            for (int y = 0; y < rowCount; y++) {
                //Creates an empty imageView
                imageViews[x][y] = new ImageView();

                //Image has to fit a cell and mustn't preserve ratio
                imageViews[x][y].setFitWidth(cellWidth);
                imageViews[x][y].setFitHeight(cellHeight);
                imageViews[x][y].setPreserveRatio(false);
                imageViews[x][y].setSmooth(true);

                //SetStyleClass for styling
                //TODO: Style tiles (https://stackoverflow.com/questions/20489908/border-radius-and-shadow-on-imageview)
                imageViews[x][y].getStyleClass().add("game-tiles");

                //Add the imageView to the cell
                grdPn.add(imageViews[x][y], x, y);

                //The image shall resize when the cell resizes
                imageViews[x][y].fitWidthProperty().bind(grdPn.widthProperty().divide(colCount).subtract(CELLGAP));
                imageViews[x][y].fitHeightProperty().bind(grdPn.heightProperty().divide(rowCount).subtract(CELLGAP));


                //TODO: Replace with standard EMPTY GameTile initialization
                Image image = switch (new Random().nextInt(0, 10)) {
                    case 0 -> new Image(tileToAddress(GameTiles.T_CROSS));
                    case 1 -> new Image(tileToAddress(GameTiles.T_PENTAGON));
                    case 2 -> new Image(tileToAddress(GameTiles.T_SQUARE));
                    case 3 -> new Image(tileToAddress(GameTiles.T_STAR));
                    case 4 -> new Image(tileToAddress(GameTiles.T_SUN));
                    case 5 -> new Image(tileToAddress(GameTiles.T_TRIANGLE));
                    default -> new Image(tileToAddress(GameTiles.EMPTY));
                };

                //imageViews[x][y].setImage(new Image("img/none.png"));
                imageViews[x][y].setImage(image);

            }
        }
        return imageViews;
    }

    /**
     * Converts GameTile to String Image Address to be referred to.
     * @param tile GameTile
     *
     * @return String Image Address
     */
    private String tileToAddress(GameTiles tile) {
        String address = "";

        switch (tile) {
            case EMPTY -> address = "img/none.png";
            case T_SUN -> address = "img/sun.png";
            case T_CROSS -> address = "img/cross.png";
            case T_TRIANGLE -> address = "img/triangle.png";
            case T_SQUARE -> address = "img/square.png";
            case T_PENTAGON -> address = "img/pentagon.png";
            case T_STAR -> address = "img/star.png";
            case WC_REMOVER -> address = "img/remove.png";
            case WC_MOVER -> address = "img/move.png";
            case WC_SWAPONBOARD -> address = "img/swapOnBoard";
            case WC_SWAPWITHHAND -> address = "img/swapWithHand";

            default -> System.out.println("ERROR: " + tile + " has no image address / does not exist");
        }

        return address;
    }

    /**
     * Creates new Overlay for creating new Game. Added to existing StackPane Node
     *
     * @param referenceWidth globalContainer.widthProperty
     * @return newGame Menu Overlay
     */
    private HBox createNewGameMenu(ReadOnlyDoubleProperty referenceWidth) {

        //Overlay Containers::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox overlayH = new HBox();
        overlayH.getStyleClass().add("overlay");
        overlayH.alignmentProperty().set(Pos.CENTER);

        VBox overlayV = new VBox();
        overlayV.alignmentProperty().set(Pos.CENTER);
        overlayV.prefWidthProperty().bind(referenceWidth.divide(2));
        overlayH.getChildren().add(overlayV);

        //Main Container::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("new-game-menu");
        overlayV.getChildren().add(borderPane);

        borderPane.prefWidthProperty().bind(overlayV.widthProperty());
        borderPane.prefHeightProperty().bind(overlayV.heightProperty().multiply(0.8));
        borderPane.setMaxHeight(700);

        //Main Container Elements
        Button startGameBtn = new Button("START GAME");
        startGameBtn.setId("StartGameBtn");

        borderPane.setTop(new HBox(new Label("---WORK IN PROGRESS---")));
        borderPane.setBottom(new HBox(startGameBtn));


        //Main Content (PlayerCount Buttons + PlayerName TextFields):::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox mainContainerH = new HBox();
        VBox mainContainerV = new VBox();

        mainContainerH.getChildren().add(mainContainerV);

        mainContainerV.alignmentProperty().set(Pos.BASELINE_CENTER);
        borderPane.setCenter(mainContainerH);

        //mainContainerV.setStyle("-fx-background-color: RED; -fx-background-radius: 10");
        mainContainerV.setStyle("-fx-background-color: " +
                "linear-gradient(from 10px 10px to 20px 20px, repeat, #ffd400 40%, #000000 40%);" +
                "-fx-background-radius: 10");

        //Player Count Container::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox playerCountContainer = new HBox();

        playerCountContainer.alignmentProperty().set(Pos.CENTER);

        mainContainerV.getChildren().add(createSpacerNode(true));
        mainContainerV.getChildren().add(playerCountContainer);

        Button twoPlayers = new Button("2 PLayers");
        Button fourPlayers = new Button("4 Players");
        twoPlayers.getStyleClass().add("buttons");
        fourPlayers.getStyleClass().add("buttons");

        playerCountContainer.getChildren().add(createSpacerNode(false));
        playerCountContainer.getChildren().add(twoPlayers);
        playerCountContainer.getChildren().add(createSpacerNode(false));
        playerCountContainer.getChildren().add(fourPlayers);
        playerCountContainer.getChildren().add(createSpacerNode(false));


        mainContainerV.getChildren().add(createSpacerNode(true));

        //Player Creation Container:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox playerCreationContainer = new HBox();
        playerCreationContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9)");

        playerCreationContainer.alignmentProperty().set(Pos.CENTER);
        mainContainerV.getChildren().add(playerCreationContainer);


        GridPane playerGrid = createContainerGrid(new int[] {30, 70}, new int[] {50, 50});
        playerCreationContainer.getChildren().add(playerGrid);
        playerGrid.setGridLinesVisible(true);

        playerGrid.add(new HBox(new Label("Team 1")), 0, 0, 1, 1);
        playerGrid.add(new HBox(new Label("Team 2")), 0, 1, 1, 1);

        //TODO: Fix Code duplication!!!
        GridPane teamGrid_1 = createContainerGrid(new int[] {70, 30}, new int[] {50, 50});
        teamGrid_1.setGridLinesVisible(true);
        playerGrid.add(new HBox(teamGrid_1), 1, 0, 1, 1);

        teamGrid_1.add(new HBox(new TextField("Player Name")), 0, 0, 1, 1);
        teamGrid_1.add(new HBox(new TextField("Player Name")), 0, 1, 1, 1);
        teamGrid_1.add(new HBox(new CheckBox(), new Label("isAI")), 1, 0, 1, 1);
        teamGrid_1.add(new HBox(new CheckBox(), new Label("isAI")), 1, 1, 1, 1);


        GridPane teamGrid_2 = createContainerGrid(new int[] {70, 30}, new int[] {50, 50});
        teamGrid_2.setGridLinesVisible(true);
        playerGrid.add(new HBox(teamGrid_2), 1, 1, 1, 1);

        teamGrid_2.add(new HBox(new TextField("Player Name")), 0, 0, 1, 1);
        teamGrid_2.add(new HBox(new TextField("Player Name")), 0, 1, 1, 1);
        teamGrid_2.add(new HBox(new CheckBox(), new Label("isAI")), 1, 0, 1, 1);
        teamGrid_2.add(new HBox(new CheckBox(), new Label("isAI")), 1, 1, 1, 1);


        mainContainerV.getChildren().add(createSpacerNode(true));

        return overlayH;
    }

    /**
     * Creates Spacer Nodes (horizontal or vertical) for better Layout
     *
     * @param isVertical vertical Spacer Node or horizontal Node
     * @return Spacer Node
     */
    private Node createSpacerNode(boolean isVertical) {
        //https://stackoverflow.com/questions/40883858/how-to-evenly-distribute-elements-of-a-javafx-vbox

        Region spacer = new Region();
        if (isVertical) {
            VBox.setVgrow(spacer, Priority.ALWAYS);
        } else {
            HBox.setHgrow(spacer, Priority.ALWAYS);
        }

        return spacer;
    }

    @FXML
    private void handleBtnStartGame() {
        //TODO: Check if Player Parameters are valid


        //Initialize New Game
        //TODO: Initialize Game with actual Players
        this.game = new Game(new gui.JavaFXGUI(gameBoardFXML, playerHandsFXML, announcementLabelFXML));

        //TODO: DO SOMETHING MORE IN THE GUI DISABLE & ENABLE THINGS
    }
}