package logic;

import javafx.scene.image.ImageView;

import java.util.List;

/**
 * FakeGUI used for testing the logic of the game Crosswise. All methods
 * do nothing, but the logic works.
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class FakeGUI implements GUIConnector{
    @Override
    public void placeTileOnBoard(GameTiles gameTile, Position pos, boolean isAITurn) {

    }

    @Override
    public void enableAndDisablePlayerHands(int playerIdx, boolean isAI) {

    }

    @Override
    public void disablePlayerHand(int playerIdx) {

    }

    @Override
    public void hideAllPlayerHands() {

    }

    @Override
    public void hideAllPlayerHands_Delay() {

    }

    @Override
    public void showPlayerHand(int playerIdx, List<GameTiles> tiles, boolean isAI) {

    }

    @Override
    public void showPlayerHand_Delay(int playerIdx, List<GameTiles> tiles, boolean isAI) {

    }

    @Override
    public void removeTileFromHandAt(int player, int handSlot) {

    }

    @Override
    public void addTileToHandAt(int player, GameTiles tile, int handSlot, boolean isAI) {

    }

    @Override
    public void setGameTileOnHand(int playerIdx, int handSlotIdx, GameTiles tile) {

    }

    @Override
    public void setCurrentPlayer(String name) {

    }

    @Override
    public void updatePointBars(int[] verticalPoints, int[] horizontalPoints) {

    }

    @Override
    public void updateTeamPoints(int teamPoints_1, int teamPoints_2) {

    }

    @Override
    public void updateUsedWildcards(int[] usedWildcards) {

    }

    @Override
    public void onGameEnd(String[] playerNamesT1, String[] playerNamesT2, int[] teamPoints, GameStates winnerTeam) {

    }

    @Override
    public void onGameEnd_Delay(String[] playerNamesT1, String[] playerNamesT2, int[] teamPoints, GameStates winnerTeam) {

    }

    @Override
    public void setSourceDragHandler(ImageView source) {

    }

    @Override
    public void setBoardDragHandler(Game game, boolean swap) {

    }

    @Override
    public void resetBoardDragHandler(Game game, boolean isSwapping) {

    }

    @Override
    public void setAnnouncementLabel(String msg) {

    }

    @Override
    public void closePopups() {

    }

    @Override
    public void createAndPlayAnimation(GameTiles tile, int currentPlayer, Integer handSlot, Position sourcePos, Position targetPos, Game game, boolean doNextTurn) {

    }

    @Override
    public void stopAnimation() {

    }

    @Override
    public void updateAnimationDuration() {

    }

    @Override
    public void forceStop() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void createAlert(ErrorType errorType, String msg) {

    }
}
