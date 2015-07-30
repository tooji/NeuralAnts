/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author tooji
 */
public class Artifact {

    private int xPos;
    private int yPos;
    private static int amountOfHomes;
    private int[] foodPheremoneNum; //amount of food pheremones for a given tribe(index) are located on this area
    private int[] homePheremoneNum; //amount of home pheremones for a given tribe(index) are located on this area
    private int[] foodPheremoneScent; //only those ants from the same tribe can recognize
    private int[] homePheremoneScent; //only those ants from the same tribe can recognize
    private int[] homeScent;    //only those ants from the same tribe can recognize
    private int foodScent;      //All ants can recognize
    private static ArrayList<Pheremone> Pheremones = new ArrayList<Pheremone>();
    /*
     *get's position of this artifact
     */

    public void setPosition(int x, int y) {
        xPos = x;
        yPos = y;
    }
    /*
     *get's the x-position of this artifact
     */

    public int getX() {
        return xPos;
    }
    /*
     *get's the y-position of this artifact
     */

    public int getY() {
        return yPos;
    }
    /*
     *print's the x and y coordinates of this artifact to std.out
     */

    public void printPosition() {
        System.out.println(xPos + " , " + yPos);
    }
    /*
     *print's the type of element that this artifact is to std.out
     */

    public void getMyType() {
        if (this instanceof water) {
            System.out.println("I am Water");
        } else if (this instanceof land) {
            System.out.println("I am Land");
        } else if (this instanceof obstacle) {
            System.out.println("I am obstacle");
        } else if (this instanceof plant) {
            System.out.println("I am plant");
        } else {
            System.out.println("I am nothing");
        }
    }

    public static void setAmountOfHomes(int i) {
        amountOfHomes = i;
    }

    public void initializePheremoneArrays() {
        try {
            if (amountOfHomes == 0) {
                System.out.println("FoodPheremone Array cannot be initialized because amountOfHomes is 0/undefined/uninitialized");
                throw new IOException();
            } else {
                 
                foodPheremoneScent = new int[amountOfHomes];
                homePheremoneScent = new int[amountOfHomes];
                homeScent = new int[amountOfHomes]; //actual home scent (different from homePheremoneScent)
            }
        } catch (IOException e) {
            System.out.println(e.getClass());
        }
    }

    /*
     *Updates the food pheremone scent at this location
     *@param h is the tribe/home number of the ant
     */
    public void updateFoodPheremoneScent(int h) {
        foodPheremoneScent[h]++;
    }

    public void setFoodPheremoneScent(int h, int value) {
        foodPheremoneScent[h] = value;
    }
    /*
     *Updates the home pheremone scent at this location
     *@param h is the tribe/home number of the ant
     */

    public void updateHomePheremoneScent(int h) {
        homePheremoneScent[h]++;
    }

    public void updateHomePheremoneScent(int h, int value) {
        homePheremoneScent[h] = value;
    }

    /*
     *Updates the home scent at this location
     *@param h is the tribe/home number of the ant
     */
    public void updateHomeScent(int h) {
        homeScent[h]++;
    }

    public void setHomeScent(int h, int value) {
        homeScent[h] = value;
    }

    public void updateFoodScent() {
        foodScent++;
    }

    public void setFoodScent(int value) {
        foodScent = value;
    }
    /*
     *adds to the home scent at this location
     *@param h is the tribe/home number of the ant
     *@param n is the amount of scent being added to the current amount
     */

    public void addFoodPheremoneScent(int h, int n) {
        foodPheremoneScent[h] = foodPheremoneScent[h] + n;

    }

    public void addHomePheremoneScent(int h, int n) {
        homePheremoneScent[h] = homePheremoneScent[h] + n;

    }

    public void addHomeScent(int h, int n) {
        homeScent[h] = homeScent[h] + n;
    }

    public void addFoodScent(int n) {
        foodScent = foodScent + n;

    }

}

class water extends Artifact {

}

class land extends Artifact {

}

class plant extends Artifact {

    private int amountOfFood;

    public boolean hasFood() {
        if (amountOfFood <= 0) {
            return false;
        } else {
            return true;
        }

    }

    public void setAmountOfFood(int n) {
        amountOfFood = n;
    }

    public void takeOneFood() {
        amountOfFood--;
    }

