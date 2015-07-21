/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.String;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.lang.Exception;
import java.io.*;

/**
 *
 * @author tooji
 */
public class myWorld {

    private static int row;
    private static int col;
    private static int homes;
    private static Artifact[][] world;
    private static Artifact[] homePositions;

    public static void createTerrain(String s) {

        home.setNumAnts(100);
        String curLine;
        String rowS;
        String colS;
        String homeS;

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
                        home.setAmountOfHomes(homes);

                        if (homes == 0) {
                            System.out.println("World must have atleast 1 home");
                            throw new IOException();
                        }

                    } else {
                        System.out.println("Third row of data file must be of format 'homes= #'");
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

        try {

            File fi = new File(s);
            br = new BufferedReader(new FileReader(fi));
            int curRowNumberX = 0;   // keep current count of rows
            int homeCount = 0;      //keep count of home/hives generated

            while ((curLine = br.readLine()) != null) {

                if (curRowNumberX > 2) { //for all the following rows read the data into the terrain

                    int terrainRow = curRowNumberX - 3;     //keep a temp var that stores the current row of the terrain being read
                    //create an array of artifacts-- artifacts can be land, obstacle, water, plant

                    if (curLine.matches("([O|L|W|P])\\w+")) {  //if the row has has the right format
                        System.out.println("Line has right format");
                        char[] lineWorld = curLine.toCharArray();   //chop up line into character array called lineworld

                        if (col != lineWorld.length) {       //check for the right size
                            System.out.println("set column size does not match actual column size, error occured on row: " + curRowNumberX);
                            throw new IOException();
                        }

                        for (int i = 0; i < col; i++) {
                            System.out.println("linworld[i]= " + lineWorld[i]);
                            if (lineWorld[i] == 'L') {
                                world[i][terrainRow] = new land();
                                world[i][terrainRow].setPosition(i, terrainRow);
                                world[i][terrainRow].getMyType();
                                System.out.println("At pos: " + i + " , " + terrainRow);

                            }

                            if (lineWorld[i] == 'W') {
                                world[i][terrainRow] = new water();
                                world[i][terrainRow].setPosition(i, terrainRow);
                                world[i][terrainRow].getMyType();
                                System.out.println("At pos: " + i + " , " + terrainRow);
                            }

                            if (lineWorld[i] == 'O') {
                                world[i][terrainRow] = new obstacle();
                                world[i][terrainRow].setPosition(i, terrainRow);
                                world[i][terrainRow].getMyType();
                                System.out.println("At pos: " + i + " , " + terrainRow);
                            }

                            if (lineWorld[i] == 'P') {
                                world[i][terrainRow] = new plant();
                                world[i][terrainRow].setPosition(i, terrainRow);
                                world[i][terrainRow].getMyType();
                                System.out.println("At pos: " + i + " , " + terrainRow);
                            }

                        }

                    } else {
                        // System.out.println("Match")
                    }

                    // System.out.println(curRowNumber);
                }

                curRowNumberX++;

            }

            //System.out.println("rows= " + row);
            //System.out.println("cols= " + col);
            //world[0][0].getMyType();
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
                // System.out.println(world[randomX][randomY] instanceof land);
                //System.out.println(!(world[randomX][randomY] instanceof home));
                world[randomX][randomY].getMyType();

                if (world[randomX][randomY] instanceof land && !(world[randomX][randomY] instanceof home)) {

                    System.out.println("homeCount is now " + homeCount + "and the amount of set homes is" + homes);
                    home h = new home();
                    world[randomX][randomY] = h;
                    homePositions[homeCount] = h;
                    ((home) world[randomX][randomY]).setHomeNumber(homeCount); //home identification tag is 0 indexed
                    homeCount++;

                }

            }
            System.out.println("done generating homes");

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

    public static Artifact[] getHomePositions() {

        return homePositions;

    }

}
