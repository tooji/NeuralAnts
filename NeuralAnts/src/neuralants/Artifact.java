/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.io.IOException;
import java.util.Iterator;
import java.util.*;
import java.lang.Math;

/**
 *
 * @author tooji
 */
public class Artifact {

    private int xPos;
    private int yPos;
    private static int amountOfHomes;
    private double[] foodPheromoneScent; //only those ants from the same tribe can recognize
    private double[] homePheromoneScent; //only those ants from the same tribe can recognize
    private double[] homeScent;    //only those ants from the same tribe can recognize
    private double foodScent;      //All ants can recognize
    private Map<Integer, HomePheromone> homePheromones = new HashMap<>();
    private Map<Integer, FoodPheromone> foodPheromones = new HashMap<>();
    private Map<Integer, Food> foodList = new HashMap<>();

    public Artifact() {
        this.foodPheromoneScent = new double[myWorld.getAmountOfHomes()];
        this.homeScent = new double[myWorld.getAmountOfHomes()];
        this.homePheromoneScent = new double[myWorld.getAmountOfHomes()];

    }

    public Artifact(Artifact another) {
        this.xPos = another.xPos;
        this.yPos = another.yPos;
    }


    /*
     *get's position of this artifact
     */
    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }
    /*
     *get's the x-position of this artifact
     */

    public int getX() {
        return this.xPos;
    }
    /*
     *get's the y-position of this artifact
     */

    public int getY() {
        return this.yPos;
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

    public void initializePheromoneArrays() {
        try {
            if (amountOfHomes == 0) {
                System.out.println("FoodPheromone Array cannot be initialized because amountOfHomes is 0/undefined/uninitialized");
                throw new IOException();
            } else {

                foodPheromoneScent = new double[amountOfHomes];
                homePheromoneScent = new double[amountOfHomes];
                homeScent = new double[amountOfHomes]; //actual home scent (different from homePheromoneScent)
            }
        } catch (IOException e) {
            System.out.println(e.getClass());
        }
    }

    /**
     * Updates the food pheremone scent at this location
     *
     * @param h is the tribe/home number of the ant
     */
    public void updateFoodPheromoneScent(int h) {
        foodPheromoneScent[h]++;
    }

    public void setFoodPheromoneScent(int h, int value) {
        foodPheromoneScent[h] = value;
    }

    /**
     * Updates the home pheromone scent at this location
     *
     * @param h is the tribe/home number of the ant
     */
    public void updateHomePheromoneScent(int h) {
        homePheromoneScent[h]++;
    }

    public void updateHomePheromoneScent(int h, int value) {
        homePheromoneScent[h] = value;
    }

    /**
     * Updates the home scent at this location
     *
     * @param h is the tribe/home number of the ant
     */
    public void updateHomeScent(int h) {
        homeScent[h]++;
    }

    public void setHomeScent(int h, int value) {
        homeScent[h] = value;
    }

    public void setHomePheromoneScent(int h, int value) {
        homePheromoneScent[h] = value;
    }

    public void updateFoodScent() {
        foodScent++;
    }

    public void setFoodScent(int value) {
        foodScent = value;
    }

    /**
     * adds to the foodPheromone scent at this location
     *
     * @param h is the tribe/home number of the ant
     * @param n is the amount of scent being added to the current amount
     */
    public void addFoodPheromoneScent(int h, double n) {
        if (foodPheromoneScent[h] > 0) {
            foodPheromoneScent[h] = 10 * Math.log10(Math.pow(10, (foodPheromoneScent[h] / 10)) + (Math.pow(10, n / 10)));
        } else if (foodPheromoneScent[h] == 0) {
            foodPheromoneScent[h] = n;
        } else {
            System.out.println("cannot add scent something went wrong");
        }
    }

    /**
     * adds to the homePheromone scent at this location
     *
     * @param h is the tribe/home number of the ant
     * @param n is the amount of scent being added to the current amount
     */
    public void addHomePheromoneScent(int h, double n) {
        if (homePheromoneScent[h] > 0) {
            homePheromoneScent[h] = 10 * Math.log10(Math.pow(10, (homePheromoneScent[h] / 10)) + (Math.pow(10, n / 10)));
        } else if (homePheromoneScent[h] == 0) {
            homePheromoneScent[h] = n;
        } else {
            System.out.println("cannot add scent something went wrong");
        }

    }

    public void addHomeScent(int h, double n) {
        if (homeScent[h] > 0) {
            homeScent[h] = 10 * Math.log10(Math.pow(10, (homeScent[h] / 10)) + (Math.pow(10, n / 10)));
        } else if (homeScent[h] == 0) {
            homeScent[h] = n;
        } else {
            System.out.println("cannot add scent something went wrong");
        }

    }

    public void addFoodScent(double n) {
        if (foodScent > 0) {
            foodScent = 10 * Math.log10(Math.pow(10, (foodScent / 10)) + (Math.pow(10, n / 10)));
        } else if (foodScent == 0) {
            foodScent = n;
        } else {
            System.out.println("cannot add scent something went wrong");
        }

    }

    public void addFoodPheromone(int h) {
        foodPheromones.put((foodPheromones.size()), new FoodPheromone());
        foodPheromones.get(foodPheromones.size() - 1).setPos(this.getX(), this.getY());
        foodPheromones.get(foodPheromones.size() - 1).setHomeNumber(h);

        for (int i = 0; i < myWorld.getAmountOfColumns(); i++) {
            for (int j = 0; j < myWorld.getAmountOfRows(); j++) {
                foodPheromones.get(foodPheromones.size() - 1).disperseScent(i, j);
            }
        }

    }

    public void addHomePheromone(int h) {
        homePheromones.put((homePheromones.size()), new HomePheromone());
        homePheromones.get(homePheromones.size() - 1).setPos(this.getX(), this.getY());
        homePheromones.get(homePheromones.size() - 1).setHomeNumber(h);

        for (int i = 0; i < myWorld.getAmountOfColumns(); i++) {
            for (int j = 0; j < myWorld.getAmountOfRows(); j++) {
                homePheromones.get(homePheromones.size() - 1).disperseScent(i, j);
            }
        }

    }

    public void addFood(Food food) {
        foodList.put(foodList.size(), food);
        foodList.get(foodList.size() - 1).setPos(this.getX(), this.getY());
    }

    public void addFood() {
        foodList.put(foodList.size(), new Food());
        foodList.get(foodList.size() - 1).setPos(this.getX(), this.getY());
    }

    public FoodPheromone getFoodPheromone(int i) {
        return foodPheromones.get(i);
    }

    public Food getFood(int i) {
        return foodList.get(i);
    }

    public void disperseFoodPheromoneScents() {
        //System.out.println("dispersing scents from this locatipn");

        for (int x = 0; x < myWorld.getAmountOfColumns(); x++) {
            for (int y = 0; y < myWorld.getAmountOfRows(); y++) {
                Iterator<Map.Entry<Integer, FoodPheromone>> entries = foodPheromones.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<Integer, FoodPheromone> entry = entries.next();

                    entry.getValue().disperseScent(x, y);
                }
            }

        }

    }

    public void disperseHomePheromoneScents() {
        //System.out.println("dispersing Pheremone Scents at x: "+this.getX()+" y: "+this.getY());

        for (int x = 0; x < myWorld.getAmountOfColumns(); x++) {
            for (int y = 0; y < myWorld.getAmountOfRows(); y++) {
                Iterator<Map.Entry<Integer, HomePheromone>> entries = homePheromones.entrySet().iterator();
                while (entries.hasNext()) {

                    Map.Entry<Integer, HomePheromone> entry = entries.next();

                    entry.getValue().disperseScent(x, y);
                }

            }
        }

    }

    public void disperseFoodScents() {

        for (int x = 0; x < myWorld.getAmountOfColumns(); x++) {
            for (int y = 0; y < myWorld.getAmountOfRows(); y++) {
                Iterator<Map.Entry<Integer, Food>> entries = foodList.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<Integer, Food> entry = entries.next();

                    entry.getValue().disperseScent(x, y);
                }
            }
        }
    }

    public void ageFoodPheromones() {
        Iterator<Map.Entry<Integer, FoodPheromone>> entries = foodPheromones.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, FoodPheromone> entry = entries.next();
            entry.getValue().agePheromone();

        }
    }

    public void ageHomePheromones() {
        Iterator<Map.Entry<Integer, HomePheromone>> entries = homePheromones.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, HomePheromone> entry = entries.next();
            entry.getValue().agePheromone();
        }

    }

    public void ageFood() {
        Iterator<Map.Entry<Integer, Food>> entries = foodList.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Food> entry = entries.next();
            entry.getValue().ageFood();
        }

    }

    public double getFoodPheromoneScent(int h) {
        return foodPheromoneScent[h];
    }

    public double getHomePheromoneScent(int h) {
        return homePheromoneScent[h];
    }

    public double getFoodScent() {
        return foodScent;
    }

    public double getHomeScent(int h) {
        return homeScent[h];
    }

    public void pickUpFirstFood(Ant x) {
        if (foodList.get(foodList.size() - 1) != null) {
            x.addToFoodIHave(this.foodList.remove(foodList.size() - 1));
            x.setHasFood(true);
            System.out.println("Hey Ma i just picked up some food! andtID " + x.getAntID());
        }
    }

    public int getAmountOfFood() {
        return this.foodList.size();
    }

    public int getAmountOfFoodPheromones() {
        return this.foodPheromones.size();
    }

    public int getAmountOfFoodPheromones(int h) {
        int count = 0;
        Iterator<Map.Entry<Integer, FoodPheromone>> entries = foodPheromones.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, FoodPheromone> entry = entries.next();
            if (entry.getValue().getHomeNumber() == h) {
                count++;
            }

        }
        return count;
    }

    public int getAmountOfHomePheromones() {
        return this.homePheromones.size();
    }

    public int getAmountOfHomePheromones(int h) {
        int count = 0;
        Iterator<Map.Entry<Integer, HomePheromone>> entries = homePheromones.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, HomePheromone> entry = entries.next();

            if (entry.getValue().getHomeNumber() == h) {
                count++;

            }
        }
        return count;
    }

}

