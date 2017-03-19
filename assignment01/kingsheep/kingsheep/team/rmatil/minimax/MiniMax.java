package kingsheep.team.rmatil.minimax;

import kingsheep.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class MiniMax {

    protected Player player;

    /**
     * Alpha for alpha-beta pruning
     */
    private int a;

    /**
     * Beta for alpha-beta pruning
     */
    private int b;

    public MiniMax(Player player) {
        this.player = player;
        this.a = Integer.MIN_VALUE;
        this.b = Integer.MAX_VALUE;
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

            if (v <= this.a) {
                return v;
            }
            this.b = Math.min(this.b, v);
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

            if (v >= this.b) {
                return v;
            }
            this.a = Math.max(this.a, v);
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

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        // check if we can go to the top
        if (currentY > 0
                && currentState.getMap()[currentY - 1][currentX] != this.player.getTeamMateType()
                && currentState.getMap()[currentY - 1][currentX] != this.player.getEqualOpponentType()
                && currentState.getMap()[currentY - 1][currentX] != Type.FENCE) {

            // move one step
            possibleActions.add(new Action(Action.Move.UP));
        }

        // check if we can go right
        if (currentX < currentState.getMap()[0].length - 1
                && currentState.getMap()[currentY][currentX + 1] != this.player.getTeamMateType()
                && currentState.getMap()[currentY][currentX + 1] != this.player.getEqualOpponentType()
                && currentState.getMap()[currentY][currentX + 1] != Type.FENCE) {

            // move one step
            possibleActions.add(new Action(Action.Move.RIGHT));
        }

        // check if we can go down
        if (currentY < currentState.getMap().length - 1
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
        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        Type[][] newMap = currentState.cloneMap();
        newMap[currentY][currentX] = Type.EMPTY;

        State newState = null;

        // TODO: check whether map is actually updated
        switch (action.getMove()) {
            case UP:
                newState = new State(newMap, currentState.getTurn(), currentX, currentY - 1);
                newState.getMap()[currentY - 1][currentX] = this.player.getType();

                if (newState.getMap()[currentY - 1][currentX] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX, currentY - 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }

                break;
            case RIGHT:
                newState = new State(newMap, currentState.getTurn(), currentX + 1, currentY);
                newState.getMap()[currentY][currentX + 1] = this.player.getType();

                if (newState.getMap()[currentY][currentX + 1] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX + 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }

                break;
            case DOWN:
                newState = new State(newMap, currentState.getTurn(), currentX, currentY + 1);
                newState.getMap()[currentY + 1][currentX] = this.player.getType();

                if (newState.getMap()[currentY + 1][currentX] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX, currentY + 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }

                break;
            case LEFT:
                newState = new State(newMap, currentState.getTurn(), currentX - 1, currentY);
                newState.getMap()[currentY][currentX - 1] = this.player.getType();

                if (newState.getMap()[currentY][currentX - 1] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX - 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }

                break;
            case WAIT:
                // no collision can happen if we stay
                newState = new State(newMap, currentState.getTurn(), currentX, currentY);
                newState.getMap()[currentY][currentX] = this.player.getType();
                break;
        }


        return newState;
    }

}