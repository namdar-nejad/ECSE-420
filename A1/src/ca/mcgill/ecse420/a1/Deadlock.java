package ca.mcgill.ecse420.a1;

public class Deadlock {

    public static void main(String[] args) {

        // The 2 Resources
        String r1 = "r1";
        String r2 = "r2";

        // The 2 Threads
        DeadlockThread t1 = new DeadlockThread(r1, r2, "T1");
        DeadlockThread t2 = new DeadlockThread(r2, r1, "T2");

        // Start the Threads
        t1.start();
        t2.start();
    }

    public static class DeadlockThread extends Thread {
        final String r1;
        final String r2;
        final String threadName;

        public DeadlockThread(String r1, String r2, String threadName) {
            this.r1 = r1;
            this.r2 = r2;
            this.threadName = threadName;
        }

        /**
         * Takes r1 first and waits on r2
         */
        public void run() {
            // takes r1
            synchronized (r1) {
                System.out.println(threadName + ": Locked " + r1);
                try {
                    // delay to make sure we reach a deadlock, to make sure r1 is acquired by the thread
                    Thread.sleep(10);
                } catch (Exception ex) {
                }
                // waits on r2
                synchronized (r2) {
                    System.out.println(threadName + ": Locked " + r2);
                }
            }
            System.out.println("Completed " + threadName);
        }
    }
}