package juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tobi
 * @Date: 2020/6/24 16:40
 *
 * 公平锁
 * Reentrantlock默认是不公平的
 * 开启公平锁：
 *     ReentrantLock lock = new ReentrantLock(true)
 *     公平锁一般没有必要，会降低并发度
 *
 * 注意：这个例子不一定能复现
 **/
public class FairReentrantLock {
    public static void main(String[] args) {
        //默认是false，不公平的
        //ReentrantLock lock = new ReentrantLock();
        //改为公平锁，在最后输出
        ReentrantLock lock = new ReentrantLock(true);
        lock.lock();
        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "-running...");
                } finally {
                    lock.unlock();
                }
            }, "t" + i).start();
        }
        //1秒后去争抢
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //强行插入，有机会在中间输出
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "-running...");
            } finally {
                lock.unlock();
            }
        }, "强行插入").start();
        lock.unlock();
    }
}
