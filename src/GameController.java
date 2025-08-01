import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;



/**
 * The class <b>GameController</b> is the controller of the game. It has a method
 * <b>selectColor</b> which is called by the view when the player selects the next
 * color. It then computes the next step of the game, and  updates model and view.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */

public class GameController implements ActionListener {

    /**
     * Reference to the view of the board
     */
    private GameView gameView;
    /**
     * Reference to the model of the game
     */
    private GameModel gameModel;
    
    private boolean actionFlag;
    
    Stack<GameModel> modelStack; 
    
    
    /**
     * Constructor used for initializing the controller. It creates the game's view 
     * and the game's model instances
     * 
     * @param size
     *            the size of the board on which the game will be played
     */
    public GameController(int size) {
        try {
            InputStream file = new FileInputStream("savedGame.ser");
            ObjectInput input = new ObjectInputStream (file);
            gameModel = (GameModel) input.readObject();
            (new File("savedGame.ser")).delete();
        } catch (Exception e) {
            gameModel = new GameModel(size);
            gameModel.setInitialDot (true);
        }
        gameView = new GameView(gameModel, this);
        modelStack = new GenericLinkedStack<GameModel>();
        flood();
        modelStack.push(gameModel.deepClone());
        gameView.update(gameModel);
    }

    /**
     * resets the game
     */
    public void reset(){
        gameModel.reset();
        modelStack = new GenericLinkedStack<GameModel>();
        gameModel.setInitialDot (true);
        flood();
        gameView.update(gameModel);
    }

