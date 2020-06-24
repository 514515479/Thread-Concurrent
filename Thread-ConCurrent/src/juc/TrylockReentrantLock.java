package juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tobi
 * @Date: 2020/6/24 16:11
 *
 * ReentrantLock
 * 锁超时tryLock（可以被打断）
 **/
public class TrylockReentrantLock {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                    System.out.println("t1获取锁失败，返回...");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("t1获取到锁...");
            } finally {
                lock.unlock();
            }
        });

        System.out.println("main获取到锁...");
        lock.lock();
        t1.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("main释放了锁...");
        }
    }
}
