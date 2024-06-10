package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Static GameFileManager Class for saving and loading GameData
 * Files / Instances from a Crosswise Game.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public final class GameFileManager {

    /**
     * Saves a current Game Instance by creating its representative GameData-Class
     * and saving it to the specified FileName.
     *
     * @param game     current Game-Instance
     * @param saveFile FileName
     */
    public static void saveGame(Game game, String saveFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        GameData saveGame = game.toGameData();

        try (FileWriter writer = new FileWriter(saveFile)) {
            gson.toJson(saveGame, writer);
        } catch (IOException e) {
            Utilities.logEntry("File couldn't be saved", ErrorType.INVALID_SAVE_FILE);
        }
    }

    /**
     * Loads GameData from a specified FileName and returns
     * a GameData Instance
     *
     * @param loadFile FileName
     * @return Loaded GameData
     * @throws JsonSyntaxException Exception, if Syntax is invalid
     */
    public static GameData loadGame(String loadFile) {
        Gson gson = new Gson();
        GameData gameData = null;

        try {
            gameData = gson.fromJson(new FileReader(loadFile), GameData.class);
        } catch (FileNotFoundException e) {
            Utilities.logEntry("No such Game to load", ErrorType.INVALID_SAVE_FILE);
        } catch (JsonSyntaxException e) {
            Utilities.logEntry("Invalid save File", ErrorType.INVALID_SAVE_FILE);
        }

        if (gameData == null) {
            Utilities.logEntry("Invalid save File", ErrorType.INVALID_SAVE_FILE);
        }

        return gameData;
    }

}
