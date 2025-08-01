import java.util.Random;
import java.io.*;

/**
 * The class <b>GameModel</b> holds the model, the state of the systems. 
 * It stores the followiung information:
 * - the state of all the ``dots'' on the board (color, captured or not)
 * - the size of the board
 * - the number of steps since the last reset
 * - the current color of selection
 *
 * The model provides all of this informations to the other classes trough 
 *  appropriate Getters. 
 * The controller can also update the model through Setters.
 * Finally, the model is also in charge of initializing the game
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */
public class GameModel implements Cloneable, Serializable  {

    /**
     * predefined values to capture the color of a DotInfo
     */
    public static final int COLOR_0           = 0;
    public static final int COLOR_1           = 1;
    public static final int COLOR_2           = 2;
    public static final int COLOR_3           = 3;
    public static final int COLOR_4           = 4;
    public static final int COLOR_5           = 5;
    public static final int NUMBER_OF_COLORS  = 6;

    /**
     * The current selection color
     */
    private int currentSelectedColor;

    /**
     * The size of the game.
     */
    private  int sizeOfGame;
 
    /**
     * A 2 dimensional array of sizeOfGame*sizeOfGame recording the state of each dot
     */
    private DotInfo[][] model;

   /**
     * The number of steps played since the last reset
     */
    private int numberOfSteps;
 
   /**
     * The number of captured dots
     */
    private int numberCaptured;

   /**
     * Random generator
     */
    private Random generator;
    
    private boolean initialDot;
    private boolean plane;
    private boolean orthogonal;

    /**
     * Constructor to initialize the model to a given size of board.
     * 
     * @param size
     *            the size of the board
     */
    public GameModel(int size) {
        generator = new Random();
        sizeOfGame = size;
        setPlaneStatus (true);
        setOrthogonalStatus(true);
        setInitialDot(true);
        reset();
    }


    /**
     * Resets the model to (re)start a game. The previous game (if there is one)
     * is cleared up . 
     */
    public void reset(){
    	model = new DotInfo[sizeOfGame][sizeOfGame];

    	for(int i = 0; i < sizeOfGame; i++){
            for(int j = 0; j < sizeOfGame; j++){
                model[i][j] = new DotInfo(i,j,generator.nextInt(NUMBER_OF_COLORS));
            }
    	}
    	
    	numberOfSteps = 0;
        numberCaptured = 1;
    }


    /**
     * Getter method for the size of the game
     * 
     * @return the value of the attribute sizeOfGame
     */   
    public int getSize(){
        return sizeOfGame;
    }

    /**
     * returns the color  of a given dot in the game
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public int getColor(int i, int j){
        if(isCaptured(i, j)) {
            return currentSelectedColor;
        } else {
    	   return model[i][j].getColor();
        }
    }
    
    /**
     * Captures the initial dot
     * @param row
     *            the x coordinate of the dot
     * @param column
     *            the y coordinate of the dot
     */   
    public void captureInitialDot (int row, int column) {
        // initially, the top left DotInfo is captured        
        currentSelectedColor = model[row][column].getColor();
        model[row][column].setCaptured(true);
    }

    /**
     * returns true is the dot is captured, false otherwise
    * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public boolean isCaptured(int i, int j){
        return model[i][j].isCaptured();
    }

    /**
     * Sets the status of the dot at coordinate (i,j) to captured
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */   
    public void capture(int i, int j){
        model[i][j].setCaptured(true);
        numberCaptured++;
    }


    /**
     * Getter method for the current number of steps
     * 
     * @return the current number of steps
     */   
    public int getNumberOfSteps(){
    	return numberOfSteps;
    }
    
    public void setNumberOfSteps (int steps) {
        numberOfSteps = steps;
    }

    /**
     * Setter method for currentSelectedColor
     * 
     * @param val
     *            the new value for currentSelectedColor
    */   
    public void setCurrentSelectedColor(int val) {
        currentSelectedColor = val;
    }

    /**
     * Getter method for currentSelectedColor
     * 
     * @return currentSelectedColor
     */   
    public int getCurrentSelectedColor() {
        return currentSelectedColor ;
    }


