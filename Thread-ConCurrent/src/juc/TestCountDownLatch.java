package juc;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: tobi
 * @Date: 2020/6/30 14:24
 *
 * CountDownLatch
 * 用来进行线程同步协作，等待所有线程完成倒计时。await()用来等待计数归零，countDown()用来让计数减一
 **/
public class TestCountDownLatch {
    public static void main(String[] args) throws InterruptedException{
        //base();
        //executorService();
        game();
    }

    //CountDownLatch基础用法
    public static void base() throws InterruptedException{
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(() -> {
            System.out.println("t1执行...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t1线程减一，count：" + latch.getCount());
            latch.countDown();
        }).start();

        new Thread(() -> {
            System.out.println("t2执行...");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2线程减一，count：" + latch.getCount());
            latch.countDown();
        }).start();

        new Thread(() -> {
            System.out.println("t3执行...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t3线程减一，count：" + latch.getCount());
            latch.countDown();
        }).start();

        System.out.println("主线程等待中...");
        latch.await();
        System.out.println("主线程执行完毕...");
    }

    //配合线程池使用
    public static void executorService() {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService service = Executors.newFixedThreadPool(4);
        service.submit(() -> {
            System.out.println("t1执行...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t1线程减一，count：" + latch.getCount());
            latch.countDown();
        });

        service.submit(() -> {
            System.out.println("t2执行...");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2线程减一，count：" + latch.getCount());
            latch.countDown();
        });

        service.submit(() -> {
            System.out.println("t3执行...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t3线程减一，count：" + latch.getCount());
            latch.countDown();
        });

        service.submit(() -> {
            try {
                System.out.println("最后一个线程等待中...");
                latch.await();
                System.out.println("最后一个线程执行完毕...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.shutdown();
    }

    //模拟玩家进入游戏
    public static void game() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService service = Executors.newFixedThreadPool(10);
        Random random = new Random();
        //玩家数组
        String[] all = new String[10];
        //10个线程代表10个玩家
        for (int j = 0; j < 10; j++) {
            int k = j;
            service.submit(() -> {
                for (int i = 0; i <=100; i++) {
                    //模拟加载游戏的延迟
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    all[k] = i + "%";
                    //"\r"会让后面的覆盖掉前面的打印
                    System.out.print("\r" + Arrays.toString(all));
                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println("\n玩家全部加载完毕，进入游戏...");
    }
}
