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
    public interface Pheromone {
    
    public void setPos(int x, int y);
    public int getX();
    public int getY();
    public void agePheromone();
    public void disperseScent(int i, int j);
    public double getCurrentLifeSpan();
    public void setHomeNumber(int h);
    public int getHomeNumber();
    public int getCurrentSmellPower();
    public double sigmoid(double x);
    
    
    
}

