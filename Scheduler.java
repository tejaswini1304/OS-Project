package SchedulingAlgo;
import java.util.*;

// Define the Process class to represent a process with necessary attributes
class Process {
    String name;
    int arrivalTime;
    int burstTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int priority; // Added priority field

    // Constructor with priority parameter
    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    // Default constructor without priority (for non-priority scheduling algorithms)
    public Process(String name, int arrivalTime, int burstTime) {
        this(name, arrivalTime, burstTime, 0); // Default priority is set to 0
    }
}

public class Scheduler {

    // First-Come, First-Served (FCFS) scheduling algorithm
    public static void FCFS(ArrayList<Process> processes) {
        int currentTime = 0;
        for (Process process : processes) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }
            process.completionTime = currentTime + process.burstTime;
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;
            currentTime = process.completionTime;
        }
    }

    // Shortest Job First (SJF) scheduling algorithm
    public static void SJF(ArrayList<Process> processes) {
        int currentTime = 0;
        ArrayList<Process> sortedProcesses = new ArrayList<>(processes);
        Collections.sort(sortedProcesses, Comparator.comparingInt(p -> p.burstTime));

        for (Process process : sortedProcesses) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }
            process.completionTime = currentTime + process.burstTime;
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;
            currentTime = process.completionTime;
        }
    }

    // Shortest Remaining Time First (SRTF) scheduling algorithm
    public static void SRTF(ArrayList<Process> processes) {
        int currentTime = 0;
        PriorityQueue<Process> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.burstTime));

        while (!processes.isEmpty() || !queue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                queue.offer(processes.remove(0));
            }

            if (queue.isEmpty()) {
                currentTime = processes.get(0).arrivalTime;
                continue;
            }

            Process currentProcess = queue.poll();
            int remainingTime = currentProcess.burstTime - 1;
            currentProcess.burstTime = remainingTime;

            currentTime++;
            currentProcess.completionTime = currentTime;

            if (remainingTime > 0) {
                queue.offer(currentProcess);
            } else {
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            }
        }
    }

    // Priority Scheduling algorithm
    public static void priorityScheduling(ArrayList<Process> processes) {
        int currentTime = 0;
        ArrayList<Process> sortedProcesses = new ArrayList<>(processes);
        Collections.sort(sortedProcesses, Comparator.comparingInt(p -> p.priority));

        for (Process process : sortedProcesses) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }
            process.completionTime = currentTime + process.burstTime;
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;
            currentTime = process.completionTime;
        }
    }

    // Round Robin scheduling algorithm
    public static void roundRobin(ArrayList<Process> processes, int quantum) {
        int currentTime = 0;
        Queue<Process> queue = new LinkedList<>(processes);

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            int remainingTime = currentProcess.burstTime;

            if (remainingTime > quantum) {
                currentTime += quantum;
                currentProcess.burstTime -= quantum;
                queue.offer(currentProcess);
            } else {
                currentTime += remainingTime;
                currentProcess.burstTime = 0;
                currentProcess.completionTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 5));
        processes.add(new Process("P2", 1, 3));
        processes.add(new Process("P3", 2, 4));
        processes.add(new Process("P4", 3, 2));
        processes.add(new Process("P5", 4, 6));

        Scanner scanner = new Scanner(System.in);
        int choice;

        // Main program loop to interact with the user and execute scheduling algorithms
        do {
            System.out.println("Select a scheduling algorithm:");
            System.out.println("1. FCFS\n2. SJF\n3. SRTF\n4. Priority Scheduling\n5. Round Robin\n6. Exit");
            System.out.print("Enter your choice : ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    FCFS(new ArrayList<>(processes));
                    break;
                case 2:
                    SJF(new ArrayList<>(processes));
                    break;
                case 3:
                    SRTF(new ArrayList<>(processes));
                    break;
                case 4:
                    priorityScheduling(new ArrayList<>(processes));
                    break;
                case 5:
                    System.out.println("Enter time quantum for Round Robin:");
                    int quantum = scanner.nextInt();
                    roundRobin(new ArrayList<>(processes), quantum);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 6); // Continue loop until user chooses to exit (choice 6)
    }
}
