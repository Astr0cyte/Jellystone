public class Wind {

    private static final double MAX_EXPOSURE_SPEED = 100.0;

    private double speed;
    private String direction;

    public Wind(double speed, String direction) {
        if (!Double.isFinite(speed) || speed < 0.0) {
            throw new IllegalArgumentException(
                    "Wind speed must be a finite, non-negative value"
            );
        }
        if (direction == null) {
            throw new IllegalArgumentException(
                    "Wind direction must not be null"
            );
        }

        this.speed = speed;
        this.direction = normaliseDirection(direction);
    }

    public double getSpeed() {
        return speed;
    }

    public String getDirection() {
        return direction;
    }

    public void applyWind(Forest forest) {
        if (forest == null) {
            throw new IllegalArgumentException("Forest must not be null");
        }

        Cell[][] grid = forest.getGrid();
        double maximumExposure = Math.min(
                speed / MAX_EXPOSURE_SPEED,
                1.0
        );

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];
                double exposure = calculateExposure(
                        row,
                        col,
                        grid.length,
                        grid[row].length,
                        maximumExposure
                );

                cell.setWindExposure(exposure);
            }
        }
    }

    private double calculateExposure(
            int row,
            int col,
            int rows,
            int columns,
            double maximumExposure) {

        double directionFactor;

        if (direction.equals("NORTH")) {
            directionFactor = (double) (rows - row) / rows;
        } else if (direction.equals("SOUTH")) {
            directionFactor = (double) (row + 1) / rows;
        } else if (direction.equals("WEST")) {
            directionFactor = (double) (columns - col) / columns;
        } else {
            directionFactor = (double) (col + 1) / columns;
        }

        return maximumExposure * directionFactor;
    }

    private static String normaliseDirection(String direction) {
        String trimmedDirection = direction.trim();

        if (trimmedDirection.equalsIgnoreCase("NORTH")) {
            return "NORTH";
        }
        if (trimmedDirection.equalsIgnoreCase("SOUTH")) {
            return "SOUTH";
        }
        if (trimmedDirection.equalsIgnoreCase("EAST")) {
            return "EAST";
        }
        if (trimmedDirection.equalsIgnoreCase("WEST")) {
            return "WEST";
        }

        throw new IllegalArgumentException(
                "Wind direction must be NORTH, SOUTH, EAST, or WEST"
        );
    }
}
