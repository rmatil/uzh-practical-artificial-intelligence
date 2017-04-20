package raphaelmatile.problemassigner;

import raphaelmatile.Square;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MinRemainingValuesSquareAssigner implements IProblemAssigner<Square> {

    private Set<Square> constrainedProblem = new HashSet<>();

    public MinRemainingValuesSquareAssigner(final Map<Integer, Map<Integer, Square>> field) {
        for (int y = 0; y < field.size(); y++) {
            for (int x = 0; x < field.get(0).size(); x++) {
                if (! field.get(y).get(x).isSolved()) {
                    this.constrainedProblem.add(field.get(y).get(x));
                }
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
        // return the field with the most constraints
        return Collections.max(this.constrainedProblem, (o1, o2) -> {
            if (null == o1) {
                return - 1;
            }
            if (null == o2) {
                return 1;
            }

            if (o1.getConstraint().size() == o2.getConstraint().size()) {
                return 0;
            }

            return (o1.getConstraint().size() < o2.getConstraint().size()) ? - 1 : 1;
        });
    }
}
