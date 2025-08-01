import java.awt.event.*; 
/**
 * The class <b>GameController</b> is the controller of the game. It has a method
 * <b>selectColor</b> which is called by the view when the player selects the next
 * color. It then computesthe next step of the game, and  updates model and view.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */


public class GameController implements ActionListener {
    
    private GameModel model; 
    private GameView view;

    /**
     * Constructor used for initializing the controller. It creates the game's view 
     * and the game's model instances
     * 
     * @param size
     *            the size of the board on which the game will be played
     */
    public GameController(int size) {
        model = new GameModel (size);
        view = new GameView (model, this);
    }

    /**
     * resets the game
     */
    public void reset(){
        model.reset();
    }

    /**
     * Callback used when the user clicks a button (reset or quit)
     *
     * @param e
     *            the ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.quitButton) System.exit(0);
        if (e.getSource() == view.resetButton) model.reset();
        for (int i = 0; i < GameModel.NUMBER_OF_COLORS; i++) {
            if (e.getSource() == view.buttonIcons[i]) {                
                selectColor(i);
            }
        }
        if (e.getSource() == view.qButton) System.exit(0);
        if (e.getSource() == view.againButton) {
            model.reset();
            view.frame.setVisible(false);
        }
        view.update();
        if (model.isFinished()) 
            view.congratualtionWindow (this);
    }

    /**
     * <b>selectColor</b> is the method called when the user selects a new color.
     * If that color is not the currently selected one, then it applies the logic
     * of the game to capture possible locations. It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives two options: start a new game, or exit
     * @param color
     *            the newly selected color
     */
    public void selectColor(int color){
        if (color == model.getCurrentSelectedColor()) return;
        model.setNumberOfSteps(model.getNumberOfSteps() + 1);
        model.setCurrentSelectedColor(color);
        model.capture(0, 0);
        stackBasedFlooding (color);      
    }
    
    private void stackBasedFlooding(int newColor) {
        // crete a new stack
        ArrayStack stack = new ArrayStack(4*model.getSize()*model.getSize());
        
        // push every captured dot in the stack
        for (int i = 0; i < model.getSize(); i++) {
            for (int j = 0; j < model.getSize(); j++) {
                if (model.isCaptured(i, j)) {
                    stack.push((DotInfo) model.get(i,j));               
                }
            }
        }       
        while (!stack.isEmpty()) {
            // remove a dot d from stack
            DotInfo dot = (DotInfo) stack.pop();
            int row = dot.getX();
            int column = dot.getY();
            // captute the neighbouring dots and push in stack
            if ( 
                    column - 1 >= 0 && 
                    model.getCurrentSelectedColor() == model.getColor(row, column - 1) &&
                    !model.isCaptured(row, column - 1) ) {
                model.capture(row, column - 1);
                stack.push((DotInfo) model.get(row, column - 1));  
            }
            if (
                    column + 1 < model.getSize() && 
                    model.getCurrentSelectedColor() == model.getColor(row, column + 1) &&
                    !model.isCaptured(row, column + 1) ) {
                model.capture(row, column + 1);
                stack.push((DotInfo) model.get(row, column + 1));  
            }
            if (
                    row - 1 >= 0 && 
                    model.getCurrentSelectedColor() == model.getColor(row - 1, column) &&
                    !model.isCaptured(row - 1, column) ) {
                model.capture(row - 1, column);
                stack.push((DotInfo) model.get(row - 1, column));  
            }
            if (
                    row + 1 < model.getSize() && 
                    model.getCurrentSelectedColor() == model.getColor(row + 1, column) &&
                    !model.isCaptured(row + 1, column) ) {
                model.capture(row + 1, column);
                stack.push((DotInfo) model.get(row + 1, column));  
            }
        }
    }
   

}
