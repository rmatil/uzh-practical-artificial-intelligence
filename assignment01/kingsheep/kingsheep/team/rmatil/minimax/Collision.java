package kingsheep.team.rmatil.minimax;

import kingsheep.Type;

import java.util.ArrayList;
import java.util.List;

public class Collision {

    private int x;
    private int y;
    private List<Type> collisionParticipants;

    public Collision(int x, int y) {
        this.x = x;
        this.y = y;
        this.collisionParticipants = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addCollisionParticipant(Type participant) {
        this.collisionParticipants.add(participant);
    }

    public List<Type> getCollisionParticipants() {
        return collisionParticipants;
    }
}
