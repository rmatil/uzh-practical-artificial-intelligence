package students;

import ch.uzh.ifi.ddis.pai.chessim.game.Agent;
import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.History;
import ch.uzh.ifi.ddis.pai.chessim.game.Move;

public class RaphaelMatile implements Agent{

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#developerAlias()
	 */
	@Override
	public String developerAlias() {
		// TODO If you want to use a pseudonym instead of your real name on the (published) list of results
		// return your pseudonym here
		return this.getClass().getSimpleName();
	}

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#nextMove(ch.uzh.ifi.ddis.pai.chessim.game.Color, ch.uzh.ifi.ddis.pai.chessim.game.Board, ch.uzh.ifi.ddis.pai.chessim.game.History, long)
	 */
	@Override
	public Move nextMove(Color yourColor, Board board, History history,
			long timeLimit) {
		// TODO Write your AI here.
		// You may add additional source files, but make sure to put them into a separate package named with your first and last name.
		return null;
	}
 

}
