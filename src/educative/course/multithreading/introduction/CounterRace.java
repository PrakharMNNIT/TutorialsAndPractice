package educative.course.multithreading.introduction;

public class CounterRace {
    int counter = 0;
    public void increment() {
        try {
            Thread.sleep(1000);
            counter++;
        }  catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Thread was interrupted");
        }

    }
    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) throws InterruptedException {
        CounterRace counterRace = new CounterRace();
        Thread t1 = new Thread(counterRace::increment);
        Thread t2 = new Thread(counterRace::increment);
        Thread t3 = new Thread(counterRace::increment);
        Thread t4 = new Thread(counterRace::increment);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println(counterRace.getCounter() + "  " + "expected was 4");

    }
}
