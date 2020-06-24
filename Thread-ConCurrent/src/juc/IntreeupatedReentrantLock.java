package juc;

import com.sun.org.apache.regexp.internal.RE;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tobi
 * @Date: 2020/6/24 15:21
 *
 * 可打断的ReentrantLock
 *     注意：Synchronized和ReentrantLock的lock方法都是不可打断的
 **/
public class IntreeupatedReentrantLock {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            //注意，这里如果写成try-catch-finally形式，就算catch里加了return，也会执行finally里的内容，如果被打断了，不应该执行unlock
            try {
                //如果没有竞争，此方法就会获取到lock锁
                //如果有竞争就进入阻塞队列，可以被其他线程用interrupted方法打断
                System.out.println("尝试获得锁...");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("被打断了，进入catch...");
                return;
            }

            try {
                System.out.println("获取到锁...");
            } finally {
                lock.unlock();
                System.out.println("解锁...");
            }

        }, "t1");

        //让主线程先获得到锁
        lock.lock();
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
    }
}
