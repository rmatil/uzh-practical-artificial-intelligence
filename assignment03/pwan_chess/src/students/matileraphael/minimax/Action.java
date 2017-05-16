package students.matileraphael.minimax;

public class Action {

    public enum Move {LEFT, RIGHT, UP, DOWN, WAIT}

    private final Move move;

    public Action(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }
}
