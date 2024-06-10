package gui;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import logic.ErrorType;
import logic.GameTiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Utilities for the gui package. Contains helpful utility methods
 * for all the classes to use.
 */
public final class Utilities {

    /**
     * gameTilesImageMap for storing only one Image Instance per GameTie
     * for the GUI to use it. Saves a lot of memory.
     */
    static final Map<GameTiles, Image> gameTilesImageMap = new HashMap<>();

    /**
     * gameTilesURL Map for comparing Images just with their respective
     * URLs.
     */
    private static final Map<String, GameTiles> gameTilesURLMap = new HashMap<>();


    /**
     * Initializes all Maps, that contains Images. Must only be called, when
     * application runs. Logic Tests are unable to work with Images without
     * Graphics not initialized
     */
    static void initializeImages() {
        for (GameTiles tile : GameTiles.values()) {
            gameTilesImageMap.put(tile, new Image(tileToAddress(tile)));
        }

        for (GameTiles tile : GameTiles.values()) {
            gameTilesURLMap.put(new Image(tileToAddress(tile)).getUrl(), tile);
        }
    }

    /**
     * Converts GameTile to String Image Address to be referred to.
     *
     * @param tile GameTile
     * @return String Image Address
     */
    static String tileToAddress(GameTiles tile) {
        String address = null;

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
            case WC_SWAPONBOARD -> address = "img/swapOnBoard.png";
            case WC_SWAPWITHHAND -> address = "img/swapWithHand.png";

            default -> System.out.println("ERROR: " + tile + " has no image address / does not exist");
        }

        return address;
    }

    /**
     * Method to get GameTile from ImageURL. Compares given imageURL
     * with static gameTilesURLMap.
     *
     * @param imageURL image URL to get GameTile from
     * @return corresponding GameTile
     */
    static GameTiles imageURLToGameTile(String imageURL) {
        GameTiles result = gameTilesURLMap.get(imageURL);

        if (result != null) {
            return result;
        }

        throw new IllegalArgumentException("Image is not a GameTile!");
    }

    /**
     * Helper Method, that gets random Images, with a higher probability
     * for EMPTY GameTiles. This Method has no functional value and is
     * just used for filling the gameBoard and playerHands at start of
     * application.
     *
     * @return random GameTile Image
     */
    static Image getRandomImage() {
        return switch (new Random().nextInt(0, 8)) {
            case 0 -> gameTilesImageMap.get(GameTiles.T_SUN);
            case 1 -> gameTilesImageMap.get(GameTiles.T_CROSS);
            case 2 -> gameTilesImageMap.get(GameTiles.T_TRIANGLE);
            case 3 -> gameTilesImageMap.get(GameTiles.T_SQUARE);
            case 4 -> gameTilesImageMap.get(GameTiles.T_PENTAGON);
            case 5 -> gameTilesImageMap.get(GameTiles.T_STAR);
            default -> gameTilesImageMap.get(GameTiles.EMPTY);
        };
    }


    static boolean isVerticalTeam(int playerIdx) {
        return playerIdx == 0 || playerIdx == 2;
    }

    /**
     * Check, if given name is valid. A name is valid, if it contains
     * letters or numbers & it's length is 15 at maximum.
     *
     * @param name to check if valid
     * @return valid name -> TRUE | invalid name -> FALSE
     */
    static boolean isValidName(String name) {
        return !name.isEmpty() && name.matches("[a-zA-Z\\d]*") && name.length() <= 15;
    }

    /**
     * Logs given message + errorType by writing it in a LogFile.
     * Optionally outputs log to console. If Log is written in
     * console is permitted by global variable LOG_IN_CONSOLE.
     * Does not write errorType, if type == NONE
     *
     * @param message message to log
     * @param type    errorType
     */
    static void logInConsole(String message, ErrorType type) {
        //Log in Console
        if (logic.Utilities.LOG_IN_CONSOLE) {
            if (type == ErrorType.NONE) {
                System.out.println(message);
            } else {
                System.out.printf("[%s] %s%n", type, message);
            }
        }
    }

    /**
     * Creates and shows a new Alert Window with specified errorType and message.
     *
     * @param errorType errorType
     * @param msg       message
     */
    static void createAlert(ErrorType errorType, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(String.valueOf(errorType));
        alert.setContentText(msg);
        alert.show();
    }
}
