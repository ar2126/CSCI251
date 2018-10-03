/**
 * Interface ModelListener specifies the interface for an object that receives
 * reports from the Model in the Six Queens Game.
 *
 * @author  Aidan Rubenstein
 * @version 02-Apr-2018
 */
public interface ModelListener
{

// Exported operations.

    /**
     * Report that a new game was started.
     */
    public void newGame();

    /**
     * Report that a mark was placed on a square.
     *
     * @param  r     Square index.
     * @param  mark  Mark.
     */
    public void setMark
    (int r, int c,
     Mark mark);

    /**
     * Report a winning combination.
     *
     * @param  i  Winning combination number.
     */
    public void setWin
    (int i);

    /**
     * Report that the player is waiting for a partner.
     */
    public void waitingForPartner();

    /**
     * Report that it's the player's turn.
     */
    public void yourTurn();

    /**
     * Report that it's the other player's turn.
     *
     * @param  name  Other player's name.
     */
    public void otherTurn
    (String name);

    /**
     * Report that the player wins.
     */
    public void youWin();

    /**
     * Report that the other player wins.
     *
     * @param  name  Other player's name.
     */
    public void otherWin
    (String name);

    /**
     * Report that the game is a draw.
     */
    public void draw();

    /**
     * Report that a player quit.
     */
    public void quit();

}