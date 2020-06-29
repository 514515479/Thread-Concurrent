package juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author: tobi
 * @Date: 2020/6/29 16:20
 *
 * aqs自定义不可重入锁
 **/
public class TestAQS {
    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(() -> {
            lock.lock();
            //lock.lock(); //在这里就阻塞住了，不可重入锁
            try {
                System.out.println(Thread.currentThread().getName() + ":locking...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + ":unlocking...");
                lock.unlock();
            }
        }, "t1").start();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + ":locking...");
            } finally {
                System.out.println(Thread.currentThread().getName() + ":unlocking...");
                lock.unlock();
            }
        }, "t2").start();
    }
}
//自定义锁，不可重入
class MyLock implements Lock {

    private MySync sync = new MySync();

    @Override //加锁（不成功会进入等待队列）
    public void lock() {
        sync.acquire(1);
    }

    @Override //加锁，可打断
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override //尝试加锁（一次）
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override //尝试加锁，带超时
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override //解锁
    public void unlock() {
        sync.release(1);
    }

    @Override //创建条件变量
    public Condition newCondition() {
        return sync.newCondition();
    }
}

class MySync extends AbstractQueuedSynchronizer {

    @Override //尝试获取锁
    protected boolean tryAcquire(int acquire) {
        //状态0表示未加锁，1表示已加锁
        //if (acquire == 1) {
        if (compareAndSetState(0, 1)) {
            //加锁后，并把owner设置为当前线程
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        //}
        return false;
    }

    @Override //尝试释放锁
    protected boolean tryRelease(int acquire) {
        if (getState() == 0) {
            throw new IllegalMonitorStateException();
        }
        //不用cas，这个时候没有线程竞争
        setState(0);
        //释放锁后，并把owner设置为null
        setExclusiveOwnerThread(null);
        return true;
    }

    @Override //是否持有独占锁
    protected boolean isHeldExclusively() {
        return getState() == 1;
    }

    public Condition newCondition() {
        return new ConditionObject();
    }
}
