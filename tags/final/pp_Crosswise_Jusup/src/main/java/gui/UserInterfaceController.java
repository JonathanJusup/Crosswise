package gui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import logic.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;

import static gui.Utilities.logInConsole;

/**
 * Main class for the user interface. All UI Element except the root are
 * created and initialized here, instead of in the .fxml file.
 *
 * @author mjo, cei, Jonathan El Jusup (cgt104707)
 */
public class UserInterfaceController implements Initializable {

    /**
     * Constant BoardGridSize.
     *
     * <Experimental>
     * Can be increased here (e.g. 10, 100, 300).
     * Its recommended to set the CELL_GAP to 0
     * </Experimental>
     */
    private static final int BOARD_GRID_SIZE = 6;
    /**
     * Constant: HandSize.
     *
     * <Experimental>
     * Can be increased here (eg. 10, 50, 100)
     * If HAND_SIZE is bigger than MAX_HAND_SIZE_FIT, Hand becomes
     * scrollable.
     * </Experimental>
     */
    public static final int HAND_SIZE = 4;
    /**
     * <Experimental>
     * Constant: MaxHandSizeFit
     * Maximum HandSize, where there's no need for the playerHand to be scrollable.
     * </Experimental>
     */
    private static final int MAX_HAND_SIZE_FIT = 6;
    /**
     * Constant: Cell Gap (gameBoard and playerHands).
     */
    private static final int CELL_GAP = 5;
    /**
     * Constant: MAX_PLAYER_COUNT: Maximal number of players.
     */
    public static final int MAX_PLAYER_COUNT = 4;


    /**
     * Root VBox.
     */
    @FXML
    private VBox root;

    /**
     * mainContainer GridPane.
     */
    @FXML
    private GridPane mainContainerFXML;
    /**
     * gameBoard ImageView 2D Array.
     */
    @FXML
    private ImageView[][] gameBoardFXML;
    /**
     * playerHands ImageView 2D Array.
     */
    @FXML
    private ImageView[][] playerHandsFXML;
    /**
     * Universal Announcement Label.
     */
    @FXML
    private Label announcementLabelFXML;
    /**
     * gameBoard GridPane.
     */
    @FXML
    private GridPane gameBoardGridFXML;

    //Optional Point Bars

    /**
     * Optional pointBar container GridPane for horizontal team (Team 2)
     */
    @FXML
    private GridPane horizontalPointsGridFXML;
    /**
     * Optional pointBar container GridPane for vertical team (Team 1)
     */
    @FXML
    private GridPane verticalPointsGridFXML;
    /**
     * Optional pointLabel Array for horizontal team (Team 2)
     */
    @FXML
    private Label[] horizontalPointsFXML;
    /**
     * Optional pointLabel Array for vertical team (Team 1)
     */
    @FXML
    private Label[] verticalPointsFXML;


    //Containers, whose children may be modified and rearranged:::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * gameBoard container GridPane (2 x 2).
     * May also contain optional pointBars; if not gameBoard spans whole container.
     */
    @FXML
    private GridPane boardContainerFXML;
    /**
     * sideContainer VBox containing teamInformation,
     * usedWildcards and optional pointsTable
     */
    @FXML
    private VBox sideContainerFXML;

    //Team Information + optional Team Points:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * teamInformation GridPanes Array for both teams
     */
    @FXML
    private GridPane[] teamInformationGridFXML;
    /**
     * team playerNames Labels Array for both teams
     */
    @FXML
    private Label[] teamPlayerNamesLabelFXML;
    /**
     * teamCaption Containers Array for both teams
     */
    @FXML
    private HBox[] teamCaptionFXML;
    /**
     * Optional teamPoints Array for both teams
     */
    @FXML
    private HBox[] teamPointsFXML;
    /**
     * usedWildcards Labels Array
     */
    @FXML
    private Label[] usedWildcardsFXML;
    /**
     * Optional PointsTable GridPane
     */
    @FXML
    private GridPane pointTableFXML;

    //PlayerHands:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * playerHand Containers.
     * <p>
     * HBox for horizontal playerHand (vertical team)
     * VBox for vertical playerHand (horizontal Team)
     */
    @FXML
    private Node[] playerHandContainerFXML;
    /**
     * playerHand GridPanes Array
     */
    @FXML
    private GridPane[] playerHandsGridFMXL;


    //New Game Player Creation::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * newGame Menu
     */
    @FXML
    private HBox newGameMenuFXML;
    /**
     * playerFormulars Array
     */
    @FXML
    private HBox[] playerFormularsFXML;
    /**
     * playerNames textFields Array
     */
    @FXML
    private TextField[] txtfld_playerNamesFXML;
    /**
     * checkBox isAI Array
     */
    @FXML
    private CheckBox[] chkBx_isAiFXML;
    /**
     * playerCreationLabel (Warning)
     */
    @FXML
    private Label playerCreationLabelFXML;

    //Menu Items::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * saveGame MenuItem. Stored as variable,
     * so it can be enabled and disabled.
     */
    @FXML
    private MenuItem saveGameFXML;

    //AI & Animation related::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * showIsAI CheckMenuItem.
     * To toggle, if the game shows or hides AI playerHand.
     */
    @FXML
    private CheckMenuItem showIsAiFXML;
    /**
     * animationDuration RadioMenuItems.
     * To switch animation Duration / Speed of AI Turns
     */
    @FXML
    private RadioMenuItem[] animationDurationFXML;
    /**
     * Independent animationLayer Pane to place temporary animated Nodes on
     * This Pane is in front of mainContainer, but behind newGameMenu, so the animation.
     * It cannot be interacted with and is fully transparent / invisible.
     */
    @FXML
    private Pane animationLayerFXML;

    //Internal variables::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * game Instance. May only be used for saving the game or
     * allocating onDragDroppedHandlers (-> triggering playerTurn)
     */
    private Game game;
    /**
     * gui Instance. The only purpose is to close any Popups,
     * after another game is started or loaded
     */
    private JavaFXGUI gui;
    /**
     * Number of active players. May change, when starting / loading a game.
     * Initially 4 active players assigned
     */
    private int activePlayers = 4;

    //Methods:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Initializes Main Application Window with all of its components.
     * Parameters are not used. This is the single biggest Method overall
     * in class, which uses all other methods in class, which are declared
     * below.
     *
     * @param location  not used
     * @param resources not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utilities.initializeImages();

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
        GridPane globalContainer = createContainerGrid(new int[]{0, 0}, new int[]{15, 0});
        globalContainer.getStyleClass().add("global-container");

        alignmentContainerH.getChildren().add(stackPane);
        stackPane.getChildren().add(globalContainer);

        //Bindings
        globalContainer.getColumnConstraints().get(0).prefWidthProperty().bind(alignmentContainerH.heightProperty());
        globalContainer.getColumnConstraints().get(1).setMinWidth(400);
        globalContainer.prefHeightProperty().bind(root.heightProperty());
        globalContainer.getRowConstraints().get(0).setMinHeight(100);

        //Global Announcement Label:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox labelContainer = new HBox();
        labelContainer.getStyleClass().add("announcement-label-container");

        globalContainer.add(labelContainer, 0, 0, 1, 1);

        labelContainer.setAlignment(Pos.CENTER);

        Label announcementLabel = new Label("CROSSWISE");
        announcementLabel.setWrapText(true);
        announcementLabel.getStyleClass().add("announcement-label");

        announcementLabelFXML = announcementLabel;
        labelContainer.getChildren().add(announcementLabel);

        //Square Box Container for Main Container:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox alignmentMainContainerH = new HBox();
        VBox alignmentMainContainerV = new VBox();
        alignmentMainContainerH.getStyleClass().add("main-container-alignmentH");
        alignmentMainContainerV.getStyleClass().add("main-container-alignmentV");

        alignmentMainContainerH.getChildren().add(alignmentMainContainerV);
        globalContainer.add(alignmentMainContainerH, 0, 1, 1, 1);

