package raphaelmatile.problemassigner;

import raphaelmatile.Square;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class SquareAssigner implements IProblemAssigner<Square> {

    private Queue<Square> constrainedProblem = new LinkedBlockingQueue<>();

    public SquareAssigner(final Map<Integer, Map<Integer, Square>> field) {
        for (int y = 0; y < field.size(); y++) {
            for (int x = 0; x < field.get(0).size(); x++) {
                this.constrainedProblem.add(field.get(y).get(x));
            }
        }
    }

    @Override
    public void addProblem(Square problem) {
        this.constrainedProblem.add(problem);
    }

    @Override
    public void removeProblem(Square problem) {
        this.constrainedProblem.remove(problem);
    }

    @Override
    public boolean hasProblemsToSolve() {
        return ! this.constrainedProblem.isEmpty();
    }

    @Override
    public Square getNextProblemToSolve() {
        return this.constrainedProblem.iterator().next();
    }
}
