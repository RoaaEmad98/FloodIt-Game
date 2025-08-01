import java.awt.Color;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * In the application <b>FloodIt</b>, a <b>DotButton</b> is a specialized color of
 * <b>JButton</b> that represents a dot in the game. It can have one of six colors
 * 
 * The icon images are stored in a subdirectory ``data''. We have 3 sizes, ``normal'',
 * ``medium'' and ``small'', respectively in directory ``N'', ``M'' and ``S''.
 *
 * The images are 
 * ball-0.png => grey icon
 * ball-1.png => orange icon
 * ball-2.png => blue icon
 * ball-3.png => green icon
 * ball-4.png => purple icon
 * ball-5.png => red icon
 *
 *  <a href=
 * "http://developer.apple.com/library/safari/#samplecode/Puzzler/Introduction/Intro.html%23//apple_ref/doc/uid/DTS10004409"
 * >Based on Puzzler by Apple</a>.
 * @author Guy-Vincent Jourdan, University of Ottawa
 */

public class DotButton extends JButton {
    
    /**
     * The button color. 
     */
    private int color;
    
    /**
     * This instance variable is true if the button is selected.
     */
    private boolean selected = false;
    
    /**
     * The coordinate of this button.
     */
    private int row, column;
    
    /**
     * icon size is small, medium, or large.
     */
    private int iconSize;
    
    /**
     * A an array is used to cache all the images. Since the images are not
     * modified. All the cells that display the same image reuse the same
     * <b>ImageIcon</b> object. Notice the use of the keyword <b>static</b>.
     */
    private static final ImageIcon[] icons = new ImageIcon[GameModel.NUMBER_OF_COLORS];

    /**
     * Constructor used for initializing a cell of a specified color.
     * 
     * @param row
     *            the row of this Cell
     * @param column
     *            the column of this Cell
     * @param color
     *            specifies the color of this cell
     * @param iconSize
     *            specifies the size to use, one of SMALL_SIZE, MEDIUM_SIZE or MEDIUM_SIZE
     */

    public DotButton(int row, int column, int color, int iconSize) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.iconSize = iconSize;
        setBackground(Color.WHITE);
	setIcon(getImageIcon());
	Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	setBorder(emptyBorder);
	setBorderPainted(false);

   }

 /**
     * Other constructor used for initializing a cell of a specified color.
     * no row or column info available. Uses "-1, -1" for row and column instead
     * 
     * @param color
     *            specifies the color of this cell
     * @param iconSize
     *            specifies the size to use, one of SMALL_SIZE, MEDIUM_SIZE or MEDIUM_SIZE
     */   
    public DotButton(int color, int iconSize) {
        row = -1;
        column = -1;
        this.color = color;
        this.iconSize = iconSize;
    }
 
    /**
     * Changes the cell color of this cell. The image is updated accordingly.
     * 
     * @param color
     *            the color to set
     */
    public void setColor(int color) {
        this.color = color;
        setIcon(getImageIcon());
        
    }

    /**
     * Getter for color
     *
     * @return color
     */
    public int getColor(){
        return color;
    }
 
    /**
     * Getter method for the attribute row.
     * 
     * @return the value of the attribute row
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter method for the attribute column.
     * 
     * @return the value of the attribute column
     */
    public int getColumn() {
        return column;
    }
    
    public ImageIcon getImageIcon() {
	if (icons[color] == null) {
	    icons[color] = new ImageIcon(DotButton.class.getResource(
                    "data/M/ball-" + color + ".png"));
	}
	return icons[color];
    }
}
