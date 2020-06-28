package threadPool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tobi
 * @Date: 2020/6/28 15:53
 *
 * 自定义线程池
 **/
public class BlockThread {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2, 10, TimeUnit.MILLISECONDS, 10);
        for (int i = 0; i < 5; i++) {
            int j = i;
            threadPool.execute(() -> {
                System.out.println("i = " + j);
            });
        }
    }
}

class ThreadPool {
    //任务队列
    private BlockQueue<Runnable> taskQueue;
    //线程集合
    private HashSet<Worker> works = new HashSet();
    //核心线程数
    int coreSize;
    //获取任务的超时时间
    private long timeout;
    private TimeUnit timeUnit;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapcity) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockQueue<>(queueCapcity);
    }

    class Worker extends Thread{
        private Runnable task;
        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //当task不为空，执行任务
            //当task执行完毕，再从任务队列中获取任务执行
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    System.out.println("正在执行task:" + task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (works) {
                System.out.println("worker被移除:" + this);
                works.remove(this);
            }
        }
    }

    //执行任务
    public void execute(Runnable task) {
        //当任务数没有超过核心线程数时，直接交给Worker对象执行
        //如果超过了核心线程数，加入任务队列暂存
        //保证works集合的线程安全
        synchronized (works) {
            if (works.size() < coreSize) {
                Worker worker = new Worker(task);
                System.out.println("新增work:" + worker + "; task:" + task);
                works.add(worker);
                worker.start();
            } else {
                taskQueue.put(task);
            }
        }
    }
}

class BlockQueue<T> {
    //任务队列
    private Deque<T> queue = new ArrayDeque<>();
    //锁
    private ReentrantLock lock = new ReentrantLock();
    //生产者条件变量
    private Condition fullwaitSet = lock.newCondition();
    //消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();
    //容量
    private int capcity;

    public BlockQueue (int capcity) {
        this.capcity = capcity;
    }

    //带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            //将timeout同意转换成纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    if (nanos <= 0) {
                        return null;
                    }
                    //awaitNanos返回的是等待时间-已经经过的时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t= queue.removeFirst();
            return t;
        } finally {
            lock.unlock();
        }
    }

    //阻塞获取
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t= queue.removeFirst();
            return t;
        } finally {
            lock.unlock();
        }
    }
    //阻塞添加
    public void put(T t) {
        lock.lock();
        try {
            while (capcity == queue.size()) {
                try {
                    fullwaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("task加入任务队列:" + t);
            queue.addFirst(t);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }
}