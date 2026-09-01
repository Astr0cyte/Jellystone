public class Cell {

    private int x;
    private int y;

    private Tree tree;

    private double windExposure;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;

        this.windExposure = 0.0;
        this.tree = null;
    }

    public void plantTree(Tree tree) {
        this.tree = tree;
    }

    public Tree getTree() {
        return tree;
    }

    public boolean hasTree() {
        return tree != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getWindExposure() {
        return windExposure;
    }

    public void setWindExposure(double windExposure) {
        this.windExposure = windExposure;
    }
}