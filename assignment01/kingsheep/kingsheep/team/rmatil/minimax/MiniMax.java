package kingsheep.team.rmatil.minimax;

import kingsheep.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class MiniMax {

    protected Player player;

    public MiniMax(Player player) {
        this.player = player;
    }

    public Action minimaxDecision(State state) {
        Map<Integer, Action> possibleActions = new TreeMap<>();

        for (Action action : actions(state)) {
            State tmp = result(state, action);

            // check whether we are min or max player
            if (this.player.getPlayerId() == 1) {
                possibleActions.put(minValue(tmp), action);
            } else {
                possibleActions.put(maxValue(tmp), action);
            }
        }

        Action[] orderedActions = (Action[]) possibleActions.entrySet().toArray();

        if (orderedActions.length > 0) {
            return orderedActions[0];
        }

        return new Action(Action.Move.WAIT);
    }

    /**
     * A terminal test for the current state, which is true when the game is over and false otherwise.
     * States where the game has ended are called terminal states.
     *
     * @param currentState The state to check if terminal
     *
     * @return True, if the given state is a terminal state, false otherwise
     */
    protected abstract boolean terminalTest(State currentState);

    /**
     * Returns an indicator (as int) how good the given state is for the configured player.
     *
     * @param s
     *
     * @return
     */
    protected abstract int utility(State s);

    private int minValue(State state) {
        if (terminalTest(state)) {
            return utility(state);
        }

        int v = Integer.MAX_VALUE;
        for (Action action : actions(state)) {
            v = Math.min(v, maxValue(result(state, action)));
        }

        return v;
    }

    private int maxValue(State state) {
        if (terminalTest(state)) {
            return utility(state);
        }

        int v = Integer.MIN_VALUE;
        for (Action action : actions(state)) {
            v = Math.max(v, minValue(result(state, action)));
        }

        return v;
    }

    /**
     * Returns the set of legal moves from the given state
     *
     * @param currentState The current state of the game
     */
    private Action[] actions(State currentState) {
        List<Action> possibleActions = new ArrayList<>();

        int currentX = this.player.getCurrentX();
        int currentY = this.player.getCurrentY();

        // check if we can go to the top
        if (currentY > 0
                && currentState.getMap()[currentY - 1][currentX] != this.player.getTeamMateType()
                && currentState.getMap()[currentY - 1][currentX] != this.player.getEqualOpponentType()
                && currentState.getMap()[currentY - 1][currentX] != Type.FENCE) {

            // move one step
            possibleActions.add(new Action(Action.Move.UP));
        }

        // check if we can go right
        if (currentX < currentState.getMap()[0].length
                && currentState.getMap()[currentY][currentX + 1] != this.player.getTeamMateType()
                && currentState.getMap()[currentY][currentX + 1] != this.player.getEqualOpponentType()
                && currentState.getMap()[currentY][currentX + 1] != Type.FENCE) {

            // move one step
            possibleActions.add(new Action(Action.Move.RIGHT));
        }

        // check if we can go down
        if (currentY < currentState.getMap().length
                && currentState.getMap()[currentY + 1][currentX] != this.player.getTeamMateType()
                && currentState.getMap()[currentY + 1][currentX] != this.player.getEqualOpponentType()
                && currentState.getMap()[currentY + 1][currentX] != Type.FENCE) {

            // move one step
            possibleActions.add(new Action(Action.Move.DOWN));
        }

        // check if we can go left
        if (currentX > 0
                && currentState.getMap()[currentY][currentX - 1] != this.player.getTeamMateType()
                && currentState.getMap()[currentY][currentX - 1] != this.player.getEqualOpponentType()
                && currentState.getMap()[currentY][currentX - 1] != Type.FENCE) {

            // move one step
            possibleActions.add(new Action(Action.Move.LEFT));
        }

        // we can also wait...
        possibleActions.add(new Action(Action.Move.WAIT));

        Action[] resultActions = new Action[possibleActions.size()];
        return possibleActions.toArray(resultActions);
    }

    /**
     * Defines which player has the move in a state
     *
     * @param s
     */
    private Player player(State s) {

        return null;
    }

    /**
     * The transition model, which defines the result of a move,
     * a.k.a. the applied action to the given state
     */
    private State result(State currentState, Action action) {
        int currentX = this.player.getCurrentX();
        int currentY = this.player.getCurrentY();

        Type[][] newMap = currentState.cloneMap();
        newMap[currentY][currentX] = Type.EMPTY;

        State newState = new State(newMap, currentState.getTurn());

        // TODO: check whether map is actually updated
        switch (action.getMove()) {
            case UP:
                if (newState.getMap()[currentY - 1][currentX] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX, currentY - 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }

                newState.getMap()[currentY - 1][currentX] = this.player.getType();
                break;
            case RIGHT:
                if (newState.getMap()[currentY][currentX + 1] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX + 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }

                newState.getMap()[currentY][currentX + 1] = this.player.getType();
                break;
            case DOWN:
                if (newState.getMap()[currentY + 1][currentX] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX, currentY + 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }

                newState.getMap()[currentY + 1][currentX] = this.player.getType();
                break;
            case LEFT:
                if (newState.getMap()[currentY][currentX - 1] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX - 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }

                newState.getMap()[currentY][currentX - 1] = this.player.getType();
                break;
            case WAIT:
                // no collision can happen if we stay
                newState.getMap()[currentY][currentX] = this.player.getType();
                break;
        }


        return newState;
    }

}
