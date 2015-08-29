/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.StringJoiner;
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
    private static int releaseAntsEvery;
    private static int foodGenerationRate;
    private static int generateFoodEvery;
    private static int trials;
    private static boolean randomlyGenerateHomes;
    private static boolean randomlyGenerateWorld;
    private static int generateAntEvery;
    private static String initialAntDir;
    private static int currentSimStep;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("please enter the path of the sim.dat file");
        //Scanner keyboard = new Scanner(System.in);

        //String path = keyboard.nextLine();
        initializeSim("C:\\Users\\Billy\\Desktop\\sim.dat");

        System.out.println("Please enter the path of the world.dat file");
        // keyboard = new Scanner(System.in);
        //path = keyboard.nextLine();

        myWorld.createTerrain("C:\\Users\\Billy\\Desktop\\MapE1T1.dat", randomlyGenerateWorld);
        //myWorld.GenerateHomes();

        System.out.println("Please enter the path for the data from this trial");
        // keyboard = new Scanner(System.in);
        //path = keyboard.nextLine();

        runSim("C:\\Users\\Billy\\Documents\\AntData");
    }

    /**
     * runs the simulation with the initialized settings
     *
     *
     */
    public static void runSim(String s) {
        if (randomlyGenerateHomes == true || randomlyGenerateWorld == true) {
            myWorld.generateHomes();
        } else {
            //intialize plant positions
            initializePlantPositions();
            //initialize obstacle positions
            initializeObstacletPositions();
        }
        myWorld.printWorld();

        myWorld.generateFood(foodGenerationRate);//generates initial food at each plant

        for (int ar = 0; ar < antReleaseFactor; ar++) {
            for (int k = 0; k < myWorld.getAmountOfHomes(); k++) {
                if (((home) myWorld.getHome(k)).getHomePoolSize() > 0) {
                    //need to make a pool of ants on the map and cycle through them and make them think()
                    myWorld.addToWorldPool(((home) myWorld.getHome(k)).releaseAnt());   //removes ant from the home's pool and adds it to the current world pool
                }
            }
        }

        System.out.println("dispersing initial smells");
        for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
            for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                //myWorld.getWorldObject(col,row).getMyType();
                //System.out.println("col  "+col+"row  "+row);
                myWorld.disperseFoodSmells(col, row);
                myWorld.disperseHomeSmells(col, row);
            }
        }

        if (initialAntDir == null) {
            myWorld.generateAntColonies();
        } else {
            myWorld.generateAntColonies(initialAntDir);
        }

        int generateFoodEveryCounter = 0;
        int generateAntEveryCounter = 0;
        int releaseAntEveryCounter = 0;

        for (currentSimStep = 0; currentSimStep < simLength; currentSimStep++) {//simulation runs for specified number of steps
            System.out.println("SimStep: " + currentSimStep + "/" + simLength);

            if (currentSimStep % 20 == 0) {
                for (int p = 0; p < myWorld.getAmountOfHomes(); p++) {
                    worldSnapShot(currentSimStep, p, s);
                }

            }

            //release specified amount of ants from every home every step 
            //releaseAnts();
            if (releaseAntEveryCounter == releaseAntsEvery) {
                releaseAntEveryCounter = 0;
                //  System.out.println("--releasing ants--simStep " + i + "/" + simLength);
                for (int ar = 0; ar < antReleaseFactor; ar++) {
                    for (int k = 0; k < myWorld.getAmountOfHomes(); k++) {
                        if (((home) myWorld.getHome(k)).getHomePoolSize() > 0) {
                            //need to make a pool of ants on the map and cycle through them and make them think()
                            myWorld.addToWorldPool(((home) myWorld.getHome(k)).releaseAnt());   //removes ant from the home's pool and adds it to the current world pool
                        }
                    }
                }
            }
            //
            myWorld.makeWorldThink(currentSimStep, s);

            //generateAnts();
            //System.out.println("--generating ants--");
            if (generateAntEveryCounter == generateAntEvery) {
                generateAntEveryCounter = 0;
                for (int u = 0; u < antGenerationRate; u++) {   //new ants are generated at every home specified by antGenerationRate
                    for (int h = 0; h < myWorld.getAmountOfHomes(); h++) {
                        //System.out.println("h is "+ h);
                        if (initialAntDir == null) {
                            ((home) myWorld.getHome(h)).generateAnt();
                        } else {
                            ((home) myWorld.getHome(h)).generateAnt(initialAntDir);
                        }

                    }

                }
            }

            if (generateFoodEveryCounter == generateFoodEvery) {
                generateFoodEveryCounter = 0;
                myWorld.generateFood(foodGenerationRate);
            }

            //agePheremones
            // System.out.println("--aging pheromones--");
            for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
                for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                    myWorld.ageFoodPheromones(col, row);
                    myWorld.ageHomePheromones(col, row);
                    myWorld.ageFood(col, row);

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

            generateFoodEveryCounter++;
            generateAntEveryCounter++;
            releaseAntEveryCounter++;

        }

        for (int i = 0; i < myWorld.getAmountOfPlants(); i++) {
            AppendStepsToPlant(myWorld.getPlant(i).getStepsToFood(), myWorld.getPlant(i).getX(), myWorld.getPlant(i).getY());
        }

        for (int i = 0; i < myWorld.getAmountOfHomes(); i++) {
            AppendStepsToHome(myWorld.getHome(i).getStepsHome(), myWorld.getHome(i).getX(), myWorld.getHome(i).getY());
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

                if (curLine.matches("antGenerationRate=\\s\\d+")) {    //if the first line is antGenerationRate= #, read the data

                    String[] parts = curLine.split("\\s");
                    antGenerationRateS = parts[1];
                    antGenerationRate = Integer.parseInt(antGenerationRateS);
                    System.out.println("Ant generation rate is: " + antGenerationRate + "");

                    if (antGenerationRate < 0) {
                        System.out.println("Ant generation rate must be at least 0");
                        throw new IOException();
                    }
                } else if (curLine.matches("antReleaseFactor=\\s\\d+")) {

                    String[] parts = curLine.split("\\s");
                    antReleaseFactorS = parts[1];
                    antReleaseFactor = Integer.parseInt(antReleaseFactorS);
                    System.out.println("Ant release factor set to " + antReleaseFactor);

                    if (antReleaseFactor < 0) {
                        System.out.println("antReleaseFactor must be atleast 0 or greater");
                        throw new IOException();
                    }
                } else if (curLine.matches("randomnessFactor=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    randomnessFactorS = parts[1];
                    randomnessFactor = Integer.parseInt(randomnessFactorS);
                    System.out.println("randomnessFactor set to " + randomnessFactor);

                    if (randomnessFactor < 0) {
                        System.out.println("randomeness factor must be atleast 0");
                        throw new IOException();
                    }

                } else if (curLine.matches("simLength=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    simLengthS = parts[1];
                    simLength = Integer.parseInt(simLengthS);
                    System.out.println("sim length set to: " + simLength);

                    if (simLength < 1) {
                        System.out.println("Simulation length must be atleast 1 step");
                        throw new IOException();
                    }

                } else if (curLine.matches("releaseAntsEvery=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    String t = parts[1];
                    releaseAntsEvery = Integer.parseInt(t);
                    System.out.println("ants will release every " + releaseAntsEvery + " step(s)");

                    if (releaseAntsEvery < 1) {
                        System.out.println("release ant every x must be at least 1 ");
                        throw new IOException();
                    }

                } else if (curLine.matches("foodGenerationRate=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    String t = parts[1];
                    foodGenerationRate = Integer.parseInt(t);
                    System.out.println(foodGenerationRate + "fruit will generate");

                    if (foodGenerationRate < 0) {
                        System.out.println("fruitGenerationRate must be greater than or equal to 0");
                        throw new IOException();
                    }

                } else if (curLine.matches("generateFoodEvery=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    String t = parts[1];
                    generateFoodEvery = Integer.parseInt(t);
                    System.out.println("fruit will generate every " + generateFoodEvery + " step(s)");

                    if (generateFoodEvery < 1) {
                        System.out.println("generate fruit every must be greater than or equal to 1 ");
                        throw new IOException();
                    }

                } else if (curLine.matches("trials=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    String t = parts[1];
                    trials = Integer.parseInt(t);
                    System.out.println("the simulation will run " + trials + " trials");

                    if (trials < 1) {
                        System.out.println("Sim must run at least 1 trial ");
                        throw new IOException();
                    }

                } else if (curLine.matches("randomlyGenerateHomes=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    String t = parts[1];
                    int temp = Integer.parseInt(t);

                    if ((temp != 1 && temp != 0)) {
                        System.out.println("Randomly Generate Homes must be either 0 or 1 ");
                        throw new IOException();
                    } else if (temp == 1) {
                        randomlyGenerateHomes = true;
                        System.out.println("randomlyGenerateHomes set to TRUE");
                    } else if (temp == 0) {
                        randomlyGenerateHomes = false;
                        System.out.println("randomlyGenerateHomes set to FALSE");
                    }

                } else if (curLine.matches("generateAntEvery=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    String t = parts[1];
                    generateAntEvery = Integer.parseInt(t);
                    System.out.println("the simulation will generate an ant every" + generateAntEvery + " trials");

                    if (generateAntEvery < 1) {
                        System.out.println("generateAntEvery must be at least 1 ");
                        throw new IOException();
                    }

                } else if (curLine.matches("initialAntDir=\\s(N|S|E|W|NE|SE|NW|SW)")) {
                    String[] parts = curLine.split("\\s");
                    String t = parts[1];
                    initialAntDir = t;
                    System.out.println("the initial direction of every ant will be" + initialAntDir);

                } else if (curLine.matches("randomlyGenerateWorld=\\s\\d+")) {
                    String[] parts = curLine.split("\\s");
                    String t = parts[1];
                    int n = Integer.parseInt(t);
                    System.out.println("the initial direction of every ant will be" + initialAntDir);

                    if ((n != 1 && n != 0)) {
                        System.out.println("Randomly Generate Worlds must be either 0 or 1 ");
                        throw new IOException();
                    } else if (n == 1) {
                        randomlyGenerateWorld = true;
                        System.out.println("randomlyGenerateWorld set to TRUE");
                    } else if (n == 0) {
                        randomlyGenerateWorld = false;
                        System.out.println("randomlyGenerateWorld set to FALSE");
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

                out.println("CurrentStep\tX\tY\tHasFood?\tfoodCollectedScore\tState\tStepsToFood\tStepsToHome");
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

            out.println(currentStep + "\t" + x.getXPos() + "\t" + x.getYPos() + "\t" + temp + "\t" + x.getFoodCollectedScore() + "\t" + x.getState() + "\t" + x.getStepsToPlant() + "\t" + x.getStepsToHome());

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

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));) {

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

    public static void appendAntIO(double[] inputArray, double[] outputArray) {
        final File file = new File("C:\\Users\\Billy\\Documents\\AntData\\AntTESTINGIOdata.txt");
        final File parent_dir = file.getParentFile();

        if (null != parent_dir) {
            parent_dir.mkdirs();
        }
        StringJoiner joiner = new StringJoiner("\t");
        for (int i = 0; i < inputArray.length; i++) {
            joiner.add(String.valueOf(inputArray[i]));
        }
        for (int i = 0; i < outputArray.length; i++) {
            joiner.add(String.valueOf(outputArray[i]));

        }

        if (outputArray[1] >= 2 || outputArray[2] >= 3) {
            System.out.println("something is wrong");

        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            // System.out.print(joiner.toString());
            out.println(joiner.toString());

        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public static void AppendStepsToPlant(ArrayList<Integer> stepsToPlant, int x, int y) {
        final File file = new File("C:\\Users\\Billy\\Documents\\AntData\\AntStepPlantData(" + x + ")(" + y + ").txt");
        final File parent_dir = file.getParentFile();

        if (null != parent_dir) {
            parent_dir.mkdirs();
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            // System.out.print(joiner.toString());
            StringJoiner joiner = new StringJoiner("\t");
            //put headings

            out.println("Steps To Find Food");
            for (int i = 0; i < stepsToPlant.size(); i++) {
                out.println(String.valueOf(stepsToPlant.get(i)));
            }

        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public static void AppendStepsToHome(ArrayList<Integer> stepsToHome, int x, int y) {
        final File file = new File("C:\\Users\\Billy\\Documents\\AntData\\AntStepHomeData(" + x + ")(" + y + ").txt");
        final File parent_dir = file.getParentFile();

        if (null != parent_dir) {
            parent_dir.mkdirs();
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            // System.out.print(joiner.toString());
            StringJoiner joiner = new StringJoiner("\t");
            //put headings
            out.println("Steps To Find Home");

            for (int i = 0; i < stepsToHome.size(); i++) {

                out.println(String.valueOf(stepsToHome.get(i)));
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
