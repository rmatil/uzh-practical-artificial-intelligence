package students.matileraphael.rulebased;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.slidingwindow.SlidingWindow;

import java.util.*;

public class RuleBasedAgent {

    private static final int SLIDING_WINDOW_SIZE_WIDTH  = 2;
    private static final int SLIDING_WINDOW_SIZE_HEIGHT = 3;

    private Color playerColor;

    public RuleBasedAgent(Color playerColor) {
        this.playerColor = playerColor;
    }

    public float makeNextMove(Board board, int turn) {
        List<SlidingWindow> windows = this.getSlidingWindows(board);
        Map<Float, Move[]> bestWindow = new TreeMap<>();

        for (SlidingWindow window : windows) {
            float incentive = this.score(window, turn);

            // there might be moves with the same incentive
            if (! bestWindow.containsKey(foundMove.getKey())) {
                bestWindow.put(foundMove.getKey(), new Move[1]);
                bestWindow.get(foundMove.getKey())[0] = foundMove.getValue();
            } else {
                // we have to extend the array
                Move[] oldValues = bestWindow.get(foundMove.getKey());
                bestWindow.put(foundMove.getKey(), Arrays.copyOf(oldValues, oldValues.length + 1));
                bestWindow.get(foundMove.getKey())[oldValues.length] = foundMove.getValue();
            }
        }

        Move[][] orderedActions = new Move[bestWindow.values().size()][];
        Float[] orderedIncentives = new Float[bestWindow.keySet().size()];

        bestWindow.values().toArray(orderedActions);
        bestWindow.keySet().toArray(orderedIncentives);

        // list is enumerated in ascending order
        if (this.playerColor.equals(Color.WHITE)) {
            return orderedActions[orderedActions.length - 1][0];
        } else {
            return orderedActions[0][0];
        }
    }

    public float score(SlidingWindow slidingWindow, int turn) {
        // white bottom, black top
        int ownMultiplier = (this.playerColor.equals(Color.WHITE)) ? 1 : - 1;
        int enemyMultiplier = - 1 * ownMultiplier;

        int ownPawns = slidingWindow.getBoard().figures(this.playerColor).entrySet().size();
        int enemyPawns = slidingWindow.getBoard().figures(this.playerColor.getOtherColor()).entrySet().size();

        int ownCaptureScore = 0;
        int enemyCaptureScore = 0;

        int ownPossibleMoves = 0;
        int enemyPossibleMoves = 0;

        for (Map.Entry<Coordinates, Figure> figure : slidingWindow.getBoard().figures(this.playerColor).entrySet()) {
            Coordinates coord = figure.getKey();

            if (figure.getValue().color.equals(this.playerColor)) {
                ownPossibleMoves += figure.getValue().possibleMoves(slidingWindow.getBoard()).entrySet().size();
            } else {
                enemyPossibleMoves += figure.getValue().possibleMoves(slidingWindow.getBoard()).entrySet().size();
            }

            Coordinates ownTargetLeft = new Coordinates(coord.getRow() + (ownMultiplier), coord.getColumn() - 1);
            Coordinates ownTargetRight = new Coordinates(coord.getRow() + (ownMultiplier), coord.getColumn() + 1);
            Coordinates enemyTargetLeft = new Coordinates(coord.getRow() + (enemyMultiplier), coord.getColumn() - 1);
            Coordinates enemyTargetRight = new Coordinates(coord.getRow() + (enemyMultiplier), coord.getColumn() + 1);

            if (figure.getValue().color.equals(this.playerColor) &&
                    slidingWindow.getBoard().onBoard(ownTargetLeft) &&
                    slidingWindow.getBoard().figureAt(ownTargetLeft) != null &&
                    slidingWindow.getBoard().figureAt(ownTargetLeft).color.equals(this.playerColor.getOtherColor())) {
                ownCaptureScore += 10;
            }

            if (figure.getValue().color.equals(this.playerColor.getOtherColor()) &&
                    slidingWindow.getBoard().onBoard(enemyTargetLeft) &&
                    slidingWindow.getBoard().figureAt(enemyTargetLeft) != null &&
                    slidingWindow.getBoard().figureAt(enemyTargetLeft).color.equals(this.playerColor)) {
                enemyCaptureScore += 10;
            }

            if (figure.getValue().color.equals(this.playerColor) &&
                    slidingWindow.getBoard().onBoard(ownTargetRight) &&
                    slidingWindow.getBoard().figureAt(ownTargetRight) != null &&
                    slidingWindow.getBoard().figureAt(ownTargetRight).color.equals(this.playerColor.getOtherColor())) {
                ownCaptureScore += 10;
            }

            if (figure.getValue().color.equals(this.playerColor.getOtherColor()) &&
                    slidingWindow.getBoard().onBoard(enemyTargetRight) &&
                    slidingWindow.getBoard().figureAt(enemyTargetRight) != null &&
                    slidingWindow.getBoard().figureAt(enemyTargetRight).color.equals(this.playerColor)) {
                enemyCaptureScore += 10;
            }
        }

        float mobility = ownPossibleMoves - enemyPossibleMoves;
        float scoreOfPawns = ownPawns - enemyPawns;
        float capture = enemyCaptureScore - ownCaptureScore;

        return scoreOfPawns + 0.1f * mobility - 0.2f * currentState.getTurn() + capture;
    }

    private List<SlidingWindow> getSlidingWindows(final Board board) {
        List<SlidingWindow> slidingWindows = new ArrayList<>();

        for (int x = 0; x < board.width - SLIDING_WINDOW_SIZE_WIDTH; x++) {
            for (int y = 0; y < board.height - SLIDING_WINDOW_SIZE_HEIGHT; y++) {

                Map<Coordinates, Figure> figures = new HashMap<>();
                StringBuilder stringBuilder = new StringBuilder();

                boolean isEmptySlidingWindow = true;
                for (int slidingWindowX = 0; slidingWindowX < SLIDING_WINDOW_SIZE_WIDTH; slidingWindowX++) {
                    for (int slidingWindowY = 0; slidingWindowY < SLIDING_WINDOW_SIZE_HEIGHT; slidingWindowY++) {
                        Coordinates tmpCoordinate = new Coordinates(slidingWindowY, slidingWindowX);
                        Figure figure = board.figureAt(tmpCoordinate);

                        if (figure != null && (figure.color.equals(Color.WHITE) || figure.color.equals(Color.BLACK))) {
                            isEmptySlidingWindow = false;
                        }

                        if (figure != null) {
                            figures.put(tmpCoordinate, figure);
                        }

                        stringBuilder.append((figure != null) ? figure.color : "null");
                    }
                }

                // do not evaluate empty sliding windows
                if (isEmptySlidingWindow) {
                    continue;
                }

                Board slidingWindowBoard = new Board(SLIDING_WINDOW_SIZE_HEIGHT, SLIDING_WINDOW_SIZE_WIDTH, figures);
                slidingWindows.add(new SlidingWindow(x, y, slidingWindowBoard, stringBuilder.toString()));
            }
        }

        return slidingWindows;
    }
}
