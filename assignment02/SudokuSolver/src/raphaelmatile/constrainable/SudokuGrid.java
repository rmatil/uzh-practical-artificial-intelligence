package raphaelmatile.constrainable;

import raphaelmatile.Square;
import raphaelmatile.constraint.IConstraint;
import raphaelmatile.constraint.IntegerConstraint;
import raphaelmatile.domainassigner.IDomainAssigner;
import raphaelmatile.domainassigner.IntegerDomainAssigner;
import raphaelmatile.problemassigner.IProblemAssigner;
import raphaelmatile.problemassigner.MinRemainingValuesSquareAssigner;
import raphaelmatile.problemvalue.IntegerProblemValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SudokuGrid implements IConstrainable<Square> {

    private       IProblemAssigner<Square>           problemAssigner;
    private       IDomainAssigner<Integer>           domainAssigner;
    private       Map<Integer, Map<Integer, Square>> field;
    private       List<Square>                       updatedFields;
    private final int[][]                            originalGrid;
    private       int                                dimension;
    private       int                                gridLength;

    public SudokuGrid(final int dimension, final int[][] originalGrid) {
        this.updatedFields = new ArrayList<>();
        this.dimension = dimension;
        this.gridLength = this.dimension * this.dimension;
        this.originalGrid = originalGrid;
        this.field = new HashMap<>();

        this.initializeField();

        this.problemAssigner = new MinRemainingValuesSquareAssigner(this.field);
        this.domainAssigner = new IntegerDomainAssigner(1, this.gridLength);
    }

    @Override
    public IProblemAssigner<Square> getProblemAssigner() {
        return this.problemAssigner;
    }

    @Override
    public void updateConstraints(Square square) {
        // search for horizontal and vertical squares at the same x resp. y position
        // and add the value of the given square as constraint
        for (int xy = 0; xy < this.gridLength; xy++) {
            Square tmp = this.field.get(xy).get(square.getXPos());

            if (! tmp.isSolved() &&
                    square.getYPos() != xy &&
                    ! tmp.getConstraint().isConstrainedBy(square.getProblemValue().getValue())
                    ) {

                // add constraint for the value we are currently evaluating at the given square
                tmp.getConstraint().addConstraint(square.getProblemValue().getValue());
                if (! updatedFields.contains(tmp)) {
                    updatedFields.add(tmp);
                }
            }

            tmp = this.field.get(square.getYPos()).get(xy);
            if (! tmp.isSolved() &&
                    square.getXPos() != xy &&
                    ! tmp.getConstraint().isConstrainedBy(square.getProblemValue().getValue())) {

                // add constraint for the value we are currently evaluating at the given square
                tmp.getConstraint().addConstraint(square.getProblemValue().getValue());
                if (! updatedFields.contains(tmp)) {
                    updatedFields.add(tmp);
                }
            }
        }

        // now we also have to search within the small box of size=dimension for any squares and add the constraint to them:
        // Divide by the dimension to get the factor of which box we are in. Multiply by the dimension, to get the correct lower boundary
        int yLowerBoundary = (square.getYPos() / this.dimension) * this.dimension;
        int xLowerBoundary = (square.getXPos() / this.dimension) * this.dimension;

        for (int y = yLowerBoundary; y < yLowerBoundary + this.dimension; y++) {
            for (int x = xLowerBoundary; x < xLowerBoundary + this.dimension; x++) {
                Square tmp = this.field.get(y).get(x);

                // check whether the square is already solved, and whether we are looking at the square given
                if (! tmp.isSolved() &&
                        ! tmp.getConstraint().isConstrainedBy(square.getProblemValue().getValue()) &&
                        x != square.getXPos() &&
                        y != square.getYPos()) {
                    tmp.getConstraint().addConstraint(square.getProblemValue().getValue());
                    if (! updatedFields.contains(tmp)) {
                        updatedFields.add(tmp);
                    }
                }
            }
        }
    }

    @Override
    public boolean isValid(Square square, Integer value) {
        // search for horizontal and vertical squares at the same x resp. y position
        // and verify, whether they have the same value or not
        for (int xy = 0; xy < this.gridLength; xy++) {
            // traverse all vertical fields on the same y position first...
            Square tmp = this.field.get(xy).get(square.getXPos());
            if (tmp.getYPos() != square.getYPos() &&
                    tmp.getProblemValue().getValue().equals(value)) {
                return false;
            }

            // ... then traverse all horizontal fields on the same x position
            tmp = this.field.get(square.getYPos()).get(xy);
            if (tmp.getXPos() != square.getXPos() &&
                    tmp.getProblemValue().getValue().equals(value)) {
                return false;
            }
        }

        // now we also have to search within the small box of size=dimension for any squares and add the constraint to them:
        // Divide by the dimension to get the factor of which box we are in. Multiply by the dimension, to get the correct lower boundary
        int yLowerBoundary = (square.getYPos() / this.dimension) * this.dimension;
        int xLowerBoundary = (square.getXPos() / this.dimension) * this.dimension;

        for (int y = yLowerBoundary; y < yLowerBoundary + this.dimension; y++) {
            for (int x = xLowerBoundary; x < xLowerBoundary + this.dimension; x++) {
                Square tmp = this.field.get(y).get(x);

                // check whether the square contains the same value...
                if (x != square.getXPos() && y != square.getYPos() &&
                        tmp.getProblemValue().getValue().equals(value)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void resetConstraintsAfterUpdate(Square square) {
        for (Square tmp : this.updatedFields) {
            tmp.getConstraint().removeConstraint(square.getProblemValue().getValue());
        }
    }

    private void initializeField() {
        for (int y = 0; y < this.dimension * this.dimension; y++) {
            for (int x = 0; x < this.dimension * this.dimension; x++) {
                if (! this.field.containsKey(y)) {
                    this.field.put(y, new HashMap<>());
                }

                IConstraint<Integer> constraint = new IntegerConstraint();
                if (this.originalGrid[y][x] != Square.EMPTY) {
                    constraint.addConstraint(this.originalGrid[y][x]);
                }

                this.field.get(y).put(x, new Square(x, y, constraint, new IntegerProblemValue(this.originalGrid[y][x])));
            }
        }
    }

    public int[][] asArray() {
        int[][] field = new int[this.gridLength][this.gridLength];

        for (int y = 0; y < this.dimension * this.dimension; y++) {
            for (int x = 0; x < this.dimension * this.dimension; x++) {

                field[y][x] = this.field.get(y).get(x).getProblemValue().getValue();
            }
        }

        return field;
    }

    public String toString() {
        String ret = "";

        for (int y = 0; y < this.dimension * this.dimension; y++) {
            for (int x = 0; x < this.dimension * this.dimension; x++) {
                ret += this.field.get(y).get(x).getProblemValue().getValue() + " ";
            }

            ret += "\n";
        }

        return ret;
    }
}
