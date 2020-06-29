package juc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: tobi
 * @Date: 2020/6/29 20:51
 *
 * 读写锁（用的是同一个Sync同步器，因此等待队列，state也是同一个）
 * 当读操作远远高于写操作时，这时候使用读写锁.
 * 让“读-读”可以并发，提高性能。（“读-写”、“写-写”还是互斥）
 *
 * 注意事项：
 *     1.读锁支持条件变量，写锁不支持条件变量。
 *     2.获取写锁前必须释放读锁。
 **/
public class TestReentrantReadWriteLock {
    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();
        //读读
        new Thread(()-> {
            dataContainer.read();
        }).start();
        new Thread(()-> {
            dataContainer.read();
        }).start();
        //读写
        new Thread(()-> {
            dataContainer.read();
        }).start();
        new Thread(()-> {
            dataContainer.write();
        }).start();
        //写写
        new Thread(()-> {
            dataContainer.write();
        }).start();
        new Thread(()-> {
            dataContainer.write();
        }).start();
    }
}

class DataContainer {
    private int data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() {
        r.lock();
        try {
            System.out.println("获取到读锁...");
            try {
                System.out.println("读取到数据...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return data;
        } finally {
            System.out.println("释放读锁...");
            r.unlock();
        }
    }

    public void write() {
        w.lock();
        try {
            System.out.println("获取到写锁...");
            try {
                System.out.println("写入数据...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            System.out.println("释放写锁...");
            w.unlock();
        }
    }
}