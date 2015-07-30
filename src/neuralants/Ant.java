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
    private String antID;
    private boolean hasFood;
    private boolean stateFindFood;
    private boolean stateFindHome;
    private boolean Alive;
    private String direction;
    

    
    public void setAntID(String id){
        antID = id;
        
    }
    /*
     *Sets identifier of the tribe the ant belonds to
     */
    public void setHomeNumber(int h) {
        homeNumber = h;

    }
    /*
     *Set's position of this ant
     *@param x sets x-position of ant
     *@param y sets y-position of ant
     */

    public void setPosition(int x, int y) {
        xPos = x;
        yPos = y;
        
        if(myWorld.getWorld()[x][y] instanceof water){
            
        }
        
    }
    /*
     *sets state of ant to find food mode to true, sets state of find home mode to false
     *if state is already FindFood nothing happens
     */

    public void setFindFood() {
        if (stateFindHome) {
            stateFindHome = false;
            stateFindFood = true;
        } else {
            stateFindFood = true;
        }
    }
    /*
     *sets state of ant to find home mode to true, sets state of find food mode to false
     *if state is already FindHome nothing happens
     */

    public void setFindHome() {
        if (stateFindFood) {
            stateFindFood = false;
            stateFindHome = true;
        } else {
            stateFindFood = true;
        }
    }
    /*
     *sets x-position of ant    
     *@param x is the x-position
     */

    public void setXpos(int x) {
        xPos = x;
    }
    /*
     *sets y-position of ant    
     *@param y is the y-position
     */

    public void setYpos(int y) {
        yPos = y;

    }
    /*
     *Drops Home Pheremone on ants location
     */

    public void dropHomePheremone() {
        ((land)myWorld.getWorldObject(this.getXPos(),this.getYPos())).addHomePheremoneScent(this.getHomeNumber(), 1);
    }
    /*
     *Drops Food Pheremone on ants location
     */

    public void dropFoodPheremone() {
        ((land)myWorld.getWorldObject(this.getXPos(),this.getYPos())).addFoodPheremoneScent(this.getHomeNumber(), 1);

    }
    /*
     *get's x-position of ant
     */

    public int getXPos() {
        return xPos;

    }
    /*
     *get's Y position of ant
     */

    public int getYPos() {
        return yPos;
    }
    /*
     *get's ant's tribe/home identification number
     */

    public int getHomeNumber() {
        return homeNumber;
    }
    
    
    public void setAlive(boolean DOA){
        Alive = DOA;
    }
    
    
    public String getDirection(){
        return direction;
    }
    
    public void setDirection(String d){
        direction = d;
    }
    /*
     *And thinks using its state and current sensory data and decides what to do using a normal distribution
     */
    public void think() {
        
        //get sensory information and state
        
        
        //compute action
        
        //update myWorld.artifact[][]

    }

    /*private static Artifact[] getSurroundingSmells(){
    
    
     }*/
    
    public void pickUpFood(){
        //if (this.xPos())
    }
}
