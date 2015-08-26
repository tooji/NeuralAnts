/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.io.BufferedReader;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;
import java.io.*;
import java.util.Collections;

/**
 *
 * @author tooji
 */
public class myWorld {

    private static int row;
    private static int col;
    private static int homes;
    private static int numAnts;
    private static int plantCount;
    private static int obstacleCount;
    private static boolean worldGeneratedFlag;
    private static Artifact[][] world;
    private static Artifact[] obstaclePositions;
    private static Artifact[] plantPositions;
    private static Artifact[] homePositions;
    private static ArrayList<Ant> onMapPool = new ArrayList<Ant>();
    private static ArrayList<Ant> deadAntPool = new ArrayList<Ant>();
    private static ArrayList<FoodPheromone> foodPheromonePool = new ArrayList<FoodPheromone>();
    private static ArrayList<HomePheromone> homePheromonePool = new ArrayList<HomePheromone>();

    /*
     *Using the .dat file provided a world is created
     *the world is a 2-D array of artifacts consisting of land, water, plants, obstacles, and homes
     *@param s should be path of .dat file
     */
    public static void createTerrain(String s) {

        final int numDataLines = 4;
        String curLine;
        String rowS;
        String colS;
        String homeS;
        String numAntS;
        plantCount = 0;
        obstacleCount = 0;

        BufferedReader br = null;

        try {
            File f = new File(s);
            br = new BufferedReader(new FileReader(f));
            int curRowNumber = 0;   // keep current count of rows

            while ((curLine = br.readLine()) != null) {
                System.out.println(curLine);

                if (curRowNumber == 0) {
                    if (curLine.matches("row=\\s\\d+")) {    //if the first line is row= #, read the data

                        String[] parts = curLine.split("\\s");
                        rowS = parts[1];
                        row = Integer.parseInt(rowS);
                        System.out.println("number of rows set to: " + row);

                        if (row == 0) {
                            System.out.println("World must Have atleast 1 row");
                            throw new IOException();
                        }
                    } else {
                        System.out.println("First row of data file must be of format 'row= #'");
                        throw new IOException();
                    }
                } else if (curRowNumber == 1) {     //if the second line is col= #, read the data

                    if (curLine.matches("col=\\s\\d+")) {

                        String[] parts = curLine.split("\\s");
                        colS = parts[1];
                        col = Integer.parseInt(colS);
                        System.out.println("Number of Columns set to: " + col);

                        if (col == 0) {
                            System.out.println("World must Have atleast 1 column");
                            throw new IOException();
                        }
                    } else {
                        System.out.println("Second row of data file must be of format 'col= #'");
                        throw new IOException();
                    }
                } else if (curRowNumber == 2) {       // if the 3rd row is homes= #, read the data

                    if (curLine.matches("homes=\\s\\d+")) {
                        String[] parts = curLine.split("\\s");
                        homeS = parts[1];
                        homes = Integer.parseInt(homeS);
                        System.out.println("Number of Homes is set to: " + homes);
                        Artifact.setAmountOfHomes(homes);

                        if (homes == 0) {
                            System.out.println("World must have atleast 1 home");
                            throw new IOException();
                        }

                    } else {
                        System.out.println("Third row of data file must be of format 'homes= #'");
                        throw new IOException();
                    }

                } else if (curRowNumber == 3) {       // if the 3rd row is homes= #, read the data

                    if (curLine.matches("numAnts=\\s\\d+")) {
                        String[] parts = curLine.split("\\s");
                        numAntS = parts[1];
                        numAnts = Integer.parseInt(numAntS);
                        System.out.println("Number of ants per home is set to: " + numAnts);

                        if (numAnts == 0) {
                            System.out.println("Each colony must have atleast 1 ant");
                            throw new IOException();
                        }

                    } else {
                        System.out.println("Third row of data file must be of format 'numAnts= #'");
                        throw new IOException();
                    }
                }
                curRowNumber++;
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getClass());

        } catch (IOException e) {
            System.out.println(e.getClass());

        } finally {
            if (br != null) {
                try {
                    System.out.println("closing BufferedReader");
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getClass());
                }
            }

        }

