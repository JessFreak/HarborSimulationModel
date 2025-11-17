package universal;

public class Dispose extends Element {
    private double sumTimeIn = 0.0;
    private double sumTimeOut = 0.0;
    private double minTime = Double.MAX_VALUE;
    private double maxTime = 0.0;

    public Dispose(String name) {
        super(name);
    }

    @Override
    public void inAct(Job job) {
        double timeIn = job.getTimeIn();
        double timeOut = job.getTimeOut();
        double timeInSystem = timeOut - timeIn;

        sumTimeIn += timeIn;
        sumTimeOut += timeOut;

        if (timeInSystem < minTime) {
            minTime = timeInSystem;
        }
        if (timeInSystem > maxTime) {
            maxTime = timeInSystem;
        }

        super.addQuantity(1);
    }

    @Override
    public void printResult() {
        super.printResult();

        int N = super.getQuantity();
        if (N > 0) {
            double avgTime = (sumTimeOut - sumTimeIn) / N;

            System.out.printf("\n   Average time in harbor (days): \t%.4f", avgTime);
            System.out.printf("\n   Minimum time in harbor (days): \t%.4f", minTime);
            System.out.printf("\n   Maximum time in harbor (days): \t%.4f", maxTime);
        }
    }
}