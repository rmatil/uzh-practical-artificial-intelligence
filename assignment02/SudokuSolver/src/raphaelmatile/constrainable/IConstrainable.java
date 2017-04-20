package raphaelmatile.constrainable;

import raphaelmatile.problemassigner.IProblemAssigner;

public interface IConstrainable<P> {

    IProblemAssigner<P> getProblemAssigner();

    void updateConstraints(P square);

    void resetConstraintsAfterUpdate(P square);

    // TODO: make generic. must equal domain assigner
    boolean isValid(P square, Integer value);
}
