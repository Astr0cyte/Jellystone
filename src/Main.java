import javax.swing.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Forest forest = new Forest(40, 60);

        Wind wind = new Wind(30, "EAST", 30, 20, 15);
        forest.setWind(wind);


        Random random = new Random();
        plantGuardians(forest, random);

        IgnitionSource source;
        Lightning lightning = null;

        int choice = random.nextInt(3);

        if (args.length > 0 && "zeus".equalsIgnoreCase(args[0])) {
            source = new ZeusJudgment();
            System.out.println("Ignition source: Zeus' Heavenly Judgment");

        } else if (args.length > 0 && "phoenix".equalsIgnoreCase(args[0])) {
            source = new PhoenixRising();
            System.out.println("Ignition source: Phoenix Rising");

        } else if (args.length > 0 && "serpent".equalsIgnoreCase(args[0])) {
            source = new FireSerpent();
            System.out.println("Ignition source: Fire Serpent");

        } else if (choice == 0) {
            lightning = new Lightning();
            source = lightning;
            System.out.println("Ignition source: Lightning");

        } else if (choice == 1) {
            source = new Arson(20, 20);
            System.out.println("Ignition source: Arson");

        } else {
            source = new Backburning(20);
            System.out.println("Ignition source: Backburning");
        }

        try {
            source.ignite(forest);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Ignition failed: " + e.getMessage());
        }


        JFrame window = new JFrame("Forest Fire Simulation");

        ForestPanel panel = new ForestPanel(forest, lightning);

        window.add(panel);

        window.setSize(900, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        Timer timer = new Timer(500, event -> {

            forest.update();

            panel.repaint();

        });

        timer.start();
    }

    static void plantGuardians(Forest forest, Random random) {
        Cell[][] grid = forest.getGrid();
        int middleRow = grid.length / 2;
        int middleColumn = grid[0].length / 2;
        int[] rowBounds = {0, middleRow, grid.length};
        int[] columnBounds = {0, middleColumn, grid[0].length};

        for (int verticalHalf = 0; verticalHalf < 2; verticalHalf++) {
            for (int horizontalHalf = 0; horizontalHalf < 2; horizontalHalf++) {
                int row = randomGuardianCoordinate(
                        random, rowBounds[verticalHalf], rowBounds[verticalHalf + 1]);
                int column = randomGuardianCoordinate(
                        random, columnBounds[horizontalHalf], columnBounds[horizontalHalf + 1]);
                grid[row][column].plantTree(new GuardianTree());
            }
        }
    }

    private static int randomGuardianCoordinate(Random random, int start, int end) {
        int margin = Math.min(3, (end - start - 1) / 2);
        return start + margin + random.nextInt(end - start - 2 * margin);
    }
}
