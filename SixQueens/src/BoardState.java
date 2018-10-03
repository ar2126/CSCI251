import java.util.Arrays;

/**
 * Class BoardState encapsulates the state of a Six Queens game board.
 *
 * @author  Aidan Rubenstein
 * @author Alan Kaminsky
 * @version 02-Apr-2018
 */
public class BoardState
{

// Exported data members.

    /**
     * Number of squares on the board.
     */
    public static final int N_SQUARES = 36;

// Hidden data members.

    private Mark[][] mark;    // Mark on each square
    private boolean[] win;  // True for each square in a winning combination

    // Table of indexes for winning combinations.
    private static final int[][] winCombo = new int[][]
            {
                    // Horizontals
                    { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                    // Verticals
                    { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                    // Diagonals
                    { 0, 4, 8 }, { 2, 4, 6 }
            };

// Exported constructors.

    /**
     * Construct a new board state object.
     */
    public BoardState()
    {
        mark = new Mark [6][6];
        win = new boolean [N_SQUARES];
        clear();
    }

// Exported operations.

    /**
     * Clear this board state object.
     */
    public void clear()
    {
        for(Mark[] r : mark)
            Arrays.fill (r, Mark.BLANK);
        Arrays.fill (win, false);
    }

    /**
     * Set the mark on the given square.
     *
     * @param  r     Square index.
     * @param  mark  Mark.
     */
    public void setMark
    (int r, int c,
     Mark mark)
    {
        this.mark[r][c] = mark;
    }

    /**
     * Get the mark on the given square.
     *
     * @param  i  Square index.
     *
     * @return  Mark.
     */
    public Mark getMark
    (int i, int j)
    {
        return this.mark[i][j];
    }

    /**
     * Determine if the marks on the board are a winning combination for the
     * given player.
     *
     * @param  player  Player (X or O).
     *
     * @return  Winning combination number if the player wins, &minus;1 if not.
     */
    public int checkWin
    (Mark player)
    {
        /*for (int i = 0; i < winCombo.length; ++ i)
            if (mark[winCombo[i][0]] == player &&
                    mark[winCombo[i][1]] == player &&
                    mark[winCombo[i][2]] == player)
                return i;*/
        return -1;
    }

    /**
     * Set a winning combination.
     *
     * @param  i  Winning combination number.
     *
     * @see  #checkWin(Mark)
     */
    public void setWin
    (int i)
    {
        win[winCombo[i][0]] =
                win[winCombo[i][1]] =
                        win[winCombo[i][2]] = true;
    }

    /**
     * Check whether the given square is part of a winning combination.
     *
     * @param  i  Square index.
     *
     * @return  True if square <TT>i</TT> is part of a winning combination,
     *          false if not.
     */
    public boolean getWin
    (int i)
    {
        return win[i];
    }

    /**
     * Determine if the marks on the board are a draw.
     *
     * @return  True if draw, false if not.
     */
    public boolean checkDraw()
    {
        /*for (int i = 0; i < N_SQUARES; ++ i)
            if (mark[i] == Mark.BLANK)
                return false; */
        return true;
    }

}