package DesignPattern;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tobi
 * @Date: 2020/6/24 21:53
 *
 * 同步模式之交替输出（Lock）
 * 线程1输出a 5次，线程2输出b 5次，线程3输出c 5次
 * 总结果输出abcabcabcabcabc
 *
 * 思路，开始所有线程await，收到signal后才能执行打印。
 * 下一个线程的打印由上一个线程通知，通过传入的current、next变量控制
 *
 * 关键语句：
 *     public void print(Condition current, Condition next, String str) {
 *         current.await();
 *         System.out.print(str);
 *         next.signal();
 *     }
 **/
public class SyncLock extends ReentrantLock {
    //循环次数
    private int loopNumber;

    public SyncLock(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void start(Condition first) {
        this.lock();
        try {
            System.out.println("第一个线程执行...");
            first.signal();
        } finally {
            this.unlock();
        }
    }

    public void print(Condition current, Condition next, String str) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                //没signal前不执行，由上一个执行的线程通知下一个线程signal
                current.await();
                System.out.print(str);
                //通知下一个线程执行
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  finally {
                this.unlock();
            }
        }
    }

    public static void main(String[] args) {
        SyncLock lock = new SyncLock(5);
        Condition aWait = lock.newCondition();
        Condition bWait = lock.newCondition();
        Condition cWait = lock.newCondition();

        new Thread(() -> {
            lock.print(aWait, bWait, "a");
        }).start();
        new Thread(() -> {
            lock.print(bWait, cWait, "b");
        }).start();
        new Thread(() -> {
            lock.print(cWait, aWait, "c");
        }).start();

        lock.start(aWait);
    }
}
