package educative.course.multithreading.example;

/**
 * Thread Synchronization Example
 * Demonstrates race conditions and synchronized blocks/methods
 */
public class ThreadSynchronization {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Synchronization Examples ===");

        // Example 1: Race condition without synchronization
        System.out.println("\n1. Without Synchronization (Race Condition):");
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        runCounterTest(unsafeCounter);

        Thread.sleep(1000);

        // Example 2: With synchronization
        System.out.println("\n2. With Synchronization (Thread Safe):");
        SafeCounter safeCounter = new SafeCounter();
        runCounterTest(safeCounter);
    }

    private static void runCounterTest(Counter counter) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final count: " + counter.getCount() + " (Expected: 2000)");
    }
}

interface Counter {
    void increment();
    int getCount();
}

// Unsafe implementation (race condition)
class UnsafeCounter implements Counter {
    private int count = 0;

    @Override
    public void increment() {
        count++; // Not atomic operation
    }

    @Override
    public int getCount() {
        return count;
    }
}

// Thread-safe implementation using synchronized
class SafeCounter implements Counter {
    private int count = 0;

    @Override
    public synchronized void increment() {
        count++; // Synchronized method
    }

    @Override
    public synchronized int getCount() {
        return count;
    }
}
