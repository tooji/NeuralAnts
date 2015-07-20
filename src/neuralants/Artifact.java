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

    public void setPosition(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public void printPosition() {
        System.out.println(xPos + " , " + yPos);
    }

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
    
    private int [] foodPheremoneScent;

}

class plant extends Artifact {

}

class obstacle extends Artifact {

}

class home extends Artifact {

    private int homeNumber;
    private static int amountOfHomes;
    private static int amountOfAnts;

    public void setHomeNumber(int h) {
        homeNumber = h;
    }

    public static void setAmountOfHomes(int i) {
        amountOfHomes = i;
    }

    public static void setNumAnts(int a) {
        amountOfAnts = a;
    }

}
