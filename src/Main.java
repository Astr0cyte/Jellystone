import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Forest forest = new Forest(40, 60);

        Wind wind = new Wind(30, "EAST");
        forest.setWind(wind);

        IgnitionSource source = new Arson(20, 20);
        source.ignite(forest);
        try {
            IgnitionSource lightning = new Lightning();
            lightning.ignite(forest);
        } 
        catch (IllegalStateException e) {
            System.out.println("Lightning strike missed: " + e.getMessage());
        }

        JFrame window = new JFrame("Forest Fire Simulation");

        ForestPanel panel = new ForestPanel(forest);

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
}
