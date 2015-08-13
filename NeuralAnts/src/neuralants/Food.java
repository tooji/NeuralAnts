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
    private int myLifeSpan =100;
    
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
        
        int SmellFactor;
        if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) >= 0) {
            if (myWorld.getWorldObject(i, j) instanceof water || myWorld.getWorldObject(i, j) instanceof land) {
                for (int n = 0; n < myWorld.getNumObstacles(); n++) {
                    if (!(myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) != myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getObstacle(n)))) {
                        SmellFactor = 1 / (int) Math.sqrt(myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())));
                        myWorld.getWorldObject(i, j).addFoodScent( SmellFactor * this.getCurrentLifeSpan());
                    } else {
                    }
                }

            }
        } else if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == 0) {
            SmellFactor = 1;
            myWorld.getWorldObject(i, j).addFoodScent(SmellFactor * this.getCurrentLifeSpan());

        } else {
            System.out.println("unable to disperse scent, distance makes no sense");
        }
        
      //  this.ageFood();
        
    }
    public int getCurrentLifeSpan(){
        return myLifeSpan;
    }
    
   
    
    
}
