package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import logic.GameStates;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Popup Controller for GameEnd Popup. Opens new window with winning
 * team or if game ended in a draw. Additionally, shows both team
 * overviews containing playerNames of each team and achieved points.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class GameEndPopupController implements Initializable {

    /**
     * Root.
     */
    @FXML
    private final AnchorPane root;

    /**
     * Array of playerNames of Team1 (Vertical).
     */
    private final String[] playerNamesT1;
    /**
     * Array of playerNames of Team2 (Horizontal).
     */
    private final String[] playerNamesT2;
    /**
     * Array of both TeamPoints.
     */
    private final int[] teamPoints;
    /**
     * GameState containing WinnerTeam or Draw.
     */
    private final GameStates winnerTeam;

    /**
     * PopupController constructor.
     *
     * @param root          root node
     * @param playerNamesT1 Array of playerNames of Team 1
     * @param playerNamesT2 Array of playerNames of Team 2
     * @param teamPoints    Array of team Points
     * @param winnerTeam    winner Team or Draw
     */
    GameEndPopupController(AnchorPane root, String[] playerNamesT1, String[] playerNamesT2,
                           int[] teamPoints, GameStates winnerTeam) {

        this.root = root;
        this.playerNamesT1 = playerNamesT1;
        this.playerNamesT2 = playerNamesT2;
        this.teamPoints = teamPoints;
        this.winnerTeam = winnerTeam;
    }

    /**
     * Initializes Popup Window with all of its components.
     * Parameters are not used.
     *
     * @param location  not used
     * @param resources not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final double alignmentMaxHeight = 500;
        final double mainContainerMaxWidth = 500;

        //Alignment Containers::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        VBox alignmentV = new VBox();
        alignmentV.prefHeightProperty().bind(root.heightProperty());
        alignmentV.alignmentProperty().set(Pos.CENTER);

        HBox alignmentH = new HBox();
        alignmentH.setAlignment(Pos.CENTER);
        alignmentH.prefWidthProperty().bind(root.widthProperty());
        alignmentH.prefHeightProperty().bind(root.heightProperty());
        alignmentH.setMaxHeight(alignmentMaxHeight);


        //Main Container::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        VBox mainContainer = new VBox();
        mainContainer.getStyleClass().add("main-container");

        //Assign Background according to winning team or if draw
        String background = "-fx-background-color: " +
                "linear-gradient(from 10px 10px to 20px 20px, repeat, #c7c7c7 40%, #ffffff 20%)";
        switch (winnerTeam) {
            case TEAM_VERTICAL -> background = "-fx-background-color: linear-gradient(" +
                    "from 10px 10px to 20px 10px, repeat, rgb(255, 184, 89) 40%, #ffffff 20%)";
            case TEAM_HORIZONTAL -> background = "-fx-background-color: linear-gradient(" +
                    "from 10px 10px to 10px 20px, repeat, rgb(118, 255, 110) 40%, #ffffff 20%)";
        }

        mainContainer.setStyle(background);
        mainContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.8));
        mainContainer.setMaxWidth(mainContainerMaxWidth);

        root.getChildren().add(alignmentV);
        alignmentV.getChildren().add(alignmentH);

        alignmentH.getChildren().add(mainContainer);
        AnchorPane.setBottomAnchor(alignmentV, 20.0);
        AnchorPane.setTopAnchor(alignmentV, 20.0);

        //Add Elements::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        mainContainer.getChildren().add(createSpacerNode());

        //Header
        Label header = new Label(winnerTeam != GameStates.DRAW ? "WINNER" : "DRAW");
        mainContainer.getChildren().add(new HBox(header));
        if (winnerTeam != GameStates.DRAW) {
            Label team = new Label(winnerTeam == GameStates.TEAM_VERTICAL ? "TEAM VERTICAL" : "TEAM HORIZONTAL");
            mainContainer.getChildren().add(new HBox(team));
        }

        mainContainer.getChildren().add(createSpacerNode());

        //Team 1 Overview
        GridPane team1 = createTeamOverview(true);
        team1.getStyleClass().add("team-information-grid");
        mainContainer.getChildren().add(team1);

        mainContainer.getChildren().add(createSpacerNode());

        //Team 2 Overview
        GridPane team2 = createTeamOverview(false);
        team2.getStyleClass().add("team-information-grid");
        mainContainer.getChildren().add(team2);

        mainContainer.getChildren().add(createSpacerNode());
    }

    /**
     * Creates Team Overview GridPane. Contains TeamName, all playerNames of
     * each team, points of each team + if win of sixes was achieved.
     *
     * @param isVertical team (TRUE -> Team 1 | FALSE -> Team 2)
     * @return Team Overview GridPane
     */
    private GridPane createTeamOverview(boolean isVertical) {
        GridPane overviewGrid = UserInterfaceController.createContainerGrid(new int[]{100}, new int[]{0, 0});

        HBox teamNameContainer = new HBox();
        teamNameContainer.getStyleClass().add("team-name-container");
        teamNameContainer.getStyleClass().add(isVertical ? "team-vertical" : "team-horizontal");

        //Team Name
        Label teamName = new Label(String.format("// %s //", isVertical ? "TEAM VERTICAL" : "TEAM HORIZONTAL"));
        teamNameContainer.getChildren().add(teamName);
        overviewGrid.add(teamNameContainer, 0, 0, 1, 1);

        //Team Player Names
        GridPane teamInformation = UserInterfaceController.createContainerGrid(new int[]{60, 40}, new int[]{50, 50});
        if (playerNamesT1.length == 2) {
            Label name1 = new Label(isVertical ? playerNamesT1[0] : playerNamesT2[0]);
            Label name2 = new Label(isVertical ? playerNamesT1[1] : playerNamesT2[1]);

            teamInformation.add(new HBox(name1), 0, 0, 1, 1);
            teamInformation.add(new HBox(name2), 0, 1, 1, 1);
        } else {
            Label name = new Label(isVertical ? playerNamesT1[0] : playerNamesT2[0]);
            teamInformation.add(new HBox(name), 0, 0, 1, 2);
        }

        //Team Points
        Label points = new Label(isVertical
                ? String.valueOf(teamPoints[0] == Integer.MAX_VALUE ? "SIXES" : teamPoints[0])
                : String.valueOf(teamPoints[1] == Integer.MAX_VALUE ? "SIXES" : teamPoints[1]));
        points.getStyleClass().add("points");
        teamInformation.add(new HBox(points), 1, 0, 1, 2);

        overviewGrid.add(teamInformation, 0, 1, 1, 1);
        return overviewGrid;
    }

    /**
     * Creates Spacer Nodes (horizontal or vertical) for better Layout.
     *
     * @return Spacer Node
     */
    private Node createSpacerNode() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        return spacer;
    }
}