        homePositions = new Artifact[homes];
        world = new Artifact[col][row];
        // System.out.println("row=  "+row+"col=  "+col);
        try {

            File fi = new File(s);
            br = new BufferedReader(new FileReader(fi));
            int curRowNumberX = 0;   // keep current count of rows
            int terrainRow = row;
            int homeCount = 0;
            while ((curLine = br.readLine()) != null) {

                if (curRowNumberX > (numDataLines - 1)) { //for all the following rows read the data into the terrain
                    // System.out.println("terrainRow =  " + terrainRow);
                    //int terrainRow = curRowNumberX - numDataLines;     //keep a temp var that stores the current row of the terrain being read
                    //create an array of artifacts-- artifacts can be land, obstacle, water, plant
                    if (curLine.matches("([O|L|W|P])\\w+")) {  //if the row has has the right format
                        //System.out.println("Line has right format");
                        char[] lineWorld = curLine.toCharArray();   //chop up line into character array called lineworld

                        if (col != lineWorld.length) {       //check for the right size
                            System.out.println("set column size does not match actual column size, error occured on row: " + curRowNumberX);
                            throw new IOException();
                        }

                        for (int i = 0; i < col; i++) {
                            //System.out.println("linworld[i]= " + lineWorld[i]);
                            if (terrainRow >= 0) {
                                if (lineWorld[i] == 'L') {
                                    world[i][terrainRow] = new land();
                                    world[i][terrainRow].setPosition(i, terrainRow);
                                    world[i][terrainRow].setFoodScent(0);
                                    //world[i][terrainRow].getMyType();
                                    // System.out.println("At pos: " + i + " , " + terrainRow);

                                }

                                if (lineWorld[i] == 'W') {
                                    world[i][terrainRow] = new water();
                                    world[i][terrainRow].setPosition(i, terrainRow);
                                    world[i][terrainRow].setFoodScent(0);
                                    //world[i][terrainRow].getMyType();
                                    // System.out.println("At pos: " + i + " , " + terrainRow);
                                }

                                if (lineWorld[i] == 'O') {
                                    world[i][terrainRow] = new obstacle();
                                    world[i][terrainRow].setPosition(i, terrainRow);
                                    world[i][terrainRow].setFoodScent(0);
                                    //world[i][terrainRow].getMyType();
                                    if (i != 0 && terrainRow != 0) {
                                        obstacleCount++;
                                    }
                                    //System.out.println("At pos: " + i + " , " + terrainRow);
                                }

                                if (lineWorld[i] == 'P') {
                                    world[i][terrainRow] = new plant();
                                    world[i][terrainRow].setPosition(i, terrainRow);
                                    world[i][terrainRow].setFoodScent(0);
                                    //world[i][terrainRow].getMyType();
                                    plantCount++;
                                    //System.out.println("At pos: " + i + " , " + terrainRow);
                                }

                                if (lineWorld[i] == 'H') {
                                    world[i][terrainRow] = new land();
                                    world[i][terrainRow].setPosition(i, terrainRow);
                                    world[i][terrainRow].setFoodScent(0);
                                    world[i][terrainRow] = new home(world[i][terrainRow]);
                                    ((home) world[i][terrainRow]).setHomeNumber(homeCount); //home identification tag is 0 indexed
                                    homePositions[homeCount] = new home(world[i][terrainRow]);
                                    homeCount++;
                                    System.out.println("homeCount is now " + homeCount + "and the amount of set homes is" + homes);
                                    
                                    if(homeCount > homes){
                                        System.out.println("More homes than specified");
                                        throw new IOException();
                                    }
                                }

                            }
                        }
                    } else {
                        // System.out.println("Match")
                    }

                    // System.out.println(curRowNumber);
                    terrainRow--;
                }

                curRowNumberX++;

            }

            plantPositions = new Artifact[plantCount];
            obstaclePositions = new Artifact[obstacleCount];

        } catch (FileNotFoundException e) {
            System.out.println(e.getClass());

        } catch (IOException e) {
            System.out.println(e.getClass());

        } finally {
            if (br != null) {
                try {
                    System.out.println("closing BufferedReader");
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getClass());
                }
            }

        }
        plantPositions = new Artifact[plantCount];
        obstaclePositions = new Artifact[obstacleCount];
        worldGeneratedFlag = true;
    }
    /*
     *randomly generates homes throughout the generated terrain
     */

    public static void GenerateHomes() {

        try {
            //System.out.println("world[10][10] is " );
            //world[10][10].getMyType();
            if (worldGeneratedFlag == false) {
                System.out.println("Error: cannot generate homes, world not yet generated -- Please call myWorld.createTerrain first and provide it with the appropriate data file");
                throw new IOException();
            }

            int homeCount = 0;      //keep count of home/hives generated
            System.out.println("Generating homes...");

            while (homeCount != homes) {
                //randomly generate homes on the land artifacts
                Random rand = new Random();

                // nextInt is normally exclusive of the top value,
                // so add 1 to make it inclusive
                int randomY = rand.nextInt((row));
                System.out.println("randomY= " + randomY);
                int randomX = rand.nextInt((col));
                System.out.println("randomX= " + randomX);
                world[randomX][randomY].getMyType();

                if (world[randomX][randomY] instanceof land && !(world[randomX][randomY] instanceof home)) {

                    if (!(randomX + 1 >= col - 1 || randomX - 1 < 0 || randomY + 1 >= row - 1 || randomY - 1 < 0)) {  //assures ants have space to leave home 

                        world[randomX][randomY] = new home(world[randomX][randomY]);
                        ((home) world[randomX][randomY]).setHomeNumber(homeCount); //home identification tag is 0 indexed
                        homePositions[homeCount] = new home(world[randomX][randomY]);
                        homeCount++;
                        System.out.println("homeCount is now " + homeCount + "and the amount of set homes is" + homes);

                    }
                }

            }
            System.out.println("done generating homes");

            System.out.println("generating ant colonies...");

            for (int k = 0; k < homePositions.length; k++) {
                System.out.println("homePositions[k].getX()=  " + homePositions[k].getX() + " homePositions[k].getY()= " + homePositions[k].getY());
                //world[homePositions[k].getX()][homePositions[k].getY()].getMyType();
                ((home) world[homePositions[k].getX()][homePositions[k].getY()]).generateAntColony(numAnts);

            }

            System.out.println("done generating ant colonies");
            //printWorld();
            //intialize plant positions
            initializePlantPositions();
            //initialize obstacle positions
            initializeObstacletPositions();

        } catch (IOException e) {
            System.out.println(e.getClass());
        }

    }

    public static void generateAntColonies(String dir) {
        for (int k = 0; k < homePositions.length; k++) {
            System.out.println("homePositions[k].getX()=  " + homePositions[k].getX() + " homePositions[k].getY()= " + homePositions[k].getY());
            //world[homePositions[k].getX()][homePositions[k].getY()].getMyType();
            ((home) world[homePositions[k].getX()][homePositions[k].getY()]).generateAntColony(numAnts, dir);

        }
    }
    
    public static void generateAntColonies() {
        for (int k = 0; k < homePositions.length; k++) {
            System.out.println("homePositions[k].getX()=  " + homePositions[k].getX() + " homePositions[k].getY()= " + homePositions[k].getY());
            //world[homePositions[k].getX()][homePositions[k].getY()].getMyType();
            ((home) world[homePositions[k].getX()][homePositions[k].getY()]).generateAntColony(numAnts);

        }
    }
    
    public static void initializePlantPositions() {
        int plantCounter = 0;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                if (world[i][j] instanceof plant) {
                    plantPositions[plantCounter] = new plant(world[i][j]);
                    plantCounter++;
                }

            }

        }

    }

    public static void initializeObstacletPositions() {
        int obstacleCounter = 0;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                if (world[i][j] instanceof obstacle) {
                    if (!(world[i][j].getY() == 0 || world[i][j].getX() == 0)) {
                        obstaclePositions[obstacleCounter] = new obstacle(world[i][j]);
                        obstacleCounter++;
                    }
                }

            }

        }

    }
    /*
     *returns array of homes on the map
     *is there a point of this??
     */

    public static Artifact[] getHomePositions() {

        return homePositions;

    }

    public static void printHomePositions() {
        for (int i = 0; i < homePositions.length; i++) {
            System.out.println("position of homeNumber " + i + " is " + homePositions[i].getX() + " , " + homePositions[i].getY());
        }
    }

    public static Artifact[][] getWorld() {
        return world;
    }

    public static Artifact getWorldObject(int x, int y) {

        if (x >= col || y >= row || x < 0 || y < 0) {
            System.out.println("cannot access world object out of bounds exception");
            return null;
        } else {
            return world[x][y];
        }

    }

    public static Artifact getHome(int h) {
        //System.out.println("homeNumber is "+ h);
        if (h < homePositions.length) {
            return world[homePositions[h].getX()][homePositions[h].getY()];
        } else {
            System.out.println("homePositions.length=  " + homePositions.length);
            return null;
        }
    }

    public static int getAmountOfHomes() {
        return homes;

    }

    public static int getAmountOfRows() {
        return row;

    }

    public static int getAmountOfColumns() {
        return col;
    }

    public static boolean isWorldGenerated() {
        return worldGeneratedFlag;
    }

    public static void addToWorldPool(Ant x) {
        onMapPool.add(x);
    }

    /*
     *gets returns 
     */
    public static int getWorldPoolSize() {
        return onMapPool.size();

    }

    public static Ant getWorldPoolAnt(int index) {
        return onMapPool.get(index);

    }

    public static Ant getDeadAnt(int index) {

        return deadAntPool.get(index);

    }

    public static void KillAnt(Ant x) {
        onMapPool.remove(x);
        x.setAlive(false);
        deadAntPool.add(x);

    }

    public boolean homeCheck() {
        for (int h = 0; h < homes; h++) {
            if (!(world[homePositions[h].getX()][homePositions[h].getY()] instanceof home)) {
                return false;
            }
        }

        return true;
    }

    public void growFruits() {
        for (int i = 0; i < plantCount; i++) {
            ((plant) world[plantPositions[i].getX()][plantPositions[i].getY()]).GenerateOneFood();
        }

    }

    public boolean plantCheck() {
        for (int h = 0; h < homes; h++) {
            if (!(world[homePositions[h].getX()][homePositions[h].getY()] instanceof home)) {
                return false;
            }
        }

        return true;
    }

    public static void ageFoodPheromones(int i, int j) {
        myWorld.getWorldObject(i, j).ageFoodPheromones();
    }

    public static void ageHomePheromones(int i, int j) {
        myWorld.getWorldObject(i, j).ageHomePheromones();
    }

    public static void ageFood(int i, int j) {
        myWorld.getWorldObject(i, j).ageFood();
    }

    /*
     *Goes through world map and sets the food scents 
     */
    public static void disperseFoodSmells(int i, int j) {

        myWorld.getWorldObject(i, j).disperseFoodScents();

    }

    public static void disperseHomeSmells(int i, int j) {

        // for (int i = 0; i < col; i++) {
        //    for (int j = 0; j < row; j++) {
        for (int p = 0; p < homes; p++) {
            boolean addScent = false;
            //System.out.println("Distance between world x: " + i + "y " + j + " and home is " + getDistance(world[i][j], myWorld.homePositions[p]));
            if (getDistance(world[i][j], myWorld.homePositions[p]) == 0) {
                if (world[i][j] instanceof home) {
                    world[i][j].addHomeScent(p, 100);
                }
            } else if (getDistance(world[i][j], homePositions[p]) <= 1) {
                if (world[i][j] instanceof land || world[i][j] instanceof water) {
                    addScent = true;
                    for (int o = 0; o < obstacleCount; o++) {
                        if (getDirectionOfArtifact(world[i][j], world[homePositions[p].getX()][homePositions[p].getY()]) == (getDirectionOfArtifact(obstaclePositions[o], homePositions[p]))) {
                            if (getDistance(world[i][j], homePositions[p]) > getDistance(obstaclePositions[o], homePositions[p])) {
                                addScent = false;
                            }
                        }
                    }
                    if (addScent == true) {
                        // System.out.println("the homeScent Before is "+ world[i][j].getHomeScent(p));
                        world[i][j].addHomeScent(p, 25);
                        //System.out.println("added 25 homescent");
                        //System.out.println("homescent at this location is now: " + world[i][j].getHomeScent(p));
                        addScent = false;
                    }
                }
            } else if (getDistance(world[i][j], homePositions[p]) <= 2) {
                if (world[i][j] instanceof land || world[i][j] instanceof water) {
                    addScent = true;
                    for (int o = 0; o < obstacleCount; o++) {
                        if (getDirectionOfArtifact(world[i][j], world[homePositions[p].getX()][homePositions[p].getY()]) == (getDirectionOfArtifact(obstaclePositions[o], homePositions[p]))) {
                            if (getDistance(world[i][j], homePositions[p]) > getDistance(obstaclePositions[o], homePositions[p])) {
                                addScent = false;
                            }

                        }
                    }
                    if (addScent == true) {
                        // System.out.println("the homeScent Before is "+ world[i][j].getHomeScent(p));
                        world[i][j].addHomeScent(p, 11);
                        // System.out.println("added 11 homescent");
                        // System.out.println("homescent at this location is now: " + world[i][j].getHomeScent(p));
                        addScent = false;
                    }

                }
            } else if (getDistance(world[i][j], homePositions[p]) <= 3) {
                if (world[i][j] instanceof land || world[i][j] instanceof water) {
                    addScent = true;
                    for (int o = 0; o < obstacleCount; o++) {
                        if (getDirectionOfArtifact(world[i][j], world[homePositions[p].getX()][homePositions[p].getY()]) == (getDirectionOfArtifact(obstaclePositions[o], homePositions[p]))) {
                            if (getDistance(world[i][j], homePositions[p]) > getDistance(obstaclePositions[o], homePositions[p])) {
                                addScent = false;
                            }

                        }
                    }
                    if (addScent == true) {
                        // System.out.println("the homeScent Before is "+ world[i][j].getHomeScent(p));
                        world[i][j].addHomeScent(p, 6);
                        //System.out.println("added 6 homescent");
                        // System.out.println("homescent at this location is now: " + world[i][j].getHomeScent(p));
                        addScent = false;
                    }
                }
            } else if (getDistance(world[i][j], homePositions[p]) <= 4) {
                if (world[i][j] instanceof land || world[i][j] instanceof water) {
                    addScent = true;
                    for (int o = 0; o < obstacleCount; o++) {
                        if (getDirectionOfArtifact(world[i][j], world[homePositions[p].getX()][homePositions[p].getY()]) == (getDirectionOfArtifact(obstaclePositions[o], homePositions[p]))) {
                            if (getDistance(world[i][j], homePositions[p]) > getDistance(obstaclePositions[o], homePositions[p])) {
                                addScent = false;
                            }

                        }
                    }
                    if (addScent == true) {
                        // System.out.println("the homeScent Before is "+ world[i][j].getHomeScent(p));
                        world[i][j].addHomeScent(p, 4);
                        // System.out.println("added 4 homescent");
                        // System.out.println("homescent at this location is now: " + world[i][j].getHomeScent(p));
                        addScent = false;
                    }

                }
            } else if (getDistance(world[i][j], homePositions[p]) <= 5) {
                if (world[i][j] instanceof land || world[i][j] instanceof water) {
                    addScent = true;
                    for (int o = 0; o < obstacleCount; o++) {
                        if (getDirectionOfArtifact(world[i][j], world[homePositions[p].getX()][homePositions[p].getY()]) == (getDirectionOfArtifact(obstaclePositions[o], homePositions[p]))) {
                            if (getDistance(world[i][j], homePositions[p]) > getDistance(obstaclePositions[o], homePositions[p])) {
                                addScent = false;
                            }

                        }
                    }
                    if (addScent == true) {
                        // System.out.println("the homeScent Before is "+ world[i][j].getHomeScent(p));
                        world[i][j].addHomeScent(p, 2.7);
                        //  System.out.println("added 2.7 homescent");
                        //  System.out.println("homescent at this location is now: " + world[i][j].getHomeScent(p));
                        addScent = false;
                    }

                }
            } else if (getDistance(world[i][j], homePositions[p]) <= 6) {
                if (world[i][j] instanceof land || world[i][j] instanceof water) {
                    addScent = true;
                    for (int o = 0; o < obstacleCount; o++) {
                        if (getDirectionOfArtifact(world[i][j], world[homePositions[p].getX()][homePositions[p].getY()]) == (getDirectionOfArtifact(obstaclePositions[o], homePositions[p]))) {
                            if (getDistance(world[i][j], homePositions[p]) > getDistance(obstaclePositions[o], homePositions[p])) {
                                addScent = false;
                            }

                        }
                    }
                    if (addScent == true) {
                        // System.out.println("the homeScent Before is "+ world[i][j].getHomeScent(p));
                        world[i][j].addHomeScent(p, 2);
                        // System.out.println("added 2 homescent");
                        // System.out.println("homescent at this location is now: " + world[i][j].getHomeScent(p));
                        addScent = false;
                    }

                }
            } else if (getDistance(world[i][j], homePositions[p]) <= 7) {
                // System.out.println("past first condition");
                if (world[i][j] instanceof land || world[i][j] instanceof water) {
                    addScent = true;
                    //  System.out.println("past second condition");
                    for (int o = 0; o < obstacleCount; o++) {
                        // System.out.println("going through obstacles");
                        if (getDirectionOfArtifact(world[i][j], world[homePositions[p].getX()][homePositions[p].getY()]) == (getDirectionOfArtifact(obstaclePositions[o], homePositions[p]))) {

                            if (getDistance(world[i][j], homePositions[p]) > getDistance(obstaclePositions[o], homePositions[p])) {
                                addScent = false;
                            }

                        }
                    }
                    if (addScent == true) {
                        // System.out.println("the homeScent Before is "+ world[i][j].getHomeScent(p));
                        world[i][j].addHomeScent(p, 1.5);
                        // System.out.println("added 1.5 homescent");
                        //  System.out.println("homescent at this location is now: " + world[i][j].getHomeScent(p));
                        addScent = false;
                    }

                }
            }

        }

        //   }
        // }
    }

    public static void disperseFoodPheromoneScent(int i, int j) {
        //  System.out.println("dispersing smells from "+i + "j");
        myWorld.getWorldObject(i, j).disperseFoodPheromoneScents();
    }

    public static void disperseHomePheromoneScent(int i, int j) {
        myWorld.getWorldObject(i, j).disperseHomePheromoneScents();
    }

    public static int getXDistance(Artifact x1, Artifact x2) {
        return ((x2.getX() - x1.getX()));

    }

    public static int getYDistance(Artifact y1, Artifact y2) {

        return ((y2.getY() - y1.getY()));
    }

    public static double getDistance(Artifact origin, Artifact dest) {

        return (Math.sqrt(Math.pow((double) getYDistance(origin, dest), 2) + Math.pow((double) getXDistance(origin, dest), 2)));
    }

    public static double getDirectionOfArtifact(Artifact origin, Artifact dest) {
        int yDist = getYDistance(origin, dest);
        int xDist = getXDistance(origin, dest);
        /*
         if (yDist == 0 && xDist == 0) {
         return "SameObject";
         } else if (yDist == 0 && xDist <= 0) {
         return "W";
         } else if (yDist == 0 && xDist >= 0) {
         return "E";
         } else if (yDist >= 0 && xDist == 0) {
         return "N";
         } else if (yDist <= 0 && xDist == 0) {
         return "S";
         } else if (yDist <= 0 && xDist <= 0) {
         return "SW";
         } else if (yDist <= 0 && xDist >= 0) {
         return "SE";
         } else if (yDist >= 0 && xDist <= 0) {
         return "NW";
         } else if (yDist >= 0 && xDist >= 0) {
         return "NE";
         } else {
         return "NULL";
         }
         */

        return FastMath.atan2(xDist, yDist);
    }

    public static Artifact getObstacle(int n) {
        return obstaclePositions[n];
    }

    public static int getNumObstacles() {
        return obstacleCount;
    }

    public static void printWorld() {
        System.out.println("col  " + col);
        System.out.println("row  " + row);
        int j = (row - 1);
        for (; j >= 0; j--) {
            for (int i = 0; i < col; i++) {
                //System.out.println("i is "+i);
                if (myWorld.getWorldObject(i, j) instanceof land) {
                    System.out.println("L");
                } else if (myWorld.getWorldObject(i, j) instanceof obstacle) {
                    System.out.println("O");
                } else if (myWorld.getWorldObject(i, j) instanceof water) {
                    System.out.println("W");
                } else if (myWorld.getWorldObject(i, j) instanceof land) {
                    System.out.println("L");
                } else if (myWorld.getWorldObject(i, j) instanceof land) {
                    System.out.println("P");
                }
            }
        }

    }

    public static void makeWorldThink(int step, String s) {
        int currWorldPoolSize = myWorld.getWorldPoolSize();

        if (currWorldPoolSize >= 1) {
            // System.out.println("--ants thinking--");
            for (Iterator<Ant> iterator = onMapPool.iterator(); iterator.hasNext();) {//all ants in the current world pool think
                //  System.out.println("Ant "+myWorld.getWorldPoolAnt(j).getAntID()+" thinking");
                Ant ant = iterator.next();
                ant.think();
                NeuralAnts.appendAntData(step, ant, s);
                if (ant.getDOA() == false) {
                    deadAntPool.add(ant);
                    iterator.remove();
                }
                //after ant has laid its pheremone scents are dispersed --current scents on map must be recalcuated->first reset them to zero then calculate new;
                currWorldPoolSize = onMapPool.size();
                //  System.out.println("reseting world smells");
            }
        }
    }

    public static void generateFood(int n) {
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < plantCount; i++) {
                ((plant) myWorld.getWorldObject(plantPositions[i].getX(), plantPositions[i].getY())).GenerateOneFood();
            }
        }
    }

    public static void sortAnts() {

        ArrayList<Ant> sortedScores = new ArrayList<Ant>();
        boolean anyCollected = false;
        sortedScores.addAll(onMapPool);
        sortedScores.addAll(deadAntPool);
        Collections.sort(sortedScores, new AntCollectionComparator());

        for (int i = 0; i < sortedScores.size(); i++) {
            if (sortedScores.get(i).getFoodCollectedScore() != 0) {
                System.out.println(sortedScores.get(i).getAntID() + "  score: " + sortedScores.get(i).getFoodCollectedScore());
                anyCollected = true;
            }
        }

        if (anyCollected == false) {
            System.out.println("Ants didnt collect any food");
        }

    }

    public static void testingEnvironment() {
        //provides area to test actions of ants
        
        //col = 5;
        //row =
    }
    

}

class AntCollectionComparator implements Comparator<Ant> {

    @Override
    public int compare(Ant x, Ant y) {
        return (x.getFoodCollectedScore() - y.getFoodCollectedScore());
    }

}
