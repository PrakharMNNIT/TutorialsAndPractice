package educative.course.multithreading.introduction;

public class DeadlockExample {

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void method1() {

        synchronized (lock1) {             // 🔒 T1 gets lock1
            System.out.println("Thread 1: Holding lock 1...");

            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }

            System.out.println("Thread 1: Waiting for lock 2...");
            synchronized (lock2) {         // ⏳ T1 waits for lock2
                System.out.println("Thread 1: Holding lock 1 & 2");
            }
        }
    }

    public void method2() {

        synchronized (lock2) {             // 🔒 T2 gets lock2
            System.out.println("Thread 2: Holding lock 2...");

            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }

            System.out.println("Thread 2: Waiting for lock 1...");
            synchronized (lock1) {         // ⏳ T2 waits for lock1
                System.out.println("Thread 2: Holding lock 1 & 2");
            }
        }
    }

    public static void main(String[] args) {

        DeadlockExample deadlock = new DeadlockExample();

        Thread t1 = new Thread(() -> deadlock.method1(), "Thread-1");
        Thread t2 = new Thread(() -> deadlock.method2(), "Thread-2");

        t1.start();
        t2.start();
        //exit the program when both threads are finished
//        while (t1.isAlive() || t2.isAlive()) {
//        }
//        System.out.println("Both threads are finished");

        //exit focefully
        Thread.currentThread().interrupt();
        System.out.println("Main thread interrupted");
        System.exit(0);
    }
}
