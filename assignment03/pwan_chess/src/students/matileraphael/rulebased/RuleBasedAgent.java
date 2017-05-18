package students.matileraphael.rulebased;

import ch.uzh.ifi.ddis.pai.chessim.game.Move;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;
import students.matileraphael.rulebased.rules.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleBasedAgent {

    Logger

    private List<IRule> rules = new ArrayList<>();

    public RuleBasedAgent() {
        this.rules.add(new TouchDownRule()); // 1.0
        this.rules.add(new CaptureRule()); // 0.95
        this.rules.add(new IncreaseAttackedPawnsDefenseRule()); // 0.75
        this.rules.add(new CreateDefenseRule()); // 0.70
        this.rules.add(new StraightForwardRule()); // 0.5
    }

    public Move makeNextMove(Context context) {

        List<Pair<Float, Move>> bestMoves = new ArrayList<>();

        for (IRule rule : this.rules) {
            Pair<Float, Move> nextMovePair = rule.getMove(context);

            if (null != nextMovePair) {
                if (!context.getBoard().figureAt(nextMovePair.getValue().from).possibleMoves(context.getBoard()).containsKey(nextMovePair.getValue())) {
                    System.out.println("Invalid move");
                }

                bestMoves.add(nextMovePair);
            }
        }

        // clear created cache for current moving state
        for (IRule rule : this.rules) {
            if (rule instanceof ICachingRule) {
                ((ICachingRule) rule).clearCache();
            }
        }

        Collections.sort(bestMoves);

        if (bestMoves.size() > 0) {
            return bestMoves.get(bestMoves.size() - 1).getValue();
        }

        System.err.println("oh oh shit");
        return new FallbackMoveRule().getMove(context).getValue();
    }
}
