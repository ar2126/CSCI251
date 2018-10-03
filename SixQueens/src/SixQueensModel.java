/**
 * Class SixQueensModel provides the application logic for the Six Queens Game.
 *
 * @author  Aidan Rubenstein
 * @author Alan Kaminsky
 * @version 02-Apr-2018
 */
public class SixQueensModel
        implements ViewListener
{

// Hidden data members.

    private String name1;
    private String name2;
    private ModelListener view1;
    private ModelListener view2;
    private ModelListener turn;
    private boolean isFinished;
    private BoardState board = new BoardState();

// Exported constructors.

    /**
     * Construct a new Six Queens model object.
     */
    public SixQueensModel()
    {
    }

// Exported operations.

    /**
     * Report that a player joined the game.
     *
     * @param  view  View object making the report.
     * @param  name  Player's name.
     */
    public synchronized void join
    (ModelListener view,
     String name)
    {
        if (name1 == null)
        {
            name1 = name;
            view1 = view;
            view1.waitingForPartner();
        }
        else
        {
            name2 = name;
            view2 = view;
            doNewGame();
        }
    }

    /**
     * Report that a square was chosen.
     *
     * @param  view  View object making the report.
     * @param  r     Square index.
     */
    public synchronized void squareChosen
    (ModelListener view,
     int r, int c)
    {
        if (view != turn || board.getMark (r,c) != Mark.BLANK)
            return;
        else if (view == view1) {
            setMark(view1, r, c, Mark.X);
        }
        else
            setMark (view2, r, c, Mark.O);
    }

    /**
     * Report that a new game was requested.
     *
     * @param  view  View object making the report.
     */
    public synchronized void newGame
    (ModelListener view)
    {
        if (name2 != null)
            doNewGame();
    }

    /**
     * Report that the player quit.
     *
     * @param  view  View object making the report.
     */
    public synchronized void quit
    (ModelListener view)
    {
        if (view1 != null)
            view1.quit();
        if (view2 != null)
            view2.quit();
        turn = null;
        isFinished = true;
    }

    /**
     * Determine whether this game session is finished.
     *
     * @return  True if finished, false if not.
     */
    public synchronized boolean isFinished()
    {
        return isFinished;
    }

// Hidden operations.

    /**
     * Start a new game.
     */
    private void doNewGame()
    {
        // Clear the board and inform the players.
        board.clear();
        view1.newGame();
        view2.newGame();

        // Player 1 gets the first turn.
        turn = view1;
        view1.yourTurn();
        view2.otherTurn (name1);
    }

    /**
     * Set a mark on the board and switch turns.
     *
     * @param  curr  View object whose turn it is.
     * @param  r     Square index.
     * @param  mark  Mark.
     */
    private void setMark
    (ModelListener curr,
     int r, int c,
     Mark mark)
    {
        // Set the mark and inform the players.
        board.setMark (r, c, mark);
        view1.setMark (r, c, mark);
        view2.setMark (r, c, mark);

        // Check for a winning combination.
        int win = board.checkWin (mark);

        // Update game state and inform the players.
        if (win != -1)
        {
            // Current player wins.
            turn = null;
            board.setWin (win);
            view1.setWin (win);
            view2.setWin (win);
            if (curr == view1)
            {
                view1.youWin();
                view2.otherWin (name1);
            }
            else
            {
                view1.otherWin (name2);
                view2.youWin();
            }
        }
        else if (board.checkDraw())
        {
            // Game is a draw.
            turn = null;
            view1.draw();
            view2.draw();
        }
        else
        {
            // No win or draw yet.
            if (curr == view1)
            {
                turn = view2;
                view1.otherTurn (name2);
                view2.yourTurn();
            }
            else
            {
                turn = view1;
                view1.yourTurn();
                view2.otherTurn (name1);
            }
        }
    }

}