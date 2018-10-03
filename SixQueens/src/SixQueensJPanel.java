import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * Class SixQueensJPanel provides a Swing widget for displaying a Six Queens
 * game board.
 *
 * @author  Alan Kaminsky
 * @version 01-Mar-2018
 */
public class SixQueensJPanel
	extends JPanel
	{

// Exported data members.

	public static final int ROWS = 6;
	public static final int COLS = 6;

// Hidden data members.

	private static final int L = 1;
	private static final int W = 40;
	private static final int D = 20;

	private static final Color LINE_COLOR = Color.BLACK;
	private static final Color SQUARE_COLOR = Color.WHITE;
	private static final Color QUEEN_COLOR = Color.BLUE;

	private static final Stroke LINE_STROKE = new BasicStroke
		(L, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);

	private boolean[][] visible = new boolean [ROWS] [COLS];
	private boolean[][] queen = new boolean [ROWS] [COLS];
	private SixQueensJPanelListener listener;

// Exported constructors.

	/**
	 * Construct a new Six Queens game board widget.
	 */
	public SixQueensJPanel()
		{
		super();
		Dimension d = new Dimension (COLS*W, ROWS*W);
		setMinimumSize (d);
		setPreferredSize (d);
		setMaximumSize (d);
		addMouseListener (new MouseAdapter()
			{
			public void mouseClicked (MouseEvent e)
				{
				if (listener != null)
					{
					int r = e.getY()/(L+W);
					int c = e.getX()/(L+W);
					if (visible[r][c] && ! queen[r][c])
						listener.squareClicked (r, c);
					}
				}
			});
		}

// Exported operations.

	/**
	 * Set this game board widget's listener.
	 *
	 * @param  listener  Board listener.
	 */
	public void setListener
		(SixQueensJPanelListener listener)
		{
		this.listener = listener;
		}

	/**
	 * Clear this game board widget.
	 */
	public void clear()
		{
		for (int r = 0; r < ROWS; ++ r)
			for (int c = 0; c < COLS; ++ c)
				{
				visible[r][c] = true;
				queen[r][c] = false;
				}
		repaint();
		}

	/**
	 * Set the given square visible or invisible.
	 *
	 * @param  r  Row.
	 * @param  c  Column.
	 * @param  v  True for visible, false for invisible.
	 */
	public void setVisible
		(int r,
		 int c,
		 boolean v)
		{
		visible[r][c] = v;
		repaint();
		}

	/**
	 * Set the given square with a queen or no queen.
	 *
	 * @param  r  Row.
	 * @param  c  Column.
	 * @param  q  True for queen, false for no queen.
	 */
	public void setQueen
		(int r,
		 int c,
		 boolean q)
		{
		queen[r][c] = q;
		repaint();
		}

// Hidden operations.

	/**
	 * Paint this component.
	 *
	 * @param  g  Graphics context.
	 */
	protected void paintComponent
		(Graphics g)
		{
		super.paintComponent (g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint
			(RenderingHints.KEY_ANTIALIASING,
			 RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw squares.
		for (int r = 0; r < ROWS; ++ r)
			for (int c = 0; c < COLS; ++ c)
				if (visible[r][c])
					{
					// Draw interior.
					g2d.setColor (SQUARE_COLOR);
					g2d.fill (new Rectangle2D.Float (c*W, r*W, W, W));

					// Draw border.
					g2d.setColor (LINE_COLOR);
					g2d.setStroke (LINE_STROKE);
					g2d.draw (new Rectangle2D.Float (c*W, r*W, W-L, W-L));

					// Draw queen.
					if (queen[r][c])
						{
						g2d.setColor (QUEEN_COLOR);
						g2d.fill (new Ellipse2D.Float
							(c*W+(W-D)/2, r*W+(W-D)/2, D, D));
						}
					}
		}

	}
