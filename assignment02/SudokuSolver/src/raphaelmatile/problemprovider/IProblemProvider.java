package raphaelmatile.problemprovider;

import raphaelmatile.problem.IProblem;

/**
 * Manages unsolved problems of the given type.
 *
 * @param <T> The type of the problems to provide
 */
public interface IProblemProvider<T extends IProblem> {

    /**
     * Add a new problem to solve.
     *
     * @param problem The problem to solve
     */
    void addProblem(T problem);

    /**
     * Remove a problem from the provider.
     *
     * @param problem The problem to remove
     */
    void removeProblem(T problem);

    /**
     * Returns true, if there are problems waiting to be resolved
     *
     * @return True, if there are problems left, false otherwise
     */
    boolean hasProblemsToSolve();

    /**
     * Returns a problem to solve.
     *
     * @return The problem to solve
     */
    T getNextProblemToSolve();
}
