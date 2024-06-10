package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Class that starts our application.
 *
 * @author mjo, Jonathan El Jusup (cgt104707)
 */
public class ApplicationMain extends Application {

    /**
     * Creating the stage and showing it. This is where the initial size and the
     * title of the window are set.
     *
     * @param stage the stage to be shown
     * @throws IOException Exception
     */
    @Override
    public void start(Stage stage) throws IOException {
        final Point2D sceneSize = new Point2D(1100, 900);
        final Point2D sceneMinSize = new Point2D(1000, 900);

        VBox root = new FXMLLoader(ApplicationMain.class.getResource("UserInterface.fxml")).load();
        root.getStyleClass().add("root");

        Scene scene = new Scene(root, sceneSize.getX(), sceneSize.getY());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        stage.setTitle("Crosswise");
        stage.getIcons().add(new Image(
                "img/cross.png",
                15,
                15,
                false,
                false));
        stage.setOnCloseRequest(event -> Platform.exit());

        stage.setMinWidth(sceneMinSize.getX());
        stage.setMinHeight(sceneMinSize.getY());
        stage.setScene(scene);

        stage.show();
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
