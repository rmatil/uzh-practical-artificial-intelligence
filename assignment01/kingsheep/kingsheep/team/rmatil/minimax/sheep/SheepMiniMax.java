package kingsheep.team.rmatil.minimax.sheep;

import kingsheep.Type;
import kingsheep.team.rmatil.UzhShortNameCreature;
import kingsheep.team.rmatil.minimax.Collision;
import kingsheep.team.rmatil.minimax.MiniMax;
import kingsheep.team.rmatil.minimax.Player;
import kingsheep.team.rmatil.minimax.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SheepMiniMax extends MiniMax {

    public static final int MAX_POSITIVE_INCENTIVE = 5;
    public static final int POSITIVE_INCENTIVE     = 1;
    public static final int INDIFFERENT_INCENTIVE  = 0;
    public static final int MAX_NEGATIVE_INCENTIVE = - 2;

    public SheepMiniMax(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The only terminal states there are for a sheep
     * <ul>
     * <li>is being eaten by the opponent's wolf</li>
     * <li>being on a food item</li>
     * </ul>
     */
    @Override
    protected boolean terminalTest(State currentState) {
        if (currentState.getTurn() == UzhShortNameCreature.MAX_NUMBER_OF_TURNS) {
            return true;
        }

        for (Collision collision : currentState.getCollisions()) {
            for (Type collisionParticipant : collision.getCollisionParticipants()) {

                // check if there is a wolf of the other player
                // or any food item
                if (collisionParticipant == this.player.getOppositeOpponentType() ||
                        collisionParticipant == Type.GRASS ||
                        collisionParticipant == Type.RHUBARB) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected int utility(State currentState) {
        if (this.terminalTest(currentState)) {
            for (Collision collision : currentState.getCollisions()) {
                // check whether a wolf has eaten us
                if (collision.getCollisionParticipants().contains(this.player.getOppositeOpponentType())) {
                    return SheepMiniMax.MAX_NEGATIVE_INCENTIVE;
                }

                if (collision.getCollisionParticipants().contains(Type.RHUBARB)) {
                    return SheepMiniMax.MAX_POSITIVE_INCENTIVE;
                }

                if (collision.getCollisionParticipants().contains(Type.GRASS)) {
                    return SheepMiniMax.POSITIVE_INCENTIVE;
                }

            }
        }

        // we are indifferent otherwise
        return SheepMiniMax.INDIFFERENT_INCENTIVE;
    }

    @Override
    protected float heuristicEval(State currentState) {
        // Once we are in here, any collisions with food items
        // or the wolf have been already resolved.
        //
        // -> What we have to do now, is to approximate how good
        //    the current state is based on the position we are on
        //
        // -> the best move we can do is not going closer to any wolf
        //    but at the same time go closer to a food item

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();
        int manhattanWolfDistance;

        List<Integer> rhubarbDistances = new ArrayList<>();
        List<Integer> grassDistances = new ArrayList<>();

        int wolfX = - 1;
        int wolfY = - 1;
        for (int y = 0; y < currentState.getMap().length; y++) {
            for (int x = 0; x < currentState.getMap()[0].length; x++) {
                if (currentState.getMap()[y][x] == this.player.getOppositeOpponentType()) {
                    wolfX = x;
                    wolfY = y;
                    break;
                }
            }

            if (wolfX != - 1 && wolfY != - 1) {
                break;
            }
        }

        manhattanWolfDistance = Math.abs(currentX - wolfX) + Math.abs(currentY - wolfY);

        for (int y = 0; y < currentState.getMap().length; y++) {
            for (int x = 0; x < currentState.getMap()[0].length; x++) {
                // calculate manhattan distance to the next rhubarb item
                if (currentState.getMap()[y][x] == Type.RHUBARB) {
                    int manhattanDistance = Math.abs(currentX - x) + Math.abs(currentY - y);
                    rhubarbDistances.add(manhattanDistance);
                } else if (currentState.getMap()[y][x] == Type.GRASS) {
                    // calculate manhattan distance to the next grass item
                    int manhattanDistance = Math.abs(currentX - x) + Math.abs(currentY - y);
                    grassDistances.add(manhattanDistance);
                }
            }
        }

        // sort ascending for getting min distance
        Collections.sort(rhubarbDistances);
        Collections.sort(grassDistances);

        if (manhattanWolfDistance == 1) {
            // flee...
            return SheepMiniMax.MAX_NEGATIVE_INCENTIVE;
        }

        // TODO: this is wrong, so wrong...

        if (grassDistances.size() > 0 && rhubarbDistances.size() > 0) {
            float rhubarbIncentive = calculateIncentive(rhubarbDistances.get(0), manhattanWolfDistance, Type.RHUBARB);
            float grassIncentive = calculateIncentive(grassDistances.get(0), manhattanWolfDistance, Type.GRASS);

            if (grassIncentive > rhubarbIncentive) {
                return grassIncentive;
            }

        } else if (rhubarbDistances.size() > 0) {
            // no grass anymore or rhubarb distance is better
            return calculateIncentive(rhubarbDistances.get(0), manhattanWolfDistance, Type.RHUBARB);
        } else if (grassDistances.size() > 0) {
            // only grass left...
            return calculateIncentive(grassDistances.get(0), manhattanWolfDistance, Type.GRASS);
        }

        // we choose the distance to the wolf if neither grass nor rhubarb exists
        return calculateWolfIncentive(manhattanWolfDistance);
    }

    private float calculateIncentive(int distance, int wolfDistance, Type type) {
        float itemIncentive;

        switch (type) {
            case RHUBARB:
                itemIncentive = SheepMiniMax.MAX_POSITIVE_INCENTIVE;
                break;
            case GRASS:
                itemIncentive = SheepMiniMax.POSITIVE_INCENTIVE;
                break;
            default:
                throw new RuntimeException("Type " + type + " not recognized");
        }

        return (1f / distance) * (1f / itemIncentive) - Math.abs((1f / wolfDistance) * (SheepMiniMax.MAX_NEGATIVE_INCENTIVE / 1f));
    }

    private float calculateWolfIncentive(int wolfDistance) {
        return (- 1f) * (1f / wolfDistance) * (SheepMiniMax.MAX_NEGATIVE_INCENTIVE / 1f);
    }
}
