package students.matileraphael.slidingwindow;

import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;

public class SlidingWindow {

    private int    x;
    private int    y;
    private Board  board;
    private String strRepresentation;
    private SlidingWindow previousSlidingWindow;

    public SlidingWindow(final int x, final int y, final Board board, final String strRepresentation) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.strRepresentation = strRepresentation;
    }

    public SlidingWindow(final Board board, final SlidingWindow previousSlidingWindow) {
        this.board = board;
        this.previousSlidingWindow = previousSlidingWindow;
        this.strRepresentation = createStringRepresentation(board);
    }

    private String createStringRepresentation(final Board board) {
        StringBuilder stringBuilder = new StringBuilder();

        if (this.previousSlidingWindow != null) {
            stringBuilder.append(this.previousSlidingWindow.getStrRepresentation());
        }

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Figure fig = board.figureAt(new Coordinates(y, x));

                if (null == fig) {
                    stringBuilder.append("null");
                } else {
                    stringBuilder.append(fig.color);
                }
            }
        }

        return stringBuilder.toString();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Board getBoard() {
        return board;
    }

    public String getStrRepresentation() {
        return strRepresentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlidingWindow that = (SlidingWindow) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (board != null ? ! board.equals(that.board) : that.board != null) return false;
        if (strRepresentation != null ? ! strRepresentation.equals(that.strRepresentation) : that.strRepresentation != null)
            return false;
        return previousSlidingWindow != null ? previousSlidingWindow.equals(that.previousSlidingWindow) : that.previousSlidingWindow == null;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + (board != null ? board.hashCode() : 0);
        result = 31 * result + (strRepresentation != null ? strRepresentation.hashCode() : 0);
        result = 31 * result + (previousSlidingWindow != null ? previousSlidingWindow.hashCode() : 0);
        return result;
    }
}