    /**
     * Callback used when the user clicks a button (reset or quit)
     *
     * @param e
     *            the ActionEvent
     */

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof DotButton) {
            actionFlag = true;
            if (gameModel.getInitialDot()) {
                gameModel.setInitialDot(false);
                gameView.update(gameModel);
                int initialx = ((DotButton)(e.getSource())).getRow();
                int initialy = ((DotButton)(e.getSource())).getColumn();
                gameModel.captureInitialDot(initialx, initialy);
            } else {
                selectColor(((DotButton)(e.getSource())).getColor());
            }
            modelStack.push(gameModel.deepClone());
        } else if (e.getSource() instanceof JButton) {
            JButton clicked = (JButton)(e.getSource());
            if (clicked.getText().equals("Quit")) {
                gameModel.serializeModel();
                System.exit(0);
            } else if (clicked.getText().equals("Reset")){
                reset();
            } else if (clicked.getText().equals("Settings")){
                gameView.settings(gameModel);
            } else if (clicked.getText().equals("OK")){
                gameView.showSettings(false);
            } else if (clicked.getText().equals("Undo")){ 
                if (actionFlag && !modelStack.isEmpty()) 
                    gameModel = modelStack.pop(); // remove current model
                if (!modelStack.isEmpty()) gameModel = modelStack.pop();
                flood();
                gameView.update(gameModel);
                actionFlag = false;
            } else if (clicked.getText().equals("Redo")){
                
            }
        } 
        else if (e.getSource() instanceof JRadioButton) {
            JRadioButton clicked = (JRadioButton)(e.getSource());
            if (clicked.getText().equals("Plane")) {
                gameModel.setPlaneStatus(true);
            } else if (clicked.getText().equals("Torus")){
                gameModel.setPlaneStatus(false);
            } else if (clicked.getText().equals("Orthogonal")){
                gameModel.setOrthogonalStatus(true);
            } else if (clicked.getText().equals("Diagonal")) {
                gameModel.setOrthogonalStatus(false);
            }
        }
    }

    /**
     * <b>selectColor</b> is the method called when the user selects a new color.
     * If that color is not the currently selected one, then it applies the logic
     * of the game to capture possible locations. It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives to options: start a new game, or exit
     * @param color
     *            the newly selected color
     */
    public void selectColor(int color){
        if(color != gameModel.getCurrentSelectedColor()) {
            gameModel.setCurrentSelectedColor(color);
            flood();
            gameModel.step();
            gameView.update(gameModel);

            if(gameModel.isFinished()) {
                Object[] options = {"Play Again", "Quit"};
                int n = JOptionPane.showOptionDialog(gameView,
                        "Congratulations, you won in " + gameModel.getNumberOfSteps() 
                                +" steps!\n Would you like to play again?",
                        "Won",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                if(n == 0) reset();
                else System.exit(0);
            }            
        }        
    }

   /**
     * <b>flood</b> is the method that computes which new dots should be ``captured'' 
     * when a new color has been selected. The Model is updated accordingly
     */
    private void flood() {
        Stack<DotInfo> stack = new GenericLinkedStack<DotInfo>();
        for(int i = 0; i < gameModel.getSize(); i++) {
           for(int j = 0; j < gameModel.getSize(); j++) {
                if(gameModel.isCaptured(i,j)) {
                    stack.push(gameModel.get(i,j));
                }
           }
        }
        
        if ( gameModel.getPlaneStatus() ) floodPlaneOrthogonal (stack);
        else floodTurus (stack);
    }
    
    private void floodPlaneOrthogonal (Stack<DotInfo> stack) {
        DotInfo dotInfo;
        int x, xLeft = -1, xRight = -1, y, yUp = -1, yDown = -1; 
        while(!stack.isEmpty()){
            dotInfo = stack.pop();
            x = dotInfo.getX(); 
            y = dotInfo.getY();
            if (x > 0) xLeft = x - 1;
            if (x < gameModel.getSize()-1) xRight = x + 1;
            if (y > 0) yUp = y - 1;
            if (y < gameModel.getSize()-1) yDown = y + 1;
            if(x > 0) captureIT(stack, xLeft, y);
            if(x < gameModel.getSize()-1) captureIT(stack, xRight, y);
            if(y > 0) captureIT(stack, x, yUp);
            if(y < gameModel.getSize()-1) captureIT(stack, x, yDown);
            if (!gameModel.getOrthogonalStatus()) {
                if ((x > 0) && (y > 0)) 
                    captureIT(stack, xLeft, yUp);
                if ((x > 0) && y < ((gameModel.getSize() - 1))) 
                    captureIT(stack, xLeft, yUp);
                if ((x < (gameModel.getSize() - 1)) && ( y > 0)) 
                    captureIT(stack, xRight, yUp);
                if ((x < (gameModel.getSize() - 1)) && (y < (gameModel.getSize() - 1))) 
                    captureIT(stack, xRight, yDown);
            }
        }
    }
    
    private void floodTurus (Stack<DotInfo> stack) {
        DotInfo dotInfo;
        int x, xLeft, xRight, y, yUp, yDown;
        while(!stack.isEmpty()){
            dotInfo = stack.pop();
            x = dotInfo.getX(); 
            y = dotInfo.getY();
            if (x > 0) xLeft = x - 1;
            else xLeft = gameModel.getSize() - 1;
            if (x < gameModel.getSize()-1) xRight = x + 1;
            else xRight = 0;
            if (y > 0) yUp = y - 1;
            else yUp = gameModel.getSize() - 1;
            if (y < gameModel.getSize()-1) yDown = y + 1;
            else yDown = 0;
            captureIT(stack, xLeft, y);
            captureIT(stack, xRight, y);
            captureIT(stack, x, yUp);
            captureIT(stack, x, yDown);
            if (!gameModel.getOrthogonalStatus()) {
                captureIT(stack, xLeft, yUp);
                captureIT(stack, xLeft, yDown);
                captureIT(stack, xRight, yUp);
                captureIT(stack, xRight, yDown);
            }
            
        }
    }
    
    private void captureIT(Stack<DotInfo> stack, int x, int y) {
        if(shouldBeCaptured (x, y)) {
            gameModel.capture(x, y);
            stack.push(gameModel.get(x, y));
        }
    }

    /**
     * <b>shouldBeCaptured</b> is a helper method that decides if the dot
     * located at position (i,j), which is next to a captured dot, should
     * itself be captured
     * @param i
     *            row of the dot
     * @param j
     *            column of the dot
     */
    private boolean shouldBeCaptured(int i, int j) {
        if(!gameModel.isCaptured(i, j) &&
           (gameModel.getColor(i,j) == gameModel.getCurrentSelectedColor())) {
            return true;
        } else {
            return false;
        }
    }
    
}