/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralants;

public class FoodPheromone implements Pheromone {

    private int myX;
    private int myY;
    private double myLifeSpan;
    private int mySmellPower;
    private int myHomeNumber;
    
    public FoodPheromone(){
        myLifeSpan= 100;
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
                    if ((myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == myWorld.getDirectionOfArtifact(myWorld.getWorldObject(i, j), myWorld.getObstacle(n)))) {
                        if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) > myWorld.getDistance(myWorld.getObstacle(n), myWorld.getWorldObject(this.getX(), this.getY()))) {
                            addScent = false;
                        }
                    } 
                }
                if (addScent == true) {
                    SmellFactor = SmellFactor / (int) Math.pow((myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY()))+1) , 2);
                    myWorld.getWorldObject(i, j).addFoodPheromoneScent(this.getHomeNumber(), SmellFactor);
                    //System.out.println("just added "+ SmellFactor+"to pos "+i+" , "+j+"homeNumber"+this.getHomeNumber()+" smell is now " +myWorld.getWorldObject(i,j).getFoodPheromoneScent(this.getHomeNumber()));
                    addScent = false;
                }

            }
        } else if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == 0 && this.myLifeSpan >= 0) {

            myWorld.getWorldObject(i, j).addFoodPheromoneScent(this.getHomeNumber(), 100);

        } else if (myWorld.getDistance(myWorld.getWorldObject(i, j), myWorld.getWorldObject(this.getX(), this.getY())) == 0 && this.myLifeSpan == 0) {
            myWorld.getWorldObject(i, j).addFoodPheromoneScent(this.getHomeNumber(), 0);
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

    public void setHomeNumber(int h) {
        myHomeNumber = h;

    }

    public int getHomeNumber() {
        return myHomeNumber;
    }

}
