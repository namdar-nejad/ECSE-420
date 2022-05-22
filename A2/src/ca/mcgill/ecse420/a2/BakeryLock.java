package ca.mcgill.ecse420.a2;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class BakeryLock implements Lock {
    private volatile boolean[] flag;
    private volatile long[] label;
    private final int n;

    public BakeryLock(int n) {
        flag = new boolean[n];
        label = new long[n];
        this.n = n;

        for (int i = 0; i < n; i++) {
            flag[i] = false;
            label[i] = 0;
        }
    }

    @Override
    public void lock() {
        int i = ThreadId.get();

        flag[i] = true;
        label[i] = Arrays.stream(label).min().getAsLong() + 1;

        for (int k = 0; k < n; k++) {
            while (flag[k] && (label[k] < label[i] || (label[k] == label[i] && k < i))); // spin
        }
    }

    @Override
    public void unlock() {
        int i = ThreadId.get();
        flag[i] = false;
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
