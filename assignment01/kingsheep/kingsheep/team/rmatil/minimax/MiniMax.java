package kingsheep.team.rmatil.minimax;

import kingsheep.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class MiniMax {

    public static final int MAX_DEPTH = 2;

    protected Player player;

    /**
     * Alpha for alpha-beta pruning
     */
    private float a;

    /**
     * Beta for alpha-beta pruning
     */
    private float b;

    public MiniMax(Player player) {
        this.player = player;
        this.a = Integer.MIN_VALUE;
        this.b = Integer.MAX_VALUE;
    }

    public Action minimaxDecision(State state) {
        Map<Float, Action> possibleActions = new TreeMap<>();

        for (Action action : actions(state)) {
            State tmp = result(state, action);

            // check whether we are min or max player
            if (this.player.getPlayerId() == 1) {
                possibleActions.put(maxValue(tmp), action);
            } else {
                possibleActions.put(minValue(tmp), action);
            }
        }

        Action[] orderedActions = new Action[possibleActions.values().size()];
        possibleActions.values().toArray(orderedActions);

        if (orderedActions.length > 0) {
            // list is enumerated in ascending order
            if (this.player.getPlayerId() == 1) {
                return orderedActions[orderedActions.length - 1];
            }

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
     * @param currentState The state to evaluate
     *
     * @return The value indicator expressing how good the given state is
     */
    protected abstract int utility(State currentState);

    /**
     * Returns a heuristic, i.e. estimated value on how good the given state is
     *
     * @param currentState The state to evaluate
     *
     * @return The value indicator expressing how good the given state is
     */
    protected abstract float heuristicEval(State currentState) ;

    private float minValue(final State state) {
        if (terminalTest(state)) {
            return utility(state);
        }

        if (cutoffTest(state)) {
            return heuristicEval(state);
        }

        float v = Integer.MAX_VALUE;
        Action[] actions = actions(state);
        for (Action action : actions) {
            State appliedState = result(state, action);
            v = Math.min(v, maxValue(appliedState));

            if (v <= this.a) {
                return v;
            }
            this.b = Math.min(this.b, v);
        }

        return v;
    }

    private float maxValue(final State state) {
        if (terminalTest(state)) {
            return utility(state);
        }

        if (cutoffTest(state)) {
            return heuristicEval(state);
        }

        float v = Integer.MIN_VALUE;
        Action[] actions = actions(state);
        for (Action action : actions) {
            State appliedState = result(state, action);
            v = Math.max(v, minValue(appliedState));

            if (v >= this.b) {
                return v;
            }
            this.a = Math.max(this.a, v);
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
     * The transition model, which defines the result of a move,
     * a.k.a. the applied action to the given state
     */
    private State result(final State currentState, final Action action) {
        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        Type[][] newMap = currentState.cloneMap();
        newMap[currentY][currentX] = Type.EMPTY;

        State newState = null;

        switch (action.getMove()) {
            case UP:
                newState = new State(newMap, currentState.getTurn(), currentX, currentY - 1, currentState.getCurrentDepth() + 1);

                if (newState.getMap()[currentY - 1][currentX] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX, currentY - 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                } else if (newState.getMap()[currentY - 1][currentX] == Type.GRASS ) {
                    Collision collision = new Collision(currentX, currentY - 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(Type.GRASS);

                    newState.addCollision(collision);
                } else if (newState.getMap()[currentY - 1][currentX] == Type.RHUBARB) {
                    Collision collision = new Collision(currentX, currentY - 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(Type.RHUBARB);

                    newState.addCollision(collision);
                }

                newState.getMap()[currentY - 1][currentX] = this.player.getType();

                break;
            case RIGHT:
                newState = new State(newMap, currentState.getTurn(), currentX + 1, currentY, currentState.getCurrentDepth() + 1);

                if (newState.getMap()[currentY][currentX + 1] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX + 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                } else if (newState.getMap()[currentY][currentX + 1] == Type.GRASS ) {
                    Collision collision = new Collision(currentX + 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(Type.GRASS);

                    newState.addCollision(collision);
                } else if (newState.getMap()[currentY][currentX + 1] == Type.RHUBARB) {
                    Collision collision = new Collision(currentX + 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(Type.RHUBARB);

                    newState.addCollision(collision);
                }

                newState.getMap()[currentY][currentX + 1] = this.player.getType();
                break;
            case DOWN:
                newState = new State(newMap, currentState.getTurn(), currentX, currentY + 1, currentState.getCurrentDepth() + 1);

                if (newState.getMap()[currentY + 1][currentX] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX, currentY + 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }  else if (newState.getMap()[currentY + 1][currentX] == Type.GRASS ) {
                    Collision collision = new Collision(currentX, currentY + 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(Type.GRASS);

                    newState.addCollision(collision);
                } else if (newState.getMap()[currentY + 1][currentX] == Type.RHUBARB) {
                    Collision collision = new Collision(currentX, currentY + 1);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(Type.RHUBARB);

                    newState.addCollision(collision);
                }

                newState.getMap()[currentY + 1][currentX] = this.player.getType();
                break;
            case LEFT:
                newState = new State(newMap, currentState.getTurn(), currentX - 1, currentY, currentState.getCurrentDepth() + 1);

                if (newState.getMap()[currentY][currentX - 1] == this.player.getOppositeOpponentType()) {
                    Collision collision = new Collision(currentX - 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(this.player.getOppositeOpponentType());

                    newState.addCollision(collision);
                }  else if (newState.getMap()[currentY][currentX - 1] == Type.GRASS ) {
                    Collision collision = new Collision(currentX - 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(Type.GRASS);

                    newState.addCollision(collision);
                } else if (newState.getMap()[currentY][currentX - 1] == Type.RHUBARB) {
                    Collision collision = new Collision(currentX - 1, currentY);
                    collision.addCollisionParticipant(this.player.getType());
                    collision.addCollisionParticipant(Type.RHUBARB);

                    newState.addCollision(collision);
                }

                newState.getMap()[currentY][currentX - 1] = this.player.getType();
                break;
            case WAIT:
                // no collision can happen if we stay
                newState = new State(newMap, currentState.getTurn(), currentX, currentY, currentState.getCurrentDepth() + 1);
                newState.getMap()[currentY][currentX] = this.player.getType();
                break;
        }


        return newState;
    }

}
