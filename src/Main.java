import universal.Dispose;
import universal.Distribution;
import universal.Model;
import universal.Route;

void main() {
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
    double simulationTime = 100_000_000.0;
    IO.println("--- Starting simulation for " + simulationTime + " days ---");

    model.simulate(simulationTime);
}