package raphaelmatile.problem;

import raphaelmatile.constraint.IConstraint;
import raphaelmatile.problemvalue.IProblemValue;

public interface IProblem<V, C> {

    IProblemValue<V> getProblemValue();

    IConstraint<C> getConstraint();

    boolean isSolved();
}
