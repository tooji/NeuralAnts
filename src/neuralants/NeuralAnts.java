/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

import java.util.Scanner;

/**
 *
 * @author tooji
 */
public class NeuralAnts {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Please enter the path of the world file");
        Scanner keyboard = new Scanner(System.in);

        String path = keyboard.nextLine();

        myWorld.createTerrain(path);
    }
}
