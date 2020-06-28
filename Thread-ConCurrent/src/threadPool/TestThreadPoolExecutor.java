package threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tobi
 * @Date: 2020/6/28 21:14
 *
 * ThreadPoolExecutor
 **/
public class TestThreadPoolExecutor {
    public static void main(String[] args) {
        method1();
        method2();

    }

    /**
     * 固定大小线程池newFixedThreadPool
     * 特点：
     *     1.核心线程数 == 最大线程数（没有救急线程被创建），因此也无需超时时间
     *     2.阻塞队列是无界的，可以放任意数量的任务
     *
     * 适用：
     *     任务量已知，相对耗时的任务
     **/
    public static void method1() {
        //ExecutorService pool = Executors.newFixedThreadPool(2);
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger t = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "线程" + t.getAndIncrement());
            }
        });
        pool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + ":1");
        });
        pool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + ":2");
        });
        pool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + ":3");
        });
        pool.shutdown();
    }

    /**
     * 带缓冲功能的线程池newCachedThreadPool
     * 特点：
     *     1.核心线程数是0，最大线程数是Integer.MAX_VALUE，救急线程的空闲生存时间是 60s.
     *      a.意味着全部都是救急线程（60s后可以回收）
     *      b.救急线程可以无限创建
     *     2.队列采用了SynchronousQueue实现特点是，它没有容量，没有线程来取是放不进去的（一手交钱、一手交货）
     *
     * 适用：
     *     整个线程池表现为线程数会根据任务量不断增长，没有上限，当任务执行完毕，空闲1分钟后释放线程。
     *     适合任务数比较密集，但每个任务执行时间较短的情况
     **/
    public static void method2() {
        //ExecutorService pool = Executors.newCachedThreadPool();
        ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {
            private AtomicInteger t = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "线程" + t.getAndIncrement());
            }
        });
        pool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + ":1");
        });
        pool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + ":2");
        });
        pool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + ":3");
        });
        pool.shutdown();
    }
}
