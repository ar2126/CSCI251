/**
 * Interface SixQueensJPanelListener specifies the interface for an object that
 * listens for clicks on a {@linkplain SixQueensJPanel}.
 *
 * @author  Alan Kaminsky
 * @version 01-Mar-2018
 */
public interface SixQueensJPanelListener
	{

// Exported operations.

	/**
	 * Report that the given board square was clicked.
	 *
	 * @param  r  Row.
	 * @param  c  Column.
	 */
	public void squareClicked
		(int r,
		 int c);

	}
