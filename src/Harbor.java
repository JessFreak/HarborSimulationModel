import universal.Job;
import universal.Process;
import java.util.ArrayList;
import java.util.List;

public class Harbor extends Process {
    private double craneWorkTime = 0.0;

    public Harbor(String name) {
        super(name, 1.0, 2);
    }

    // корабель прибуває на стоянку або в чергу
    @Override
    public void inAct(Job job) {
        Channel freeBerth = getFreeChannel();
        Ship arrivingShip = (Ship) job;

        if (freeBerth != null) {
            freeBerth.setCurrentJob(arrivingShip);
            updateCraneAllocation();
        } else {
            if (queue.size() < getMaxQueueSize()) {
                queue.add(arrivingShip);
            } else {
                failures++;
            }
        }
    }

    // корабель завершує розвантаження
    @Override
    public void outAct() {
        // 1. всі кораблі, що завершили розвантаження ПРЯМО ЗАРАЗ
        ArrayList<Channel> finishingChannels = getChannelsWithMinTNext();

        for (Channel channel : finishingChannels) {
            if (channel.getCurrentJob() == null) continue;

            Job finishedJob = channel.getCurrentJob();
            finishedJob.setTimeOut(super.getTCurr());

            // 2. завершений корабель на вихід (Dispose)
            var nextRoute = getNextRoute(finishedJob);
            if (nextRoute.getElement() != null) {
                nextRoute.getElement().inAct(finishedJob);
            }

            // 3. звільняємо стоянку
            channel.setCurrentJob(null);
            channel.setTNext(Double.MAX_VALUE);
            addQuantity(1);
        }

        // 4. беремо кораблі з черги, якщо з'явилися вільні стоянки
        while (!queue.isEmpty() && getFreeChannel() != null) {
            Channel freeBerth = getFreeChannel();
            Ship waitingShip = (Ship) queue.poll();
            freeBerth.setCurrentJob(waitingShip);
        }

        // 5. перерозподіляємо крани
        updateCraneAllocation();
    }

    private void updateCraneAllocation() {
        List<Ship> activeShips = new ArrayList<>();
        List<Channel> activeChannels = new ArrayList<>();

        for (Channel channel : channels) {
            if (channel.getState() == 1) {
                activeShips.add((Ship) channel.getCurrentJob());
                activeChannels.add(channel);
            }
        }

        double tCurr = super.getTCurr();

        if (activeShips.size() == 1) {
            Ship ship = activeShips.getFirst();
            Channel channel = activeChannels.getFirst();

            updateShipAndChannel(ship, channel, tCurr, 2.0);

        } else if (activeShips.size() == 2) {
            for (int i = 0; i < 2; i++) {
                Ship ship = activeShips.get(i);
                Channel channel = activeChannels.get(i);

                updateShipAndChannel(ship, channel, tCurr, 1.0);
            }
        }
    }

    private void updateShipAndChannel(Ship ship, Channel channel, double tCurr, double newRate) {
        if (ship.getCurrentServiceRate() != newRate) {
            double workDone = (tCurr - ship.getTimeServiceRateSet()) * ship.getCurrentServiceRate();
            ship.setRemainingWork(ship.getRemainingWork() - workDone);

            ship.setCurrentServiceRate(newRate);
            ship.setTimeServiceRateSet(tCurr);
        }

        if (ship.getRemainingWork() > 1e-9) {
            channel.setTNext(tCurr + ship.getRemainingWork() / newRate);
        } else {
            channel.setTNext(tCurr);
        }
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue += queue.size() * delta;

        int activeBerths = 0;
        for (Channel channel : channels) {
            activeBerths += channel.getState();
        }

        workTime += activeBerths * delta;

        if (activeBerths > 0) {
            craneWorkTime += 2.0 * delta;
        }
    }

    @Override
    public void printResult() {
        super.printResult();
        System.out.println("   Mean Crane Workload (per crane) = " + (craneWorkTime / (2.0 * super.getTCurr())));
    }
}