/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.io.IOException;
import java.util.ArrayList;
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
    private int foodCollectedSCore;
    private String antID;
    private boolean hasFood;
    private boolean stateFindFood;
    private boolean stateFindHome;
    private boolean alive;
    private String direction;
    private static ArrayList<Food> foodIHave = new ArrayList<Food>();
    private Artifact[] mySurroundings = new Artifact[3];
    private double[] myMovementScores = new double[9];
    //Pheromone Scores
    private double dropHPScore;
    private double dropFPScore;
    private double dropNPScore;
    //change state scores
    private double changeStateScore;
    private double keepStateScore;
    //pick up food or drop food score
    private double pickUpFoodScore;
    private double dropFoodScore;
    private double dontPickUpScore;

    public Ant() {
        this.hasFood = false;
        this.alive = true;
    }

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
        if(myWorld.getWorldObject(x, y) instanceof water){
            this.killAnt(); //remove from worldPool, add to dead ant pool
        }else if(myWorld.getWorldObject(x, y) instanceof obstacle){
            //do nothing
        }else if (myWorld.getWorldObject(x,y) instanceof home){
            
            this.xPos = x;
            this.yPos = y;
        }else if (x>= myWorld.getAmountOfColumns() || y>=myWorld.getAmountOfRows()){
            System.out.println("Cannot move ant there, out of bounds exception");
        }else{
            this.xPos = x;
            this.yPos = y;
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
        alive = DOA;
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
        //get scores for each action
        this.setActionScores();
        //carry out action based on best score(probability)
        this.computeActions();

    }

    /*private static Artifact[] getSurroundingSmells(){
    
    
     }*/
    public void pickUpFood() {
        if(foodIHave.isEmpty()){
            myWorld.getWorldObject(this.xPos, this.yPos).pickUpFirstFood(this);
        }
    }

    public void dropFood() {
        if(!foodIHave.isEmpty()){
            if (myWorld.getWorldObject(this.xPos,this.yPos) instanceof land){
                if (myWorld.getWorldObject(this.xPos,this.yPos) instanceof home){
                    if(((home)(myWorld.getWorldObject(this.xPos,this.yPos))).getHomeNumber() == this.getHomeNumber()){
                        foodIHave.remove(foodIHave.size());
                        this.foodCollectedSCore++;
                        ((home)myWorld.getWorldObject(this.xPos,this.yPos)).collectFood();
                        
                    }
                }else if (!(myWorld.getWorldObject(this.xPos,this.yPos) instanceof home)){
                    myWorld.getWorldObject(this.xPos,this.yPos).addFood(foodIHave.remove(foodIHave.size()));
                }
            }
        }
    }

    public void move(Artifact x) {
        this.xPos = x.getX();
        this.yPos = x.getY();
    }

    private void getSurroundings() {
        if (direction.matches("N")) {
            mySurroundings[0] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() + 1);
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos(), this.getYPos() + 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() + 1);

        } else if (direction.matches("E")) {
            mySurroundings[0] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() + 1);
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos());
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() - 1);

        } else if (direction.matches("W")) {
            mySurroundings[0] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() - 1);
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos());
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() + 1);

        } else if (direction.matches("S")) {
            mySurroundings[0] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() - 1);
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() - 1);

        } else if (direction.matches("NE")) {
            mySurroundings[0] = myWorld.getWorldObject(this.getXPos(), this.getYPos() + 1);
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() + 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos());

        } else if (direction.matches("NW")) {
            mySurroundings[0] = myWorld.getWorldObject(this.getXPos(), this.getYPos() + 1);
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() + 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos());

        } else if (direction.matches("SE")) {
            mySurroundings[0] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos());
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() - 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1);

        } else if (direction.matches("SW")) {
            mySurroundings[0] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos());
            mySurroundings[1] = myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() - 1);
            mySurroundings[2] = myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1);

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
                    myMovementScores[i] = 2 * mySurroundings[i].getFoodScent() + mySurroundings[i].getFoodPheromoneScent(this.homeNumber) + randFactor;
                } else {
                    myMovementScores[i] = randFactor;
                }
            }

        } else if (this.getState().matches("FindHome")) {
            for (int i = 0; i < myMovementScores.length; i++) {
                Random rand = new Random();
                int randFactor = rand.nextInt(randomnessFactor + 1);
                if (i < mySurroundings.length) {
                    myMovementScores[i] = 2 * mySurroundings[i].getHomePheromoneScent(this.homeNumber) + mySurroundings[i].getHomeScent(this.homeNumber) + randFactor;
                } else {
                    myMovementScores[i] = randFactor;
                }
            }

        } else {
            System.out.println("Ant state invalid");

        }

    }

    private void setDropPheromoneScores() {
        double temp;
        double highestScore = 0;
        int highestIndex = 0;
        Random rand = new Random();
        for (int i = 0; i < myMovementScores.length; i++) {
            temp = myMovementScores[i];
            if (highestScore < myMovementScores[i]) {
                highestScore = myMovementScores[i];
                highestIndex = i;
            }
        }

        if (this.getState().matches("FindFood")) {
            if (highestIndex < 3) {

                int randFactor = rand.nextInt(randomnessFactor + 1);

                dropFPScore = 2 * mySurroundings[highestIndex].getFoodScent() + mySurroundings[highestIndex].getFoodPheromoneScent(this.homeNumber) + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropHPScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropNPScore = randFactor;
            } else {
                int randFactor = rand.nextInt(randomnessFactor + 1);
                dropNPScore = 100 + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropHPScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFPScore = randFactor;

            }

        } else if (this.getState().matches("FindHome")) {
            if (highestIndex < 3) {

                int randFactor = rand.nextInt(randomnessFactor + 1);
                dropHPScore = 2 * mySurroundings[highestIndex].getHomeScent(this.homeNumber) + mySurroundings[highestIndex].getHomePheromoneScent(this.homeNumber) + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFPScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropNPScore = randFactor;
            } else {
                int randFactor = rand.nextInt(randomnessFactor + 1);
                dropNPScore = 100 + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropHPScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFPScore = randFactor;
            }

        } else {
            System.out.println("Ant state invalid cannot compute dropP score");

        }

    }

    private void setChangeStateScore() {
        Random rand = new Random();
        if ((this.getState().matches("FindFood") && !(this.hasFood)) || (this.getState().matches("FindHome") && (this.hasFood))) {

            int randFactor = rand.nextInt(randomnessFactor + 1);
            keepStateScore = 50 + randFactor;
            randFactor = rand.nextInt(randomnessFactor + 1);
            changeStateScore = randFactor;

        } else if ((this.getState().matches("FindFood") && this.hasFood) || (this.getState().matches("FindHome") && !(this.hasFood))) {
            int randFactor = rand.nextInt(randomnessFactor + 1);
            changeStateScore = 50 + randFactor;
            randFactor = rand.nextInt(randomnessFactor + 1);
            keepStateScore = randFactor;
        } else {
            System.out.println("something went wrong could not set change state score");
        }

    }

    private void setPickUpFoodScore() {

        double temp;
        double highestScore = 0;
        int highestIndex = 0;
        Random rand = new Random();
        for (int i = 0; i < myMovementScores.length; i++) {
            temp = myMovementScores[i];
            if (highestScore < myMovementScores[i]) {
                highestScore = myMovementScores[i];
                highestIndex = i;
            }
        }

        if (highestIndex < mySurroundings.length) {
            int randFactor = rand.nextInt(randomnessFactor + 1);
            if (mySurroundings[highestIndex].getFoodScent() >= 100 && this.getState().matches("FindFood")) {
                pickUpFoodScore = 100 + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFoodScore = randFactor;

            } else if (mySurroundings[highestIndex].getHomeScent(this.homeNumber) >= 100 && this.getState().matches("FindHome")) {
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFoodScore = 100 + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                pickUpFoodScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dontPickUpScore = randFactor;

            } else {
                randFactor = rand.nextInt(randomnessFactor + 1);
                dontPickUpScore = 100 + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFoodScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                pickUpFoodScore = randFactor;

            }

        } else {
            int randFactor = rand.nextInt(randomnessFactor + 1);
            dontPickUpScore = 100 + randFactor;
            randFactor = rand.nextInt(randomnessFactor + 1);
            dropFoodScore = randFactor;
            randFactor = rand.nextInt(randomnessFactor + 1);
            pickUpFoodScore = randFactor;

        }

    }

    private void setActionScores() {
        this.setMoveScores();
        this.setDropPheromoneScores();
        this.setChangeStateScore();
        this.setPickUpFoodScore();

    }

    private void computeActions() {
        
        moveFunction();
        dropPheromoneFunction();
        stateChangeFunction();
        pickUpFoodFunction();

        
        

    }

    private void moveFunction() {
        double temp;
        double highestScore = 0;
        int highestIndex = 0;
        Random rand = new Random();
        for (int i = 0; i < myMovementScores.length; i++) {
            temp = myMovementScores[i];
            if (highestScore < myMovementScores[i]) {
                highestScore = myMovementScores[i];
                highestIndex = i;
            }
        }
        
        
        int prevX = this.xPos;
        int prevY = this.yPos;
        if (highestIndex >= 0 && highestIndex <= 2) {

            this.setPosition(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY());

        } else {
            
            int randomX = rand.nextInt(3) - 1;
            int randomY = rand.nextInt(3) - 1;
            this.setPosition(this.getXPos() + randomX, this.getYPos() + randomY);

        }

        if ((this.yPos - prevY) > 0) {
            this.setDirection("N");
            if ((this.xPos - prevX) > 0) {
                this.setDirection("NE");
            } else if ((this.xPos - prevX) < 0) {
                this.setDirection("NW");
            }
        } else if ((this.yPos - prevY) < 0) {
            this.setDirection("S");
            if (this.xPos - prevX > 0) {
                this.setDirection("SE");
            } else if ((this.xPos - prevX) < 0) {
                this.setDirection("SW");
            }
        } else if ((this.yPos - prevY) == 0) {
            if ((this.xPos - prevX) > 0) {
                this.setDirection("E");
            } else if ((this.xPos - prevX) < 0) {
                this.setDirection("W");
            }
        }

    }
    
    private void dropPheromoneFunction(){
        if (dropNPScore >= dropFPScore && dropNPScore >= dropHPScore) {

        } else if (dropFPScore >= dropNPScore && dropFPScore >= dropHPScore) {
            this.dropFoodPheromone();
        } else if (dropHPScore >= dropFPScore && dropHPScore >= dropNPScore) {
            this.dropHomePheromone();
        }
    }
    
    private void stateChangeFunction(){
        if (keepStateScore >= changeStateScore) {

        } else if (changeStateScore > keepStateScore) {
            this.changeState();
        }
    }
    
    private void pickUpFoodFunction(){
         
    
        if(dontPickUpScore >= pickUpFoodScore && dontPickUpScore >= dropFoodScore){
            
        }else if(pickUpFoodScore >= dontPickUpScore && pickUpFoodScore >= dropFoodScore){
            this.pickUpFood();
        }else if (dropFoodScore >= dontPickUpScore && dropFoodScore >= dontPickUpScore){
            this.dropFood();
        }
    }
    
    private void killAnt(){
        myWorld.KillAnt(this);    
    }
    
    public void addToFoodIHave(Food food){
        if(foodIHave.isEmpty()){
            foodIHave.add(food);
        }
    }

}
