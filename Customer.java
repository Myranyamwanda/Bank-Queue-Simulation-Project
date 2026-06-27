public class Customer {

    float interArrivalTime; // IAT  - minutes since the previous customer arrived
    float arrivalTime;      // AT   - absolute clock time this customer arrived
    float serviceTime;      // ST   - minutes required to serve this customer
    float serviceStart;     // startS - absolute clock time service begins
    float serviceEnd;       // endS   - absolute clock time service ends
    int numberInQueue;      // customers waiting ahead of this one (excludes any one being served)
    int numberInSystem;     // customers ahead of this one including any one being served
    float waitingTime;      // WT - time this customer spent waiting before being served

    public Customer(float interArrivalTime, float serviceTime, float serviceStart,
                     float serviceEnd, int numberInQueue, int numberInSystem,
                     float waitingTime, float arrivalTime) {
        this.interArrivalTime = interArrivalTime;
        this.serviceTime = serviceTime;
        this.serviceStart = serviceStart;
        this.serviceEnd = serviceEnd;
        this.numberInQueue = numberInQueue;
        this.numberInSystem = numberInSystem;
        this.waitingTime = waitingTime;
        this.arrivalTime = arrivalTime;
    }

    /** Total time this customer spent in the bank: waiting + being served. */
    public float getTimeInSystem() {
        return waitingTime + serviceTime;
    }
}