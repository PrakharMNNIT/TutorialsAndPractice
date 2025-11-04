package educative.course.multithreading.introduction;

public class ThreadStateDemo {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Thread State Demonstration ===\n");

        // Thread 1: Will be in TIMED_WAITING state (sleep)
        Thread sleepingThread = new Thread(() -> {
            try {
                System.out.println("[SleepingThread] Starting...");
                Thread.sleep(10000); // Sleep for 10 seconds
                System.out.println("[SleepingThread] Woke up!");
            } catch (InterruptedException e) {
                System.out.println("[SleepingThread] Interrupted!");
            }
        }, "SleepingThread");

        // Thread 2: Will be in WAITING state (wait)
        Thread waitingThread = new Thread(() -> {
            synchronized (lock1) {
                try {
                    System.out.println("[WaitingThread] Going to wait...");
                    lock1.wait(); // Wait indefinitely
                    System.out.println("[WaitingThread] Notified!");
                } catch (InterruptedException e) {
                    System.out.println("[WaitingThread] Interrupted!");
                }
            }
        }, "WaitingThread");

        // Thread 3: Will be in BLOCKED state (trying to acquire lock)
        Thread blockedThread = new Thread(() -> {
            System.out.println("[BlockedThread] Trying to acquire lock...");
            synchronized (lock2) {
                System.out.println("[BlockedThread] Got the lock!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BlockedThread");

        // Thread 4: Will be in RUNNABLE state (busy loop)
        Thread runnableThread = new Thread(() -> {
            System.out.println("[RunnableThread] Running busy loop...");
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 12000) {
                // Busy work - stays RUNNABLE
                Math.sqrt(Math.random());
            }
            System.out.println("[RunnableThread] Finished!");
        }, "RunnableThread");

        // Thread 5: Will be in TIMED_WAITING state (join with timeout)
        Thread timedJoinThread = new Thread(() -> {
            try {
                System.out.println("[TimedJoinThread] Waiting for RunnableThread...");
                runnableThread.join(15000); // Wait up to 15 seconds
                System.out.println("[TimedJoinThread] Done waiting!");
            } catch (InterruptedException e) {
                System.out.println("[TimedJoinThread] Interrupted!");
            }
        }, "TimedJoinThread");

        // Thread 6: Will quickly move to TERMINATED state
        Thread quickThread = new Thread(() -> {
            System.out.println("[QuickThread] Running and finishing quickly!");
        }, "QuickThread");

        // Monitor thread - checks states periodically
        Thread monitor = new Thread(() -> {
            try {
                for (int i = 0; i < 15; i++) {
                    System.out.println("\n>>> TIME: " + i + " seconds <<<");
                    printThreadState(sleepingThread);
                    printThreadState(waitingThread);
                    printThreadState(blockedThread);
                    printThreadState(runnableThread);
                    printThreadState(timedJoinThread);
                    printThreadState(quickThread);
                    System.out.println("----------------------------");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Monitor");

        // Demonstrate NEW state
        System.out.println("Initial state (NEW):");
        printThreadState(sleepingThread);
        printThreadState(waitingThread);
        System.out.println();

        // Hold lock2 to make blockedThread block
        synchronized (lock2) {
            // Start all threads
            sleepingThread.start();
            Thread.sleep(100);

            waitingThread.start();
            Thread.sleep(100);

            blockedThread.start(); // This will block trying to get lock2
            Thread.sleep(100);

            runnableThread.start();
            Thread.sleep(100);

            timedJoinThread.start();
            Thread.sleep(100);

            quickThread.start();
            quickThread.join(); // Wait for it to finish

            monitor.start();

            // Hold the lock for 5 seconds so blockedThread stays BLOCKED
            Thread.sleep(5000);
        } // lock2 released here - blockedThread can now proceed

        System.out.println("\n[Main] Released lock2 - BlockedThread should unblock\n");

        // After 6 seconds, notify the waiting thread
        Thread.sleep(6000);
        synchronized (lock1) {
            System.out.println("\n[Main] Notifying WaitingThread\n");
            lock1.notify();
        }

        // Wait for monitor to finish
        monitor.join();

        System.out.println("\n=== Demo Complete ===");
    }

    private static void printThreadState(Thread thread) {

        System.out.printf("%-20s: %s%n", thread.getName(), thread.getState());
    }
}
//```
//
//    ## Output Example:
//    ```
//    === Thread State Demonstration ===
//
//Initial state (NEW):
//SleepingThread      : NEW
//WaitingThread       : NEW
//
//[SleepingThread] Starting...
//    [WaitingThread] Going to wait...
//    [BlockedThread] Trying to acquire lock...
//    [RunnableThread] Running busy loop...
//    [TimedJoinThread] Waiting for RunnableThread...
//    [QuickThread] Running and finishing quickly!
//
//    >>> TIME: 0 seconds <
//    SleepingThread      : TIMED_WAITING
//WaitingThread       : WAITING
//BlockedThread       : BLOCKED
//RunnableThread      : RUNNABLE
//TimedJoinThread     : TIMED_WAITING
//QuickThread         : TERMINATED
//----------------------------
//
//    >>> TIME: 1 seconds <
//    SleepingThread      : TIMED_WAITING
//WaitingThread       : WAITING
//BlockedThread       : BLOCKED
//RunnableThread      : RUNNABLE
//TimedJoinThread     : TIMED_WAITING
//QuickThread         : TERMINATED
//----------------------------
//    ...
