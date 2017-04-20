import raphaelmatile.BackTracker;
import raphaelmatile.problem.Square;
import raphaelmatile.constrainable.SudokuGrid;

public class RaphaelMatile implements SudokuAgent {

    private String name;

    RaphaelMatile() {
        this.name = "Raphael Matile";
    }


    public int[][] solve(int dimension, int[][] puzzle) {
        SudokuGrid grid = new SudokuGrid(dimension, puzzle);
        BackTracker<Square, Integer> backTracker = new BackTracker<>(grid);


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
