package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersNoStarvation {

    public static void main(String[] args) {

        int numberOfPhilosophers = 5;
        Lock[] chopsticks = new Lock[numberOfPhilosophers];

        // init locks
        for (int i = 0; i < numberOfPhilosophers; i++) {
            // fair lock policy
            chopsticks[i] = new ReentrantLock(true);
        }

        // init philosophers threads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfPhilosophers);

        for (int i = 0; i < numberOfPhilosophers; i++) {
            Lock firstChopstick, secondChopstick;
            // the first philosopher will pick up his right chopstick first
            if (i == 0) {
                firstChopstick = chopsticks[(i + 1) % 5];
                secondChopstick = chopsticks[i];
            } else {
                firstChopstick = chopsticks[i];
                secondChopstick = chopsticks[(i + 1) % 5];
            }

            executor.execute(new Philosopher(firstChopstick, secondChopstick));
        }

        // shutdown threads
        executor.shutdown();
    }

    public static class Philosopher implements Runnable {

        private static final long RUN_TIME = 20; // seconds

        private final Lock leftChopstick;
        private final Lock rightChopstick;

        public Philosopher(Lock leftChopstick, Lock rightChopstick) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        @Override
        public void run() {
            try {
                long startTime = System.currentTimeMillis();
                long eatingTime = 0;
                long waitingTime = 0;

                // run for 60 secs
                while (System.currentTimeMillis() - startTime < RUN_TIME * 1000) {
                    // think
                    System.out.format("%s: Thinking\n", Thread.currentThread().getName());
                    Thread.sleep(((int) (Math.random() * 100)));

                    // eat
                    long startWait = System.currentTimeMillis();
                    // try to pick up left chopstick if available (keep fairness policy)
                    if (leftChopstick.tryLock(0, TimeUnit.SECONDS)) {
                        System.out.format("%s: Picked up left chopstick\n", Thread.currentThread().getName());
                        Thread.sleep(((int) (Math.random() * 100)));

                        // try to pick up right chopstick if available (keep fairness policy)
                        if (rightChopstick.tryLock(0, TimeUnit.SECONDS)) {
                            System.out.format("%s: Picked up right chopstick\n", Thread.currentThread().getName());
                            waitingTime += System.currentTimeMillis() - startWait;

                            long startEating = System.currentTimeMillis();
                            Thread.sleep(((int) (Math.random() * 100)));
                            eatingTime += System.currentTimeMillis() - startEating;

                            rightChopstick.unlock();
                        }

                        leftChopstick.unlock();
                    }
                }

                System.out.format("%s: Waited = %d, Ate = %d\n", Thread.currentThread().getName(), waitingTime, eatingTime);
            } catch (Exception e) {
                leftChopstick.unlock();
                rightChopstick.unlock();
            }
        }


    }

}
