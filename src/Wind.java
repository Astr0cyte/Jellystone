public class Wind {

    private double speed;
    private String direction;

    public Wind(double speed, String direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public String getDirection() {
        return direction;
    }

    public void applyWind(Forest forest) {

        Cell[][] grid = forest.getGrid();

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                double exposure = speed / 100.0;

                cell.setWindExposure(exposure);
            }
        }
    }
}
