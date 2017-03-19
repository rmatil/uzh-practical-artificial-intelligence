package kingsheep.team.rmatil;

import kingsheep.Simulator;
import kingsheep.Type;
import kingsheep.team.rmatil.minimax.Action;
import kingsheep.team.rmatil.minimax.MiniMax;
import kingsheep.team.rmatil.minimax.Player;
import kingsheep.team.rmatil.minimax.State;
import kingsheep.team.rmatil.minimax.sheep.SheepMiniMax;

import java.util.logging.Logger;

public class Wolf extends UzhShortNameCreature {

    private static final Logger logger = Logger.getLogger(Sheep.class.getName());

    public Wolf(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
    }

    protected void think(Type map[][]) {
        /*
        TODO
		YOUR WOLF CODE HERE
		
		BASE YOUR LOGIC ON THE INFORMATION FROM THE ARGUMENT map[][]
		
		YOUR CODE NEED TO BE DETERMINISTIC. 
		THAT MEANS, GIVEN A DETERMINISTIC OPPONENT AND MAP THE ACTIONS OF YOUR WOLF HAVE TO BE REPRODUCIBLE
		
		SET THE MOVE VARIABLE TO ONE TOF THE 5 VALUES
        move = Move.UP;
        move = Move.DOWN;
        move = Move.LEFT;
        move = Move.RIGHT;
        move = Move.WAIT;
		*/

        State state = new State(map, this.currentTurn, this.x, this.y);
        Player player = new Player(this.type, playerID);
        logger.info("current x: " + this.x + ", current y: " + this.y);

        MiniMax miniMax = new SheepMiniMax(player);
        Action actionToExecute = miniMax.minimaxDecision(state);

        switch (actionToExecute.getMove()) {
            case UP:
                move = Move.UP;
                break;
            case RIGHT:
                move = Move.RIGHT;
                break;
            case DOWN:
                move = Move.DOWN;
                break;
            case LEFT:
                move = Move.LEFT;
                break;
            case WAIT:
                move = Move.WAIT;
                break;
        }

        logger.info("Wolf made move " + move.name());
        super.think(map);
    }
}