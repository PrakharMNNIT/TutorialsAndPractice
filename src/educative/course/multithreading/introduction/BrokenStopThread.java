package educative.course.multithreading.introduction;

public class BrokenStopThread {
    // ❌ NO volatile! This is the problem!
    // Each thread will cache this value in their CPU cache
    private static boolean stopRequested = false;

    public static void main(String[] args) throws InterruptedException {
        // Create background thread
        Thread backgroundThread = new Thread(() -> {
            int i = 0;

            // This is the loop that checks stopRequested
            while (!stopRequested) {
                i++;
                System.out.println(i);

                // 🎯 THE PROBLEM HAPPENS HERE:
                // 1. First time: Thread reads stopRequested from main memory = false
                // 2. CPU caches it: "Hey, stopRequested = false, let me remember this!"
                // 3. Next iterations: Thread reads from CACHE (super fast!)
                // 4. Thread NEVER checks main memory again!
                // 5. Even when main thread changes it to true, this thread doesn't see it!
            }
            System.out.println("Stopped at: " + i);
        });

        backgroundThread.start();
        Thread.sleep(1000);  // Wait 1 second

        // Main thread changes the value
        stopRequested = true;
        // ⚠️ This writes to main memory (RAM)
        // But background thread is still reading its cached copy!

        System.out.println("Stop requested!");

        // 😱 Background thread might NEVER stop!
        // It's stuck in an infinite loop reading its cached false value!
    }
}
