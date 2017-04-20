package raphaelmatile.problem;

/**
 * Represents a particular Constraint Satisfaction Problem which can be solved by
 * adding constraints to it, until it is specified to a single possible value.
 *
 * @param <V> The domain type of the problem
 * @param <C> The constraint type of the problem
 */
public interface IProblem<V, C> {

    /**
     * Returns the value of the problem
     *
     * @return The problem's value
     */
    V getValue();

    /**
     * Sets the value of the problem
     *
     * @param value The value to set
     */
    void setValue(V value);

    /**
     * Returns true, if this problem is solved, i.e. a solution exists.
     *
     * @return True, if this problem is solved, false otherwise
     */
    boolean isSolved();

    /**
     * Adds the given constraint to this problem.
     *
     * @param constraint The constraint to add
     */
    void addConstraint(C constraint);

    /**
     * Removes the given constraint from this problem.
     * If not contained, then applying this method has no effect.
     *
     * @param constraint The constraint to remove
     */
    void removeConstraint(C constraint);

    /**
     * Returns true, if this set of constraints contains the given constraint.
     *
     * @param constraint The constraint to check for
     *
     * @return True, if contained, false otherwise
     */
    boolean isConstrainedBy(C constraint);

    /**
     * Returns true, if this set of constraints does not contain the given constraint.
     *
     * @param constraint The constraint to check for
     *
     * @return True, if not contained, false otherwise
     */
    boolean isAssignable(C constraint);

    /**
     * Returns the number of constraints in this set.
     *
     * @return The number of constraints contained
     */
    int constraintSize();
}
