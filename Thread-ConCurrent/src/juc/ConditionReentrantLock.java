package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tobi
 * @Date: 2020/6/24 18:20
 *
 * ReentrantLock的条件变量
 *
 * await前需要获取到锁，执行后会释放锁，进入conditionObject等待
 * await线程被唤醒后（或者打断、超时），去重新竞争lock锁
 * 竞争lock锁成功后，从await后面继续执行
 **/
public class ConditionReentrantLock {
    private static final ReentrantLock lock = new ReentrantLock();
    private static Condition waitSmoke = lock.newCondition();
    private static Condition waitBreakfast = lock.newCondition();
    private static boolean hasSmoke = false;
    private static boolean hasBreakfast = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                while(!hasSmoke) {
                    try {
                        System.out.println("等烟中...");
                        waitSmoke.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("等到了烟，干活...");
            } finally {
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                while(!hasBreakfast) {
                    try {
                        System.out.println("等早餐中...");
                        waitBreakfast.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("等到了早餐，干活...");
            } finally {
                lock.unlock();
            }
        });

        t1.start();
        t2.start();

        try {
            Thread.sleep(1000);
            sendSmoke();
            Thread.sleep(1000);
            sendBreakfast();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //送烟
    private static void sendSmoke() {
        lock.lock();
        try {
            System.out.println("烟到了...");
            hasSmoke = true;
            waitSmoke.signal();
            //全部唤醒
            //waitSmoke.signalAll();
        } finally {
            lock.unlock();
        }
    }

    //送早餐
    private static void sendBreakfast() {
        lock.lock();
        try {
            System.out.println("早餐到了...");
            hasBreakfast = true;
            waitBreakfast.signal();
            //全部唤醒
            //waitSmoke.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
