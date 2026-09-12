public class Wind {

    private static final double MAX_EXPOSURE = 100.0;
    private static double DOWN = 0.4;
    private static double SIDE = 0.1;
    private static double UP = -0.2;

    private double speed;
    private String direction;
    private int startX;
    private int startY;
    private int radius;

    public Wind(double speed, String direction, int startX, int startY, int radius) {
        if (!Double.isFinite(speed) || speed < 0.0) {
            throw new WindArgumentException(
                    "Wind speed must be a finite, non-negative value"
            );
        }
        if (direction == null) {
            throw new WindArgumentException(
                    "Wind direction must not be null"
            );
        }
        if (startX < 0 || startY < 0) {
            throw new WindArgumentException(
                    "Starting coordinates must be non-negative"
            );
        }
        if (radius < 0) {
            throw new WindArgumentException(
                    "Wind radius must be non-negative"
            );
        }

        this.speed = speed;
        this.direction = normaliseDirection(direction);
        this.startX = startX;
        this.startY = startY;
        this.radius = radius;
    }

    public double getSpeed() {
        return speed;
    }

    public String getDirection() {
        return direction;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getRadius() {
        return radius;
    }

    public void applyWind(Forest forest) {
        if (forest == null) {
            throw new WindArgumentException("Forest must not be null");
        }

        Cell[][] grid = forest.getGrid();

        if (grid.length == 0 || grid[0].length == 0) {
            throw new WindArgumentException(
                    "Wind cannot be applied to an empty forest"
            );
        }

        if (startY >= grid.length ||
            startX >= grid[startY].length) {

            throw new WindArgumentException(
                    "Starting cell must be inside the forest"
            );
        }

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];
                double exposure = calculateExposure(col, row);

                cell.setWindExposure(exposure);
            }
        }
    }

    // private double calculateExposure(int row, int col, int rows, int columns, double maximumExposure) {

    //     double directionFactor;

    //     if (direction.equals("NORTH")) {
    //         directionFactor = (double) (rows - row) / rows;
    //     } else if (direction.equals("SOUTH")) {
    //         directionFactor = (double) (row + 1) / rows;
    //     } else if (direction.equals("WEST")) {
    //         directionFactor = (double) (columns - col) / columns;
    //     } else {
    //         directionFactor = (double) (col + 1) / columns;
    //     }

    //     return maximumExposure * directionFactor;
    // }



    public double calculateExposure(int x, int y) {
        if (x < 0 || y < 0) {
            return 0.0;
        }

        int relativeX = x - startX;
        int relativeY = y - startY;
        int directionX = getDirectionX();
        int directionY = getDirectionY();

        int forwardDistance =
                relativeX * directionX + relativeY * directionY;
        int sidewaysDistance = Math.abs(
                relativeX * directionY - relativeY * directionX
        );
        double distance = Math.hypot(relativeX, relativeY);

        boolean insideCone =
                forwardDistance >= 0 && sidewaysDistance <= forwardDistance;

        if (!insideCone || distance > radius) {
            return 0.0;
        }

        double maximumExposure = Math.min(
                speed / MAX_EXPOSURE,
                1.0
        );
        double distanceFactor = 1.0 - distance / (radius + 1.0);

        return maximumExposure * distanceFactor;
    }

    public double getSpreadModifier(int sourceX, int sourceY, int targetX, int targetY) {

        int movementX = targetX - sourceX;
        int movementY = targetY - sourceY;
        int alignment =
                movementX * getDirectionX() +
                movementY * getDirectionY();
        double targetExposure = calculateExposure(targetX, targetY);

        if (alignment > 0) {
            return targetExposure * DOWN;
        }
        if (alignment < 0) {
            return targetExposure * UP;
        }
        return targetExposure * SIDE;
    }

    private int getDirectionX() {
        if (direction.equals("EAST")) {
            return 1;
        }
        if (direction.equals("WEST")) {
            return -1;
        }
        return 0;
    }

    private int getDirectionY() {
        if (direction.equals("SOUTH")) {
            return 1;
        }
        if (direction.equals("NORTH")) {
            return -1;
        }
        return 0;
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

        throw new WindArgumentException(
                "Wind direction must be NORTH, SOUTH, EAST, or WEST"
        );
    }
}
