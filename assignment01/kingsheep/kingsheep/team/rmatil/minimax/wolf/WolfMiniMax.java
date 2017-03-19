package kingsheep.team.rmatil.minimax.wolf;

import kingsheep.Type;
import kingsheep.team.rmatil.UzhShortNameCreature;
import kingsheep.team.rmatil.minimax.Collision;
import kingsheep.team.rmatil.minimax.MiniMax;
import kingsheep.team.rmatil.minimax.Player;
import kingsheep.team.rmatil.minimax.State;

public class WolfMiniMax extends MiniMax {

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
            return Integer.MAX_VALUE;
        }

        int currentX = currentState.getCurrentX();
        int currentY = currentState.getCurrentY();

        if (currentState.getMap()[currentY][currentX] == Type.RHUBARB) {
            return 1;
        }

        if (currentState.getMap()[currentY][currentX] == Type.GRASS) {
            return 1;
        }

        // TODO: block other wolf

        // we are indifferent otherwise
        return 0;
    }
}
