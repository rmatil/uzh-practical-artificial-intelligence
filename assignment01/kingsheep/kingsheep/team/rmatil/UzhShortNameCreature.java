package kingsheep.team.rmatil;

import kingsheep.Creature;
import kingsheep.Simulator;
import kingsheep.Type;

/**
 * Created by kama on 04.03.16.
 */
public abstract class UzhShortNameCreature extends Creature {

    public final static int MAX_NUMBER_OF_TURNS = 100;

    protected int currentTurn;

    public UzhShortNameCreature(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
        this.currentTurn = 1;
    }

    public String getNickname(){
        return "rmatil";
    }

    protected void think(Type[][] map) {
        this.currentTurn++;
    }
}
