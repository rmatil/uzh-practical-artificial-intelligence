package kingsheep.team.rmatil.minimax;

import kingsheep.Type;

public class Player {

    private final Type type;
    private final int  playerId;

    public Player(Type type, int playerId) {
        this.type = type;
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Type getType() {
        return type;
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
