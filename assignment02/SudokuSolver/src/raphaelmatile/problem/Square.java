package raphaelmatile.problem;

import java.util.Set;

/**
 * A Square of a SudokuGrid, i.e. a single Field. In the world of our Constraint Satisfaction Problems,
 * this class represents a particular problem, which can be solved on the domain of Integers with constraints
 * of Integers.
 */
public class Square implements IProblem<Integer, Integer> {

    public static final int EMPTY = 0;

    private Set<Integer> constraints;
    private Integer      value;
    private int          xPos;
    private int          yPos;

    /**
     * @param xPos        The x-position of this square in the grid
     * @param yPos        The y-position of this square in the grid
     * @param constraints A set of constraints on the value of this square. Can be empty, if no constraint is set yet
     * @param value       The actual value of this square
     */
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

    /**
     * Returns the x-position of this square in the grid
     *
     * @return The x position
     */
    public int getXPos() {
        return this.xPos;
    }

    /**
     * Returns the y-position of this square in the grid
     *
     * @return The y position
     */
    public int getYPos() {
        return this.yPos;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Square)) {
            return false;
        }

        Square squ = (Square) obj;

        // we only compare the current position of the grid
        return (this.xPos == squ.getXPos()) && (this.yPos == squ.getYPos());
    }
}
