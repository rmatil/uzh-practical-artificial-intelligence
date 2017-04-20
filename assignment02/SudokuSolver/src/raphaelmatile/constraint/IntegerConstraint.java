package raphaelmatile.constraint;

import java.util.HashSet;
import java.util.Set;

public class IntegerConstraint implements IConstraint<Integer> {

    private Set<Integer> constraints = new HashSet<>();

    @Override
    public void addConstraint(Integer constraint) {
        this.constraints.add(constraint);
    }

    @Override
    public void removeConstraint(Integer constraint) {
        this.constraints.remove(constraint);
    }

    @Override
    public boolean isConstrainedBy(Integer constraint) {
        return this.constraints.contains(constraint);
    }

    @Override
    public boolean isAssignable(Integer value) {
        return ! this.constraints.contains(value);
    }

    @Override
    public int size() {
        return this.constraints.size();
    }
}
