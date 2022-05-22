package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersNoDeadlock {

    public static void main(String[] args) {

        int numberOfPhilosophers = 5;
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        Lock[] chopsticks = new Lock[numberOfPhilosophers];


        // init locks
        for (int i = 0; i < numberOfPhilosophers; i++) {
            chopsticks[i] = new ReentrantLock();
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

        executor.shutdown();
    }

    public static class Philosopher implements Runnable {

        final Lock leftChopstick;
        final Lock rightChopstick;

        public Philosopher(Lock leftChopstick, Lock rightChopstick) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // think
                    System.out.format("%s: Thinking\n", Thread.currentThread().getName());
                    Thread.sleep(((int) (Math.random() * 100)));

                    // try to eat
                    synchronized (leftChopstick) {
                        System.out.format("%s: Picked up left chopstick\n", Thread.currentThread().getName());
                        Thread.sleep(((int) (Math.random() * 100)));
                        synchronized (rightChopstick) {
                            System.out.format("%s: Picked up right chopstick\n", Thread.currentThread().getName());
                            Thread.sleep(((int) (Math.random() * 100)));
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


    }

}
