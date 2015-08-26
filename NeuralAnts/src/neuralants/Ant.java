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
import java.util.Arrays;

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
    private boolean appendingBegan;
    private String state;
    private boolean alive;
    private String direction;
    private static ArrayList<Food> foodIHave = new ArrayList<Food>();
    private Artifact[] mySurroundings = new Artifact[3];
    private double[] myMovementScores = new double[4];
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
        this.state = "FindFood";
        this.appendingBegan = false;
    }

    public void setAntID(String id) {
        antID = id;

    }

    public void setRandomnessFactor(int r) {
        randomnessFactor = r;
    }

    public String getAntID() {
        return this.antID;
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
        if (myWorld.getWorldObject(x, y) instanceof water) {
            this.killAnt(); //remove from worldPool, add to dead ant pool
            // System.out.println("This ant just stepped into water and died..antID: "+ this.antID);
        } else if (myWorld.getWorldObject(x, y) instanceof obstacle) {
            //do nothing
        } else if (myWorld.getWorldObject(x, y) instanceof home) {

            this.xPos = x;
            this.yPos = y;
        } else if (x >= myWorld.getAmountOfColumns() || y >= myWorld.getAmountOfRows() || y<0 || x<0) {
            System.out.println("Cannot move ant there, out of bounds exception");
        } else {
            this.xPos = x;
            this.yPos = y;
            if (myWorld.getWorldObject(this.xPos, this.yPos) instanceof plant){
                System.out.println("look ma I got maself to a plant!");
            }
            
        }
    }

    /**
     * sets state of ant to find food mode to true, sets state of find home mode
     * to false if state is already FindFood nothing happens
     */
    public void setFindFood() {
        this.state = "FindFood";
    }

    /**
     * sets state of ant to find home mode to true, sets state of find food mode
     * to false if state is already FindHome nothing happens
     */
    public void setFindHome() {
        this.state = "FindHome";
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
        if (myWorld.getWorldObject(xPos, yPos) instanceof land ||myWorld.getWorldObject(xPos, yPos) instanceof plant ||myWorld.getWorldObject(xPos, yPos) instanceof home) {
            myWorld.getWorldObject(xPos, yPos).addHomePheromone(homeNumber);

        }
    }

    /**
     * Drops Food Pheromone on ants location
     */
    public void dropFoodPheromone() {
        if (myWorld.getWorldObject(xPos, yPos) instanceof land ||myWorld.getWorldObject(xPos, yPos) instanceof plant ||myWorld.getWorldObject(xPos, yPos) instanceof home) {
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
        return this.homeNumber;
    }

    /**
     * boolean telling us if ant is dead or alive
     *
     * @param DOA true if alive false if dead
     */
    public void setAlive(boolean DOA) {
        this.alive = DOA;
    }

    /**
     * gives us direction the ant is pointing
     *
     * @return N\E\W\S\NE\NW\SE\SW
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     *
     * @param d must be either N\E\W\S\NE\NW\SE\SW
     */
    public void setDirection(String d) {

        if (!((d).matches("(N|S|E|W|NE|SE|NW|SW)"))) {
            System.out.println("wrong format for ant direction");

        } else {
            this.direction = d;
        }
    }

    /**
     * Ant thinks using its state and current sensory data and decides what to
     * do using a normal distribution
     */
    public void think() {

        //get sensory information and state
        // System.out.println("Ant "+this.getAntID()+"is thinking");
        // System.out.println("Getting Sensory Information");
        this.getSurroundings();
        // System.out.println("Setting Scores");
        //get scores for each action
        this.setActionScores();
        //System.out.println("Computing Action");
        //carry out action based on best score(probability)
        this.computeActions();

    }

    /*private static Artifact[] getSurroundingSmells(){
    
    
     }*/
    public void pickUpFood() {
        if (foodIHave.isEmpty()) {
            myWorld.getWorldObject(this.xPos, this.yPos).pickUpFirstFood(this);
            
        }
    }

    public void dropFood() {
        if (!foodIHave.isEmpty()) {
            System.out.println("hey ma im droppin some food");
            if (myWorld.getWorldObject(this.xPos, this.yPos) instanceof land) {
                if (myWorld.getWorldObject(this.xPos, this.yPos) instanceof home) {
                    if (((home) (myWorld.getWorldObject(this.xPos, this.yPos))).getHomeNumber() == this.getHomeNumber()) {
                        foodIHave.remove(foodIHave.size());
                        this.hasFood = false;
                        this.foodCollectedSCore++;
                        System.out.println("ant ID " + this.antID + "just brought some food back home his score is now " + this.foodCollectedSCore);
                        ((home) myWorld.getWorldObject(this.xPos, this.yPos)).collectFood();
                        System.out.println("the total food brought back to this home is now " + ((home) myWorld.getWorldObject(this.xPos, this.yPos)).getFoodColleectedSCore());

                    }
                } else if (!(myWorld.getWorldObject(this.xPos, this.yPos) instanceof home)) {
                    this.hasFood = false;
                    myWorld.getWorldObject(this.xPos, this.yPos).addFood(foodIHave.remove(foodIHave.size()));
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
            mySurroundings[0] = new Artifact(myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() - 1));
            mySurroundings[1] = new Artifact(myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1));
            mySurroundings[2] = new Artifact(myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() - 1));

        } else if (direction.matches("NE")) {
            mySurroundings[0] = new Artifact(myWorld.getWorldObject(this.getXPos(), this.getYPos() + 1));
            mySurroundings[1] = new Artifact(myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() + 1));
            mySurroundings[2] = new Artifact(myWorld.getWorldObject(this.getXPos() + 1, this.getYPos()));

        } else if (direction.matches("NW")) {
            mySurroundings[0] = new Artifact(myWorld.getWorldObject(this.getXPos(), this.getYPos() + 1));
            mySurroundings[1] = new Artifact(myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() + 1));
            mySurroundings[2] = new Artifact(myWorld.getWorldObject(this.getXPos() - 1, this.getYPos()));

        } else if (direction.matches("SE")) {
            mySurroundings[0] = new Artifact(myWorld.getWorldObject(this.getXPos() + 1, this.getYPos()));
            mySurroundings[1] = new Artifact(myWorld.getWorldObject(this.getXPos() + 1, this.getYPos() - 1));
            mySurroundings[2] = new Artifact(myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1));

        } else if (direction.matches("SW")) {
            mySurroundings[0] = new Artifact(myWorld.getWorldObject(this.getXPos() - 1, this.getYPos()));
            mySurroundings[1] = new Artifact(myWorld.getWorldObject(this.getXPos() - 1, this.getYPos() - 1));
            mySurroundings[2] = new Artifact(myWorld.getWorldObject(this.getXPos(), this.getYPos() - 1));

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
        return this.state;
    }

    public void setState(String s) {
        if (!(s.matches(("FindHome|FindFood")))) {
            System.out.println("state not set because input is incorrect");
        } else if (s.matches("FindHome")) {
            this.state = "FindHome";
        } else if (s.matches("FindFood")) {
            this.state = "FindFood";
        }

    }

    private void setMoveScores() {
        if (this.getState().matches("FindFood")) {
            for (int i = 0; i < myMovementScores.length; i++) {
                Random rand = new Random();
                int randFactor = rand.nextInt(randomnessFactor + 1);
                if (i < mySurroundings.length) {
                    myMovementScores[i] = 30 * myWorld.getWorldObject(mySurroundings[i].getX(), mySurroundings[i].getY()).getFoodScent() + myWorld.getWorldObject(mySurroundings[i].getX(), mySurroundings[i].getY()).getFoodPheromoneScent(this.homeNumber) + randFactor;
                    
                } else {
                    randFactor = rand.nextInt(randomnessFactor +1);
                    myMovementScores[i] = randFactor;
                }
            }

        } else if (this.getState().matches("FindHome")) {
            for (int i = 0; i < myMovementScores.length; i++) {
                Random rand = new Random();
                int randFactor = rand.nextInt(randomnessFactor + 1);
                if (i < mySurroundings.length) {
                    myMovementScores[i] =  myWorld.getWorldObject(mySurroundings[i].getX(), mySurroundings[i].getY()).getHomePheromoneScent(this.homeNumber) + 30* myWorld.getWorldObject(mySurroundings[i].getX(), mySurroundings[i].getY()).getHomeScent(this.homeNumber) + randFactor;
                } else {
                    randFactor = rand.nextInt(randomnessFactor + 1);
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

                dropFPScore = 2 * myWorld.getWorldObject(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY()).getFoodScent() + myWorld.getWorldObject(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY()).getFoodPheromoneScent(this.homeNumber) + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropHPScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropNPScore = randFactor;
                if (myWorld.getWorldObject(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY()).getFoodScent() == 0) {
                    dropNPScore += 100;
                }
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
                dropHPScore = 2 * myWorld.getWorldObject(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY()).getHomeScent(this.homeNumber) + myWorld.getWorldObject(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY()).getHomePheromoneScent(this.homeNumber) + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFPScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropNPScore = randFactor;
                
                if (myWorld.getWorldObject(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY()).getFoodScent() == 0) {
                    dropNPScore += 50;
                }
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
            if (myWorld.getWorldObject(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY()).getFoodScent() >= 100 && this.getState().matches("FindFood")) {
                 pickUpFoodScore = 100 + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFoodScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dontPickUpScore = randFactor;

            } else if (myWorld.getWorldObject(mySurroundings[highestIndex].getX(), mySurroundings[highestIndex].getY()).getHomeScent(this.homeNumber) >= 100 && this.getState().matches("FindHome")) {
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
        this.setChangeStateScore();
        this.stateChangeFunction();
        
        this.setMoveScores();
        this.setDropPheromoneScores();
        this.setPickUpFoodScore();
        

    }

    private void computeActions() {

        moveFunction();

        if (this.alive == true) {
            this.dropPheromoneFunction();
            this.pickUpFoodFunction();
            
        }
    }

    private void moveFunction() {
        boolean threshHoldPassed = false;
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
        /*
         for (int i = 0; i < myMovementScores.length; i++) {
         for (int j = 0; j < myMovementScores.length; j++) {
         if (Math.abs(myMovementScores[i] - myMovementScores[j]) <= this.randomnessFactor) {
         threshHoldPassed = false;
         } else {
         threshHoldPassed = true;
         }
         }

         }
        
         int[] tempArray = new int [9];
         System.arraycopy(myMovementScores, 0, tempArray, 0, tempArray.length);
         Arrays.sort(tempArray);
        
         int[] sortedIndex = new int [9];
         for(int i=0; i<9;i++){
         for(int j = 0; j<9; j++){
         // if
         // sortedIndex[i] =
         }
         }
        
         double t =0;
         int u = 0;
        
         for(int i = 0; i< sortedIndex.length; i++){
         for(int j = 1; j<sortedIndex.length; j++){
         t=myMovementScores[i];
         }
         }
         if (threshHoldPassed == false) {
         int[] normalDistribution = new int [9];
         for(int i = 4; i < normalDistribution.length; i++){
                
         }
            
            
            

         }// else { */
    //if(this.threshHoldPassed)
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
        //}
    }

    private void dropPheromoneFunction() {
        if (dropNPScore >= dropFPScore && dropNPScore >= dropHPScore) {

        } else if (dropFPScore >= dropNPScore && dropFPScore >= dropHPScore) {
            this.dropFoodPheromone();
        } else if (dropHPScore >= dropFPScore && dropHPScore >= dropNPScore) {
            this.dropHomePheromone();
        }
    }

    private void stateChangeFunction() {
        if (keepStateScore >= changeStateScore) {

        } else if (changeStateScore > keepStateScore) {
            this.changeState();
        }
    }

    private void pickUpFoodFunction() {

        if (dontPickUpScore >= pickUpFoodScore && dontPickUpScore >= dropFoodScore) {

        } else if (pickUpFoodScore >= dontPickUpScore && pickUpFoodScore >= dropFoodScore) {
            this.pickUpFood();
        } else if (dropFoodScore >= dontPickUpScore && dropFoodScore >= pickUpFoodScore) {
            this.dropFood();
        }
    }

    private void killAnt() {
        this.alive = false;

    }

    public void addToFoodIHave(Food food) {
        if (foodIHave.isEmpty()) {
            foodIHave.add(food);
        }
    }

    public boolean hasFood() {
        if (this.hasFood == true) {
            return true;
        } else {
            return false;
        }
    }

    public int getFoodCollectedScore() {
        return this.foodCollectedSCore;
    }

    
    public boolean getDOA() {
        return this.alive;
    }

    public boolean getAppendingBegan() {
        return this.appendingBegan;
    }

    public void setAppendingBegan(boolean yn) {
        this.appendingBegan = yn;

    }
    
    public void setHasFood(boolean yn){
        if (yn == true){
            this.hasFood= true;
        }else this.hasFood = false;
        
    }

}
