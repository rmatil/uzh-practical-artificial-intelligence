package kingsheep.team.rmatil.minimax.wolf;

import kingsheep.Type;
import kingsheep.team.rmatil.UzhShortNameCreature;
import kingsheep.team.rmatil.minimax.Collision;
import kingsheep.team.rmatil.minimax.MiniMax;
import kingsheep.team.rmatil.minimax.Player;
import kingsheep.team.rmatil.minimax.State;
import kingsheep.team.rmatil.minimax.sheep.SheepMiniMax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WolfMiniMax extends MiniMax {

    public static final int MAX_POSITIVE_INCENTIVE = 100;
    public static final int POSITIVE_INCENTIVE     = 1;
    public static final int INDIFFERENT_INCENTIVE  = 0;
    public static final int MAX_NEGATIVE_INCENTIVE = - 2;

    public WolfMiniMax(Player player) {
        super(player);
    }

    @Override
    protected boolean terminalTest(State currentState) {
        if (currentState.getTurn() == UzhShortNameCreature.MAX_NUMBER_OF_TURNS) {
            return true;
        }

        for (Collision collision : currentState.getCollisions()) {
            for (Type collisionParticipant : collision.getCollisionParticipants()) {

                // check if there is a sheep of the other player
                if (collisionParticipant == this.player.getOppositeOpponentType()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected int utility(State currentState) {
        // only terminal for wolf is by eating the other sheep
        if (this.terminalTest(currentState)) {
            return WolfMiniMax.MAX_POSITIVE_INCENTIVE;
        }

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        if (currentState.getMap()[currentY][currentX] == Type.RHUBARB) {
            return WolfMiniMax.POSITIVE_INCENTIVE;
        }

        if (currentState.getMap()[currentY][currentX] == Type.GRASS) {
            return WolfMiniMax.POSITIVE_INCENTIVE;
        }

        // TODO: block other wolf

        // we are indifferent otherwise
        return WolfMiniMax.INDIFFERENT_INCENTIVE;
    }

    @Override
    protected float heuristicEval(State currentState) {
        // the best move we can do is going closer to the other sheep
        // or eat food items

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        List<Integer> foodItemDistances = new ArrayList<>();

        // TODO: block other wolf
        int opponentSheepX = - 1;
        int opponentSheepY = - 1;
        int opponentSheepDistance = - 1;
        for (int y = 0; y < currentState.getMap().length; y++) {
            for (int x = 0; x < currentState.getMap()[0].length; x++) {
                if (currentState.getMap()[y][x] == this.player.getOppositeOpponentType()) {
                    opponentSheepX = x;
                    opponentSheepY = y;
                    break;
                }
            }

            if (opponentSheepX != - 1 && opponentSheepY != - 1) {
                break;
            }
        }

        opponentSheepDistance = Math.abs((currentX - opponentSheepX) + (currentY - opponentSheepY));

        for (int y = 0; y < currentState.getMap().length; y++) {
            for (int x = 0; x < currentState.getMap()[0].length; x++) {
                // calculate manhattan distance to the next rhubarb item
                if (currentState.getMap()[y][x] == Type.RHUBARB || currentState.getMap()[y][x] == Type.GRASS) {
                    int manhattanDistance = Math.abs(currentX - x) + Math.abs(currentY - y);
                    int manhattanWolfDistance = Math.abs(opponentSheepX - x) + Math.abs(opponentSheepY - y);

                    foodItemDistances.add(manhattanDistance - manhattanWolfDistance);
                }
            }
        }

        // sort ascending for getting min distance
        Collections.sort(foodItemDistances);

        int proportionFactor = 20;
        if (foodItemDistances.size() > 0
                && Math.round(WolfMiniMax.MAX_POSITIVE_INCENTIVE / proportionFactor) * foodItemDistances.get(0) > opponentSheepDistance) {
            // use weighted distances with their expected values
            return roundToNextHalfAndRoundToInt(scaleDistanceToFoodIncentive(foodItemDistances.get(0)));
        }

        // we choose the distance to the sheep if no food items exist or they are too far away
        return roundToNextHalfAndRoundToInt(scaleDistanceToSheepIncentive((currentX - opponentSheepX) + (currentY - opponentSheepY)));
    }

    private float scaleDistanceToFoodIncentive(int value) {
        return ((float) value - SheepMiniMax.MAX_NEGATIVE_INCENTIVE) / (SheepMiniMax.POSITIVE_INCENTIVE - SheepMiniMax.MAX_NEGATIVE_INCENTIVE);
    }

    private float scaleDistanceToSheepIncentive(int value) {
        return ((float) value - SheepMiniMax.MAX_NEGATIVE_INCENTIVE) / (SheepMiniMax.MAX_POSITIVE_INCENTIVE - SheepMiniMax.MAX_NEGATIVE_INCENTIVE);
    }

    private int roundToNextHalfAndRoundToInt(float value) {
        return (int) Math.round(Math.round(value - 0.5) + 0.5);
    }
}
