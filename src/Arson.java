public class Arson extends IgnitionSource {

    private int row;
    private int col;

    public Arson(int row, int col) {

        // Weaker than a lightning strike (severity 0.8): a deliberately
        // lit fire is smaller and only takes hold on sufficiently
        // flammable fuel. 
        super(0.55, 0.5);

        this.row = row;
        this.col = col;
    }

    @Override
    public void ignite(Forest forest) {

        Cell[][] grid = forest.getGrid();

        // 1 - Check for out of bounds
        if (row < 0 || row >= grid.length || 
            col < 0 || col >= grid[0].length) {

            throw new IllegalArgumentException(
                "Coordinates must be within x: 0-"+grid.length+
                " & y: 0-"+grid[0].length);
        }

        Cell cell = grid[row][col];

        // 2 - Check for valid tree: "while there is 
        // no valid tree and we are within bounds"
        while(!cell.hasTree() && (row >= 0 &&  
            row < grid.length && col >= 0 && 
            col < grid[0].length)) {

            cell = grid[row++][col];
        }

        // 3 - Edge case: retried for valid cell 
        // containing a tree, but ran out of bounds while doing so.
        // hasTree() would still be false if this is the case.
        if (!cell.hasTree()) {

            throw new IllegalArgumentException(
                "Ran out of bounds while trying to find a tree to ignite.");
        }
        
        // 4 - Ignition
        if (cell.getTree().getSpreadability() >= spreadability) {
            cell.getTree().ignite(severity);
        }

    }
}
