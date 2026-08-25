public class Tree {
    private boolean burning;
    private double spreadability; 

    public Tree(double spreadability){
        this.spreadability = spreadability; 
        this.burning = false; 
    }

    public double getSpreadability() {
        return this.spreadability;
    }

    public boolean isBurning() {
        return this.burning;
    }

    public void ignite() {
        this.burning = true;
    }

    public void update(){
        // fire spread logic can go here
    }


}