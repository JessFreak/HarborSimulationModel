import universal.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ShipGenerator generator = new ShipGenerator(
                "Ship Generator",
                1.25,
                0.5,
                1.5
        );
        generator.setDistribution(Distribution.EXPONENTIAL);
        Harbor harbor = new Harbor("Harbor Process");
        Dispose exit = new Dispose("Exit Dispose");

        generator.addRoutes(new Route(harbor));
        harbor.addRoutes(new Route(exit));

        Model model = new Model(generator, harbor, exit);
        double simulationTime = 100_000_00.0;
        System.out.println("--- Starting simulation for " + simulationTime + " days ---");

        model.simulate(simulationTime);
        ArrayList<Job> processedJobs = exit.getProcessedJobs();

        double minTime = Double.MAX_VALUE;
        double maxTime = 0.0;
        double sumTime = 0.0;

        for (Job job : processedJobs) {
            Ship ship = (Ship) job;

            double timeInSystem = ship.getTimeOut() - ship.getTimeIn();

            sumTime += timeInSystem;

            if (timeInSystem < minTime) {
                minTime = timeInSystem;
            }
            if (timeInSystem > maxTime) {
                maxTime = timeInSystem;
            }
        }

        double avgTime = sumTime / processedJobs.size();

        System.out.printf("   Average time in harbor (days): \t%.4f\n", avgTime);
        System.out.printf("   Minimum time in harbor (days): \t%.4f\n", minTime);
        System.out.printf("   Maximum time in harbor (days): \t%.4f\n", maxTime);
    }
}