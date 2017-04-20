package raphaelmatile.problemprovider;

import raphaelmatile.problem.Square;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides new problems of type {@link Square} decreasingly ordered by the amount of constraints they are restricted to.
 * In the world of Constraint Satisfaction Problems, this approach is either called
 * having a <i>minimum remaining variable</i> or a <i>most constrained variable</i>.
 */
public class MinRemainingValuesSquareProvider implements IProblemProvider<Square> {

    private Set<Square> constrainedProblem = new HashSet<>();

    /**
     * Creates a new instance using the given field as a source of problems.
     *
     * @param field The field containing problems
     */
    public MinRemainingValuesSquareProvider(final Map<Integer, Map<Integer, Square>> field) {
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

            if (o1.constraintSize() == o2.constraintSize()) {
                return 0;
            }

            return (o1.constraintSize() < o2.constraintSize()) ? - 1 : 1;
        });
    }
}
