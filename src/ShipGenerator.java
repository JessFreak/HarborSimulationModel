import universal.Create;
import universal.FunRand;
import universal.Job;

public class ShipGenerator extends Create {

    private final double minWork;
    private final double maxWork;

    public ShipGenerator(String name, double delay, double minWork, double maxWork) {
        super(name, delay);
        this.minWork = minWork;
        this.maxWork = maxWork;
    }

    @Override
    protected Job createJob() {
        double totalWork = FunRand.Uniform(minWork, maxWork);
        return new Ship(super.getTCurr(), totalWork);
    }
}