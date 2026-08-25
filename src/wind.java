import java.util.Random;

public class Wind {

    private Cell[][] grid;

    // private char calc;

    private int calc;
    private double windSpeed;
    private String windDirection;


    private Random r = new Random();
    //Random num generator
    //For my own part

    public Wind(){
        this.calc = calc;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    public double windSeverity(){
        return this.windSpeed;
        // calc is used with windSpeed to calculate Severity
    }


    public Cell applyWind(){
        this.row = r.nextInt(30);
        this.column = r.nextInt(30);
        // use returned value from Severity?
        return Cell[row][column];
    }


    public void setDirection(){
        // void, suppose we are changing the state of wind without displaying
    }

    // Accounting for WindDirection, More to your* liking


}
