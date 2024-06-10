package gui;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Game;
import logic.GameBoard;
import logic.GameTiles;

import java.io.IOException;
import java.util.Arrays;

/**
 * Class that starts our application.
 *
 * @author mjo
 */
public class ApplicationMain extends Application {
        /**
     * Creating the stage and showing it. This is where the initial size and the
     * title of the window are set.
     *
     * @param stage the stage to be shown
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new FXMLLoader(ApplicationMain.class.getResource("UserInterface.fxml")).load();
        root.getStyleClass().add("root");

        Scene scene = new Scene(root, 1000, 752);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Crosswise");
        stage.setMinWidth(1000);
        stage.setMinHeight(750);
        stage.setScene(scene);
        stage.show();

        //Initialize Game:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        //Game game = new Game();

    }

    /**
     * Main method
     *
     * @param args unused
     */
    public static void main(String... args) {
        launch(args);
    }
}
