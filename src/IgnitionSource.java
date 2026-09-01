public abstract class IgnitionSource {

    protected double severity;
    protected double spreadability;

    public IgnitionSource(
            double severity,
            double spreadability) {

        this.severity = severity;
        this.spreadability = spreadability;
    }

    public abstract void ignite(Forest forest);
}