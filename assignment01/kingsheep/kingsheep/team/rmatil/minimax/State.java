package kingsheep.team.rmatil.minimax;

import kingsheep.Type;

import java.util.ArrayList;
import java.util.List;

public class State {

    private Type            map[][];
    private List<Collision> collisions;
    private int             currentTurn;

    public State(Type[][] map, int currenTurn) {
        this.map = map;
        this.collisions = new ArrayList<>();
        this.currentTurn = currenTurn;
    }

    public Type[][] getMap() {
        return map;
    }

    public void addCollision(Collision collision) {
        this.collisions.add(collision);
    }

    public List<Collision> getCollisions() {
        return collisions;
    }

    public int getTurn() {
        return currentTurn;
    }

    public Type[][] cloneMap() {
        Type[][] clonedMap = new Type[this.map.length][];

        for (int i = 0; i < this.map.length; i++) {
            Type[] column = this.map[i];
            int columnLength = column.length;
            clonedMap[i] = new Type[columnLength];
            System.arraycopy(column, 0, clonedMap[i], 0, columnLength);
        }

        return clonedMap;
    }
}
