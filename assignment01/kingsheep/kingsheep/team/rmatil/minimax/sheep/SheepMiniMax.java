package kingsheep.team.rmatil.minimax.sheep;

import kingsheep.Type;
import kingsheep.team.rmatil.UzhShortNameCreature;
import kingsheep.team.rmatil.minimax.Collision;
import kingsheep.team.rmatil.minimax.MiniMax;
import kingsheep.team.rmatil.minimax.Player;
import kingsheep.team.rmatil.minimax.State;

public class SheepMiniMax extends MiniMax {

    public SheepMiniMax(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     *
     * The only terminal state there is for a sheep, is being eaten by the opponents wolf
     */
    @Override
    protected boolean terminalTest(State currentState) {
        if (currentState.getTurn() == UzhShortNameCreature.MAX_NUMBER_OF_TURNS) {
            return true;
        }

        for (Collision collision : currentState.getCollisions()) {
            for (Type collisionParticipant: collision.getCollisionParticipants()) {

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
            return Integer.MIN_VALUE;
        }

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        if (currentState.getMap()[currentY][currentX] == Type.RHUBARB) {
            // double the value, so that the incentive for the wolf is still reasonable
            return 10;
        }

        if (currentState.getMap()[currentY][currentX] == Type.GRASS) {
            // double the value, so that the incentive for the wolf is still reasonable
            return 2;
        }

        // we are indifferent otherwise
        return 0;
    }
}
