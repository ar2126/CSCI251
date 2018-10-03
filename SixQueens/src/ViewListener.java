
/**
 * Interface ViewListener specifies the interface for an object that receives
 * reports from the View in the Six Queens Game.
 *
 * @author  Aidan Rubenstein
 * @author Alan Kaminsky
 * @version 02-Apr-2018
 */
public interface ViewListener
{

// Exported operations.

    /**
     * Report that a player joined the game.
     *
     * @param  view  View object making the report.
     * @param  name  Player's name.
     */
    public void join
    (ModelListener view,
     String name);

    /**
     * Report that a square was chosen.
     *
     * @param  view  View object making the report.
     * @param  r     Square index.
     */
    public void squareChosen
    (ModelListener view,
     int r, int c);

    /**
     * Report that a new game was requested.
     *
     * @param  view  View object making the report.
     */
    public void newGame
    (ModelListener view);

    /**
     * Report that the player quit.
     *
     * @param  view  View object making the report.
     */
    public void quit
    (ModelListener view);

}