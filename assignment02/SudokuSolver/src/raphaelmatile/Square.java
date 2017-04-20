package raphaelmatile;

import raphaelmatile.constraint.IConstraint;
import raphaelmatile.problem.IProblem;
import raphaelmatile.problemvalue.IProblemValue;

public class Square implements IProblem<Integer, Integer> {

    public static final int EMPTY = 0;

    private IConstraint<Integer>   constraint;
    private IProblemValue<Integer> value;
    private int                    xPos;
    private int                    yPos;

    public Square(int xPos, int yPos, IConstraint<Integer> constraint, IProblemValue<Integer> value) {
        this.constraint = constraint;
        this.value = value;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public IConstraint<Integer> getConstraint() {
        return this.constraint;
    }

    @Override
    public IProblemValue<Integer> getProblemValue() {
        return this.value;
    }

    @Override
    public boolean isSolved() {
        return this.value.getValue() != Square.EMPTY;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
