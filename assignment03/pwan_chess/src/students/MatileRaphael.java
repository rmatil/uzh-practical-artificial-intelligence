package students;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.minimax.MiniMax;

import java.util.Map;

public class MatileRaphael implements Agent {


    private int initialStartingPosition = 0;

    /* (non-Javadoc)
     * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#developerAlias()
     */
    @Override
    public String developerAlias() {
        // TODO If you want to use a pseudonym instead of your real name on the (published) list of results
        // return your pseudonym here
        return "rmatil";
    }

    /* (non-Javadoc)
     * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#nextMove(ch.uzh.ifi.ddis.pai.chessim.game.Color, ch.uzh.ifi.ddis.pai.chessim.game.SlidingWindow, ch.uzh.ifi.ddis.pai.chessim.game.History, long)
     */
    @Override
    public Move nextMove(Color yourColor, Board board, History history, long timeLimit) {
        // white color is represented by X on the display

        // calculate the initial starting side
        if (history.size() <= 2) {
            for (Map.Entry<Coordinates, Figure> entry : board.figures(yourColor).entrySet()) {
                if (entry.getKey().getRow() > (board.height / 2)) {
                    this.initialStartingPosition = board.height;
                }
            }
        }

        MiniMax miniMax = new MiniMax(yourColor);
        return miniMax.slidingWindowMinimax(board, this.initialStartingPosition, history.size());
    }

}
