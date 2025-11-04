package educative.course.multithreading.example;

/**
 * Basic Thread Creation Example
 * Topic: Java Multithreading for Senior Engineering Interviews - Introduction
 */
public class BasicThread {

    public static void main(String[] args) {
        System.out.println("=== Basic Thread Creation Examples ===");

        // Method 1: Extending Thread class
        MyThread thread1 = new MyThread("Thread-1");
        thread1.start();

        // Method 2: Implementing Runnable interface
        Thread thread2 = new Thread(new MyRunnable("Thread-2"));
        thread2.start();

        // Method 3: Using Lambda expression (Java 8+)
        Thread thread3 = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("Lambda Thread executing: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread3.start();

        System.out.println("Main thread continues...");
    }
}

// Method 1: Extending Thread class
class MyThread extends Thread {
    private String threadName;

    public MyThread(String name) {
        this.threadName = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(threadName + " executing: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// Method 2: Implementing Runnable interface
class MyRunnable implements Runnable {
    private String threadName;

    public MyRunnable(String name) {
        this.threadName = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(threadName + " executing: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
