import universal.Job;

public class Ship extends Job {
    private double remainingWork;
    private double currentServiceRate;
    private double timeServiceRateSet;

    public Ship(double timeIn, double totalWorkRequired) {
        super(timeIn);
        this.remainingWork = totalWorkRequired;
        this.currentServiceRate = 0.0; // Поки не обслуговується
        this.timeServiceRateSet = timeIn;
    }

    public double getRemainingWork() {
        return remainingWork;
    }

    public void setRemainingWork(double remainingWork) {
        this.remainingWork = remainingWork;
    }

    public double getCurrentServiceRate() {
        return currentServiceRate;
    }

    public void setCurrentServiceRate(double currentServiceRate) {
        this.currentServiceRate = currentServiceRate;
    }

    public double getTimeServiceRateSet() {
        return timeServiceRateSet;
    }

    public void setTimeServiceRateSet(double timeServiceRateSet) {
        this.timeServiceRateSet = timeServiceRateSet;
    }
}