/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author tooji
 */
public class Ant {

    private int randomnessFactor;
    private int xPos;
    private int yPos;
    private int homeNumber;
    private String antID;
    private boolean hasFood;
    private boolean stateFindFood;
    private boolean stateFindHome;
    private boolean Alive;
    private String direction;
    private Artifact[] mySurroundings = new Artifact[3];
    private double[] myMovementScores = new double[9];
    private double[] myDropPScores = new double[3];
    Map<String, Integer> scores = new HashMap<>();

    public void setAntID(String id) {
        antID = id;

    }

    public void setRandomnessFactor(int r) {
        randomnessFactor = r;
    }

    /**
     * Sets identifier of the tribe the ant belongs to
     *
     * @param h is the homeNumber or tribeNumber
     */
    public void setHomeNumber(int h) {
        homeNumber = h;

    }

    /**
     * Set's position of this ant
     *
     * @param x sets x-position of ant
     * @param y sets y-position of ant
     */
    public void setPosition(int x, int y) {
        xPos = x;
        yPos = y;

        if (myWorld.getWorld()[x][y] instanceof water) {

        }

    }

    /**
     * sets state of ant to find food mode to true, sets state of find home mode
     * to false if state is already FindFood nothing happens
     */
    public void setFindFood() {
        if (stateFindHome) {
            stateFindHome = false;
            stateFindFood = true;
        } else {
            stateFindFood = true;
        }
    }

    /**
     * sets state of ant to find home mode to true, sets state of find food mode
     * to false if state is already FindHome nothing happens
     */
    public void setFindHome() {
        if (stateFindFood) {
            stateFindFood = false;
            stateFindHome = true;
        } else {
            stateFindFood = true;
        }
    }

    /**
     * sets x-position of ant
     *
     * @param x is the x-position
     */
    public void setXpos(int x) {
        xPos = x;
    }

    /**
     * sets y-position of ant
     *
     * @param y is the y-position
     */
    public void setYpos(int y) {
        yPos = y;

    }

    /**
     * Drops Home Pheromone on ants location
     */
    public void dropHomePheromone() {
        if (myWorld.getWorldObject(xPos, yPos) instanceof land) {
            myWorld.getWorldObject(xPos, yPos).addHomePheromone(homeNumber);
        }
    }

    /**
     * Drops Food Pheromone on ants location
     */
    public void dropFoodPheromone() {
        if (myWorld.getWorldObject(xPos, yPos) instanceof land) {
            myWorld.getWorldObject(xPos, yPos).addFoodPheromone(homeNumber);
        }

    }

    /**
     * gets x-position of ant
     */
    public int getXPos() {
        return xPos;

    }

    /**
     * gets Y position of ant
     */
    public int getYPos() {
        return yPos;
    }

    /**
     * gets ant's tribe/home identification number
     */
    public int getHomeNumber() {
        return homeNumber;
    }

    /**
     * boolean telling us if ant is dead or alive
     *
     * @param DOA true if alive false if dead
     */
    public void setAlive(boolean DOA) {
        Alive = DOA;
    }

    /**
     * gives us direction the ant is pointing
     *
     * @return N\E\W\S\NE\NW\SE\SW
     */
    public String getDirection() {
        return direction;
    }

    /**
     *
     * @param d must be either N\E\W\S\NE\NW\SE\SW
     */
    public void setDirection(String d) {

        if (!((d).matches("(N|S|E|W|NE|SE|NW|SW)"))) {
            System.out.println("wrong format for ant direction");

        } else {
            direction = d;
        }
    }

    /**
     * Ant thinks using its state and current sensory data and decides what to
     * do using a normal distribution
     */
    public void think() {

        //get sensory information and state
        this.getSurroundings();
        //this.assignMoveScores();

        //this.assignDropPheromoneScores();
        //this.assignChangeStateScores();
        //this.assignPickUpScore();
        //compute action(s)
        //update myWorld.artifact[][] (done within simulation method NeuralAnts.java)
        
        //compute actions
        //move
        //drop pheremones -->compute drop score, if it matches place where ant will move then do it
        
    }

