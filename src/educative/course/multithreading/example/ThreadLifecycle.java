package educative.course.multithreading.example;

/**
 * Thread Lifecycle Demonstration
 * Shows different states: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
 */
public class ThreadLifecycle {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Lifecycle States ===");

        // Create thread (NEW state)
        Thread thread = new Thread(new LifecycleDemo());
        System.out.println("1. Thread state after creation: " + thread.getState()); // NEW

        // Start thread (RUNNABLE state)
        thread.start();
        System.out.println("2. Thread state after start(): " + thread.getState()); // RUNNABLE

        // Let it run for a bit
        Thread.sleep(100);
        System.out.println("3. Thread state while running: " + thread.getState()); // RUNNABLE or TIMED_WAITING

        // Wait for thread to complete
        thread.join();
        System.out.println("4. Thread state after completion: " + thread.getState()); // TERMINATED

        System.out.println("Main thread finished.");
    }
}

class LifecycleDemo implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Thread started execution");

            // TIMED_WAITING state
            Thread.sleep(2000);

            System.out.println("Thread about to finish");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted");
        }
    }
}
