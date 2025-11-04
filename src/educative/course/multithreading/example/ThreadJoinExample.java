package educative.course.multithreading.example;

/**
 * Thread Join Example
 * Demonstrates thread.join() for coordination between threads
 */
public class ThreadJoinExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Join Examples ===");

        System.out.println("Main thread started");

        // Create worker threads
        Thread worker1 = new Thread(new Worker("Worker-1", 2000));
        Thread worker2 = new Thread(new Worker("Worker-2", 3000));
        Thread worker3 = new Thread(new Worker("Worker-3", 1000));

        // Start all threads
        worker1.start();
        worker2.start();
        worker3.start();

        System.out.println("All workers started, main thread waiting...");

        // Wait for all threads to complete
        worker1.join(); // Main thread waits for worker1 to finish
        System.out.println("Worker-1 completed");

        worker2.join(); // Main thread waits for worker2 to finish
        System.out.println("Worker-2 completed");

        worker3.join(); // Main thread waits for worker3 to finish
        System.out.println("Worker-3 completed");

        System.out.println("All workers completed, main thread finishing");

        // Example with timeout
        demonstrateJoinWithTimeout();
    }

    private static void demonstrateJoinWithTimeout() throws InterruptedException {
        System.out.println("\n=== Join with Timeout Example ===");

        Thread longRunningTask = new Thread(new Worker("Long-Task", 5000));
        longRunningTask.start();

        // Wait for maximum 2 seconds
        longRunningTask.join(2000); // This returns void, not boolean

        // Check if thread is still alive after timeout
        if (!longRunningTask.isAlive()) {
            System.out.println("Long task completed within timeout");
        } else {
            System.out.println("Long task did not complete within timeout");
            longRunningTask.interrupt(); // Interrupt the long-running task
        }
    }
}

class Worker implements Runnable {
    private String name;
    private long sleepTime;

    public Worker(String name, long sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " started working...");
            Thread.sleep(sleepTime);
            System.out.println(name + " finished work");
        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
