import javax.swing.*;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && "benchmark".equalsIgnoreCase(args[0])) {
            SpreadBenchmark.run();
            return;
        }
        if (args.length > 0 && "replay".equalsIgnoreCase(args[0])) {
            FireReplay.run(args);
            return;
        }

        String ignition = args.length == 0 || "mixed".equalsIgnoreCase(args[0])
                ? chooseIgnition()
                : args[0].toLowerCase(Locale.ROOT);
        if (ignition == null) {
            return;
        }

        IgnitionSource source = switch (ignition) {
            case "lightning" -> new Lightning();
            case "arson" -> new Arson(20, 20);
            case "backburning" -> new Backburning(20);
            case "zeus" -> new ZeusJudgment();
            case "serpent" -> new FireSerpent();
            case "phoenix" -> new PhoenixRising();
            default -> null;
        };
        if (source == null) {
            System.out.println("Unknown ignition. Run Main without arguments to see the menu.");
            return;
        }

        Forest forest = new Forest(40, 60, true);
        Wind wind = new Wind(30, "EAST", 30, 20, 15);
        forest.setWind(wind);
        plantGuardians(forest, new Random());

        String name = switch (ignition) {
            case "zeus" -> "Zeus' Heavenly Judgment";
            case "serpent" -> "Fire Serpent";
            case "phoenix" -> "Phoenix Rising";
            default -> source.getClass().getSimpleName();
        };
        System.out.println("Ignition source: " + name);
        System.out.println("Starting trees by species: " + forest.countTreesBySpecies());
        Lightning lightning = source instanceof Lightning strike ? strike : null;

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

            boolean fireWasActive = forest.hasBurningTrees();
            forest.update();
            if (fireWasActive && !forest.hasBurningTrees()) {
                System.out.println("Surviving trees by species: " + forest.countTreesBySpecies());
            }

            panel.repaint();

        });

        timer.start();
    }

    private static String chooseIgnition() {
        String[] choices = {"lightning", "arson", "backburning", "zeus", "serpent", "phoenix"};
        System.out.println("""
                Choose an ignition source
                  1. Lightning
                  2. Arson
                  3. Backburning
                  4. Zeus' Heavenly Judgment
                  5. Fire Serpent
                  6. Phoenix Rising""");
        System.out.print("Enter 1-6 and press Enter: ");

        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String choice = input.nextLine().trim();
            if (choice.length() == 1 && choice.charAt(0) >= '1' && choice.charAt(0) <= '6') {
                return choices[choice.charAt(0) - '1'];
            }
            System.out.print("Please enter a number from 1 to 6: ");
        }
        return null;
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
