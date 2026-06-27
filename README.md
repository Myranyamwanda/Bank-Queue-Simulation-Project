# Bank Queue Simulation



A single-server queue simulation for a bank, built for a Computer Simulation

and Modelling assignment. Inter-arrival times are drawn from a uniform

distribution on **(1, 8) minutes** and service times from a uniform

distribution on **(1, 6) minutes**. The program generates the requested

sample of customers (100), derives the standard queueing

statistics, and presents everything through two interactive windows.



## Features



- **Input window** – set the number of customers and the bounds of both

&#x20; uniform distributions, then run the simulation. Stays open so you can

&#x20; re-run with different parameters.

- **Output window** – a sortable table of every customer's inter-arrival

&#x20; time, arrival time, service time, service start/end, waiting time, time

&#x20; in system, and queue/system counts, plus a summary statistics panel:

&#x20; - Average inter-arrival time / service time

&#x20; - Average waiting time and average time in system

&#x20; - Average and maximum number in queue

&#x20; - Average number in system

&#x20; - Probability a customer has to wait

&#x20; - Server utilisation and total idle time

&#x20; - "Save as CSV" button to export the table for the report



## Files



- `Customer.java` – per-customer data holder

- `QueueProcess.java` – simulation engine (random variate generation, queue logic)

- `SimulationStatistics.java` – aggregates the summary statistics

- `InputWindow.java` –  input window

- `OutputWindow.java` –  output window

- `BankSimulation.java` – main() entry point



## Build and run



```bash

javac *.java

java BankSimulation

```



## Design notes



The server's idle time is tracked

starting from the first customer: customer 1 is recorded with 0 idle time,

and idle time for every customer after that is the gap between the previous

customer's service ending and this customer's service starting.



**Known limitation:** queue/system counts are recomputed by re-scanning the

full customer list for every new arrival (O(n²) overall). Fine for n = 100,

not ideal at much larger scale.

