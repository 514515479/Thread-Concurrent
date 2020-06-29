package juc;

import java.util.concurrent.Semaphore;

/**
 * @Author: tobi
 * @Date: 2020/6/29 23:47
 *
 * Semaphore信号量（用来限制能同时访问共享资源的线程上限）
 *
 **/
public class TestSemaphore {
    public static void main(String[] args) {
        //创建Semaphore对象 第一个参数是许可数量，被线程用一个就减一，没有许可就得等待，等其他线程用完了释放掉
        Semaphore semaphore = new Semaphore(3);

        //10个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + ":running...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                System.out.println(Thread.currentThread().getName() + ":end...");
            }).start();
        }
    }
}
