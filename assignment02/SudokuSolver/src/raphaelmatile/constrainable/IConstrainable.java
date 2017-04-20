package raphaelmatile.constrainable;

import raphaelmatile.domainprovider.IDomainProvider;
import raphaelmatile.problem.IProblem;
import raphaelmatile.problemprovider.IProblemProvider;

/**
 * Represents a world with a problem on a particular domain,
 * which can be solved using a Constraint Satisfaction Problem
 *
 * @param <P> A class which represents the problem on a particular domain
 * @param <D> The domain on which the problem exists
 */
public interface IConstrainable<P extends IProblem<D, D>, D> {

    /**
     * Returns a problem assigner, responsible for
     * releasing new problems of this constraint.
     *
     * @return The problem assigner
     */
    IProblemProvider<P> getProblemAssigner();

    /**
     * Updates the current constraints on the problem.
     * Use {@link IConstrainable#resetConstraintsAfterUpdate(IProblem)} to reset applied changes on this constraint
     * if the given problem does not lead to a solution.
     *
     * @param updatedProblem An updated problem whose changes should also be reflected on this constraint
     */
    void updateConstraints(P updatedProblem);

    /**
     * Resets all changes made by an updated problem on this constraint.
     * If no updates were caused by the given problem, applying this method has no effect.
     *
     * @param updatedProblem The updated problem which may have caused some updates on this constraint
     */
    void resetConstraintsAfterUpdate(P updatedProblem);

    /**
     * Returns true, if the given domain value can be safely applied to the given problem.
     *
     * @param problem     The problem on which the application should be validated
     * @param domainValue The value which should be evaluated on the problem
     *
     * @return True, if the domainValue can be safely applied on the given problem, false otherwise
     */
    boolean isValid(P problem, D domainValue);

    /**
     * Returns a new assigner instance of the problem domain
     *
     * @return A new domain assigner
     */
    IDomainProvider<D> getNewDomainAssigner();
}
