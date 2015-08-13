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
        Scanner keyboard = new Scanner(System.in);
        
        String path = keyboard.nextLine();
        initializeSim(path);
        
        
        System.out.println("Please enter the path of the world.dat file");
        keyboard = new Scanner(System.in);
        path = keyboard.nextLine();
        
        myWorld.createTerrain(path);
        myWorld.GenerateHomes();
        
        runSim();
    }
    /**
     *runs the simulation with the initialized settings 
     *
     **/
    public static void runSim() {
        
        for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
                        for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                            myWorld.disperseFoodSmells(col, row);
                            myWorld.disperseHomeSmells(col, row);
                        }
        }
        
        
        
        for (int i = 0; i < simLength; i++) {//simulation runs for specified number of steps
           //release specified amount of ants from every home every step 
            for (int ar = 0; ar<antReleaseFactor ; ar++) {
                for (int k = 0; k < myWorld.getAmountOfHomes(); k++) {
                    
                    //need to make a pool of ants on the map and cycle through them and make them think()
                    myWorld.addToWorldPool(((home) myWorld.getHome(k)).releaseAnt());   //removes ant from the home's pool and adds it to the current world pool
                    
                }
            }
            int currWorldPoolSize = myWorld.getWorldPoolSize();
            
            /*if (currWorldPoolSize > 1) {
                
                for (int j = 0; j < currWorldPoolSize; j++) {//all ants in the current world pool think
                    
                    myWorld.getWorldPoolAnt(j).think();
                    //after ant has laid its pheremone scents are dispersed --current scents on map must be recalcuated->first reset them to zero then calculate new;
            
                    for (int col = 0; col <myWorld.getAmountOfColumns();col++){
                        for (int row = 0; row<myWorld.getAmountOfRows();row++){
                            for(int h=0; h<myWorld.getAmountOfHomes();h++){
                                 myWorld.getWorldObject.setFoodPheromoneScent(h, 0);
                                 myWorld.getWorldObject.setHomePheromoneScent(h, 0);
                                 myWorld.getWorldObject.setFoodScent(0);
                            }
                        }
                    }   
                    //new scents are now calculated
                    for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
                        for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                            myWorld.disperseFoodPheromoneScent(col, row);
                            myWorld.disperseHomePheromoneScent(col, row);
                            myWorld.disperseFoodSmells(col, row);
                            
                            
                        }
                    }
                    
                }
                
            }*/
            
            for (int u = 0; u < antGenerationRate; u++) {   //new ants are generated at every home specified by antGenerationRate
                for (int h = 0; h < myWorld.getAmountOfHomes(); u++) {
                    
                    ((home) myWorld.getHome(u)).generateAnt();
                    
                }
                
            }
            
            for (int col = 0; col < myWorld.getAmountOfColumns(); col++) {
                        for (int row = 0; row < myWorld.getAmountOfRows(); row++) {
                            myWorld.ageFoodPheromones(col, row);
                            myWorld.ageHomePheromones(col, row);
                            myWorld.ageFoodPheromones(col, row);
                           
                        }
                    }
            
        }
        
    }
    /**
     * file format should be as follows:
     * 
     * antGenerationRate= #
     * antReleaseFactor= #
     * directionWind= N|S|E|W|NE|SE|NW|SW
     * simLength= #
     * 
     * antGenerationRate is the amount of ants generated in every home every step
     * ant release factor is the amount of ants released from every home at every step
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
                        
                        if (antGenerationRate < 1) {
                            System.out.println("Ant generation rate must be at least 1");
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
                        System.out.println("Ant release factor set to " + antReleaseFactor + "1 ant every ");
                        
                        if (antReleaseFactor < 0) {
                            System.out.println("antReleaseFactor must be atleast 1 or greater");
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
                        System.out.println("randomnessFactor set to "+randomnessFactor);
                        
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
    
    public static int getRandomnessFactor(){
        return randomnessFactor;
    }
    
}
