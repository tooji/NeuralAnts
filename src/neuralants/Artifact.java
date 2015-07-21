/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

/**
 *
 * @author tooji
 */
public class Artifact {

    private int xPos;
    private int yPos;
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

}

class water extends Artifact {

}

class land extends Artifact {

    private int[] foodPheremoneScent;

}

class plant extends Artifact {

}

class obstacle extends Artifact {

}

class home extends Artifact {

    private int homeNumber;
    private static int amountOfHomes;
    private static int amountOfAnts;
    private static Ant[] colony;

    /*
     *Generates Ant Colony with amount of ants specified
     *Tags every ant with its colony number (0-indexed)
     *sets every ant with it's initial position at the home's position
     */
    public void generateAntColony() {
        colony = new Ant[amountOfAnts];

        for (int i = 0; i < colony.length; i++) {
            colony[i].setHomeNumber(homeNumber); //tags ant with tribe identity
            colony[i].setPosition(this.getX(), this.getY());
        }

    }

    /*
     *releases Ant from home 
     */
    public void releaseAnt() {

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

    public static void setNumAnts(int a) {
        amountOfAnts = a;
    }

}
