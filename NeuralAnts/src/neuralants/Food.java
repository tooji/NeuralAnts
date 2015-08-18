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
public class Food {
    
    
    private int myX;
    private int myY;
    private double myLifeSpan;
   
    

    public Food() {
       if (this.myLifeSpan > 0){
        this.myLifeSpan--;
        }else {
            
        }
        
    }
    
    public static double sigmoid(double age){
     return age*(1/(1+Math.exp(5-(age/10))));
    }
    
    public void setPos(int x, int y){
        myX = x;
        myY=y;
        
    }
    public int getX(){
        return myX;
        
    }
    public int getY(){
        return myY;
        
    }
    public void ageFood(){
        myLifeSpan--;
        
    }
    public void disperseScent(int i, int j){
        double SmellFactor;
        if (this.myLifeSpan == 0){
            SmellFactor =0;
        }else SmellFactor = sigmoid(this.myLifeSpan);
        
        if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) > 0) {
            if (myWorld.getWorldObject(i, j) instanceof water || myWorld.getWorldObject(i, j) instanceof land) {
                for (int n = 0; n < myWorld.getNumObstacles(); n++) {
                    if (!(myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) != myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getObstacle(n)))) {
                        SmellFactor = 1 / (int) Math.sqrt(myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())));
                        myWorld.getWorldObject(i, j).addFoodScent( SmellFactor);
                    } else {
                    }
                }

            }
        } else if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == 0 && this.myLifeSpan >= 0) {
            
            myWorld.getWorldObject(i, j).addFoodScent(100);

        } else if(myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == 0 && this.myLifeSpan == 0){
            myWorld.getWorldObject(i, j).addFoodScent(0);
        }else {
            System.out.println("Unable to disperse scent , distance makes no sense");
        }
        
      //  this.ageFood();
        
    }
    public double getCurrentLifeSpan(){
        return myLifeSpan;
    }
    
   
    
    
}
