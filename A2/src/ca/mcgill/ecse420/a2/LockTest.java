package ca.mcgill.ecse420.a2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LockTest {

    static final int THREAD_COUNT = 8;
    static final int DELAY = 1; //ms
    static final int COUNT = 1000;
    static final int PER_THREAD_COUNT = COUNT / THREAD_COUNT;
    static int count = 0;

    static final FilterLock filterLock = new FilterLock(THREAD_COUNT);
    static final BakeryLock bakeryLock = new BakeryLock(THREAD_COUNT);

    public static void main(String[] args) {
        /* NO LOCK */
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new NoLockThread());
        }
        executor.shutdown();
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {}
        System.out.format("NO LOCK TEST -> Expected: %d, Actual: %d\n", COUNT, count);

        /* FILTER LOCK */
        count = 0;
        ThreadId.reset();
        executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new FilterLockThread());
        }
        executor.shutdown();
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {}
        System.out.format("FILTER LOCK TEST -> Expected: %d, Actual: %d\n", COUNT, count);

        /* BAKERY LOCK */
        count = 0;
        ThreadId.reset();
        executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new BakeryLockThread());
        }
        executor.shutdown();
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {

        }
        System.out.format("BAKERY LOCK TEST -> Expected: %d, Actual: %d\n", COUNT, count);

        System.exit(0);
    }

    public static class NoLockThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < PER_THREAD_COUNT; i++) {
                int tmp = count;

                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                count = tmp + 1;
            }
        }
    }

    public static class FilterLockThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < PER_THREAD_COUNT; i++) {
                filterLock.lock();
                try {
                    int tmp = count;

                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    count = tmp + 1;
                } finally {
                    filterLock.unlock();
                }
            }
        }
    }

    public static class BakeryLockThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < PER_THREAD_COUNT; i++) {
                bakeryLock.lock();
                try {
                    int tmp = count;

                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    count = tmp + 1;
                } finally {
                    bakeryLock.unlock();
                }
            }
        }
    }

}
