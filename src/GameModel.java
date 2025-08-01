// ADD YOUR IMPORTS HERE

/**
 * The class <b>GameModel</b> holds the model, the state of the systems. 
 * It stores the following information:
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

import java.util.Random;

public class GameModel {


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
    
    private DotInfo [][] model;
    private int sizeOfGame;
    private int nsteps;
    private int currentSelectedColor;

    /**
     * Constructor to initialize the model to a given size of board.
     * 
     * @param size
     *            the size of the board
     */
    public GameModel(int size) {
        sizeOfGame = size;
        model = new DotInfo[size][size];
        reset ();
    }

    /**
     * Resets the model to (re)start a game. The previous game (if there is one)
     * is cleared up . 
     */
    public void reset(){
        Random rand = new Random();
        int  c;
        for (int i = 0; i < sizeOfGame; i++) {
            for (int j = 0; j < sizeOfGame; j++) {
                c = rand.nextInt(NUMBER_OF_COLORS);
                model[i][j] = new DotInfo (i, j, c);
                model[i][j].setCaptured(false);
            }
        }
        currentSelectedColor = -1;
        nsteps = 0;
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
     * returns the current color  of a given dot in the game
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public int getColor(int i, int j){
        return model[i][j].getColor();
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
    }
    
    /**
     * Setter method for the current number of steps
     * @param nsteps number of steps so far
     */   
    public void setNumberOfSteps(int nsteps){
        this.nsteps = nsteps;
    }

    /**
     * Getter method for the current number of steps
     * 
     * @return the current number of steps
     */   
    public int getNumberOfSteps(){
        return nsteps;
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
        return currentSelectedColor;
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
     * The method <b>step</b> updates the number of steps. It must be called 
     * once the model has been updated after the payer selected a new color.
     */
     public void step(){
         nsteps++;
     }
 
   /**
     * The method <b>isFinished</b> returns true if the game is finished, that
     * is, all the dots are captured.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished(){
        for (int i = 0; i < sizeOfGame; i++) {
            for (int j = 0; j < sizeOfGame; j++) 
                if (model[i][j].isCaptured() == false) 
                    return false;
        }
        return true;
    }
    
    /**
     * Builds a String representation of the model
     *
     * @return String representation of the model
     */
    public String toString(){
        return "Game size: " + sizeOfGame +", number of steps so far: " + nsteps;
    }
}
