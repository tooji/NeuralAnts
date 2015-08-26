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
    private double myLifeSpan;
    private int mySmellPower;
    private double decayRatePrevX;
    private final double stepRate = 0.005;

    public HomePheromone() {
        this.myLifeSpan = 100;
        
    }

    @Override
    public double sigmoid(double age) {
        return age * (1 / (1 + Math.exp(5 - (age / 10))));
    }

    @Override
    public void setPos(int x, int y) {
        myX = x;
        myY = y;
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
        if (this.myLifeSpan > 0) {
            this.myLifeSpan--;
        } else {

        }
    }

    @Override
    public void disperseScent(int i, int j) {
        double SmellFactor;

        if (this.myLifeSpan == 0) {
            SmellFactor = 0;
        } else {
            SmellFactor = sigmoid(this.myLifeSpan);
        }
        boolean addScent = false;
        if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) > 0) {
            if (myWorld.getWorldObject(i, j) instanceof water || myWorld.getWorldObject(i, j) instanceof land) {
                addScent = true;
                for (int n = 0; n < myWorld.getNumObstacles(); n++) {
                    if ((myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == myWorld.getDirectionOfArtifact(myWorld.getObstacle(n), myWorld.getWorldObject(this.getX(), this.getY())))) {
                        if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) > myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getObstacle(n))) {
                            addScent = false;
                        }
                    }
                }
                if (addScent = true) {
                    
                    SmellFactor = SmellFactor / Math.pow(myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY()))+1, 2);
                    myWorld.getWorldObject(i, j).addHomePheromoneScent(this.getHomeNumber(), SmellFactor);
                    addScent = false;
                }

            }
        } else if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == 0 && this.myLifeSpan >= 0) {

            myWorld.getWorldObject(i, j).addHomePheromoneScent(this.getHomeNumber(), 100);

        } else if (this.myLifeSpan == 0 && myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == 0) {
            myWorld.getWorldObject(i, j).addHomePheromoneScent(this.getHomeNumber(), 0);
        } else {
            System.out.println("distance makes no sense cannot disperse scent");
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

    public void setHomeNumber(int h) {
        myHomeNumber = h;

    }

    public int getHomeNumber() {
        return myHomeNumber;
    }

}
