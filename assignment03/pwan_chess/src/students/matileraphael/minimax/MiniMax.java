package students.matileraphael.minimax;


import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.slidingwindow.SlidingWindow;

import java.util.*;

public class MiniMax {

    private static final int SLIDING_WINDOW_SIZE_WIDTH  = 2;
    private static final int SLIDING_WINDOW_SIZE_HEIGHT = 3;

    private static final int MAX_DEPTH         = 1;
    private static final int WINNING_INCENTIVE = 100;
    private static final int LOOSING_INCENTIVE = 0;

    private Color                          playerColor;
    private Map<String, Pair<Float, Move>> cachedMoves;

    public MiniMax(Color playerColor) {
        this.playerColor = playerColor;
        this.cachedMoves = new HashMap<>();
    }

    public Move slidingWindowMinimax(Board board, int initialStartingPos, int turn) {
        List<SlidingWindow> slidingWindows = this.getSlidingWindows(board);

        Map<Float, Move[]> bestWindow = new TreeMap<>();

        for (SlidingWindow window : slidingWindows) {
            State windowState = new State(
                    window,
                    this.playerColor,
                    board.width,
                    board.height,
                    initialStartingPos,
                    0,
                    turn
            );

            Pair<Float, Move> foundMove = this.minimax(windowState);

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

    private Pair<Float, Move> minimax(State state) {
        if (cachedMoves.containsKey(state.getSlidingWindow().getStrRepresentation())) {
            Pair<Float, Move> chosenMove = cachedMoves.get(state.getSlidingWindow().getStrRepresentation());

            Coordinates from = new Coordinates(chosenMove.getValue().from.getRow() + state.getSlidingWindow().getY(), chosenMove.getValue().from.getColumn() + state.getSlidingWindow().getX());
            Coordinates to = new Coordinates(chosenMove.getValue().to.getRow() + state.getSlidingWindow().getY(), chosenMove.getValue().to.getColumn() + state.getSlidingWindow().getX());

            // adjust coordinates
            return new Pair<>(chosenMove.getKey(), new Move(from, to));
        }

        Map<Float, Move[]> possibleActions = new TreeMap<>();

        // go over all applicable states
        for (Figure figure : new ArrayList<>(state.getSlidingWindow().getBoard().figures(state.getColor()).values())) {
            // apply move
            for (Map.Entry<Move, Board> appliedMove : figure.possibleMoves(state.getSlidingWindow().getBoard()).entrySet()) {
                State tmp = new State(
                        new SlidingWindow(appliedMove.getValue(), state.getSlidingWindow()),
                        state.getColor(),
                        state.getOrigBoardWidth(),
                        state.getOrigBoardHeight(),
                        state.getStartingSide(),
                        state.getCurrentDepth() + 1,
                        state.getTurn()
                );

                // check whether we are min or max player
                float incentive;
                if (this.playerColor.equals(Color.WHITE)) {
                    incentive = maxValue(tmp, Integer.MIN_VALUE, Integer.MAX_VALUE);
                } else {
                    incentive = minValue(tmp, Integer.MIN_VALUE, Integer.MAX_VALUE);
                }

                // there might be moves with the same incentive
                if (! possibleActions.containsKey(incentive)) {
                    possibleActions.put(incentive, new Move[1]);
                    possibleActions.get(incentive)[0] = appliedMove.getKey();
                } else {
                    // we have to extend the array
                    Move[] oldValues = possibleActions.get(incentive);
                    possibleActions.put(incentive, Arrays.copyOf(oldValues, oldValues.length + 1));
                    possibleActions.get(incentive)[oldValues.length] = appliedMove.getKey();
                }
            }
        }


        Move[][] orderedActions = new Move[possibleActions.values().size()][];
        Float[] orderedIncentives = new Float[possibleActions.keySet().size()];
        possibleActions.values().toArray(orderedActions);
        possibleActions.keySet().toArray(orderedIncentives);

        Float chosenIncentive;
        Move chosenMove;
        // list is enumerated in ascending order
        if (this.playerColor.equals(Color.WHITE)) {
            chosenIncentive = orderedIncentives[orderedActions.length - 1];
            chosenMove = orderedActions[orderedActions.length - 1][0];
        } else {
            chosenIncentive = orderedIncentives[0];
            chosenMove = orderedActions[0][0];
        }

        cachedMoves.put(state.getSlidingWindow().getStrRepresentation(), new Pair<>(chosenIncentive, chosenMove));

        Coordinates from = new Coordinates(chosenMove.from.getRow() + state.getSlidingWindow().getY(), chosenMove.from.getColumn() + state.getSlidingWindow().getX());
        Coordinates to = new Coordinates(chosenMove.to.getRow() + state.getSlidingWindow().getY(), chosenMove.to.getColumn() + state.getSlidingWindow().getX());

        // adjust coordinates
        return new Pair<>(chosenIncentive, new Move(from, to));

    }

    /**
     * A terminal test for the current state, which is true when the game is over and false otherwise.
     * States where the game has ended are called terminal states.
     *
     * @param currentState The state to check if terminal
     *
     * @return True, if the given state is a terminal state, false otherwise
     */
    private boolean terminalTest(State currentState) {
        // is any of the pawns on the other side?
        // or can we move with any of our pawns?

        boolean stillMovable = false;
        for (Map.Entry<Coordinates, Figure> entry : currentState.getSlidingWindow().getBoard().figures().entrySet()) {

            // check whether we are on the other side
            if (entry.getValue().color.equals(currentState.getColor()) &&
                    entry.getKey().getRow() == currentState.getTargetSide()) {
                return true;
            }

            // check whether our opponent reached our side
            if (entry.getValue().color.equals(currentState.getColor().getOtherColor()) &&
                    entry.getKey().getRow() == currentState.getStartingSide()) {
                return true;
            }

            if (! entry.getValue().possibleMoves(currentState.getSlidingWindow().getBoard()).entrySet().isEmpty()) {
                stillMovable = true;
            }
        }

        return ! stillMovable;
    }

    /**
     * Returns an indicator (as int) how good the given state is for the configured player.
     *
     * @param currentState The state to evaluate
     *
     * @return The value indicator expressing how good the given state is
     */
    private float utility(State currentState) {

        for (Map.Entry<Coordinates, Figure> entry : currentState.getSlidingWindow().getBoard().figures().entrySet()) {

            // check whether we are on the other side
            if (entry.getValue().color.equals(currentState.getColor()) && entry.getKey().getRow() == currentState.getTargetSide()) {
                return WINNING_INCENTIVE;
            }

            // check whether our opponent reached our side
            if (entry.getValue().color.equals(currentState.getColor().getOtherColor()) && entry.getKey().getRow() == currentState.getStartingSide()) {
                return LOOSING_INCENTIVE;
            }
        }

        return 0;
    }

    /**
     * Returns a heuristic, i.e. estimated value on how good the given state is
     *
     * @param currentState The state to evaluate
     *
     * @return The value indicator expressing how good the given state is
     */
    private float heuristicEval(State currentState) {
        return this.score(currentState);
    }

    private float score(State currentState) {
        // white bottom, black top
        int ownMultiplier = (this.playerColor.equals(Color.WHITE)) ? 1 : - 1;
        int enemyMultiplier = - 1 * ownMultiplier;

        int ownPawns = currentState.getSlidingWindow().getBoard().figures(currentState.getColor()).entrySet().size();
        int enemyPawns = currentState.getSlidingWindow().getBoard().figures(currentState.getColor().getOtherColor()).entrySet().size();

        int ownCaptureScore = 0;
        int enemyCaptureScore = 0;

        int ownPossibleMoves = 0;
        int enemyPossibleMoves = 0;

        for (Map.Entry<Coordinates, Figure> figure : currentState.getSlidingWindow().getBoard().figures(this.playerColor).entrySet()) {
            Coordinates coord = figure.getKey();

            if (figure.getValue().color.equals(this.playerColor)) {
                ownPossibleMoves += figure.getValue().possibleMoves(currentState.getSlidingWindow().getBoard()).entrySet().size();
            } else {
                enemyPossibleMoves += figure.getValue().possibleMoves(currentState.getSlidingWindow().getBoard()).entrySet().size();
            }

            Coordinates ownTargetLeft = new Coordinates(coord.getRow() + (ownMultiplier), coord.getColumn() - 1);
            Coordinates ownTargetRight = new Coordinates(coord.getRow() + (ownMultiplier), coord.getColumn() + 1);
            Coordinates enemyTargetLeft = new Coordinates(coord.getRow() + (enemyMultiplier), coord.getColumn() - 1);
            Coordinates enemyTargetRight = new Coordinates(coord.getRow() + (enemyMultiplier), coord.getColumn() + 1);

            if (figure.getValue().color.equals(this.playerColor) &&
                    currentState.getSlidingWindow().getBoard().onBoard(ownTargetLeft) &&
                    currentState.getSlidingWindow().getBoard().figureAt(ownTargetLeft) != null &&
                    currentState.getSlidingWindow().getBoard().figureAt(ownTargetLeft).color.equals(this.playerColor.getOtherColor())) {
                ownCaptureScore += 10;
            }

            if (figure.getValue().color.equals(this.playerColor.getOtherColor()) &&
                    currentState.getSlidingWindow().getBoard().onBoard(enemyTargetLeft) &&
                    currentState.getSlidingWindow().getBoard().figureAt(enemyTargetLeft) != null &&
                    currentState.getSlidingWindow().getBoard().figureAt(enemyTargetLeft).color.equals(this.playerColor)) {
                enemyCaptureScore += 10;
            }

            if (figure.getValue().color.equals(this.playerColor) &&
                    currentState.getSlidingWindow().getBoard().onBoard(ownTargetRight) &&
                    currentState.getSlidingWindow().getBoard().figureAt(ownTargetRight) != null &&
                    currentState.getSlidingWindow().getBoard().figureAt(ownTargetRight).color.equals(this.playerColor.getOtherColor())) {
                ownCaptureScore += 10;
            }

            if (figure.getValue().color.equals(this.playerColor.getOtherColor()) &&
                    currentState.getSlidingWindow().getBoard().onBoard(enemyTargetRight) &&
                    currentState.getSlidingWindow().getBoard().figureAt(enemyTargetRight) != null &&
                    currentState.getSlidingWindow().getBoard().figureAt(enemyTargetRight).color.equals(this.playerColor)) {
                enemyCaptureScore += 10;
            }
        }

        float mobility = ownPossibleMoves - enemyPossibleMoves;
        float scoreOfPawns = ownPawns - enemyPawns;
        float capture = enemyCaptureScore - ownCaptureScore;

        return scoreOfPawns + 0.1f * mobility - 0.2f * currentState.getTurn() + capture;
    }

    private float minValue(final State state, float alpha, float beta) {
        if (terminalTest(state)) {
            return utility(state);
        }

        if (cutoffTest(state)) {
            return heuristicEval(state);
        }

        float v = Integer.MAX_VALUE;

        for (Figure figure : new ArrayList<>(state.getSlidingWindow().getBoard().figures(state.getColor()).values())) {
            // apply move
            for (Map.Entry<Move, Board> appliedMove : figure.possibleMoves(state.getSlidingWindow().getBoard()).entrySet()) {
                State appliedState = new State(
                        new SlidingWindow(appliedMove.getValue(), state.getSlidingWindow()),
                        state.getColor(),
                        state.getOrigBoardWidth(),
                        state.getOrigBoardHeight(),
                        state.getStartingSide(),
                        state.getCurrentDepth() + 1,
                        state.getTurn()
                );
                v = Math.min(v, maxValue(appliedState, alpha, beta));

                if (v <= alpha) {
                    return v;
                }
                beta = Math.min(beta, v);
            }
        }

        return v;
    }

    private float maxValue(final State state, float alpha, float beta) {
        if (terminalTest(state)) {
            return utility(state);
        }

        if (cutoffTest(state)) {
            return heuristicEval(state);
        }

        float v = Integer.MIN_VALUE;

        for (Figure figure : new ArrayList<>(state.getSlidingWindow().getBoard().figures(state.getColor()).values())) {
            // apply move
            for (Map.Entry<Move, Board> appliedMove : figure.possibleMoves(state.getSlidingWindow().getBoard()).entrySet()) {
                State appliedState = new State(
                        new SlidingWindow(appliedMove.getValue(), state.getSlidingWindow()),
                        state.getColor(),
                        state.getOrigBoardWidth(),
                        state.getOrigBoardHeight(),
                        state.getStartingSide(),
                        state.getCurrentDepth() + 1,
                        state.getTurn()
                );
                v = Math.max(v, minValue(appliedState, alpha, beta));

                if (v >= beta) {
                    return v;
                }
                alpha = Math.max(alpha, v);
            }
        }

        return v;
    }

    /**
     * Checks whether we reached the maximum depth allowed for searching
     *
     * @param currentState The current state to check on
     *
     * @return True, if we reached the maximum depth, false otherwise
     */
    private boolean cutoffTest(State currentState) {
        return currentState.getCurrentDepth() >= MiniMax.MAX_DEPTH;
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
