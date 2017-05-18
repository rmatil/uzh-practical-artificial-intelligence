package students;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.commons.Context;
import students.matileraphael.rulebased.RuleBasedAgent;

public class MatileRaphael implements Agent {

    private RuleBasedAgent agent;

    public MatileRaphael() {
        this.agent = new RuleBasedAgent();
    }

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

        return this.agent.makeNextMove(new Context(yourColor, board, history));
    }

}
