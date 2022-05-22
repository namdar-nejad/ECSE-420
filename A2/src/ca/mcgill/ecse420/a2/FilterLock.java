package ca.mcgill.ecse420.a2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FilterLock implements Lock {
    private volatile int[] level;
    private volatile int[] victim;
    private final int n;

    public FilterLock(int n) {
        this.level = new int[n];
        this.victim = new int[n];
        this.n = n;
    }

    @Override
    public void lock() {
        int i = ThreadId.get();
        for (int l = 1; l < n; l++) {
            this.level[i] = l;
            this.victim[l] = i;

            for (int k = 0; k < n; k++) {
                if (k == i) continue;

                while (level[k] >= l && victim[l] == i); // spin
            }
        }

    }

    @Override
    public void unlock() {
        int i = ThreadId.get();
        this.level[i] = 0;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
