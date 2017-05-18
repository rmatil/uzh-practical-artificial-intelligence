package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.Move;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

public interface IRule {

    int MAX_INCENTIVE = 1;
    int MIN_INCENTIVE = - 1;

    Pair<Float, Move> getMove(Context context);
}
