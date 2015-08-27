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
    private int currHighestIndex;

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
        } else if (x >= myWorld.getAmountOfColumns() || y >= myWorld.getAmountOfRows() || y < 0 || x < 0) {
            System.out.println("Cannot move ant there, out of bounds exception");
        } else {
            this.xPos = x;
            this.yPos = y;
            if (myWorld.getWorldObject(this.xPos, this.yPos) instanceof plant) {
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
        if (myWorld.getWorldObject(xPos, yPos) instanceof land || myWorld.getWorldObject(xPos, yPos) instanceof plant || myWorld.getWorldObject(xPos, yPos) instanceof home) {
            myWorld.getWorldObject(xPos, yPos).addHomePheromone(homeNumber);

        }
    }

    /**
     * Drops Food Pheromone on ants location
     */
    public void dropFoodPheromone() {
        if (myWorld.getWorldObject(xPos, yPos) instanceof land || myWorld.getWorldObject(xPos, yPos) instanceof plant || myWorld.getWorldObject(xPos, yPos) instanceof home) {
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

            if (myWorld.getWorldObject(this.xPos, this.yPos) instanceof home) {
                if (((home) (myWorld.getWorldObject(this.xPos, this.yPos))).getHomeNumber() == this.getHomeNumber()) {
                    foodIHave.remove(foodIHave.size()-1);
                    this.hasFood = false;
                    this.foodCollectedSCore++;
                    System.out.println("ant ID " + this.antID + "just brought some food back home his score is now " + this.foodCollectedSCore);
                    ((home) myWorld.getWorldObject(this.xPos, this.yPos)).collectFood();
                    System.out.println("the total food brought back to this home is now " + ((home) myWorld.getWorldObject(this.xPos, this.yPos)).getFoodColleectedSCore());

                } else System.out.println("this aint yo house bruh!");
            } else if (!(myWorld.getWorldObject(this.xPos, this.yPos) instanceof home)) {
                this.hasFood = false;
                myWorld.getWorldObject(this.xPos, this.yPos).addFood(foodIHave.remove(foodIHave.size()-1));
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
                    randFactor = rand.nextInt(randomnessFactor + 1);
                    myMovementScores[i] = randFactor;
                }
            }

        } else if (this.getState().matches("FindHome")) {
            for (int i = 0; i < myMovementScores.length; i++) {
                Random rand = new Random();
                int randFactor = rand.nextInt(randomnessFactor + 1);
                if (i < mySurroundings.length) {
                    myMovementScores[i] = myWorld.getWorldObject(mySurroundings[i].getX(), mySurroundings[i].getY()).getHomePheromoneScent(this.homeNumber) + 30 * myWorld.getWorldObject(mySurroundings[i].getX(), mySurroundings[i].getY()).getHomeScent(this.homeNumber) + randFactor;
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

        Random rand = new Random();
        for (int i = 0; i < myMovementScores.length; i++) {
            temp = myMovementScores[i];
            if (highestScore < myMovementScores[i]) {
                highestScore = myMovementScores[i];
                currHighestIndex = i;
            }
        }

        if (this.getState().matches("FindFood")) {
            if (currHighestIndex < 3) {

                int randFactor = rand.nextInt(randomnessFactor + 1);

                dropHPScore = 2 * myWorld.getWorldObject(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY()).getFoodScent() + myWorld.getWorldObject(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY()).getFoodPheromoneScent(this.homeNumber) + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFPScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropNPScore = randFactor;
                if (myWorld.getWorldObject(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY()).getFoodScent() == 0) {
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

        } else if (this.getState().matches("FindHome")) {
            if (currHighestIndex < 3) {

                int randFactor = rand.nextInt(randomnessFactor + 1);
                dropFPScore = 2 * myWorld.getWorldObject(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY()).getHomeScent(this.homeNumber) + myWorld.getWorldObject(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY()).getHomePheromoneScent(this.homeNumber) + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                 dropHPScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropNPScore = randFactor;

                if (myWorld.getWorldObject(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY()).getHomeScent(this.homeNumber) == 0) {
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

        Random rand = new Random();
        for (int i = 0; i < myMovementScores.length; i++) {
            temp = myMovementScores[i];
            if (highestScore < myMovementScores[i]) {
                highestScore = myMovementScores[i];
                currHighestIndex = i;
            }
        }

        if (currHighestIndex < mySurroundings.length) {
            int randFactor = rand.nextInt(randomnessFactor + 1);
            if (myWorld.getWorldObject(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY()).getFoodScent() >= 100 && this.getState().matches("FindFood")) {
                pickUpFoodScore = 100 + randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dropFoodScore = randFactor;
                randFactor = rand.nextInt(randomnessFactor + 1);
                dontPickUpScore = randFactor;

            } else if (myWorld.getWorldObject(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY()).getHomeScent(this.homeNumber) >= 100 && this.getState().matches("FindHome")) {
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

        Random rand = new Random();

        for (int i = 0; i < myMovementScores.length; i++) {
            for (int j = 0; j < myMovementScores.length; j++) {
                if (Math.abs(myMovementScores[i] - myMovementScores[j]) > this.randomnessFactor) {
                    threshHoldPassed = true;
                }
            }
        }
        int prevX = this.xPos;
        int prevY = this.yPos;

        if (threshHoldPassed == true) { //simply use the highest score
            for (int i = 0; i < myMovementScores.length; i++) {
                temp = myMovementScores[i];
                if (highestScore < myMovementScores[i]) {
                    highestScore = myMovementScores[i];
                    currHighestIndex = i;
                }
            }
        } else {
            //need sorted array of indexes
            //first copy movement array to temp array
            // then create array to store sorted indexes
            // sort the temp array then compare each value in there to the value in the original 
            //when the value in the real array matches the temp one save the index of the real one into the corrosponding index of the sorted array of indices
            /*double[] tempArray = new double[myMovementScores.length];
             System.arraycopy(myMovementScores, 0, tempArray, 0, myMovementScores.length);
             Arrays.sort(tempArray);
             int[] sortedIndices = new int[myMovementScores.length];
             for(int i = 0; i< myMovementScores.length; i++){
             for(int j = 0; j< myMovementScores.length; j++){
             if(tempArray[i] == myMovementScores[j]){
             sortedIndices[i]=j;
             }
             }
             }*/
            int t = rand.nextInt(9);
            currHighestIndex = t;

        }
        if (currHighestIndex >= 0 && currHighestIndex <= 2) {

            this.setPosition(mySurroundings[currHighestIndex].getX(), mySurroundings[currHighestIndex].getY());
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

    private void dropPheromoneFunction() {

        double[] dropPArray = new double[3];
        dropPArray[0] = dropNPScore;
        dropPArray[1] = dropFPScore;
        dropPArray[2] = dropHPScore;
        boolean threshHoldPassed = false;

        for (int i = 0; i < dropPArray.length; i++) {
            for (int j = 0; j < dropPArray.length; j++) {
                if (Math.abs(dropPArray[i] - dropPArray[j]) > randomnessFactor) {
                    threshHoldPassed = true;
                }
            }
        }

        if (threshHoldPassed == true) {
            if (dropNPScore >= dropFPScore && dropNPScore >= dropHPScore) {

            } else if (dropFPScore >= dropNPScore && dropFPScore >= dropHPScore) {
                this.dropFoodPheromone();
            } else if (dropHPScore >= dropFPScore && dropHPScore >= dropNPScore) {
                this.dropHomePheromone();
            }
        } else {
            Random rand = new Random();
            int r = rand.nextInt(3);
            if (r == 2) {
                this.dropHomePheromone();
            } else if (r == 1) {
                this.dropFoodPheromone();
            }

        }
    }

    private void stateChangeFunction() {
        boolean threshHoldPassed = false;
        if (Math.abs(keepStateScore - changeStateScore) > randomnessFactor) {
            threshHoldPassed = true;
        }

        if (threshHoldPassed = false) {

            Random rand = new Random();
            int r = rand.nextInt(2);

            if (r == 0) {
                this.changeState();
            }

        } else {

            if (keepStateScore >= changeStateScore) {

            } else if (changeStateScore > keepStateScore) {
                this.changeState();
            }

        }
    }

    private void pickUpFoodFunction() {
        double[] pickUpFoodArray = new double[3];
        pickUpFoodArray[0] = dontPickUpScore;
        pickUpFoodArray[1] = pickUpFoodScore;
        pickUpFoodArray[2] = dropFoodScore;
        boolean threshHoldPassed = false;

        for (int i = 0; i < pickUpFoodArray.length; i++) {
            for (int j = 0; j < pickUpFoodArray.length; j++) {
                if (Math.abs(pickUpFoodArray[i] - pickUpFoodArray[j]) > randomnessFactor) {
                    threshHoldPassed = true;
                }
            }
        }
        if (threshHoldPassed == true) {
            if (dontPickUpScore >= pickUpFoodScore && dontPickUpScore >= dropFoodScore) {

            } else if (pickUpFoodScore >= dontPickUpScore && pickUpFoodScore >= dropFoodScore) {
                this.pickUpFood();
            } else if (dropFoodScore >= dontPickUpScore && dropFoodScore >= pickUpFoodScore) {
                this.dropFood();
            }
        } else {
            Random rand = new Random();
            int r = rand.nextInt(3);
            if (r == 3) {
                this.dropFood();
            } else if (r == 2) {
                this.pickUpFood();
            }

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

    public void setHasFood(boolean yn) {
        if (yn == true) {
            this.hasFood = true;
        } else {
            this.hasFood = false;
        }

    }

}
