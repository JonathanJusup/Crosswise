package logic;

/**
 * Enumeration for Game Evaluation States
 * Contains, whether Game is still ongoing, there's no winner (draw)
 * and both winner teams (vertical & horizontal)
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public enum GameEvaluationStates {
    ONGOING_GAME,
    DRAW,
    TEAM_VERTICAL,
    TEAM_HORIZONTAL
}