        //Bindings
        alignmentMainContainerV.prefWidthProperty().bind(alignmentMainContainerH.heightProperty());
        alignmentMainContainerV.prefHeightProperty().bind(alignmentMainContainerH.widthProperty());
        alignmentMainContainerH.alignmentProperty().set(Pos.CENTER);
        alignmentMainContainerV.alignmentProperty().set(Pos.TOP_CENTER);


        //Main Container Grid:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        GridPane mainContainer = createContainerGrid(new int[]{15, 70, 15}, new int[]{15, 70, 15});
        mainContainerFXML = mainContainer;
        mainContainer.getStyleClass().add("main-container");

        alignmentMainContainerV.getChildren().add(mainContainer);

        //Bindings
        mainContainer.prefWidthProperty().bind(alignmentMainContainerV.widthProperty());
        mainContainer.prefHeightProperty().bind(alignmentMainContainerV.widthProperty());


        //BoardContainer Grid:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        GridPane boardContainer = createContainerGrid(new int[]{90, 10}, new int[]{90, 10});
        boardContainerFXML = boardContainer;
        boardContainer.getStyleClass().add("board-container");

        mainContainer.add(boardContainer, 1, 1, 1, 1);


        //Game Board Grid:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        GridPane gameBoard = createGameGrid();
        gameBoardGridFXML = gameBoard;
        gameBoard.getStyleClass().add("game-board");

        //Initialize Grid with game tiles
        gameBoardFXML = initGameBoardImages(gameBoard);
        boardContainer.add(gameBoard, 0, 0, 1, 1);


        //Player Hand Grids:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        playerHandsFXML = new ImageView[MAX_PLAYER_COUNT][];
        GridPane[] playerHands = createPlayerHands();
        playerHandsGridFMXL = playerHands;

        //Init every playerHand with initial Images
        for (int i = 0; i < playerHands.length; i++) {
            playerHands[i].getStyleClass().add("player-hands");
            initHand(playerHands[i], i);
        }

        //Hand Containers which can either be cast to VBox or HBox
        Node[] handContainer = new Node[playerHands.length];
        for (int i = 0; i < handContainer.length; i++) {
            handContainer[i] = (i % 2 == 0) ? new HBox() : new VBox();

            if (i % 2 == 0) {
                //Vertical Team Hands
                ((HBox) handContainer[i]).alignmentProperty().set(Pos.CENTER);
                ((HBox) handContainer[i]).getChildren().add(playerHands[i]);
                handContainer[i].getStyleClass().add("playerHand-container-V");
            } else {
                //Horizontal Team Hands
                ((VBox) handContainer[i]).alignmentProperty().set(Pos.CENTER);
                ((VBox) handContainer[i]).getChildren().add(playerHands[i]);
                handContainer[i].getStyleClass().add("playerHand-container-H");
            }
        }

        //Experimental ScrollPanes for hansSizes over 5
        ScrollPane[] scrollPanes = new ScrollPane[handContainer.length];
        if (HAND_SIZE > 5) {
            for (int i = 0; i < handContainer.length; i++) {
                if (i % 2 == 0) {
                    scrollPanes[i] = new ScrollPane(handContainer[i]);
                    scrollPanes[i].setFitToHeight(true);
                } else {
                    scrollPanes[i] = new ScrollPane(handContainer[i]);
                    scrollPanes[i].setFitToWidth(true);
                }
            }

            //Additionally wrap handContainers in scrollPanes and then to mainContainer
            mainContainer.add(scrollPanes[0], 1, 0, 1, 1);
            mainContainer.add(scrollPanes[1], 2, 1, 1, 1);
            mainContainer.add(scrollPanes[2], 1, 2, 1, 1);
            mainContainer.add(scrollPanes[3], 0, 1, 1, 1);
        } else {
            //Standard case

            //Add Player Hands to mainContainer. May change, when new game started
            mainContainer.add(handContainer[0], 1, 0, 1, 1);    //Top
            mainContainer.add(handContainer[1], 2, 1, 1, 1);    //Right
            mainContainer.add(handContainer[2], 1, 2, 1, 1);    //Bottom
            mainContainer.add(handContainer[3], 0, 1, 1, 1);    //Left
        }

        playerHandContainerFXML = new Node[4];
        System.arraycopy(handContainer, 0, playerHandContainerFXML, 0, playerHandContainerFXML.length);


        //Optional Panels:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        //Point Grids (Horizontal & Vertical)
        GridPane horizontalPointsGrid = pointGridHorizontal();
        GridPane verticalPointsGrid = pointGridVertical();
        horizontalPointsGridFXML = horizontalPointsGrid;
        verticalPointsGridFXML = verticalPointsGrid;
        horizontalPointsGrid.getStyleClass().add("point-grid-H");
        verticalPointsGrid.getStyleClass().add("point-grid-V");

        //Bindings
        horizontalPointsGrid.prefHeightProperty().bind(gameBoard.heightProperty());
        verticalPointsGrid.prefWidthProperty().bind(gameBoard.widthProperty());
        horizontalPointsGrid.setPadding(new Insets(5, 5, 5, 5));
        verticalPointsGrid.setPadding(new Insets(5, 5, 5, 5));

        //Add pointGrids to boardContainer
        boardContainer.add(horizontalPointsGrid, 1, 0, 1, 1);
        boardContainer.add(verticalPointsGrid, 0, 1, 1, 1);

        //Remove and let gameBoard span through whole boardContainer
        boardContainer.getChildren().clear();
        boardContainer.add(gameBoard, 0, 0, 2, 2);


        //Team Information (optional Team Points), Point Table, Used Wildcards::::::::::::::::::::::::::::::::::::::::::
        VBox sideContainer = new VBox();
        sideContainer.getStyleClass().add("side-container");
        sideContainerFXML = sideContainer;

        //Team Information + optional Team Points
        teamInformationGridFXML = new GridPane[2];
        teamCaptionFXML = new HBox[2];
        teamPointsFXML = new HBox[2];
        teamPlayerNamesLabelFXML = new Label[4];

        for (int i = 0; i < teamPlayerNamesLabelFXML.length; i++) {
            teamPlayerNamesLabelFXML[i] = new Label("...");
        }

        //Team Information Grid (Team 1) (Team vertical)
        teamInformationGridFXML[0] = createTeamInformationGrid(0);
        teamInformationGridFXML[0].setMinHeight(100);
        sideContainer.getChildren().add(teamInformationGridFXML[0]);

        //Team Information Grid (Team 2) (Team horizontal)
        teamInformationGridFXML[1] = createTeamInformationGrid(1);
        teamInformationGridFXML[1].setMinHeight(100);
        sideContainer.getChildren().add(teamInformationGridFXML[1]);


        //Used Wildcards
        GridPane usedWildcards = createUsedWildcardsTable();
        usedWildcards.setMinHeight(180);
        usedWildcards.getStyleClass().add("wildcard-table");
        sideContainer.getChildren().add(usedWildcards);


        //Point Table
        GridPane pointTable = createPointTable();
        pointTableFXML = pointTable;
        pointTable.getStyleClass().add("point-table");

        sideContainer.getChildren().add(pointTable);
        globalContainer.add(sideContainer, 1, 0, 1, 2);

        //Bind 2. Column of Global Container to mainContainer so it grows with it
        globalContainer.getColumnConstraints().get(1).prefWidthProperty().bind(
                mainContainer.widthProperty().divide(2)
        );


        //Overlay for new Game::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox newGameMenu = createNewGameMenu(globalContainer.widthProperty());
        newGameMenuFXML = newGameMenu;
        stackPane.getChildren().add(newGameMenu);

        //Dev Note: Comment this out to show newGameMenu at start
        newGameMenu.toBack();

        //Transparent Animation Pane::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        Pane animationPane = new Pane();
        animationLayerFXML = animationPane;

        animationPane.setMouseTransparent(true);
        animationPane.setPickOnBounds(true);
        animationPane.toFront();

