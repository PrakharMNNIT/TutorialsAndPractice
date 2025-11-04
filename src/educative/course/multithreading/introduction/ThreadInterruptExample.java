package educative.course.multithreading.introduction;

public class ThreadInterruptExample {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Thread Interrupt Example ===");
        Thread workerThread = new WorkerThread();
        workerThread.start();
        Thread.sleep(1000);
        System.out.println("Main thread is trying to interrupt worker thread...");
        workerThread.interrupt();
        System.out.println("Main thread is done interrupting worker thread.");
        workerThread.join();
        System.out.println("Main thread is done.");
    }

}

class WorkerThread extends Thread {

    private long serialVersionID = 1L;

    @Override
    public void run() {

        try {
            while (!isInterrupted()) {
                System.out.println("Worker thread is running..." + serialVersionID);
                serialVersionID++;
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
            System.out.println("Worker thread interrupted.");
        }
        if(isInterrupted()){
            System.out.println("Worker thread interrupted so it is done.");
        } else {
            System.out.println("Worker thread is not interrupted so it is done.");
        }
    }
}
