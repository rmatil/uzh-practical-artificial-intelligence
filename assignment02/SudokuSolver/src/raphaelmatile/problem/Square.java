package raphaelmatile.problem;

import java.util.Set;

public class Square implements IProblem<Integer, Integer> {

    public static final int EMPTY = 0;

    private Set<Integer> constraints;
    private Integer      value;
    private int          xPos;
    private int          yPos;

    public Square(int xPos, int yPos, Set<Integer> constraints, Integer value) {
        this.constraints = constraints;
        this.value = value;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean isSolved() {
        return this.value != Square.EMPTY;
    }

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
    public boolean isAssignable(Integer constraint) {
        return ! this.constraints.contains(constraint);
    }

    @Override
    public int constraintSize() {
        return this.constraints.size();
    }

    public int getXPos() {
        return this.xPos;
    }

    public int getYPos() {
        return this.yPos;
    }
}
