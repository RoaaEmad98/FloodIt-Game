/**
 * The class <b>GameView</b> provides the current view of the entire Game. It extends
 * <b>JFrame</b> and lays out the actual game and 
 * two instances of JButton. The action listener for the buttons is the controller.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */


import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
        
public class GameView extends JFrame {
    
    DotButton board [][];
    GameModel model;
    JPanel boardPanel;    
    public JButton resetButton, quitButton;
    public JButton[] buttonIcons;
    public JButton qButton, againButton;
    public JFrame frame;    
    private JTextField nstepsField;
    private ImageIcon[] imageIcons;
    private int nrows, ncols, size;
    private boolean winCreated = false;

    /**
     * Constructor used for initializing the Frame
     * @param model
     *            the model of the game (already initialized)
     * @param gameController
     *            the controller
     */

    public GameView(GameModel model, GameController gameController) {
        super ("Flood it -- the ITI 1121 version");
        this.model = model;
        nrows = ncols = size = model.getSize();
        board = new DotButton[nrows][ncols];
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBackground(Color.WHITE);
        boardPanel = new JPanel ();
        boardPanel.setBackground(Color.WHITE);
	boardPanel.setLayout(new GridLayout(nrows, ncols));
	boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        for (int x = 0; x < nrows; x++) {
	    for (int y = 0; y < ncols; y++) {
		board[x][y] = new DotButton(x, y, model.getColor(x, y), size);
		boardPanel.add(board[x][y]);
	    }
	}
	add(boardPanel, BorderLayout.NORTH);
        
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(Color.WHITE);
	colorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        buttonIcons = new JButton[GameModel.NUMBER_OF_COLORS];
        imageIcons = new ImageIcon[GameModel.NUMBER_OF_COLORS];
        for (int i = 0; i < GameModel.NUMBER_OF_COLORS; i++) {
            imageIcons[i] = new ImageIcon(GameView.class.getResource(
                    "data/N/ball-" + i + ".png"));
            buttonIcons[i] = new JButton();
            buttonIcons[i].setIcon((imageIcons[i]));
            buttonIcons[i].setBackground(Color.WHITE);
            buttonIcons[i].setBorderPainted(false);
            colorPanel.add(buttonIcons[i]);
            buttonIcons[i].addActionListener(gameController);
        }
        add(colorPanel, BorderLayout.CENTER);
        
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.add(new JLabel ("Number of steps: "));        
        nstepsField = new JTextField("0", 2);
        nstepsField.setEditable(false);
        menuPanel.add(nstepsField);
        resetButton = new JButton("Reset");
        menuPanel.add(resetButton);	
	resetButton.addActionListener(gameController);
        quitButton = new JButton("Quit");
        menuPanel.add(quitButton);	
	quitButton.addActionListener(gameController);
        add (menuPanel, BorderLayout.SOUTH);
        setSize(42*model.getSize(), 42*model.getSize());
        setVisible(true);
    }

    /**
     * update the status of the board's DotButton instances based on the current game model
     */
    public void update(){
        for (int x = 0; x < nrows; x++) {
	    for (int y = 0; y < ncols; y++) {
                if (model.isCaptured(x, y)) {
                    board[x][y].setColor(model.getCurrentSelectedColor());
                    board[x][y].setIcon(board[x][y].getImageIcon());
                } else {
                    board[x][y].setColor(model.getColor(x, y));
                    board[x][y].setIcon(board[x][y].getImageIcon());
                }
	    }
	}
        int n = model.getNumberOfSteps();
        String nstr = Integer.toString(n);
        nstepsField.setText(nstr);
        boardPanel.invalidate();
        boardPanel.repaint();
    }
    
    public void congratualtionWindow (GameController gameController) {
        if (!winCreated) {
            winCreated = true;
            frame = new JFrame("WON");
            frame.setLocationByPlatform(true);
            frame.setLayout(new FlowLayout());
            JLabel l1 = new JLabel ("Congratulations, you won in " + 
                    model.getNumberOfSteps() + "!");
            JLabel l2 = new JLabel ("Would you like to play again");
            qButton = new JButton ("Quit");
            qButton.addActionListener(gameController);
            againButton = new JButton ("Play Again");
            againButton.addActionListener(gameController);
            frame.add(l1);
            frame.add(l2);
            JPanel p = new JPanel ();
            p.add(qButton);
            p.add(againButton);
            frame.add(p);
            frame.setSize(250, 150);
        }
        frame.setVisible(true);
    }

}
