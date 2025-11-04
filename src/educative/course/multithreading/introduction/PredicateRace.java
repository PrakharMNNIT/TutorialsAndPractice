package educative.course.multithreading.introduction;

public class PredicateRace {

    private int anInt;

    public PredicateRace(int anInt) {

        this.anInt = anInt = 0;
    }

    public PredicateRace() {

    }

    public int getAnInt() {

        return anInt;
    }

    public void incrementInt() {

        anInt = anInt + 1;
    }

    public static void main(String[] args) {

        PredicateRace predicateRace = new PredicateRace();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                synchronized(predicateRace) {  // ✅ ADD THIS!
                    predicateRace.incrementInt();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {  // ✅ Move loop OUTSIDE
                synchronized(predicateRace) {   // ✅ Sync EACH iteration
                    int value = predicateRace.getAnInt();
                    if (value % 5 == 0 && value < 10000) {
                        System.out.println(value);
                    }
                }
            }
        });


        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(predicateRace.getAnInt());
    }
}