class water extends Artifact {

    public water() {
    }

    public water(Artifact another) {
        this.setPosition(another.getX(), another.getY());
    }

}

class land extends Artifact {

    public land() {
    }

    public land(Artifact another) {
        this.setPosition(another.getX(), another.getY());
    }

}

class plant extends Artifact {

    public plant() {
    }

    public plant(Artifact another) {
        this.setPosition(another.getX(), another.getY());
    }

    public void GenerateOneFood() {

        this.addFood();

    }

}

class obstacle extends Artifact {

    public obstacle() {
    }

    public obstacle(Artifact another) {
        this.setPosition(another.getX(), another.getY());
    }

}

class home extends Artifact {

    private int homeNumber;
    private int currNumAnts;
    private int nextAnt = 0;
    private int foodCollectedScore;
    Map<Integer, Ant> colony = new HashMap<>();
    private ArrayList<Ant> atHomePool = new ArrayList<Ant>();

    public home() {
    }

    public home(Artifact another) {
        this.setPosition(another.getX(), another.getY());
    }

    /**
     * Generates Ant Colony with amount of ants specified Tags every ant with
     * its colony number (0-indexed) sets every ant with it's initial position
     * at the home's position
     *
     * @param initialAnts is the initial amount of Ants the colony begins with
     */
    public void generateAntColony(int initialAnts) {

        for (int i = 0; i < initialAnts; i++) {
            colony.put(i, new Ant());

            colony.get(i).setAntID(String.format("%03d", this.homeNumber) + String.format("%04d", i));

            colony.get(i).setPosition(this.getX(), this.getY());
            colony.get(i).setHomeNumber(homeNumber);
            colony.get(i).setRandomnessFactor(NeuralAnts.getRandomnessFactor());
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

    public void generateAntColony(int initialAnts, String dir) {

        for (int i = 0; i < initialAnts; i++) {
            colony.put(i, new Ant());

            colony.get(i).setAntID(String.format("%03d", this.homeNumber) + String.format("%04d", i));

            colony.get(i).setPosition(this.getX(), this.getY());
            colony.get(i).setHomeNumber(homeNumber);
            colony.get(i).setRandomnessFactor(NeuralAnts.getRandomnessFactor());
            colony.get(i).setAlive(true);
            colony.get(i).setDirection(dir);
            currNumAnts = initialAnts;
            atHomePool.add(colony.get(i));

        }
    }

    /**
     * releases Ant from home reduces currNumAnts by 1 removes it from home's
     * "atHomePool" takes first ant in the pool
     */
    public Ant releaseAnt() {
        currNumAnts--;
        if (atHomePool.get(0) != null) {
            return atHomePool.remove(0);
        } else {
            return null;
        }
    }

    /**
     * set's home identification number (0-indexed)
     *
     * @param h is the ants home number
     */
    public void setHomeNumber(int h) {
        homeNumber = h;
    }

    /**
     * returns homeNumber of the specified home
     *
     * @return
     */
    public int getHomeNumber() {
        return homeNumber;
    }

    /**
     * returns ant with specified id
     *
     * @param id is the ants ID
     * @return returns colony member with that ID
     */
    public Ant getAnt(int id) { //ant ID
        return colony.get(id);
    }

    /**
     * generates a new ant in the colony sets the ants ID to its HomeNumber
     * along with its place in the colony for example ant with homenumber 1 and
     * is the 3rd ant in the colony has ID 13, homeNumber 1 and 33 ant ID is 133
     * sets ants positions to homes current position sets its alive status to
     * true sets its initial direction
     */
    public void generateAnt() {
        currNumAnts++;
        colony.put(colony.size(), new Ant());

        colony.get(colony.size() - 1).setAntID(String.format("%03d", this.homeNumber) + String.format("%04d", colony.size() - 1));
        colony.get(colony.size() - 1).setPosition(this.getX(), this.getY());
        colony.get(colony.size() - 1).setHomeNumber(homeNumber);
        colony.get(colony.size() - 1).setAlive(true);
        colony.get(colony.size() - 1).setRandomnessFactor(NeuralAnts.getRandomnessFactor());
        atHomePool.add(colony.get(colony.size() - 1));

        if (!(myWorld.getWorldObject(this.getX() + 1, this.getY()) instanceof water) || !(myWorld.getWorldObject(this.getX() + 1, this.getY()) instanceof obstacle)) {
            colony.get(colony.size() - 1).setDirection("E");

        } else if (!(myWorld.getWorldObject(this.getX() - 1, this.getY()) instanceof water) || !(myWorld.getWorldObject(this.getX() - 1, this.getY()) instanceof obstacle)) {
            colony.get(colony.size() - 1).setDirection("W");

        } else if (!(myWorld.getWorldObject(this.getX(), this.getY() + 1) instanceof water) || !(myWorld.getWorldObject(this.getX(), this.getY() + 1) instanceof obstacle)) {
            colony.get(colony.size() - 1).setDirection("N");

        } else if (!((myWorld.getWorldObject(this.getX(), this.getY() - 1) instanceof water) || !(myWorld.getWorldObject(this.getX(), this.getY() - 1) instanceof obstacle))) {
            colony.get(colony.size() - 1).setDirection("S");
        }

    }

    public void generateAnt(String dir) {
        currNumAnts++;
        colony.put(colony.size(), new Ant());

        colony.get(colony.size() - 1).setAntID(String.format("%03d", this.homeNumber) + String.format("%04d", colony.size() - 1));
        colony.get(colony.size() - 1).setPosition(this.getX(), this.getY());
        colony.get(colony.size() - 1).setHomeNumber(homeNumber);
        colony.get(colony.size() - 1).setAlive(true);
        colony.get(colony.size() - 1).setRandomnessFactor(NeuralAnts.getRandomnessFactor());
        colony.get(colony.size() - 1).setDirection(dir);
        atHomePool.add(colony.get(colony.size() - 1));

    }

    public void collectFood() {
        this.foodCollectedScore++;
    }
    
    public int getHomePoolSize(){
        return this.atHomePool.size();
    }

    public int getFoodColleectedSCore() {
        return this.foodCollectedScore;
    }
}
