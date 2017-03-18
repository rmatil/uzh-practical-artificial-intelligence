package kingsheep.team.rmatil.minimax;

import kingsheep.Type;

public class Player {

    private final Type type;
    private final int  playerId;
    private final int  currentX;
    private final int  currentY;

    public Player(Type type, int playerId, int currentX, int currentY) {
        this.type = type;
        this.playerId = playerId;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Type getType() {
        return type;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public Type getTeamMateType() {
        switch (this.type) {
            case SHEEP1:
                return Type.WOLF1;
            case WOLF1:
                return Type.SHEEP1;
            case SHEEP2:
                return Type.WOLF2;
            case WOLF2:
                return Type.SHEEP2;
        }

        return null;
    }

    public Type getEqualOpponentType() {
        switch (this.type) {
            case SHEEP1:
                return Type.SHEEP2;
            case WOLF1:
                return Type.WOLF2;
            case SHEEP2:
                return Type.SHEEP1;
            case WOLF2:
                return Type.WOLF1;
        }

        return null;
    }

    public Type getOppositeOpponentType() {
        switch (this.type) {
            case SHEEP1:
                return Type.WOLF2;
            case WOLF1:
                return Type.SHEEP2;
            case SHEEP2:
                return Type.WOLF1;
            case WOLF2:
                return Type.SHEEP1;
        }

        return null;
    }
}
