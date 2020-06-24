package juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tobi
 * @Date: 2020/6/24 15:07
 *
 * ReentrantLock使用
 * 基本用法：
 *     lock.lock()
 *     try {
 *
 *     } finally {
 *         lock.unlock()
 *     }
 *
 **/
public class TestReentrantLock {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        //测试ReentrantLock的可重入性
        lock.lock();
        try {
            System.out.println("进入main...");
            m1();
        } finally {
            lock.unlock();
        }
    }

    private static void m1() {
        lock.lock();
        try {
            System.out.println("进入m1...");
            m2();
        } finally {
            lock.unlock();
        }
    }

    private static void m2() {
        lock.lock();
        try {
            System.out.println("进入m2...");
        } finally {
            lock.unlock();
        }
    }
}
