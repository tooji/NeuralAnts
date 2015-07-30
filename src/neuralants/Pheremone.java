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
public interface Pheremone {
    
    public void setPos(int x, int y);
    public int getX();
    public int getY();
    public void agePheremone();
    public void disperseScent();
    public int getCurrentLifeSpan();
    public int getCurrentSmellPower();
    
    
    
}