        stackPane.getChildren().add(animationPane);
    }

    /**
     * Creates Menu Bar with 2 Main Menus <Game>, <View>, <AI Options>.
     *
     * <Game> Menu has MenuItems <New Game>, <Save>, <Load>, <Quit>.
     * <View> Menu has CheckMenuItems <Show Team Points>, <Show Point Bars>, <Show Point Table>.
     * <AI Options> Menu has MenuItem <Skip Animation>, <Show AI Hand> and RadioMenuItems for
     * animation duration.
     *
     * @return MenuBar
     */
    private MenuBar createMenuBar() {
        //Overall Menu Bar::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        MenuBar menuBar = new MenuBar();
        Menu game = new Menu("Game");
        Menu view = new Menu("View");
        Menu optionsAI = new Menu("AI Options");

        menuBar.getMenus().addAll(game, view, optionsAI);

        //Game Menu Items:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(this::handleMI_newGame);

        MenuItem saveGame = new MenuItem("Save");
        saveGameFXML = saveGame;
        saveGameFXML.setDisable(true);  //Save Game initially disabled

        MenuItem loadGame = new MenuItem("Load");
        loadGame.setOnAction(this::handleMI_loadGame);

        MenuItem saveAndQuit = new MenuItem("Quit");
        saveAndQuit.setOnAction(this::handleMI_Quit);

        game.getItems().addAll(newGame, saveGame, loadGame, saveAndQuit);

        //View Menu toggleable CheckMenu Items::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        CheckMenuItem teamPoints = new CheckMenuItem("Show Team Points");
        teamPoints.setOnAction(event -> handleCheckMI_toggleTeamPoints(event, teamPoints));
        teamPoints.setSelected(true);   //Initially hidden

        CheckMenuItem pointBars = new CheckMenuItem("Show Point Bars");
        pointBars.setOnAction(event -> handleCheckMI_togglePointBars(event, pointBars));
        pointBars.setSelected(false);   //Initially hidden

        CheckMenuItem pointTable = new CheckMenuItem("Show Point Table");
        pointTable.setOnAction(event -> handleCheckMI_togglePointTable(event, pointTable));
        pointTable.setSelected(true);   //Initially shown

        view.getItems().addAll(teamPoints, pointBars, pointTable);

        //AI Options Menu:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        MenuItem skipAnimation = new MenuItem("Skip Animation");
        skipAnimation.setOnAction(this::handleMenu_skipAnimation);

        SeparatorMenuItem separator = new SeparatorMenuItem();

        CheckMenuItem AIHand = new CheckMenuItem("Show AI Hand");
        showIsAiFXML = AIHand;

        //SubMenu of AI Turn Durations (Short, Medium, Long)
        Menu durationAITurn = new Menu("AI Turn Duration");

        RadioMenuItem duration_short = new RadioMenuItem("Short");
        duration_short.setOnAction(event -> handleCheckMI_duration(event, duration_short));
        duration_short.setSelected(true);

        RadioMenuItem duration_medium = new RadioMenuItem("Medium");
        duration_medium.setOnAction(event -> handleCheckMI_duration(event, duration_medium));

        RadioMenuItem duration_long = new RadioMenuItem("Long");
        duration_long.setOnAction(event -> handleCheckMI_duration(event, duration_long));


        animationDurationFXML = new RadioMenuItem[]{duration_short, duration_medium, duration_long};
        durationAITurn.getItems().addAll(duration_short, duration_medium, duration_long);
        optionsAI.getItems().addAll(skipAnimation, separator, AIHand, durationAITurn);

        return menuBar;
    }

    /**
     * Creates universal GridContainer with custom ColumnConstraints and RowConstraints
     * If percentage value is 0, width / height of column / row is set to some arbitrary
     * small value.
     * <p>
     * This method can be used anywhere, when a custom GridPane has to be created. For
     * this reason this method is package private and static for it to be used e.g. in
     * the PopupController.
     *
     * @param colLayout Integer Array with percentage width for each Column, if 0
     *                  Column gets initialized with minWidth value
     * @param rowLayout Integer Array with percentage height for each Row, if 0
     *                  Row gets initialized with minHeight value
     * @return gridPane with custom Column- & Row-Constraints
     */
    static GridPane createContainerGrid(int[] colLayout, int[] rowLayout) {
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
     * Creates square n x n Game GridPane with a gap between each column and row.
     * n equals constant BOARD_GRID_SIZE. This method allows to create gameBoard
     * GridPanes of various sizes, as long the BOARD_GRID_SIZE is higher than 0.
     *
     * @return n x n GridPane
     */
    private GridPane createGameGrid() {
        GridPane gridPane = new GridPane();
        for (int x = 0; x < BOARD_GRID_SIZE; x++) {
            //Columns
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(2);
            gridPane.getColumnConstraints().add(col);

            //Rows
            RowConstraints row = new RowConstraints();
            row.setMinHeight(2);
            gridPane.getRowConstraints().add(row);
        }

        //Set Gap between Rows and Columns + Center Alignment
        gridPane.setHgap(CELL_GAP);
        gridPane.setVgap(CELL_GAP);
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    /**
     * Creates optional horizontal pointGrid Bar for the
     * horizontal team. GridSize matches the boardSize.
     *
     * @return horizontal pointsBar GridPane
     */
    private GridPane pointGridHorizontal() {
        horizontalPointsFXML = new Label[BOARD_GRID_SIZE];

        GridPane gridPane = new GridPane();
        for (int i = 0; i < BOARD_GRID_SIZE; i++) {
            RowConstraints row = new RowConstraints();
            gridPane.getRowConstraints().add(row);

            row.setMinHeight(2);
            row.setVgrow(Priority.ALWAYS);
        }

        ColumnConstraints col = new ColumnConstraints();
        gridPane.getColumnConstraints().add(col);

        col.setMinWidth(2);
        col.setHgrow(Priority.ALWAYS);
        gridPane.setHgap(CELL_GAP);
        gridPane.setVgap(CELL_GAP);
        gridPane.setAlignment(Pos.CENTER);

        //Point Labels
        for (int i = 0; i < BOARD_GRID_SIZE; i++) {
            Label label = new Label("0");
            horizontalPointsFXML[i] = label;

            label.setFont(Font.font("Calibri", 20));
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            gridPane.add(label, 0, i, 1, 1);
        }

        return gridPane;
    }

    /**
     * Creates optional vertical pointGrid Bar for the
     * vertical team. GridSize matches the boardSize.
     *
     * @return vertical pointsBar GridPane
     */
    private GridPane pointGridVertical() {
        verticalPointsFXML = new Label[BOARD_GRID_SIZE];

        GridPane gridPane = new GridPane();
        for (int i = 0; i < BOARD_GRID_SIZE; i++) {
            ColumnConstraints col = new ColumnConstraints();
            gridPane.getColumnConstraints().add(col);

            col.setMinWidth(2);
            col.setHgrow(Priority.ALWAYS);
        }

        RowConstraints row = new RowConstraints();
        gridPane.getRowConstraints().add(row);

        row.setMinHeight(2);
        row.setVgrow(Priority.ALWAYS);
        gridPane.setHgap(CELL_GAP);
        gridPane.setVgap(CELL_GAP);
        gridPane.setAlignment(Pos.CENTER);

        //Point Labels
        for (int i = 0; i < BOARD_GRID_SIZE; i++) {
            Label label = new Label("0");
            verticalPointsFXML[i] = label;

            label.setFont(Font.font("Calibri", 20));
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            gridPane.add(label, i, 0, 1, 1);
        }

        return gridPane;
    }

    /**
     * Creates GridPane Array of (4) PlayerHands. PlayerHands of team vertical
     * are wrapped in horizontal GridPanes, PlayerHands of team horizontal are
     * wrapped in vertical GridPanes.
     *
     * @return GridPane Array of PlayerHands
     */
    private GridPane[] createPlayerHands() {
        int playerCount = 4;

        GridPane[] playerHands = new GridPane[playerCount];

        for (int i = 0; i < activePlayers; i++) {
            if (i % 2 == 0) {

                //Horizontal Hands (Vertical Team)
                playerHands[i] = new GridPane();

                //HAND_SIZE times Columns
                for (int j = 0; j < HAND_SIZE; j++) {
                    ColumnConstraints col = new ColumnConstraints();
                    col.setMinWidth(10);
                    playerHands[i].getColumnConstraints().add(col);
                }

                //1 Row
                RowConstraints row = new RowConstraints();
                row.setMinHeight(10);
                playerHands[i].getRowConstraints().add(row);

                //Gap & Bindings
                playerHands[i].setHgap(CELL_GAP);
                playerHands[i].prefWidthProperty().bind(
                        playerHands[i].heightProperty().multiply(HAND_SIZE).add(CELL_GAP));
            } else {

                //Vertical Hands (Horizontal Team)
                playerHands[i] = new GridPane();

                //HAND_SIZE times Rows
                for (int j = 0; j < HAND_SIZE; j++) {
                    RowConstraints row = new RowConstraints();
                    row.setMinHeight(10);
                    playerHands[i].getRowConstraints().add(row);
                }

                //1 Column
                ColumnConstraints col = new ColumnConstraints();
                col.setMinWidth(10);
                playerHands[i].getColumnConstraints().add(col);

                //Gap & Bindings
                playerHands[i].setVgap(CELL_GAP);
                playerHands[i].prefHeightProperty().bind(
                        playerHands[i].widthProperty().multiply(HAND_SIZE).add(CELL_GAP));
            }
        }

        return playerHands;
    }

    /**
     * Gets Grid of player hand.
     * <p>
     * <Design Decision> Initializes ImageViews with random GameTile Images.
     * This is not a functional method. Its sole purpose is to fill the playerHands,
     * so they don't look empty, when the user starts the application.
     *
     * @param grdPn     player hand gridPane
     * @param playerIdx player Index of playerHand
     */
    private void initHand(GridPane grdPn, int playerIdx) {
        assert grdPn.getColumnCount() == HAND_SIZE || grdPn.getRowCount() == HAND_SIZE;

        int colCount = grdPn.getColumnCount();
        int rowCount = grdPn.getRowCount();
        int cellWidth = (int) grdPn.getWidth() / colCount;
        int cellHeight = (int) grdPn.getHeight() / rowCount;

        ImageView[] hand = new ImageView[HAND_SIZE];
        playerHandsFXML[playerIdx] = hand;

        for (int i = 0; i < hand.length; i++) {
            hand[i] = new ImageView();

            hand[i].setFitWidth(cellWidth);
            hand[i].setFitHeight(cellHeight);
            hand[i].setPreserveRatio(false);
            hand[i].setSmooth(true);

            hand[i].setImage(Utilities.getRandomImage());

            //ImageView resizes when window resizes
            hand[i].fitWidthProperty().bind(
                    grdPn.widthProperty().divide(colCount).subtract(colCount > rowCount ? CELL_GAP : 0));
            hand[i].fitHeightProperty().bind(
                    grdPn.heightProperty().divide(rowCount).subtract(colCount > rowCount ? 0 : CELL_GAP));

            grdPn.add(hand[i], (colCount > rowCount ? i : 0), (colCount > rowCount ? 0 : i), 1, 1);
        }

    }

    /**
     * Creates Team Information Grid for one team at a time.
     * Contains the team name Caption, their optional teamPoints Label and
     * all playerNames of their respective teams.
     *
     * @param teamIdx team Index (0 -> Team Vertical | 1 -> Team Horizontal)
     * @return Team Information Grid
     */
    private GridPane createTeamInformationGrid(int teamIdx) {
        GridPane teamGrid = createContainerGrid(new int[]{50, 50}, new int[]{0, 0});
        teamGrid.getStyleClass().add("team-information");

        //Team Name Caption
        teamCaptionFXML[teamIdx] = new HBox(new Label(teamIdx == 0 ? "VERTICAL" : "HORIZONTAL"));
        teamCaptionFXML[teamIdx].getStyleClass().add("team-caption-" + (teamIdx == 0 ? "V" : "H"));
        teamGrid.add(teamCaptionFXML[teamIdx], 0, 0, 1, 1);

        //Team Points
        teamPointsFXML[teamIdx] = new HBox(new Label("0"));
        teamGrid.add(teamPointsFXML[teamIdx], 1, 0, 1, 1);

        //Player 1 Container
        HBox playerNameContainer1 = new HBox();
        playerNameContainer1.getStyleClass().add("player-name-container");
        playerNameContainer1.getChildren().add(teamPlayerNamesLabelFXML[teamIdx == 0 ? 0 : 1]);
        teamGrid.add(playerNameContainer1, 0, 1, 1, 1);

        //Player 2 Container
        HBox playerNameContainer2 = new HBox();
        playerNameContainer2.getStyleClass().add("player-name-container");
        playerNameContainer2.getChildren().add(teamPlayerNamesLabelFXML[teamIdx == 0 ? 2 : 3]);
        teamGrid.add(playerNameContainer2, 1, 1, 1, 1);

        return teamGrid;
    }

    /**
     * Create Used Wildcards Gridpane for showing, which wildcards are used up
     * to a certain amount.
     *
     * @return usedWildcards Table GridPane
     */
    private GridPane createUsedWildcardsTable() {
        GridPane gridContainer = createContainerGrid(new int[]{0}, new int[]{0, 0, 0});

        //1. Row (Header)
        Label usedWildcardsLabel = new Label("Used Wildcards");
        gridContainer.add(new HBox(usedWildcardsLabel), 0, 0, 1, 1);

        //2. Row (Wildcard ImageViews)
        GridPane imageGrid = createContainerGrid(new int[]{25, 25, 25, 25}, new int[]{0});
        imageGrid.prefWidthProperty().bind(gridContainer.widthProperty());
        gridContainer.add(imageGrid, 0, 1, 1, 1);

        //3. Row (X-out-of-Numbers)
        GridPane wildcardUsedGrid = createContainerGrid(new int[]{25, 25, 25, 25}, new int[]{0});
        wildcardUsedGrid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);

        gridContainer.add(wildcardUsedGrid, 0, 2, 1, 1);

        //Create and add Labels
        Label[] usedWildcards = new Label[wildcardUsedGrid.getColumnCount()];
        for (int i = 0; i < wildcardUsedGrid.getColumnCount(); i++) {
            usedWildcards[i] = new Label("0/3");
            wildcardUsedGrid.add(new HBox(usedWildcards[i]), i, 0, 1, 1);
        }
        usedWildcardsFXML = usedWildcards;

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        //1st and 3rd Row should grow based on 2nd Row dimensions
        DoubleBinding binding = imageGrid.prefWidthProperty().divide(10);
        gridContainer.getRowConstraints().get(0).prefHeightProperty().bind(binding);
        gridContainer.getRowConstraints().get(2).prefHeightProperty().bind(binding);

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        int colCount = imageGrid.getColumnCount();
        int rowCount = imageGrid.getRowCount();
        int cellWidth = (int) imageGrid.getWidth() / colCount;
        int cellHeight = (int) imageGrid.getHeight() / rowCount;

        //Wildcard Array to iterate through
        GameTiles[] wildcards = new GameTiles[]{
                GameTiles.WC_REMOVER, GameTiles.WC_MOVER, GameTiles.WC_SWAPONBOARD, GameTiles.WC_SWAPWITHHAND
        };

        //Initialize ImageViews
        for (int i = 0; i < colCount; i++) {
            ImageView imageView = new ImageView();

            //Image has to fit a cell and mustn't preserve ratio
            imageView.setFitWidth(cellWidth);
            imageView.setFitHeight(cellHeight);
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);

            //Image resizes, when window resizes; Based on lower Grid (wildcardsUsedGrid)
            imageView.fitWidthProperty().bind(wildcardUsedGrid.widthProperty().divide(colCount).subtract(CELL_GAP));
            imageView.fitHeightProperty().bind(wildcardUsedGrid.widthProperty().divide(colCount).subtract(CELL_GAP));

            imageGrid.add(new HBox(imageView), i, 0, 1, 1);
            imageView.setImage(Utilities.gameTilesImageMap.get(wildcards[i]));
        }

        return gridContainer;
    }

    /**
     * Creates optional PointTable Helper for showing corresponding Points
     * to given GameTile combinations.
     * <p>
     * All different = 6 Points
     * 2x same tiles = 1 Point
     * 3x same tiles = 3 Points
     * 4x same tiles = 5 Points
     * 5x same tiles = 7 Points
     * 6x same tiles = Integer.MAX_VALUE (Instant Win)
     *
     * @return pointTable GridPane
     */
    private GridPane createPointTable() {
        GridPane pointTable = createContainerGrid(new int[]{75, 25}, new int[]{0, 0, 0, 0, 0, 0, 0});

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
            combinations[i] = createContainerGrid(new int[]{0, 0, 0, 0, 0, 0}, new int[]{0});
            combinations[i].getStyleClass().add("combination-grid");

            gridContainerV.getChildren().add(combinations[i]);
            gridContainerV.alignmentProperty().set(Pos.CENTER);
            gridContainerV.prefWidthProperty().bind(gridContainerH.widthProperty());

            int colCount = combinations[i].getColumnCount();
            int rowCount = combinations[i].getRowCount();

            //Grid Layout Properties
            combinations[i].setHgap(CELL_GAP);
            combinations[i].setAlignment(Pos.CENTER);

            //Bindings
            combinations[i].prefWidthProperty().bind(gridContainerV.widthProperty());
            combinations[i].prefHeightProperty().bind(combinations[i].widthProperty().divide(combinations.length));


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
                imageView.fitWidthProperty().bind(combinations[i].widthProperty().divide(colCount).subtract(CELL_GAP));
                imageView.fitHeightProperty().bind(combinations[i].heightProperty().divide(rowCount).subtract(CELL_GAP));
            }
        }

        //Combination : <All GameTiles Different> : 6 Points
        setPointCombinationTilesImages(combinations[0], new GameTiles[]{
                GameTiles.T_SQUARE, GameTiles.T_CROSS, GameTiles.T_TRIANGLE,
                GameTiles.T_SUN, GameTiles.T_PENTAGON, GameTiles.T_STAR
        });

        //Combination : <2 same GameTiles> : 1 Points
        setPointCombinationTilesImages(combinations[1], new GameTiles[]{
                GameTiles.T_SQUARE, GameTiles.T_SQUARE
        });

        //Combination : <3 same GameTiles> : 3 Points
        setPointCombinationTilesImages(combinations[2], new GameTiles[]{
                GameTiles.T_SQUARE, GameTiles.T_SQUARE, GameTiles.T_SQUARE
        });

        //Combination : <4 same GameTiles> : 5 Points
        setPointCombinationTilesImages(combinations[3], new GameTiles[]{
                GameTiles.T_SQUARE, GameTiles.T_SQUARE, GameTiles.T_SQUARE,
                GameTiles.T_SQUARE
        });

        //Combination : <5 same GameTiles> : 7 Points
        setPointCombinationTilesImages(combinations[4], new GameTiles[]{
                GameTiles.T_SQUARE, GameTiles.T_SQUARE, GameTiles.T_SQUARE,
                GameTiles.T_SQUARE, GameTiles.T_SQUARE
        });

        //Combination : <6 same GameTiles> : Instant Win
        setPointCombinationTilesImages(combinations[5], new GameTiles[]{
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
     * Allocates given combination of GameTiles to specified Grid.
     * Images that corresponds to each gameTile in combination are
     * set on imageViews.
     *
     * @param grid        6 x 1 GridPane
     * @param combination GameTiles Array with set combination
     */
    private void setPointCombinationTilesImages(GridPane grid, GameTiles[] combination) {
        assert combination.length <= 6;

        for (int i = 0; i < 6; i++) {
            ImageView image = (ImageView) grid.getChildren().get(i);

            //Fill with given Combination, fill rest with EMPTY
            if (i < combination.length) {
                image.setImage(Utilities.gameTilesImageMap.get(combination[i]));
            } else {
                image.setImage(Utilities.gameTilesImageMap.get(GameTiles.EMPTY));
            }
        }
    }

    /**
     * Creates an array of imageViews corresponding to the gridPane.
     * Each imageView becomes a child of the gridPane and fills a cell.
     * For proper resizing it is bound to the gridPanes width and height.
     * <p>
     * <Design Decision> Initialize ImageViews with random GameTile Images.
     * This is not a functional method. Its sole purpose is to fill the
     * gameBoard, so it doesn't look empty, when the user starts the
     * application.
     *
     * @param grdPn gameBoard GridPane to fill
     * @return 2D Array of imageViews (with random Images) added to gridPane
     */
    private ImageView[][] initGameBoardImages(GridPane grdPn) {
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
                imageViews[x][y].getStyleClass().add("game-tiles");

                //Image has to fit a cell and mustn't preserve ratio
                imageViews[x][y].setFitWidth(cellWidth);
                imageViews[x][y].setFitHeight(cellHeight);
                imageViews[x][y].setPreserveRatio(false);
                imageViews[x][y].setSmooth(true);

                //Add the imageView to the cell
                grdPn.add(imageViews[x][y], x, y);

                //The image shall resize when the cell resizes
                imageViews[x][y].fitWidthProperty().bind(grdPn.widthProperty().divide(colCount).subtract(CELL_GAP));
                imageViews[x][y].fitHeightProperty().bind(grdPn.heightProperty().divide(rowCount).subtract(CELL_GAP));

                //Random assignment of Images to imageViews
                imageViews[x][y].setImage(Utilities.getRandomImage());
            }
        }
        return imageViews;
    }

    /**
     * Creates new Overlay for creating new Game. Added to existing StackPane Node.
     * Initially moved to Back. Contains Player Count Selection, Player Formulars
     * (playerName + isAI) and a startGame Button.
     *
     * @param referenceWidth globalContainer.widthProperty
     * @return newGame Menu Overlay
     */
    private HBox createNewGameMenu(ReadOnlyDoubleProperty referenceWidth) {
        final double maxWidth = 600;
        final double minHeight = 700;
        final double maxHeight = 750;


        //Overlay Containers::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox overlayH = new HBox();
        overlayH.getStyleClass().add("overlay");
        overlayH.alignmentProperty().set(Pos.CENTER);

        VBox overlayV = new VBox();
        overlayV.alignmentProperty().set(Pos.CENTER);
        overlayV.prefWidthProperty().bind(referenceWidth.divide(2));
        overlayV.setMaxWidth(maxWidth);
        overlayH.getChildren().add(overlayV);

        //Main Container::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("new-game-menu");
        overlayV.getChildren().add(borderPane);

        borderPane.prefWidthProperty().bind(overlayV.widthProperty());
        borderPane.prefHeightProperty().bind(overlayV.heightProperty().multiply(0.8));
        borderPane.setMinHeight(minHeight);
        borderPane.setMaxHeight(maxHeight);

        //Main Container Elements
        Button startGameBtn = new Button("START GAME");
        startGameBtn.setOnAction(event -> handleBtnStartGame());
        startGameBtn.setId("StartGameBtn");

        Label header = new Label("NEW GAME");
        header.getStyleClass().add("header");
        borderPane.setTop(new HBox(header));

        HBox buttonContainer = new HBox();
        buttonContainer.getStyleClass().add("button-container");
        buttonContainer.getChildren().add(startGameBtn);
        borderPane.setBottom(buttonContainer);


        //Main Content (PlayerCount Buttons + PlayerName TextFields)::::::::::::::::::::::::::::::::::::::::::::::::::::

        HBox mainContainerH = new HBox();
        VBox mainContainerV = new VBox();

        mainContainerH.getChildren().add(mainContainerV);

        mainContainerV.alignmentProperty().set(Pos.BASELINE_CENTER);
        borderPane.setCenter(mainContainerH);

        //Bindings
        mainContainerV.prefWidthProperty().bind(mainContainerH.widthProperty());


        //Player Count Selection Container::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        HBox playerCountContainerH = new HBox();
        playerCountContainerH.getStyleClass().add("player-count-container");

        CheckBox twoPlayers = new CheckBox();
        CheckBox fourPlayers = new CheckBox();
        fourPlayers.setSelected(true);  //Initially 4 Players

        //Add both button handlers
        twoPlayers.setOnAction(event -> handleCheckBox_2players(event, twoPlayers, fourPlayers, playerFormularsFXML));
        fourPlayers.setOnAction(event -> handleCheckBox_4players(event, twoPlayers, fourPlayers, playerFormularsFXML));

        //Add button elements
        playerCountContainerH.getChildren().addAll(
                createSpacerNode(false),
                new HBox(new Label("2 PLAYERS "), twoPlayers),
                createSpacerNode(false),
                new HBox(new Label("4 PLAYERS "), fourPlayers),
                createSpacerNode(false)
        );

        mainContainerV.getChildren().add(createSpacerNode(true));
        mainContainerV.getChildren().add(playerCountContainerH);
        mainContainerV.getChildren().add(createSpacerNode(true));

        //Player Creation Container:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        TextField[] playerNames = new TextField[MAX_PLAYER_COUNT];
        CheckBox[] isAI = new CheckBox[MAX_PLAYER_COUNT];
        HBox[] playerFormulars = new HBox[MAX_PLAYER_COUNT];

        txtfld_playerNamesFXML = playerNames;
        chkBx_isAiFXML = isAI;
        playerFormularsFXML = playerFormulars;

        //Team 1 Creation:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox playerCreationT1 = new HBox();
        playerCreationT1.getStyleClass().add("team-container");
        playerCreationT1.alignmentProperty().set(Pos.CENTER);
        mainContainerV.getChildren().add(playerCreationT1);

        GridPane playerCreationT1_grid = createContainerGrid(new int[]{0}, new int[]{0, 0, 0});
        playerCreationT1_grid.getColumnConstraints().get(0).prefWidthProperty().bind(
                mainContainerV.widthProperty().multiply(0.7)
        );

        playerCreationT1_grid.getStyleClass().add("new-player-grid");
        playerCreationT1.getChildren().add(playerCreationT1_grid);

        HBox labelContainerT1 = new HBox(new Label("// TEAM VERTICAL //"));
        labelContainerT1.getStyleClass().add("team-label-container-V");
        playerCreationT1_grid.add(labelContainerT1, 0, 0, 1, 1);
        playerCreationT1_grid.getRowConstraints().get(0).setMinHeight(40);

        //Player 1 Formular
        playerNames[0] = new TextField("PlayerName1");
        isAI[0] = new CheckBox();
        playerFormulars[0] = createPlayerCreationFormular(playerNames[0], isAI[0]);
        playerCreationT1_grid.add(playerFormulars[0], 0, 1, 1, 1);
        playerCreationT1_grid.getRowConstraints().get(1).setMinHeight(60);

        //Player 3 Formular
        playerNames[2] = new TextField("PlayerName3");
        isAI[2] = new CheckBox();
        playerFormulars[2] = createPlayerCreationFormular(playerNames[2], isAI[2]);
        playerCreationT1_grid.add(playerFormulars[2], 0, 2, 1, 1);
        playerCreationT1_grid.getRowConstraints().get(2).setMinHeight(60);


        //Team 2 Creation:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        HBox playerCreationT2 = new HBox();
        playerCreationT2.getStyleClass().add("team-container");
        playerCreationT2.alignmentProperty().set(Pos.CENTER);
        mainContainerV.getChildren().add(playerCreationT2);

        GridPane playerCreationT2_grid = createContainerGrid(new int[]{0}, new int[]{0, 0, 0});
        playerCreationT2_grid.getColumnConstraints().get(0).prefWidthProperty().bind(
                mainContainerV.widthProperty().multiply(0.7)
        );

        playerCreationT2_grid.getStyleClass().add("new-player-grid");
        playerCreationT2.getChildren().add(playerCreationT2_grid);

        HBox labelContainerT2 = new HBox(new Label("// TEAM HORIZONTAL //"));
        labelContainerT2.getStyleClass().add("team-label-container-H");
        playerCreationT2_grid.add(labelContainerT2, 0, 0, 1, 1);
        playerCreationT2_grid.getRowConstraints().get(0).setMinHeight(40);

        //Player 2 Formular
        playerNames[1] = new TextField("PlayerName2");
        isAI[1] = new CheckBox();
        playerFormulars[1] = createPlayerCreationFormular(playerNames[1], isAI[1]);
        playerCreationT2_grid.add(playerFormulars[1], 0, 1, 1, 1);
        playerCreationT2_grid.getRowConstraints().get(1).setMinHeight(60);

        //Player 4 Formular
        playerNames[3] = new TextField("PlayerName4");
        isAI[3] = new CheckBox();
        playerFormulars[3] = createPlayerCreationFormular(playerNames[3], isAI[3]);
        playerCreationT2_grid.add(playerFormulars[3], 0, 2, 1, 1);
        playerCreationT2_grid.getRowConstraints().get(2).setMinHeight(60);


        //Player Creation Label:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        Label playerCreationLbl = new Label();
        playerCreationLabelFXML = playerCreationLbl;
        playerCreationLbl.getStyleClass().add("player-creation-label");
        mainContainerV.getChildren().add(playerCreationLbl);

        mainContainerV.getChildren().add(createSpacerNode(true));

        return overlayH;
    }

    /**
     * Creates single Player Creation HBox Bundle containing TextField,
     * Label, Checkbox.
     *
     * @param textField playerName textFiled
     * @param checkBox  isAI checkBox
     * @return PlayerCreation HBox Bundle
     */
    private HBox createPlayerCreationFormular(TextField textField, CheckBox checkBox) {
        HBox creationContainerH = new HBox();

        HBox isAiContainerH = new HBox();
        Label isAI_lbl = new Label("AI ");

        isAiContainerH.getChildren().addAll(isAI_lbl, checkBox);
        creationContainerH.getChildren().addAll(
                textField,
                createSpacerNode(false),
                isAiContainerH,
                createSpacerNode(false)
        );

        return creationContainerH;
    }

    /**
     * Reallocates mainContainer based on activePlayers. Adding all playerHands
     * and the gameBoard Container.
     *
     * <EXPERIMENTAL>:
     * It is possible to have a HANDSIZE bigger than 4. When the HANDSIZE reaches
     * MAX_HAND_SIZE_FIT, there's not enough space anymore for all Images to be
     * square. Therefore, all playerHands are getting wrapped in ScrollPanes with
     * their respective scroll Direction.
     *
     * @param activePlayers number of active Players
     */
    private void initMainContainer(int activePlayers) {
        mainContainerFXML.getChildren().clear();

        if (HAND_SIZE > MAX_HAND_SIZE_FIT) {
            if (activePlayers == 4) {
                mainContainerFXML.add(boardContainerFXML, 1, 1, 1, 1);

                mainContainerFXML.add(wrapInScrollPane(playerHandContainerFXML[0], true),
                        1, 0, 1, 1);  //Top
                mainContainerFXML.add(wrapInScrollPane(playerHandContainerFXML[1], false),
                        2, 1, 1, 1);  //Right
                mainContainerFXML.add(wrapInScrollPane(playerHandContainerFXML[2], true),
                        1, 2, 1, 1);  //Bottom
                mainContainerFXML.add(wrapInScrollPane(playerHandContainerFXML[3], false),
                        0, 1, 1, 1);  //Left
            } else {
                mainContainerFXML.add(boardContainerFXML, 1, 1, 2, 2);

                mainContainerFXML.add(wrapInScrollPane(playerHandContainerFXML[0], true),
                        1, 0, 2, 1);  //Top
                mainContainerFXML.add(wrapInScrollPane(playerHandContainerFXML[1], false),
                        0, 1, 1, 2);  //Left
            }
        } else {
            if (activePlayers == 4) {
                //Add gameBoard Container
                mainContainerFXML.add(boardContainerFXML, 1, 1, 1, 1);

                //Add PlayerHands
                mainContainerFXML.add(
                        playerHandContainerFXML[0], 1, 0, 1, 1);  //Top
                mainContainerFXML.add(
                        playerHandContainerFXML[1], 2, 1, 1, 1);  //Right
                mainContainerFXML.add(
                        playerHandContainerFXML[2], 1, 2, 1, 1);  //Bottom
                mainContainerFXML.add(
                        playerHandContainerFXML[3], 0, 1, 1, 1);  //Left
            } else {
                //Add gameBoard Container (Spans over 2 Columns & Rows)
                mainContainerFXML.add(boardContainerFXML, 1, 1, 2, 2);

                //Add PlayerHands
                mainContainerFXML.add(
                        playerHandContainerFXML[0], 1, 0, 2, 1);  //Top
                mainContainerFXML.add(
                        playerHandContainerFXML[1], 0, 1, 1, 2);  //Left
            }
        }
    }

    /**
     * Wraps a given Node into a ScrollPane. Locks ScrollDirection based on
     * fitToHeight Parameter.
     *
     * @param node        Node to wrap in a ScrollPane
     * @param fitToHeight Lock to only scroll horizontally
     * @return ScrollPane with Node
     */
    private ScrollPane wrapInScrollPane(Node node, boolean fitToHeight) {
        ScrollPane scrollPane = new ScrollPane(node);
        scrollPane.setFitToHeight(fitToHeight);
        scrollPane.setFitToWidth(!fitToHeight);

        return scrollPane;
    }

    /**
     * Creates Spacer Nodes (horizontal or vertical) for better Layout.
     *
     * @param isVertical vertical Spacer Node or horizontal Node
     * @return Spacer Node
     */
    private Node createSpacerNode(boolean isVertical) {
        Region spacer = new Region();
        if (isVertical) {
            VBox.setVgrow(spacer, Priority.ALWAYS);
        } else {
            HBox.setHgrow(spacer, Priority.ALWAYS);
        }

        return spacer;
    }


    //Interaction Handlers::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Toggles PointBar Helpers to show or hide on GUI.
     * If PointBar Helpers are hidden, the gameBoard fills
     * the whole space of its parent Container.
     *
     * @param event         ActionEvent (Click)
     * @param checkMenuItem checkMenuItem to readFrom
     */
    @FXML
    private void handleCheckMI_togglePointBars(ActionEvent event, CheckMenuItem checkMenuItem) {
        boardContainerFXML.getChildren().clear();

        if (checkMenuItem.isSelected()) {
            boardContainerFXML.add(gameBoardGridFXML, 0, 0, 1, 1);
            boardContainerFXML.add(horizontalPointsGridFXML, 1, 0, 1, 1);
            boardContainerFXML.add(verticalPointsGridFXML, 0, 1, 1, 1);
        } else {
            boardContainerFXML.add(gameBoardGridFXML, 0, 0, 2, 2);
        }

        event.consume();
    }

    /**
     * Toggles TeamPoints Helpers in Team Information GridPane (top right corner of UI).
     * If hidden. TeamName Captions fill the horizontal space.
     *
     * @param event         ActionEvent (Click)
     * @param checkMenuItem checkMenuItem to read from
     */
    @FXML
    private void handleCheckMI_toggleTeamPoints(ActionEvent event, CheckMenuItem checkMenuItem) {
        for (int i = 0; i < 2; i++) {
            teamInformationGridFXML[i].getChildren().removeAll(teamCaptionFXML[i], teamPointsFXML[i]);

            if (checkMenuItem.isSelected()) {
                teamInformationGridFXML[i].add(teamCaptionFXML[i], 0, 0, 1, 1);
                teamInformationGridFXML[i].add(teamPointsFXML[i], 1, 0, 1, 1);
            } else {
                teamInformationGridFXML[i].add(teamCaptionFXML[i], 0, 0, 2, 1);
            }
        }

        event.consume();
    }

    /**
     * Toggles PointTable to show or hide on GUI SideContainer.
     *
     * @param event         ActionEvent (Click)
     * @param checkMenuItem checkMenuItem to read from
     */
    @FXML
    private void handleCheckMI_togglePointTable(ActionEvent event, CheckMenuItem checkMenuItem) {
        if (checkMenuItem.isSelected()) {
            sideContainerFXML.getChildren().add(pointTableFXML);
        } else {
            sideContainerFXML.getChildren().remove(pointTableFXML);
        }

        event.consume();
    }

    /**
     * AnimationDuration RadioMenuItem Handler. User can select 1 out of n
     * radioMenuItems, to change the AI Animation Duration. If User selects
     * one radioMenuItem, all other items become unselected. Same item can
     * be clicked multiple times
     *
     * @param event         ActionEvent (Click)
     * @param radioMenuItem radioMenuItems to select AnimationDuration
     */
    @FXML
    private void handleCheckMI_duration(ActionEvent event, RadioMenuItem radioMenuItem) {
        radioMenuItem.setSelected(true);

        if (radioMenuItem.isSelected()) {
            for (RadioMenuItem item : animationDurationFXML) {
                //Unselect all the other radioMenuItems
                if (!item.equals(radioMenuItem)) {
                    item.setSelected(false);
                }
            }
        }
        event.consume();
    }

    /**
     * Handles MenuItem for skipping the currently running animation, by stopping it.
     * Does not influence the pause, which is running parallel to the animation.
     *
     * @param event ActionEvent (Click)
     */
    @FXML
    private void handleMenu_skipAnimation(ActionEvent event) {
        if (this.gui != null) {
            gui.stopAnimation();
        }
        event.consume();
    }

    /**
     * Shows newGame Menu Overlay to create a new Game. Resets playerCreationLabel,
     * but keeps all previously entered inputs (playerCount, playerNames, isAI).
     *
     * @param event ActionEvent (Click)
     */
    @FXML
    private void handleMI_newGame(ActionEvent event) {
        playerCreationLabelFXML.setText("");
        newGameMenuFXML.toFront();
        event.consume();
    }

    /**
     * Saves the current game. Opens new FileChooser Instance for the user
     * to create a new Save File at a specified location. Saving can be cancelled,
     * then nothing happens. Logs Saving Errors.
     *
     * @param event ActionEvent (Click)
     */
    @FXML
    private void handleMI_save(ActionEvent event) {
        File currDir = null;

        //Get current Directory, if possible
        try {
            currDir = new File(JarMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            //Do nothing, no error
        }

        //Setup FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        if (currDir != null) {
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json"));


        //Choose File, then save
        File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());

        if (selectedFile != null) {
            GameFileManager.saveGame(game, selectedFile.getPath());
        } else {
            logInConsole("Saving cancelled", ErrorType.NONE);
        }

        event.consume();
    }

    /**
     * Loads a selected SaveFile and initializes game from it. Opens new FileChooser
     * Instance for the user to select an existing Save File. Loading can be cancelled,
     * then nothing happens. Logs Loading Errors.
     *
     * @param event ActionEvent (Click)
     */
    @FXML
    private void handleMI_loadGame(ActionEvent event) {
        File currDir = null;

        //Get current Directory, if possible
        try {
            currDir = new File(JarMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            //Do nothing, no error
        }

        //Setup FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        if (currDir != null) {
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json"));


        //Choose SaveFile, then try to load game
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (selectedFile != null) {
            GameData gameData = GameFileManager.loadGame(selectedFile.getPath());
            if (game != null) {
                game.forceGameStop();
                game.stopOldGame();
            }

            if (gameData != null) {
                if (this.gui != null) {
                    this.gui.closePopups();
                }

                this.gui = new JavaFXGUI(
                        gameBoardFXML, gameBoardGridFXML,
                        playerHandsFXML, playerHandsGridFMXL,
                        verticalPointsFXML, horizontalPointsFXML,
                        teamPointsFXML, usedWildcardsFXML,
                        announcementLabelFXML, animationLayerFXML,
                        showIsAiFXML, animationDurationFXML);

                //Try to instantiate game. At this point gameData can be invalid
                try {
                    this.game = new Game(gameData, gui);
                    activePlayers = this.game.getActivePlayers();
                    initGame();

                } catch (Exception e) {
                    String message = "Couldn't load save File!";
                    Utilities.logInConsole(message, ErrorType.INVALID_SAVE_FILE);
                    Utilities.createAlert(ErrorType.INVALID_SAVE_FILE, message);
                }

            } else {
                String message = "Couldn't load save File!";
                Utilities.logInConsole(message, ErrorType.INVALID_SAVE_FILE);
                Utilities.createAlert(ErrorType.INVALID_SAVE_FILE, message);
            }
        } else {
            String message = "Loading cancelled";
            Utilities.logInConsole(message, ErrorType.NONE);
        }

        newGameMenuFXML.toBack();
        event.consume();
    }

    /**
     * Quits the application.
     *
     * @param event ActionEvent (Click)
     */
    @FXML
    private void handleMI_Quit(ActionEvent event) {
        event.consume();
        System.exit(0);
    }


    //New Game Menu Handlers::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Handles checkBox for 2 players in newGame Menu. If clicked, itself gets
     * selected, checkBox for 4 players get unselected. Disables 1 playerName
     * creation container per team. Can be clicked multiple times.
     *
     * @param event           ActionEvent (Click)
     * @param checkBox_2P     checkBox for 2 players
     * @param checkBox_4P     checkBox for 4 players
     * @param playerFormulars playerCreation Container Array
     */
    @FXML
    private void handleCheckBox_2players(ActionEvent event, CheckBox checkBox_2P, CheckBox checkBox_4P,
                                         HBox[] playerFormulars) {

        checkBox_2P.setSelected(true);
        checkBox_4P.setSelected(false);

        //Enable only 1 Player Formular per Team
        if (checkBox_2P.isSelected() && !playerFormulars[2].isDisabled() && !playerFormulars[3].isDisabled()) {
            playerFormulars[2].setDisable(true);
            playerFormulars[3].setDisable(true);
        }

        event.consume();
    }

    /**
     * Handles checkBox for 4 players in newGame Menu. If clicked, itself gets
     * selected, checkBox for 2 players get unselected. Enables all playerName
     * creation containers. Can be clicked multiple times.
     *
     * @param event           Action Event (Click)
     * @param checkBox_2P     checkBox for 2 players
     * @param checkBox_4P     checkBox for 4 players
     * @param playerFormulars playerCreation Container Array
     */
    @FXML
    private void handleCheckBox_4players(ActionEvent event, CheckBox checkBox_2P, CheckBox checkBox_4P,
                                         HBox[] playerFormulars) {

        checkBox_4P.setSelected(true);
        checkBox_2P.setSelected(false);

        //Enable 2 Player Formulars per Team
        if (checkBox_4P.isSelected() && playerFormulars[2].isDisabled() && playerFormulars[3].isDisabled()) {
            playerFormulars[2].setDisable(false);
            playerFormulars[3].setDisable(false);
        }

        event.consume();
    }

    /**
     * Handles gameStart by validating playerNames and blocking, if necessary.
     * If all playerNames are valid, all necessary components are allocated and
     * instantiated to create a new Game-Instance.
     */
    @FXML
    private void handleBtnStartGame() {
        if (game != null) {
            game.forceGameStop();
            game.stopOldGame();
        }

        activePlayers = (int) Arrays.stream(txtfld_playerNamesFXML)
                .filter(Predicate.not(TextField::isDisabled)).count();

        //Check, if names are valid
        boolean allNamesValid = true;
        for (TextField playerName : txtfld_playerNamesFXML) {
            if (!Utilities.isValidName(playerName.getText()) && !playerName.isDisabled()) {
                String message = "Name(s) invalid!";
                playerCreationLabelFXML.setText(message);
                logInConsole(message, ErrorType.INVALID_PLAYER_NAME);
                allNamesValid = false;
            }
        }

        //No duplicate names allowed
        if (allNamesValid) {
            //Add all playerNames to Set
            Set<String> nameSet = new HashSet<>();
            for (TextField playerName : txtfld_playerNamesFXML) {
                if (!playerName.isDisabled()) {
                    nameSet.add(playerName.getText());
                }
            }

            //Check, if duplicates are present
            if (nameSet.size() != activePlayers) {
                String message = "No duplicate Names allowed!";
                playerCreationLabelFXML.setText(message);
                logInConsole(message, ErrorType.INVALID_PLAYER_NAME);
                allNamesValid = false;
            }
        }

        //PLayer Name Validation done, prepare for instantiating new Game
        if (allNamesValid) {
            newGameMenuFXML.toBack();

            //Initialize Players
            Player[] players = new Player[MAX_PLAYER_COUNT];
            if (activePlayers < players.length) {

                //Instantiate Player (Human / AI) - 2 Players
                players[0] = chkBx_isAiFXML[0].isSelected()
                        ? new AI_Player(txtfld_playerNamesFXML[0].getText(), true, true,
                        Utilities.isVerticalTeam(0))
                        : new Player(txtfld_playerNamesFXML[0].getText(), true, false);
                players[1] = chkBx_isAiFXML[1].isSelected()
                        ? new AI_Player(txtfld_playerNamesFXML[1].getText(), true, true,
                        Utilities.isVerticalTeam(1))
                        : new Player(txtfld_playerNamesFXML[1].getText(), true, false);

                //Inactive Players
                players[2] = new Player("", false, false);
                players[3] = new Player("", false, false);
            } else {

                //Instantiate Player (Human / AI) - 4 Players
                for (int i = 0; i < players.length; i++) {
                    players[i] = chkBx_isAiFXML[i].isSelected()
                            ? new AI_Player(txtfld_playerNamesFXML[i].getText(), true, true,
                            Utilities.isVerticalTeam(i))
                            : new Player(txtfld_playerNamesFXML[i].getText(), true, false);
                }
            }

            //Hide all Popups, if possible
            if (this.gui != null) {
                this.gui.closePopups();
            }

            this.gui = new JavaFXGUI(
                    gameBoardFXML, gameBoardGridFXML,
                    playerHandsFXML, playerHandsGridFMXL,
                    verticalPointsFXML, horizontalPointsFXML,
                    teamPointsFXML, usedWildcardsFXML,
                    announcementLabelFXML, animationLayerFXML,
                    showIsAiFXML, animationDurationFXML);

            this.game = new Game(BOARD_GRID_SIZE, players, gui);
            initGame();
        }
    }


    //Helper Methods::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Prepares all relevant GUI Elements for starting a new Game or
     * loading an existing one. Activates Saving-Function, sets all
     * playerNames, initializes mainContainer with playerHands and
     * gameBoard and assigns Drag n' Drop Handlers.
     */
    private void initGame() {
        saveGameFXML.setOnAction(this::handleMI_save);
        saveGameFXML.setDisable(false);

        //Set Player Names Label to actual Player Names
        for (int i = 0; i < 2; i++) {
            //Reset Team Player Names Label Text (important, if only 2 players and 2. Name is not set)
            teamPlayerNamesLabelFXML[i].setText("");
            teamPlayerNamesLabelFXML[i + 2].setText("");

            //Set new Team Player Names Labels
            teamPlayerNamesLabelFXML[i].setText(txtfld_playerNamesFXML[i == 0 ? 0 : 1].getText());
            if (activePlayers == 4) {
                teamPlayerNamesLabelFXML[i + 2].setText(txtfld_playerNamesFXML[i == 0 ? 2 : 3].getText());
            }
        }

        //Initialize main Container based on active players
        initMainContainer(activePlayers);

        //Assign Target Drag n' Drop Handler
        for (ImageView[] imageViews : gameBoardFXML) {
            for (ImageView imageView : imageViews) {
                DragHandlers.setOnDragExited(imageView);
                DragHandlers.setOnDragEntered(imageView, false);
                DragHandlers.setOnDragDropped(imageView, game);
                DragHandlers.setOnDragOver(imageView, false);
            }
        }

        //Assign whole GridPane gameBoard Drag n' Drop Handler
        DragHandlers.setOnDragEntered_board(gameBoardGridFXML);
        DragHandlers.setOnDragExited_board(gameBoardGridFXML);
    }

}