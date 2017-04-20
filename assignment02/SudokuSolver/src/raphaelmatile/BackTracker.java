package raphaelmatile;

import raphaelmatile.constrainable.IConstrainable;
import raphaelmatile.domainprovider.IDomainProvider;
import raphaelmatile.problem.IProblem;

/**
 * Implements the generic approach to solve Constraint Satisfaction Problems using
 * the backtracking algorithm described in  <i>Artificial Intelligence - A Modern Approach, 3rd Edition</i>
 * on page 215.
 *
 * @param <P> The type of the problem which should be solved
 * @param <D> The domain on which the problem is defined
 */
public class BackTracker<P extends IProblem<D, D>, D> {

    private IConstrainable<P, D> constrainable;

    /**
     * @param constrainable The world of a problem which can be solved using CSPs.
     */
    public BackTracker(IConstrainable<P, D> constrainable) {
        this.constrainable = constrainable;
    }

    /**
     * Solves the problems in the world using backtracking:
     * Values are traversed recursively until a path to a state is found, which solves all current constraints.
     * If the problem is solvable and a solution was found, true is returned, false otherwise.
     *
     * @return True, if a solution was found, false otherwise
     */
    public boolean backtrack() {
        if (! this.constrainable.getProblemAssigner().hasProblemsToSolve()) {
            return true;
        }

        // find unsolved problems in the world
        P nextSquareToSolve = this.constrainable.getProblemAssigner().getNextProblemToSolve();

        // get a new instance of a domain provider so that recursive calls work independently
        IDomainProvider<D> domainAssigner = this.constrainable.getNewDomainAssigner();

        while (domainAssigner.hasNextDomainValue()) {
            D nextDomainValue = domainAssigner.getNextDomainValue();

            // check whether current constraints on the problem do not allow us to assign the domain value
            if (! nextSquareToSolve.isAssignable(nextDomainValue)) {
                continue;
            }

            // check whether we are still in a valid state (not evaluated using constraints),
            // if we apply the domain value on the problem
            if (! this.constrainable.isValid(nextSquareToSolve, nextDomainValue)) {
                continue;
            }

            // remove the problem from the unsolved problems
            this.constrainable.getProblemAssigner().removeProblem(nextSquareToSolve);

            // set value and find all new constraints based on this assignment
            D originalValue = nextSquareToSolve.getValue();
            nextSquareToSolve.setValue(nextDomainValue);
            this.constrainable.updateConstraints(nextSquareToSolve);

            // try to find a path which leads us to a state where there are no problems to solve anymore
            if (this.backtrack()) {
                // we found a solution
                return true;
            }

            // the chosen path did not lead to a solution which resolves the problem
            // so we have to re-add it for later evaluation
            this.constrainable.resetConstraintsAfterUpdate(nextSquareToSolve);
            nextSquareToSolve.setValue(originalValue);
            this.constrainable.getProblemAssigner().addProblem(nextSquareToSolve);
        }

        return false;
    }
}
