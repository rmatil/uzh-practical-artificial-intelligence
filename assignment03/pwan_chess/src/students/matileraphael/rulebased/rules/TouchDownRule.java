package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A rule which returns moves representing a step forward in order to make a touch down to the other side of the board.
 */
public class TouchDownRule implements IRule {

    @Override
    public Pair<Float, Move> getMove(Context context) {
        List<Pair<Float, Move>> possibleMoves = new ArrayList<>();

        Map<Coordinates, Figure> figures = context.getBoard().figures(context.getColor());

        // white starts from bottom of field
        int directionFactor = context.getColor().equals(Color.WHITE) ? 1 : - 1;

        for (Map.Entry<Coordinates, Figure> entry : figures.entrySet()) {
            Coordinates upperStraight = new Coordinates(entry.getKey().getRow() + directionFactor, entry.getKey().getColumn());

            if (context.getBoard().onBoard(upperStraight) && context.getBoard().figureAt(upperStraight) == null) {
                int touchDownPossible = this.touchDownPossible(entry.getKey(), context.getBoard(), context.getColor(), directionFactor);

                if (touchDownPossible > 0) {
                    float distanceToTargetFactor = (1 / (float) Math.pow(touchDownPossible, 2)) * 0.05f;
                    float incentive = (IRule.MAX_INCENTIVE * 0.95f) + distanceToTargetFactor;

                    possibleMoves.add(new Pair<>(incentive, new Move(entry.getKey(), upperStraight)));
                }
            }
        }

        if (possibleMoves.size() > 0) {
            Collections.sort(possibleMoves);
            return possibleMoves.get(possibleMoves.size() - 1);
        }

        return null;
    }

    private int touchDownPossible(Coordinates startingPosition, Board board, Color ownColor, int directionIndicator) {

        // avoid starting at position we are currently standing on
        int distanceToTarget = 0;
        for (int i = startingPosition.getRow() + directionIndicator; (i < board.height && i >= 0); i = i + directionIndicator) {
            Coordinates target = new Coordinates(i, startingPosition.getColumn());
            Coordinates leftTarget = new Coordinates(i, startingPosition.getColumn() - 1);
            Coordinates rightTarget = new Coordinates(i, startingPosition.getColumn() + 1);

            if (! board.onBoard(target) || board.figureAt(target) != null) {
                distanceToTarget = - 1;
                break;
            }

            if (! board.onBoard(leftTarget) || (board.figureAt(leftTarget) != null && ! board.figureAt(leftTarget).color.equals(ownColor))) {
                distanceToTarget = - 1;
                break;
            }

            if (! board.onBoard(rightTarget) || (board.figureAt(rightTarget) != null && ! board.figureAt(rightTarget).color.equals(ownColor))) {
                distanceToTarget = - 1;
                break;
            }

            distanceToTarget++;
        }

        return distanceToTarget;
    }
}