    public void GenerateOneFood() {
        amountOfFood++;
    }

    public void addFood(int f) {
        amountOfFood = amountOfFood + f;
    }

    public void takeAwayFood(int f) {
        amountOfFood = amountOfFood - f;
    }

}

class obstacle extends Artifact {

}

class home extends Artifact {

    private int homeNumber;
    private static int amountOfHomes;
    private int currNumAnts;
    private int nextAnt = 0;
    Map<Integer, Ant> colony = new HashMap<>();
    private ArrayList<Ant> atHomePool = new ArrayList<Ant>();

    /*
     *Generates Ant Colony with amount of ants specified
     *Tags every ant with its colony number (0-indexed)
     *sets every ant with it's initial position at the home's position
     */
    public void generateAntColony(int initialAnts) {

        for (int i = 0; i < initialAnts; i++) {
            colony.put(i, new Ant());

            colony.get(i).setAntID(Integer.toString(this.getHomeNumber()) + Integer.toString(i));

            colony.get(i).setPosition(this.getX(), this.getY());
            colony.get(i).setHomeNumber(homeNumber);
            colony.get(i).setAlive(true);
            currNumAnts = initialAnts;
            atHomePool.add(colony.get(i));

            if (!(myWorld.getWorldObject(this.getX() + 1, this.getY()) instanceof water) || !(myWorld.getWorldObject(this.getX() + 1, this.getY()) instanceof obstacle)) {
                colony.get(i).setDirection("E");

            } else if (!(myWorld.getWorldObject(this.getX() - 1, this.getY()) instanceof water) || !(myWorld.getWorldObject(this.getX() - 1, this.getY()) instanceof obstacle)) {
                colony.get(i).setDirection("W");

            } else if (!(myWorld.getWorldObject(this.getX(), this.getY() + 1) instanceof water) || !(myWorld.getWorldObject(this.getX(), this.getY() + 1) instanceof obstacle)) {
                colony.get(i).setDirection("N");

            } else if (!((myWorld.getWorldObject(this.getX(), this.getY() - 1) instanceof water) || !(myWorld.getWorldObject(this.getX(), this.getY() - 1) instanceof obstacle))) {
                colony.get(i).setDirection("S");
            }

        }

    }

    /*
     *releases Ant from home 
     */
    public Ant releaseAnt() {
        currNumAnts--;
        return atHomePool.remove(0);
    }
    /*
     *set's home identification number (0-indexed)
     */

    public void setHomeNumber(int h) {
        homeNumber = h;
    }
    /*
     *class value stores how many total homes exist in the world
     */

    public static void setAmountOfHomes(int i) {
        amountOfHomes = i;
    }
    /*
     *class value sets the number of ants that will live in every colony
     */

    public int getHomeNumber() {
        return homeNumber;
    }

    public Ant getAnt(int id) { //ant ID
        return colony.get(id);
    }

    public void generateAnt() {
        currNumAnts++;
        colony.put(colony.size(), new Ant());

        colony.get(colony.size()).setAntID(Integer.toString(this.getHomeNumber()) + Integer.toString(colony.size()));
        colony.get(colony.size()).setPosition(this.getX(), this.getY());
        colony.get(colony.size()).setHomeNumber(homeNumber);
        colony.get(colony.size()).setAlive(true);

        if (!(myWorld.getWorldObject(this.getX() + 1, this.getY()) instanceof water) || !(myWorld.getWorldObject(this.getX() + 1, this.getY()) instanceof obstacle)) {
            colony.get(colony.size()).setDirection("E");

        } else if (!(myWorld.getWorldObject(this.getX() - 1, this.getY()) instanceof water) || !(myWorld.getWorldObject(this.getX() - 1, this.getY()) instanceof obstacle)) {
            colony.get(colony.size()).setDirection("W");

        } else if (!(myWorld.getWorldObject(this.getX(), this.getY() + 1) instanceof water) || !(myWorld.getWorldObject(this.getX(), this.getY() + 1) instanceof obstacle)) {
            colony.get(colony.size()).setDirection("N");

        } else if (!((myWorld.getWorldObject(this.getX(), this.getY() - 1) instanceof water) || !(myWorld.getWorldObject(this.getX(), this.getY() - 1) instanceof obstacle))) {
            colony.get(colony.size()).setDirection("S");
        }

    }
}
