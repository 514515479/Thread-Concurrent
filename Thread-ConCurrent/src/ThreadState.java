/**
 * @Author: tobi
 * @Date: 2020/6/19 16:06
 **/

/**
 * 线程6种状态（API层面）
 **/
public class ThreadState {
    public static void main(String[] args) throws InterruptedException {

        //NEW
        Thread t1 = new Thread(() -> {
            System.out.println("t1  running...");
        });
        System.out.println("t1-" + t1.getState());

        //RUNNABLE
        Thread t2 = new Thread(() -> {
            while (true) {

            }
        });
        t2.start();
        Thread.sleep(1000);
        System.out.println("t2-" + t2.getState());

        //TERMINATED
        Thread t3 = new Thread(() -> {
            System.out.println("t3  running...");
        });
        t3.start();
        Thread.sleep(1000);
        System.out.println("t3-" + t3.getState());

        //TIMED_WAITING 有时限的等待（sleep有时限）
        Thread t4 = new Thread(() -> {
            synchronized (ThreadState.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t4.start();
        Thread.sleep(1000);
        System.out.println("t4-" + t4.getState());

        //WAITING 无时限的等待（t2一直死循环）
        Thread t5 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t5.start();
        Thread.sleep(1000);
        System.out.println("t5-" + t5.getState());

        //BLOCKED  t4已经上锁了，并且在t6执行时间内没有释放锁，t6获取不到锁
        Thread t6 = new Thread(() -> {
            synchronized (ThreadState.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t6.start();
        Thread.sleep(1000);
        System.out.println("t6-" + t6.getState());
    }
}
