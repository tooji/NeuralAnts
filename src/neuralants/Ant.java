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
public class Ant {
   
    private int xPos;
    private int yPos;
    private int homeNumber;
    private boolean hasFood;
    private boolean stateFindFood;
    private boolean stateFindHome;
    
    
    /*
    *Sets identifier of the tribe the ant belonds to
    */
    public void setHomeNumber(int h){
        homeNumber = h;
        
    }
    
    public void setFindFood(){
        if (stateFindHome){
            stateFindHome = false;
            stateFindFood = true;
        }
        
        else stateFindFood = true;
    }
    
        public void setFindHome(){
        if (stateFindFood){
            stateFindFood = false;
            stateFindHome = true;
        }
        
        else stateFindFood = true;
    }
    
    public void setXpos(int x){
        xPos = x;
        
    }
    
    public void setYpos(int y){
        yPos=y;
        
    }
    /*
    *Drops Home Pheremone on ants location
    */
    public void dropHomePheremone(){
        
        
    }
    /*
    *Drops Food Pheremone on ants location
    */
    public void dropFoodPheremone(){
        
    
    }
    
    public int getXPos(){
      return xPos;
        
    }
    
    public int getYPos(){
        return yPos;
    }
    
    public int getHomeNumber(){
        return homeNumber;
    }
    
    public void think(){
      
        
        
    }
    
    
    /*private static Artifact[] getSurroundingSmells(){
    
    
    }*/
    
    
    
}
