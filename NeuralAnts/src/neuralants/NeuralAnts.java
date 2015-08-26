/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import static neuralants.myWorld.disperseFoodPheromoneScent;
import static neuralants.myWorld.disperseFoodSmells;
import static neuralants.myWorld.disperseHomePheromoneScent;
import static neuralants.myWorld.initializeObstacletPositions;
import static neuralants.myWorld.initializePlantPositions;

/**
 *
 * @author tooji
 */
public class NeuralAnts {

    private static int antGenerationRate; //1 is fastest, higher is faster
    private static int antReleaseFactor; //after how many steps ants are released from every home, 1 is every step(fastest), 2 is every other step...etc
    private static int randomnessFactor;
    private static int simLength;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("please enter the path of the sim.dat file");
        //Scanner keyboard = new Scanner(System.in);

        //String path = keyboard.nextLine();
        initializeSim("C:\\Users\\tooji\\Desktop\\sim.dat");

        System.out.println("Please enter the path of the world.dat file");
        // keyboard = new Scanner(System.in);
        //path = keyboard.nextLine();

        myWorld.createTerrain("C:\\Users\\tooji\\Desktop\\testWorld1.dat");
        //myWorld.GenerateHomes();

        System.out.println("Please enter the path for the data from this trial");
        // keyboard = new Scanner(System.in);
        //path = keyboard.nextLine();

