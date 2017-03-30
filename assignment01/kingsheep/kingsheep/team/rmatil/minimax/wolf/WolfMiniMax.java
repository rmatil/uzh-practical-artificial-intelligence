package kingsheep.team.rmatil.minimax.wolf;

import kingsheep.Type;
import kingsheep.team.rmatil.UzhShortNameCreature;
import kingsheep.team.rmatil.minimax.Collision;
import kingsheep.team.rmatil.minimax.MiniMax;
import kingsheep.team.rmatil.minimax.Player;
import kingsheep.team.rmatil.minimax.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class WolfMiniMax extends MiniMax {

    private static final Logger logger = Logger.getLogger(WolfMiniMax.class.getName());

    public static final int MAX_POSITIVE_INCENTIVE    = 5;
    public static final int BETTER_POSITIVE_INCENTIVE = 2;
    public static final int POSITIVE_INCENTIVE        = 1;
    public static final int INDIFFERENT_INCENTIVE     = 0;

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
                // or any type of food
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
    protected float utility(State currentState) {
        // only terminal for wolf is by eating the other sheep
        if (this.terminalTest(currentState)) {
            return (1 / currentState.getCurrentDepth()) * WolfMiniMax.MAX_POSITIVE_INCENTIVE;
        }

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        if (currentState.getMap()[currentY][currentX] == Type.RHUBARB) {
            return (1 / currentState.getCurrentDepth()) * WolfMiniMax.POSITIVE_INCENTIVE;
        }

        if (currentState.getMap()[currentY][currentX] == Type.GRASS) {
            return (1 / currentState.getCurrentDepth()) * WolfMiniMax.POSITIVE_INCENTIVE;
        }

        // we are indifferent otherwise
        return (1 / currentState.getCurrentDepth()) * WolfMiniMax.INDIFFERENT_INCENTIVE;
    }

    @Override
    protected float heuristicEval(State currentState) {
        // Once we are in here, any collisions with food items
        // or the sheep have been already resolved.
        //
        // -> What we have to do now, is to approximate how good
        //    the current state is based on the position we are on
        //
        // -> the best move we can do is going closer to the other sheep
        //    while also going closer to any food item

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();
        int manhattanSheepDistance;

        List<Integer> rhubarbDistances = new ArrayList<>();
        List<Integer> grassDistances = new ArrayList<>();

        int sheepX = - 1;
        int sheepY = - 1;
        for (int y = 0; y < currentState.getMap().length; y++) {
            for (int x = 0; x < currentState.getMap()[0].length; x++) {
                if (currentState.getMap()[y][x] == this.player.getOppositeOpponentType()) {
                    sheepX = x;
                    sheepY = y;
                    break;
                }
            }

            if (sheepX != - 1 && sheepY != - 1) {
                break;
            }
        }

        manhattanSheepDistance = Math.abs(currentX - sheepX) + Math.abs(currentY - sheepY);
        // we choose the distance to the wolf if neither grass nor rhubarb exists
        float sheepIncentive = calculateSheepIncentive(manhattanSheepDistance);
        logger.info(String.format("[depth: %d][x: %d, y: %d][sheep] heuristic utility: %.2f", currentState.getCurrentDepth(), currentState.getCurrentX(), currentState.getCurrentY(), sheepIncentive));

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

        // check if there is a food item with bigger incentive than the sheep
        if (rhubarbDistances.size() > 0 && grassDistances.size() > 0) {
            float rhubarbIncentive = calculateFoodIncentive(rhubarbDistances.get(0), manhattanSheepDistance, Type.RHUBARB);
            float grassIncentive = calculateFoodIncentive(grassDistances.get(0), manhattanSheepDistance, Type.GRASS);

            if (rhubarbIncentive > sheepIncentive && grassIncentive > sheepIncentive) {
                if (rhubarbIncentive > grassIncentive) {
                    logger.info(String.format("[depth: %d][x: %d, y: %d][food] heuristic utility: %.2f", currentState.getCurrentDepth(), currentState.getCurrentX(), currentState.getCurrentY(), rhubarbIncentive));
                    return rhubarbIncentive;
                }

                logger.info(String.format("[depth: %d][x: %d, y: %d][food] heuristic utility: %.2f", currentState.getCurrentDepth(), currentState.getCurrentX(), currentState.getCurrentY(), grassIncentive));
                return grassIncentive;
            }
        }

        // check if there is a rhubarb item with bigger incentive than the sheep
        if (rhubarbDistances.size() > 0) {
            float rhubarbIncentive = calculateFoodIncentive(rhubarbDistances.get(0), manhattanSheepDistance, Type.RHUBARB);

            if (rhubarbIncentive > sheepIncentive) {
                logger.info(String.format("[depth: %d][x: %d, y: %d][food] heuristic utility: %.2f", currentState.getCurrentDepth(), currentState.getCurrentX(), currentState.getCurrentY(), rhubarbIncentive));
                return rhubarbIncentive;
            }
        }

        // check if there is a grass item with bigger incentive than the sheep
        if (grassDistances.size() > 0) {
            float grassIncentive = calculateFoodIncentive(grassDistances.get(0), manhattanSheepDistance, Type.GRASS);

            if (grassIncentive > sheepIncentive) {
                logger.info(String.format("[depth: %d][x: %d, y: %d][food] heuristic utility: %.2f", currentState.getCurrentDepth(), currentState.getCurrentX(), currentState.getCurrentY(), grassIncentive));
                return grassIncentive;
            }
        }

        return sheepIncentive;
    }

    private float calculateFoodIncentive(int distance, int sheepDistance, Type type) {
        float itemIncentive;

        switch (type) {
            case RHUBARB:
                itemIncentive = WolfMiniMax.BETTER_POSITIVE_INCENTIVE;
                break;
            case GRASS:
                itemIncentive = WolfMiniMax.POSITIVE_INCENTIVE;
                break;
            default:
                throw new RuntimeException("Type " + type + " not recognized");
        }

        return (1f / distance) * (1f / itemIncentive);
    }

    private float calculateSheepIncentive(int sheepDistance) {
        return (1f / sheepDistance) * (WolfMiniMax.MAX_POSITIVE_INCENTIVE / 1f);
    }

}
