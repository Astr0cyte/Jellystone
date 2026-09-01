class Cell {
    private int x;
    private int y;
    private Tree cellTree;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.cellTree = null;
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

    public void plantTree(Tree cellTree) {
        this.cellTree = cellTree;
    }

    public boolean hasTree() {
        if(this.cellTree == null) {
            return false;
        }
        return true;
    }

    public Tree getTree() {
        return this.cellTree;
    }
}