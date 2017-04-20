import raphaelmatile.BackTracker;
import raphaelmatile.constrainable.SudokuGrid;
import raphaelmatile.domainassigner.IDomainAssigner;
import raphaelmatile.domainassigner.IntegerDomainAssigner;
import raphaelmatile.problemassigner.IProblemAssigner;
import raphaelmatile.problemassigner.SquareAssigner;

public class RaphaelMatile implements SudokuAgent {

    private String name;

    RaphaelMatile() {
        this.name = "Raphael Matile";
    }


    public int[][] solve(int dimension, int[][] puzzle) {
        SudokuGrid grid = new SudokuGrid(dimension, puzzle);

        BackTracker backTracker = new BackTracker(grid);
        boolean result = backTracker.backtrack();

        if (! result) {
            System.err.println("Oups, something went wrong");
        }


        return grid.asArray();
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
