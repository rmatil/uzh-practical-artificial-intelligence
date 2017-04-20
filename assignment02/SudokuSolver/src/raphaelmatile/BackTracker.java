package raphaelmatile;

import raphaelmatile.constrainable.IConstrainable;
import raphaelmatile.domainassigner.IDomainAssigner;
import raphaelmatile.domainassigner.IntegerDomainAssigner;

public class BackTracker {

    private IConstrainable<Square> constrainable;

    public BackTracker(IConstrainable<Square> constrainable) {
        this.constrainable = constrainable;
    }

    public boolean backtrack() {
        if (! this.constrainable.getProblemAssigner().hasProblemsToSolve()) {
            return true;
        }

        // TODO: change the assigner's value
        IDomainAssigner<Integer> domainAssigner = new IntegerDomainAssigner(1, 9);
        Square nextSquareToSolve = this.constrainable.getProblemAssigner().getNextProblemToSolve();

        while (domainAssigner.hasNextDomainValue()) {
            Integer nextDomainValue = domainAssigner.getNextDomainValue();

            if (! nextSquareToSolve.getConstraint().isAssignable(nextDomainValue)) {
                continue;
            }

            if (this.constrainable.isValid(nextSquareToSolve, nextDomainValue)) {
//                System.out.println("--------------------------");
//                System.out.println("x: " + nextSquareToSolve.getXPos() + ", y: " + nextSquareToSolve.getYPos());
//                System.out.println("nextDomainValue " + nextDomainValue);
//                System.out.println(this.constrainable.toString());
                this.constrainable.getProblemAssigner().removeProblem(nextSquareToSolve);

                // set value and find all new constraints based on this assignment
                int originalValue = nextSquareToSolve.getProblemValue().getValue();
                nextSquareToSolve.getProblemValue().setValue(nextDomainValue);
                this.constrainable.updateConstraints(nextSquareToSolve);

                if (this.backtrack()) {
                    // we found a solution
                    return true;
                }

                this.constrainable.resetConstraintsAfterUpdate(nextSquareToSolve);
                nextSquareToSolve.getProblemValue().setValue(originalValue);

                this.constrainable.getProblemAssigner().addProblem(nextSquareToSolve);
            }
        }

        return false;
    }
}
