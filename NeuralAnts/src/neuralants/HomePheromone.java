/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;


public class HomePheromone implements Pheromone {
    
    private int myHomeNumber;
    private int myX;
    private int myY;
    private double myLifeSpan =100;
    private int mySmellPower;
    
    @Override
    public void setPos(int x, int y) {
        myX =x;
        myY =y;
    }

    @Override
    public int getX() {
        return myX;
    }

    @Override
    public int getY() {
        return myY;
    }

    @Override
    public void agePheromone() {
        myLifeSpan--;
    }

    @Override
    public void disperseScent(int i, int j) {
     double SmellFactor;
        if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) >= 0) {
            if (myWorld.getWorldObject(i, j) instanceof water || myWorld.getWorldObject(i, j) instanceof land) {
                for (int n = 0; n < myWorld.getNumObstacles(); n++) {
                    if (!(myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) != myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getObstacle(n)))) {
                        SmellFactor = 1 / Math.sqrt(myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())));
                        myWorld.getWorldObject(i, j).addHomePheromoneScent(this.getHomeNumber(), SmellFactor * (this.getCurrentLifeSpan()));
                    } else {
                    }
                }

            }
        } else if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == 0) {
            SmellFactor = 1;
            myWorld.getWorldObject(i, j).addHomePheromoneScent(this.getHomeNumber(), SmellFactor * this.getCurrentLifeSpan());

        } else {
            System.out.println("unable to disperse scent, distance makes no sense");
        }
        
        //this.agePheromone();
    }

    @Override
    public double getCurrentLifeSpan() {
        return myLifeSpan;
    }

    @Override
    public int getCurrentSmellPower() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     public void setHomeNumber(int h){
        myHomeNumber =h;
        
    }
    
    public int getHomeNumber(){
        return myHomeNumber;
    }
    
}
