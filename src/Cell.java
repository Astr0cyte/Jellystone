class Cell {
    private int x;
    private int y;
    private Tree tree;
    private double windExposure;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.tree = null;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public void plantTree(Tree tree) {
        this.tree = tree;
    }

    public boolean hasTree() {
        if(this.tree == null) {
            return false;
        }
        return true;
    }

    public Tree getTree() {
        return this.tree;
    }

        public double getWindExposure() {
        return windExposure;
    }

    public void setWindExposure(double windExposure) {
        this.windExposure = windExposure;
    }
}