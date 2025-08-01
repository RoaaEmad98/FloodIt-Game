import java.awt.*;
import javax.swing.*;

/**
 * The class <b>GameView</b> provides the current view of the entire Game. It extends
 * <b>JFrame</b> and lays out an instance of  <b>BoardView</b> (the actual game) and 
 * two instances of JButton. The action listener for the buttons is the controller.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */

public class GameView extends JFrame {

    /**
     * The board is a two dimensionnal array of DotButtons instances
     */
    private DotButton[][] board;
 
    private GameController gameController;

    private JLabel scoreLabel;
    private JButton buttonUndo;
    private JButton buttonRedo;
    
    
    
    
    private JFrame settingsFrame; 
    
    /**
     * Constructor used for initializing the Frame
     * 
     * @param model
     *            the model of the game (already initialized)
     * @param gameController
     *            the controller
     */

    public GameView(GameModel model, GameController gameController) {
        
        super("Flood it -- the ITI 1121 version");

        this.gameController = gameController;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setBackground(Color.WHITE);
        
        buttonUndo = new JButton("Undo");
        buttonUndo.setFocusPainted(false);
        buttonUndo.setEnabled(false);
        buttonUndo.addActionListener(gameController);
        
        buttonRedo = new JButton("Redo");
        buttonRedo.setFocusPainted(false);
        buttonRedo.setEnabled(false);
        buttonRedo.addActionListener(gameController);

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.setFocusPainted(false);
        buttonSettings.addActionListener(gameController);
        
        JPanel ctrl = new JPanel();
        ctrl.setBackground(Color.WHITE);
        ctrl.add(buttonUndo);
        ctrl.add(buttonRedo);
        ctrl.add(buttonSettings);
        
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(3,1));
        northPanel.add(ctrl);
        northPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        northPanel.setBackground(Color.WHITE);
        add(northPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(model.getSize(), model.getSize()));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        board = new DotButton[model.getSize()][model.getSize()];

        for (int row = 0; row < model.getSize(); row++) {
            for (int column = 0; column < model.getSize(); column++) {
                board[row][column] = new DotButton(row, column, model.getColor(row,column), 
                    (model.getSize() < 26 ? DotButton.MEDIUM_SIZE : DotButton.SMALL_SIZE));
                panel.add(board[row][column]);
                board[row][column].addActionListener(gameController);
            }
        }
    	add(panel, BorderLayout.CENTER);

        JButton buttonReset = new JButton("Reset");
        buttonReset.setFocusPainted(false);
        buttonReset.addActionListener(gameController);

        JButton buttonExit = new JButton("Quit");
        buttonExit.setFocusPainted(false);
        buttonExit.addActionListener(gameController);

        JPanel control = new JPanel();
        control.setBackground(Color.WHITE);
        scoreLabel = new JLabel();
        control.add(scoreLabel);
        control.add(buttonReset);
        control.add(buttonExit);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(3,1));
        southPanel.add(control);
        southPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        southPanel.setBackground(Color.WHITE);
        add(southPanel, BorderLayout.SOUTH);


    	pack();
    	//setResizable(false);
    	setVisible(true);

    }

    /**
     * update the status of the board's DotButton instances based on the current game model
     */

    public void update(GameModel gameModel){
        for(int i = 0; i < gameModel.getSize(); i++){
            for(int j = 0; j < gameModel.getSize(); j++){
                board[i][j].setColor(gameModel.getColor(i,j));
            }
        }
        if (gameModel.getInitialDot()) {
           scoreLabel.setText("Select initial dot");
           buttonUndo.setEnabled(false);
        } else {
            scoreLabel.setText("Number of steps: " + gameModel.getNumberOfSteps());
            buttonUndo.setEnabled(true);
        }
        repaint();
    }
    
    public void settings(GameModel model) {
        if(settingsFrame != null) {
            showSettings(true);
            return;
        }
        settingsFrame = new JFrame("Message");
    	settingsFrame.setBackground(Color.WHITE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,1));

        JLabel planeTorus = new JLabel("Play on plane or torus?");
        JRadioButton planeButton = new JRadioButton("Plane");
        planeButton.setBackground(Color.WHITE);
        planeButton.addActionListener(gameController);
        planeButton.setSelected(model.getPlaneStatus());
        JRadioButton torusButton = new JRadioButton("Torus");
        torusButton.setBackground(Color.WHITE);
        torusButton.addActionListener(gameController);
        torusButton.setSelected(!model.getPlaneStatus());
        ButtonGroup planeTorusGroup = new ButtonGroup();
        planeTorusGroup.add(planeButton);
        planeTorusGroup.add(torusButton);
        
        JLabel diagonalMoves = new JLabel("Diagonal moves?");
        JRadioButton orthogonalButton = new JRadioButton("Orthogonal");
        orthogonalButton.setBackground(Color.WHITE);
        orthogonalButton.addActionListener(gameController);
        orthogonalButton.setSelected(model.getOrthogonalStatus());
        JRadioButton diagonalButton = new JRadioButton("Diagonal");
        diagonalButton.setBackground(Color.WHITE);
        diagonalButton.addActionListener(gameController);
        diagonalButton.setSelected(!model.getOrthogonalStatus());
        ButtonGroup diagonalGroup = new ButtonGroup();
        diagonalGroup.add(orthogonalButton);
        diagonalGroup.add(diagonalButton);
        
        panel.add(planeTorus);
        panel.add(planeButton);
        panel.add(torusButton);
        panel.add(diagonalMoves);
        panel.add(orthogonalButton);
        panel.add(diagonalButton);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
        panel.setBackground(Color.WHITE);
        settingsFrame.add (panel, BorderLayout.CENTER);
        
        JPanel okPanel = new JPanel();
        JButton buttonOk = new JButton("OK");
        buttonOk.setFocusPainted(false);
        buttonOk.addActionListener(gameController);
        okPanel.add(buttonOk);
        settingsFrame.add(okPanel, BorderLayout.SOUTH);
    	settingsFrame.setSize(300, 300);
    	setResizable(false);
        settingsFrame.setLocationRelativeTo(null); 
    	showSettings(true);
    }
    
    public void showSettings (boolean visible) {
        settingsFrame.setVisible(visible);
    }

}