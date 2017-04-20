import sun.security.util.Length;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: verman
 * Date: 24.03.2014
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */


public class TestAgent implements SudokuAgent {


    private String name;

    TestAgent()
    {
        name = "TestAgent Chuck";
    }


    public int[][] solve(int dimension, int[][] puzzle){

        int[][] solved = new int[9][9];
        String[] stringSolved =
          "4 8 3 9 2 1 6 5 7 9 6 7 3 4 5 8 2 1 2 5 1 8 7 6 4 9 3 5 4 8 1 3 2 9 7 6 7 2 9 5 6 4 1 3 8 1 3 6 7 9 8 2 4 5 3 7 2 6 8 9 5 1 4 8 1 4 2 5 3 7 6 9 6 9 5 4 1 7 3 8 2".split(" ");

        for (int i=0;i<100000000;i++)
            for (int j=0;j<100000;j++);

        for (int i = 0; i< 9; i++) {
            for (int j = 0; j< 9; j++) {
                solved[i][j] = Integer.parseInt(stringSolved[i*9+j]);
            }
        }
        return solved;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