        runSim("C:\\Users\\tooji\\Documents\\AntData");
    }

    /**
     * runs the simulation with the initialized settings
     *
     *
     */
    public static void runSim(String s) {
        //disperseWorldSmells();
        //generate food
        //intialize plant positions
        initializePlantPositions();
        //initialize obstacle positions
        initializeObstacletPositions();
        myWorld.generateFood(5);//generates 5 food at each plant
        System.out.println("dispersing initial smells");
        for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
            for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                //myWorld.getWorldObject(col,row).getMyType();
                //System.out.println("col  "+col+"row  "+row);
                myWorld.disperseFoodSmells(col, row);
                myWorld.disperseHomeSmells(col, row);
            }
        }

        myWorld.generateAntColonies("N");

        for (int i = 0; i < simLength; i++) {//simulation runs for specified number of steps
            if (i % 20 == 0) {
                for (int p = 0; p < myWorld.getAmountOfHomes(); p++) {
                    worldSnapShot(i, p, s);
                }

            }

            //release specified amount of ants from every home every step 
            //releaseAnts();
            System.out.println("--releasing ants--simStep " + i + "/" + simLength);
            for (int ar = 0; ar < antReleaseFactor; ar++) {
                for (int k = 0; k < myWorld.getAmountOfHomes(); k++) {
                    if (((home) myWorld.getHome(k)).getHomePoolSize() > 0) {
                        //need to make a pool of ants on the map and cycle through them and make them think()
                        myWorld.addToWorldPool(((home) myWorld.getHome(k)).releaseAnt());   //removes ant from the home's pool and adds it to the current world pool
                    }
                }
            }

            //
            myWorld.makeWorldThink(i, s);

            //generateAnts();
            //System.out.println("--generating ants--");
            for (int u = 0; u < antGenerationRate; u++) {   //new ants are generated at every home specified by antGenerationRate
                for (int h = 0; h < myWorld.getAmountOfHomes(); h++) {
                    //System.out.println("h is "+ h);
                    ((home) myWorld.getHome(h)).generateAnt("N");

                }

            }

            //agePheremones
            // System.out.println("--aging pheromones--");
            for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
                for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                    myWorld.ageFoodPheromones(col, row);
                    myWorld.ageHomePheromones(col, row);
                    myWorld.ageFoodPheromones(col, row);

                }
            }

            for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
                for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                    for (int h = 0; h < myWorld.getAmountOfHomes(); h++) {
                        myWorld.getWorldObject(col, row).setFoodPheromoneScent(h, 0);
                        myWorld.getWorldObject(col, row).setHomePheromoneScent(h, 0);
                        myWorld.getWorldObject(col, row).setFoodScent(0);
                    }
                }
            }
            //System.out.println("Calculating new smells");
            //new scents are now calculated disperseWorldSmells()
            for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
                for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                    disperseFoodPheromoneScent(col, row);
                    disperseHomePheromoneScent(col, row);
                    disperseFoodSmells(col, row);

                }
            }

        }

        System.out.println("--simulation is over--");
        myWorld.sortAnts();

    }

    /**
     * file format should be as follows:
     *
     * antGenerationRate= # antReleaseFactor= # directionWind=
     * N|S|E|W|NE|SE|NW|SW simLength= #
     *
     * antGenerationRate is the amount of ants generated in every home every
     * step ant release factor is the amount of ants released from every home at
     * every step
     *
     * @param s is the path of the initialization file
     */
    public static void initializeSim(String s) {
        String antGenerationRateS;
        String antReleaseFactorS;
        String randomnessFactorS;
        String simLengthS;
        String curLine;
        BufferedReader br = null;

        try {
            File f = new File(s);
            br = new BufferedReader(new FileReader(f));
            int curRowNumber = 0;   // keep current count of rows

            while ((curLine = br.readLine()) != null) {
                System.out.println(curLine);

                if (curRowNumber == 0) {
                    if (curLine.matches("antGenerationRate=\\s\\d+")) {    //if the first line is antGenerationRate= #, read the data

                        String[] parts = curLine.split("\\s");
                        antGenerationRateS = parts[1];
                        antGenerationRate = Integer.parseInt(antGenerationRateS);
                        System.out.println("Ant generation rate is: " + antGenerationRate + "");

                        if (antGenerationRate < 0) {
                            System.out.println("Ant generation rate must be at least 0");
                            throw new IOException();
                        }
                    } else {
                        System.out.println("First row of data file must be of format 'antGenerationRate= #'");
                        throw new IOException();
                    }
                } else if (curRowNumber == 1) {     //if the second line is antReleaseFactor= # read the data

                    if (curLine.matches("antReleaseFactor=\\s\\d+")) {

                        String[] parts = curLine.split("\\s");
                        antReleaseFactorS = parts[1];
                        antReleaseFactor = Integer.parseInt(antReleaseFactorS);
                        System.out.println("Ant release factor set to " + antReleaseFactor);

                        if (antReleaseFactor < 0) {
                            System.out.println("antReleaseFactor must be atleast 0 or greater");
                            throw new IOException();
                        }
                    } else {
                        System.out.println("Second row of data file must be of format 'antReleaseFactor #'");
                        throw new IOException();
                    }
                } else if (curRowNumber == 2) {       // if the 3rd row is directionWind= s,e,n,w, se,sw,ne or nw, read the data

                    if (curLine.matches("randomnessFactor=\\s\\d+")) {
                        String[] parts = curLine.split("\\s");
                        randomnessFactorS = parts[1];
                        randomnessFactor = Integer.parseInt(randomnessFactorS);
                        System.out.println("randomnessFactor set to " + randomnessFactor);

                    } else {
                        System.out.println("Third row of data file must be of format 'randomnessFactor= #'");
                        throw new IOException();
                    }

                } else if (curRowNumber == 3) {       // if the 4th row is simLength= #, read the data

                    if (curLine.matches("simLength=\\s\\d+")) {
                        String[] parts = curLine.split("\\s");
                        simLengthS = parts[1];
                        simLength = Integer.parseInt(simLengthS);
                        System.out.println("Number of ants per home is set to: " + simLength);

                        if (simLength < 1) {
                            System.out.println("Simulation length must be atleast 1 step");
                            throw new IOException();
                        }

                    } else {
                        System.out.println("Third row of data file must be of format 'simLength= #'");
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

    }

    public static void WriteWorldToCSV(String path) {

    }

    public static int getRandomnessFactor() {
        return randomnessFactor;
    }

    public static void appendAntData(int currentStep, Ant x, String s) {
        final File file = new File(s + "\\Ants" + x.getAntID() + "data.xls");
        final File parent_dir = file.getParentFile();

        if (null != parent_dir) {
            parent_dir.mkdirs();
        }

        if (x.getAppendingBegan() == false) {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {

                out.println("CurrentStep\tX\tY\tHasFood?\tfoodCollectedScore\tState");
                x.setAppendingBegan(true);
            } catch (IOException e) {
                System.err.println(e);
            }

        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            int temp;
            if (x.hasFood() == true) {
                temp = 1;
            } else {
                temp = 0;
            }

            out.println(currentStep + "\t" + x.getXPos() + "\t" + x.getYPos() + "\t" + temp + "\t" + x.getFoodCollectedScore() + "\t" + x.getState());

        } catch (IOException e) {
            System.err.println(e);
        }

    }

    private static void worldSnapShot(int currentStep, int homeNumber, String s) {
        final File file = new File(s + "\\" + currentStep + "Home" + homeNumber + "Data.xls");
        final File parent_dir = file.getParentFile();

        if (null != parent_dir) {
            parent_dir.mkdirs();
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));) {

            out.println("X\tY\tfoodPheromoneScent\tfoodScent\tHomePheromoneScent\tHomeScent\tFoodPheromones\tHomePheromones\tFood");

            for (int i = 0; i < myWorld.getAmountOfColumns(); i++) {
                for (int j = 0; j < myWorld.getAmountOfRows(); j++) {
                    out.println(i + "\t" + j + "\t" + myWorld.getWorldObject(i, j).getFoodPheromoneScent(homeNumber) + "\t" + myWorld.getWorldObject(i, j).getFoodScent() + "\t" + myWorld.getWorldObject(i, j).getHomePheromoneScent(homeNumber) + "\t" + myWorld.getWorldObject(i, j).getHomeScent(homeNumber) + "\t" + myWorld.getWorldObject(i, j).getAmountOfFoodPheromones(homeNumber) + "\t" + myWorld.getWorldObject(i, j).getAmountOfHomePheromones(homeNumber) + "\t" + myWorld.getWorldObject(i, j).getAmountOfFood() + "\t");
                }
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
