package juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author: tobi
 * @Date: 2020/6/30 16:29
 *
 * CyclicBarrier 循环栅栏
 *
 * 用来进行线程协作，等待线程满足某个计数。
 * 构造时设置“计数个数”，每个线程执行到某个需要“同步”的时刻调用await()方法进行等待，当等待的线程数满足“计数个数”时，继续执行。
 * 调用await会重置计数
 *
 * CyclicBarrier与CountDownLatch的主要区别：
 * CyclicBarrier是可以重用的，CyclicBarrier可以被比喻为“人满发车”
 **/
public class TestCyclicBarrier {
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cb = new CyclicBarrier(2, () -> {
            //等两个线程都执行了，才会执行
            System.out.println(Thread.currentThread().getName() + ":over执行...");
        });

        //重置计数是因为调用了await()方法，这里再次进入循环，调用了await，计数再次变为2
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + ":t1执行...");
                    cb.await();  // 2 - 1 = 1
                    System.out.println("t1执行完毕...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();

            Thread.sleep(2000);

            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + ":t2执行...");
                    cb.await(); // 1 - 1 = 0 两个线程都运行
                    System.out.println("t2执行完毕...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
