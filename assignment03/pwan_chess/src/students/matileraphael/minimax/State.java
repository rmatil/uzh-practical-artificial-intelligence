package students.matileraphael.minimax;


import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import students.matileraphael.slidingwindow.SlidingWindow;

public class State {

    private int           origBoardWidth;
    private int           origBoardHeight;
    private int           startingSide;
    private int           targetSide;
    private SlidingWindow slidingWindow;
    private Color         color;
    private int           currentDepth;
    private int           turn;

    public State(SlidingWindow slidingWindow, Color color, int origBoardWidth, int origBoardHeight, int startingSide, int currentDepth, int turn) {
        this.origBoardWidth = origBoardWidth;
        this.origBoardHeight = origBoardHeight;
        this.slidingWindow = slidingWindow;
        this.color = color;
        this.startingSide = startingSide;
        this.targetSide = Math.abs(startingSide - slidingWindow.getBoard().height);
        this.currentDepth = currentDepth;
        this.turn = turn;
    }

    public int getOrigBoardWidth() {
        return origBoardWidth;
    }

    public int getOrigBoardHeight() {
        return origBoardHeight;
    }

    public SlidingWindow getSlidingWindow() {
        return slidingWindow;
    }

    public Color getColor() {
        return color;
    }

    public int getStartingSide() {
        return startingSide;
    }

    public int getTargetSide() {
        return targetSide;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public int getTurn() {
        return turn;
    }
}
