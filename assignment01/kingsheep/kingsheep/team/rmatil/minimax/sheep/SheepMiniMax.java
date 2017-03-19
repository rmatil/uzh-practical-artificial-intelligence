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
    public static final int POSITIVE_INCENTIVE = 1;
    public static final int INDIFFERENT_INCENTIVE = 0;
    public static final int MAX_NEGATIVE_INCENTIVE = -2;

    public SheepMiniMax(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The only terminal state there is for a sheep, is being eaten by the opponents wolf
     */
    @Override
    protected boolean terminalTest(State currentState) {
        if (currentState.getTurn() == UzhShortNameCreature.MAX_NUMBER_OF_TURNS) {
            return true;
        }

        for (Collision collision : currentState.getCollisions()) {
            for (Type collisionParticipant : collision.getCollisionParticipants()) {

                // check if there is a wolf of the other player
                if (collisionParticipant == this.player.getOppositeOpponentType()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected int utility(State currentState) {
        // only terminal for sheep is being eaten by the other wolf
        if (this.terminalTest(currentState)) {
            return SheepMiniMax.MAX_NEGATIVE_INCENTIVE;
        }

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        if (currentState.getMap()[currentY][currentX] == Type.RHUBARB) {
            return SheepMiniMax.MAX_POSITIVE_INCENTIVE;
        }

        if (currentState.getMap()[currentY][currentX] == Type.GRASS) {
            return SheepMiniMax.POSITIVE_INCENTIVE;
        }

        // we are indifferent otherwise
        return SheepMiniMax.INDIFFERENT_INCENTIVE;
    }

    @Override
    protected int heuristicEval(State currentState) {
        // the best move we can do is not going closer to any wolf
        // but at the same time go closer to a food item

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

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

        for (int y = 0; y < currentState.getMap().length; y++) {
            for (int x = 0; x < currentState.getMap()[0].length; x++) {
                // calculate manhattan distance to the next rhubarb item
                if (currentState.getMap()[y][x] == Type.RHUBARB) {
                    int manhattanDistance = Math.abs(currentX - x) + Math.abs(currentY - y);
                    int manhattanWolfDistance = Math.abs(wolfX - x) + Math.abs(wolfY - y);

                    if (manhattanWolfDistance < 3) {
                        manhattanDistance = manhattanDistance - manhattanWolfDistance;
                    }

                    rhubarbDistances.add(manhattanDistance);
                } else if (currentState.getMap()[y][x] == Type.GRASS) {
                    // calculate manhattan distance to the next grass item
                    int manhattanDistance = Math.abs(currentX - x) + Math.abs(currentY - y);
                    int manhattanWolfDistance = Math.abs(wolfX - x) + Math.abs(wolfY - y);

                    if (manhattanWolfDistance < 3) {
                        manhattanDistance = manhattanDistance - manhattanWolfDistance;
                    }

                    grassDistances.add(manhattanDistance);
                }
            }
        }

        // sort ascending for getting min distance
        Collections.sort(rhubarbDistances);
        Collections.sort(grassDistances);

        if (grassDistances.size() > 0
                && rhubarbDistances.size() > 0
                && SheepMiniMax.MAX_POSITIVE_INCENTIVE * grassDistances.get(0) > rhubarbDistances.get(0)) {
            // use weighted distances with their expected values
            return roundToNextHalfAndRoundToInt(scaleDistanceToFoodIncentive(grassDistances.get(grassDistances.size() - 1) - grassDistances.get(0)));
        } else if (rhubarbDistances.size() > 0) {
            // no grass anymore or rhubarb distance is better
            return roundToNextHalfAndRoundToInt(scaleDistanceToFoodIncentive(rhubarbDistances.get(rhubarbDistances.size() - 1) - rhubarbDistances.get(0)));
        } else if (grassDistances.size() > 0) {
            // only grass left...
            return roundToNextHalfAndRoundToInt(scaleDistanceToFoodIncentive(grassDistances.get(grassDistances.size() - 1) - grassDistances.get(0)));
        }

        // we choose the distance to the wolf if neither grass nor rhubarb exists
        return roundToNextHalfAndRoundToInt(scaleDistanceToWolfIncentive((currentX - wolfX) + (currentY - wolfY)));
    }

    private float scaleDistanceToFoodIncentive(int value) {
        return ((float) value - SheepMiniMax.MAX_NEGATIVE_INCENTIVE) / (SheepMiniMax.MAX_POSITIVE_INCENTIVE - SheepMiniMax.MAX_NEGATIVE_INCENTIVE);
    }

    private float scaleDistanceToWolfIncentive(int value) {
        return ((float) value - SheepMiniMax.MAX_NEGATIVE_INCENTIVE) / (SheepMiniMax.POSITIVE_INCENTIVE - SheepMiniMax.MAX_NEGATIVE_INCENTIVE);
    }

    private int roundToNextHalfAndRoundToInt(float value) {
        return (int) Math.round(Math.round(value - 0.5) + 0.5);
    }
}
