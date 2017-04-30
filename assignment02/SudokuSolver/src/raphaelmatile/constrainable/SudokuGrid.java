package raphaelmatile.constrainable;

import raphaelmatile.domainprovider.IDomainProvider;
import raphaelmatile.domainprovider.IntegerDomainProvider;
import raphaelmatile.problem.Square;
import raphaelmatile.problemprovider.IProblemProvider;
import raphaelmatile.problemprovider.MinRemainingValuesSquareProvider;

import java.util.*;

/**
 * Represents the sudoku world in terms of a Constraint Satisfaction Problem, specified by a square,
 * and its domain, specified by an integer.
 */
public class SudokuGrid implements IConstrainable<Square, Integer> {

    private       IProblemProvider<Square>           problemProvider;
    private       Map<Integer, Map<Integer, Square>> field;
    private final int[][]                            originalGrid;
    private final int                                dimension;
    private final int                                gridLength;

    /**
     * Creates a new sudoku Constraint Satisfaction Problem
     *
     * @param dimension    The dimension of a small square of a sudoku out of which the grid is composed
     * @param originalGrid The original grid. Must be of size <code>dimension</code><sup>2</sup>
     */
    public SudokuGrid(final int dimension, final int[][] originalGrid) {
        this.dimension = dimension;
        this.gridLength = this.dimension * this.dimension;
        this.originalGrid = originalGrid;
        this.field = new HashMap<>();

        this.initializeField();

        this.problemProvider = new MinRemainingValuesSquareProvider(this.field);
    }

    @Override
    public IProblemProvider<Square> getProblemProvider() {
        return this.problemProvider;
    }

    @Override
    public List<Square> updateConstraints(Square updatedProblem) {
        List<Square> updatedFields = new ArrayList<>();

        // search for horizontal and vertical squares at the same x resp. y position
        // and add the value of the given square as constraint
        for (int xy = 0; xy < this.gridLength; xy++) {
            Square tmp = this.field.get(xy).get(updatedProblem.getXPos());

            if (! tmp.isSolved() &&
                    updatedProblem.getYPos() != xy &&
                    ! tmp.isConstrainedBy(updatedProblem.getValue())
                    ) {

                // add constraint for the value we are currently evaluating at the given square
                tmp.addConstraint(updatedProblem.getValue());
                if (! updatedFields.contains(tmp)) {
                    updatedFields.add(tmp);
                }
            }

            tmp = this.field.get(updatedProblem.getYPos()).get(xy);
            if (! tmp.isSolved() &&
                    updatedProblem.getXPos() != xy &&
                    ! tmp.isConstrainedBy(updatedProblem.getValue())) {

                // add constraint for the value we are currently evaluating at the given square
                tmp.addConstraint(updatedProblem.getValue());
                if (! updatedFields.contains(tmp)) {
                    updatedFields.add(tmp);
                }
            }
        }

        // now we also have to search within the small box of size=dimension for any squares and add the constraint to them:
        // Divide by the dimension to get the factor of which box we are in. Multiply by the dimension, to get the correct lower boundary
        int yLowerBoundary = (updatedProblem.getYPos() / this.dimension) * this.dimension;
        int xLowerBoundary = (updatedProblem.getXPos() / this.dimension) * this.dimension;

        for (int y = yLowerBoundary; y < yLowerBoundary + this.dimension; y++) {
            for (int x = xLowerBoundary; x < xLowerBoundary + this.dimension; x++) {
                Square tmp = this.field.get(y).get(x);

                // check whether the square is already solved, and whether we are looking at the square given
                if (! tmp.isSolved() &&
                        ! tmp.isConstrainedBy(updatedProblem.getValue()) &&
                        x != updatedProblem.getXPos() &&
                        y != updatedProblem.getYPos()) {
                    tmp.addConstraint(updatedProblem.getValue());
                    if (! updatedFields.contains(tmp)) {
                        updatedFields.add(tmp);
                    }
                }
            }
        }

        return updatedFields;
    }

    @Override
    public boolean isValid(Square problem, Integer domainValue) {
        // search for horizontal and vertical squares at the same x resp. y position
        // and verify, whether they have the same value or not
        for (int xy = 0; xy < this.gridLength; xy++) {
            // traverse all vertical fields on the same y position first...
            Square tmp = this.field.get(xy).get(problem.getXPos());
            if (tmp.getYPos() != problem.getYPos() &&
                    tmp.getValue().equals(domainValue)) {
                return false;
            }

            // ... then traverse all horizontal fields on the same x position
            tmp = this.field.get(problem.getYPos()).get(xy);
            if (tmp.getXPos() != problem.getXPos() &&
                    tmp.getValue().equals(domainValue)) {
                return false;
            }
        }

        // now we also have to search within the small box of size=dimension for any squares and add the constraint to them:
        // Divide by the dimension to get the factor of which box we are in. Multiply by the dimension, to get the correct lower boundary
        int yLowerBoundary = (problem.getYPos() / this.dimension) * this.dimension;
        int xLowerBoundary = (problem.getXPos() / this.dimension) * this.dimension;

        for (int y = yLowerBoundary; y < yLowerBoundary + this.dimension; y++) {
            for (int x = xLowerBoundary; x < xLowerBoundary + this.dimension; x++) {
                Square tmp = this.field.get(y).get(x);

                if (y == problem.getYPos() && x == problem.getXPos()) {
                    continue;
                }

                // check whether the square contains the same value...
                if (x != problem.getXPos() && y != problem.getYPos() &&
                        tmp.getValue().equals(domainValue)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public IDomainProvider<Integer> getNewDomainAssigner() {
        return new IntegerDomainProvider(1, this.gridLength);
    }

    @Override
    public void resetConstraintsAfterUpdate(List<Square> updatedFields, Square updatedProblem) {
        for (Square tmp : updatedFields) {
            tmp.removeConstraint(updatedProblem.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        for (int y = 0; y < this.dimension * this.dimension; y++) {
            for (int x = 0; x < this.dimension * this.dimension; x++) {
                ret.append(this.field.get(y).get(x).getValue()).append(" ");
            }

            ret.append("\n");
        }

        return ret.toString();
    }

    /**
     * Returns an array representation of the current sudoku grid.
     *
     * @return The sudoku grid
     */
    public int[][] asArray() {
        int[][] field = new int[this.gridLength][this.gridLength];

        for (int y = 0; y < this.dimension * this.dimension; y++) {
            for (int x = 0; x < this.dimension * this.dimension; x++) {

                field[y][x] = this.field.get(y).get(x).getValue();
            }
        }

        return field;
    }

    /**
     * Initialize the Grid with the predefined values and add
     * all constraints based on them.
     */
    private void initializeField() {
        for (int y = 0; y < this.dimension * this.dimension; y++) {
            for (int x = 0; x < this.dimension * this.dimension; x++) {
                if (! this.field.containsKey(y)) {
                    this.field.put(y, new HashMap<>());
                }

                Set<Integer> constraints = new HashSet<>();
                this.field.get(y).put(x, new Square(x, y, constraints, this.originalGrid[y][x]));
            }
        }

        // Eventually, add all constraints of the predefined fields
        for (Map.Entry<Integer, Map<Integer, Square>> row : this.field.entrySet()) {
            for (Map.Entry<Integer, Square> entry : row.getValue().entrySet()) {
                if (entry.getValue().getValue() != Square.EMPTY) {
                    this.updateConstraints(entry.getValue());
                }
            }
        }
    }
}