    /**
     * Getter method for the model's dotInfo reference
     * at location (i,j)
     *
      * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     *
     * @return model[i][j]
     */   
    public DotInfo get(int i, int j) {
        return model[i][j];
    }
    
    /**
     * The metod <b>step</b> updates the number of steps. It must be called 
     * once the model has been updated after the payer selected a new color.
     */
    public void step(){
        numberOfSteps++;
    }
    
    /**
     * The method <b>getPlaneStatus</b> returns whether the game is in the
     * plane or turus mode. 
     * @return game setting: true (plane) or false (turus).
     */
    public boolean getPlaneStatus () {
        return plane;
    }
    
    /**
     * The method <b>setPlaneStatus</b> sets the game: plan or turus. 
     * @param pstatus: true plane; false turus.
     */
    public void setPlaneStatus (boolean pstatus) {
        plane = pstatus;
    }
     
    /**
     * The method <b>getDiagonalStatus</b> returns whether the game is in the
     * diagonal or orthogonal mode. 
     * @return game setting: true (diagonal) or false (orthogonal).
     */
    public boolean getOrthogonalStatus () {
        return orthogonal;
    }
    
    /**
     * The method <b>setPlaneStatus</b> sets the game: plan or turus. 
     * @param dstatus: true plane; false turus.
     */
    public void setOrthogonalStatus (boolean dstatus) {
        orthogonal = dstatus;
    }
    
    public void setInitialDot (boolean flag) {
        initialDot = flag;
    }
    
    public boolean getInitialDot () {
        return initialDot;
    }
    
    public void setNumberCaptured (int n) {
        numberCaptured = n;
    }
    
    public int getNumberCaptured () {
        return numberCaptured;
    }

    /**
     * The method <b>isFinished</b> returns true if the game is finished, that
     * is, all the dats are captured.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished(){
        return numberCaptured == sizeOfGame*sizeOfGame;
    }
    
    /**
     * The method <b>clone</b> returns a deep copy of the GameModel object
     * @return a deep copy of this GameModel object
     */
    public GameModel clone() {
        try {
            GameModel gmodel = (GameModel) super.clone();
            gmodel.model = (DotInfo[][]) gmodel.model.clone();
            for(int i = 0; i < sizeOfGame; i++)
                for(int j = 0; j < sizeOfGame; j++) {
                    gmodel.model[i][j] = this.model[i][j].clone();
                }
            return gmodel;
        } catch (CloneNotSupportedException e) {
        return null;
        }
        
    }
    
    
    public GameModel deepClone() {
        GameModel clonedModel = new GameModel (sizeOfGame);
        for(int i = 0; i < sizeOfGame; i++) {
                for(int j = 0; j < sizeOfGame; j++) {
                    clonedModel.model[i][j].setColor(this.model[i][j].getColor());
                    clonedModel.model[i][j].setCaptured(this.isCaptured (i, j));
                }
        }
        clonedModel.setCurrentSelectedColor(this.getCurrentSelectedColor());
        clonedModel.setNumberOfSteps (this.getNumberOfSteps());
        clonedModel.setNumberCaptured (this.getNumberCaptured());
        clonedModel.setInitialDot(this.getInitialDot());
        clonedModel.setPlaneStatus(this.getPlaneStatus());
        clonedModel.setOrthogonalStatus(this.getOrthogonalStatus());
        return clonedModel;
    }
    
    
    public void serializeModel() {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream("savedGame.ser");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
        } catch (Exception ex) { }
        finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) { }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) { }
            }
        }
    }
    
    
   /**
     * Builds a String representation of the model
     *
     * @return String representation of the model
     */
    public String toString(){
        StringBuffer b = new StringBuffer();
        for(int i = 0; i < sizeOfGame; i++){
            for(int j = 0; j < sizeOfGame; j++){
                if (isCaptured(i, j)) b.append(getColor(i, j) + "c");
                else b.append(getColor(i, j) + " ");
            }
            b.append("\n");
        }
        return ("Current Selected Color: " + currentSelectedColor +
                "\nNumber Of Steps: " + numberOfSteps +
                "\nNumber Captured: " + numberCaptured + "\n"+ 
                b.toString() + "\n");
    }
}