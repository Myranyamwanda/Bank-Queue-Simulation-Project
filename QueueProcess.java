import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QueueProcess {

    List<Customer> customerList;
    List<Double> interArrivalTimes;
    List<Double> serviceTimes;
    List<Double> idleTimes;
    boolean isBusy;
    float currentTime = 0;

    int expectedNoOfCustomers;
    double arrivalLower, arrivalUpper;
    double serviceLower, serviceUpper;

    private final Random random = new Random();

    public QueueProcess(int expectedNoOfCustomers,
                         double arrivalLower, double arrivalUpper,
                         double serviceLower, double serviceUpper) {
        this.expectedNoOfCustomers = expectedNoOfCustomers;
        this.arrivalLower = arrivalLower;
        this.arrivalUpper = arrivalUpper;
        this.serviceLower = serviceLower;
        this.serviceUpper = serviceUpper;
        this.customerList = new ArrayList<>();
        this.interArrivalTimes = new ArrayList<>();
        this.serviceTimes = new ArrayList<>();
        this.idleTimes = new ArrayList<>();
    }

    /** Generates the 100 random inter-arrival and service times. */
    public void getRandomVariates() {
        for (int i = 0; i < expectedNoOfCustomers; i++) {
            double iat = arrivalLower + (arrivalUpper - arrivalLower) * random.nextDouble();
            interArrivalTimes.add(iat);
            double st = serviceLower + (serviceUpper - serviceLower) * random.nextDouble();
            serviceTimes.add(st);
        }
    }

    /** Counts, among customers already added, how many have not yet started service as of currentTime. */
    public int getQueueNo() {
        int count = 0;
        this.isBusy = false;
        for (Customer c : customerList) {
            if (c.serviceStart > this.currentTime) {
                count++;
            }
            if (c.serviceStart <= currentTime && c.serviceEnd > currentTime) {
                isBusy = true;
            }
        }
        return count;
    }

    public int getSysNo() {
        return getQueueNo() + 1;
    }


    public void getIdleTimes() {
        if (customerList.isEmpty()) {
            return;
        }
        idleTimes.add(0.0); //the first customer's idle time is 0
        for (int i = 1; i < customerList.size(); i++) {
            double idle = customerList.get(i).serviceStart - customerList.get(i - 1).serviceEnd;
            idleTimes.add(Math.max(idle, 0));
        }
    }

    public void addCustomer() {
        int index = customerList.size();
        float iat = interArrivalTimes.get(index).floatValue();
        float st = serviceTimes.get(index).floatValue();

        float arrivalTime = iat;
        float serviceStart = iat;
        float serviceEnd = serviceStart + st;
        float waitingTime = 0;
        int numberInQueue = 0;
        int numberInSystem = 1; //the first customer is alone in the system
        this.currentTime = arrivalTime;

        if (!customerList.isEmpty()) {
            Customer prev = customerList.get(index - 1);
            arrivalTime = prev.arrivalTime + iat;
            this.currentTime = arrivalTime;

            serviceStart = prev.serviceEnd;
            if (arrivalTime > serviceStart) {
                serviceStart = arrivalTime;
            }
            serviceEnd = serviceStart + st;
            waitingTime = serviceStart - currentTime;

            int count = getQueueNo();
            if (isBusy) {
                count++;
            }
            numberInQueue = count;
            numberInSystem = count + 1;
        }

        customerList.add(new Customer(iat, st, serviceStart, serviceEnd,
                numberInQueue, numberInSystem, waitingTime, arrivalTime));
    }

    /** Runs the whole simulation end-to-end: generate variates, build each customer, then idle times. */
    public void run() {
        getRandomVariates();
        for (int i = 0; i < expectedNoOfCustomers; i++) {
            addCustomer();
        }
        getIdleTimes();
    }
}