    /*private static Artifact[] getSurroundingSmells(){
    
    
     }*/
    public void pickUpFood() {
        //if (this.xPos())
    }
    
    public void dropFood(){
        
    }
    
    public void move(Artifact x){
        this.xPos = x.getX();
        this.yPos = x.getY();
    }

    private void getSurroundings() {
        if (direction.matches("N")) {
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() + 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos(), this.getYPos() + 1);
            mySurroundings[3] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() + 1);

        } else if (direction.matches("E")) {
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() + 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos());
            mySurroundings[3] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() - 1);

        } else if (direction.matches("W")) {
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() - 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos());
            mySurroundings[3] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() + 1);

        } else if (direction.matches("S")) {
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() - 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1);
            mySurroundings[3] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() - 1);

        } else if (direction.matches("NE")) {
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos(), this.getYPos() + 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() + 1);
            mySurroundings[3] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos());

        } else if (direction.matches("NW")) {
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos(), this.getYPos() + 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() + 1);
            mySurroundings[3] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos());

        } else if (direction.matches("SE")) {
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos());
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() - 1);
            mySurroundings[3] = myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1);

        } else if (direction.matches("SW")) {
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos());
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() - 1);
            mySurroundings[3] = myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1);

        } else {
            System.out.println("Error gathering direction in getSurroundings() method");
        }

    }

    public void changeState() {
        if (this.getState().matches("FindFood")) {
            this.setState("FindHome");
        } else {
            this.setState("FindFood");
        }
    }

    public String getState() {
        if (stateFindFood) {
            return "FindFood";
        } else {
            return "FindHome";
        }
    }

    public void setState(String s) {
        if (!(s.matches(("FindHome|FindFood")))) {
            System.out.println("state not set because input is incorrect");
        } else if (s.matches("FindHome")) {
            stateFindHome = true;
        } else if (s.matches("FindFood")) {
            stateFindFood = true;
        }

    }

    private void setMoveScores() {
        if (this.getState().matches("FindFood")) {
            for (int i = 0; i < myMovementScores.length; i++) {
                Random rand = new Random();
                int randFactor = rand.nextInt(randomnessFactor + 1);
                if (i < mySurroundings.length) {
                    myMovementScores[i] = 2*mySurroundings[i].getFoodScent() + mySurroundings[i].getFoodPheromoneScent(this.homeNumber) + randFactor;
                }
                else myMovementScores[i] = randFactor;
            }

        } else if (this.getState().matches("FindHome")) {
            for(int i =0; i< myMovementScores.length; i++){
                Random rand = new Random();
                int randFactor = rand.nextInt(randomnessFactor+1);
                if (i<mySurroundings.length){
                    myMovementScores[i]= 2*mySurroundings[i].getHomePheromoneScent(this.homeNumber) + mySurroundings[i].getHomeScent(this.homeNumber) + randFactor;
                }
                else myMovementScores[i] = randFactor;
            }

        } else {
            System.out.println("Ant state invalid");

        }

    }

    private void setDropPheromoneScores() {
        
        if (this.getState().matches("FindFood")) {
            for (int i = 0; i<mySurroundings.length; i++){
                for (int j = 0 ; j< myDropPScores.length; j++){
                    Random rand = new Random();
                    if (j == 0){
                        int randFactor = rand.nextInt(randomnessFactor+1);
                        
                        
                    }else if (j == 1){
                        int randFactor = rand.nextInt(randomnessFactor+1);

                        
                    }else if (j == 2){
                         int randFactor = rand.nextInt(randomnessFactor+1);
                        
                    }else{
                        
                    }
                    
                    
                }
            }
            
            
            
            

        } else if (this.getState().matches("FindHome")) {
           

        } else {
            System.out.println("Ant state invalid");

        }

        

    }

    private void setChangeStateScore() {

    }

    private void setPickUpFoodScore() {

    }
    
    

}
