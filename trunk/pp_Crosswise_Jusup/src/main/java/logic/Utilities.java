package logic;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility Class for simple helper methods that are used in the logic package.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public final class Utilities {
    /**
     * Public constant for Enabling/ Disabling Console Logging.
     */
    public static final boolean LOG_IN_CONSOLE = false;

    /**
     * Constant for Coloring the Log Entries.
     * Should be false, if Log File is of relevance.
     */
    static final boolean LOG_IN_COLOR = false;

    /**
     * Helper Method to translate given 2D int-Array
     * representing the gameBoard with ordinal values
     *
     * @param toTranslate toTranslate 2D Array
     * @return translated 2D Array
     */
    static int[][] transposeBoard(int[][] toTranslate) {
        int[][] translated = new int[toTranslate.length][];
        for (int x = 0; x < translated.length; x++) {
            translated[x] = new int[toTranslate[x].length];
            for (int y = 0; y < translated[x].length; y++) {
                translated[x][y] = toTranslate[y][x];
            }
        }
        return translated;
    }

    /**
     * Converts given number to respective GameTile with same ordinal.
     *
     * @param number ordinal of GameTile
     * @return respective GameTile
     */
    static GameTiles ordinalToGameTiles(int number) {
        return switch (number) {
            case 0 -> GameTiles.EMPTY;
            case 1 -> GameTiles.T_SUN;
            case 2 -> GameTiles.T_CROSS;
            case 3 -> GameTiles.T_TRIANGLE;
            case 4 -> GameTiles.T_SQUARE;
            case 5 -> GameTiles.T_PENTAGON;
            case 6 -> GameTiles.T_STAR;
            case 7 -> GameTiles.WC_REMOVER;
            case 8 -> GameTiles.WC_MOVER;
            case 9 -> GameTiles.WC_SWAPONBOARD;
            case 10 -> GameTiles.WC_SWAPWITHHAND;
            default -> throw new IllegalArgumentException("[ERROR] No GameTile for number <" + number + ">");
        };
    }

    /**
     * Debug Method. Returns colored String of given GameTile with its respective
     * color to make it easier to debug.
     *
     * @param tile GameTile
     * @return Colored String of GameTile
     */
    static String colorGameTileMsg(GameTiles tile) {
        return switch (tile) {
            case T_SUN -> "\u001B[43m" + tile.ordinal() + "\u001B[0m";
            case T_CROSS -> "\u001B[44m" + tile.ordinal() + "\u001B[0m";
            case T_TRIANGLE -> "\u001B[42m" + tile.ordinal() + "\u001B[0m";
            case T_SQUARE -> "\u001B[41m" + tile.ordinal() + "\u001B[0m";
            case T_PENTAGON -> "\u001B[47m" + tile.ordinal() + "\u001B[0m";
            case T_STAR -> "\u001B[45m" + tile.ordinal() + "\u001B[0m";
            default -> "\u001B[49m" + tile.ordinal() + "\u001B[0m";
        };
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
    static void logEntry(String message, ErrorType type) {
        //Log in Console
        if (LOG_IN_CONSOLE) {
            if (type == ErrorType.NONE) {
                System.out.println(message);
            } else {
                System.out.printf("[%s] %s%n", type, message);
            }
        }

        //Log in File
        try {
            FileWriter logWriter = new FileWriter("./src/log/Log.txt", true);
            if (type == ErrorType.NONE) {
                logWriter.write(String.format("%s\n", message));
            } else {
                logWriter.write(String.format("[%s] %s\n", type, message));
            }
            logWriter.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Gets to specified player Index its corresponding team.
     * playerIdx == 0 || 2 -> Team Vertical
     * playerIdx == 1 || 3 -> Team Horizontal
     *
     * @param playerIdx playerIndex
     * @return If player is team vertical or not (-> horizontal)
     */
    static boolean isVerticalTeam(int playerIdx) {
        return playerIdx == 0 || playerIdx == 2;
    }

}
