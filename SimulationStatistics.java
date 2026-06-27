public class SimulationStatistics {

    public final double avgInterArrivalTime;
    public final double avgServiceTime;
    public final double avgWaitingTime;
    public final double avgTimeInSystem;
    public final double avgNumberInQueue;
    public final double avgNumberInSystem;
    public final int maxQueueLength;
    public final double probabilityOfWaiting;
    public final double serverUtilization;
    public final double totalIdleTime;
    public final double totalSimulationTime;

    public SimulationStatistics(QueueProcess process) {
        var customers = process.customerList;
        int n = customers.size();

        double sumIAT = 0, sumST = 0, sumWT = 0, sumTIS = 0, sumQ = 0, sumSys = 0;
        int waited = 0;
        int maxQ = 0;

        for (Customer c : customers) {
            sumIAT += c.interArrivalTime;
            sumST += c.serviceTime;
            sumWT += c.waitingTime;
            sumTIS += c.getTimeInSystem();
            sumQ += c.numberInQueue;
            sumSys += c.numberInSystem;
            if (c.waitingTime > 0) waited++;
            if (c.numberInQueue > maxQ) maxQ = c.numberInQueue;
        }

        avgInterArrivalTime = sumIAT / n;
        avgServiceTime = sumST / n;
        avgWaitingTime = sumWT / n;
        avgTimeInSystem = sumTIS / n;
        avgNumberInQueue = sumQ / n;
        avgNumberInSystem = sumSys / n;
        maxQueueLength = maxQ;
        probabilityOfWaiting = (double) waited / n;

        totalSimulationTime = customers.get(n - 1).serviceEnd;
        double idleSum = 0;
        for (double idle : process.idleTimes) idleSum += idle;
        totalIdleTime = idleSum;
        serverUtilization = (totalSimulationTime - totalIdleTime) / totalSimulationTime;
    }
}