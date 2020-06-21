/**
 * @Author: tobi
 * @Date: 2020/6/21 14:13
 **/

/**
 * wait方法会让线程进入WAITING状态，释放锁。必须获取到锁的时候才能使用（和synchronized一起使用）
 * notify//notifyAll方法可以唤醒WAITING的线程，但不是马上就分配到cpu时间片
 **/

/**
 * wait/notify的正确写法，避免虚假唤醒（唤醒错误的线程）
 *
 * synchronized(lock) {
 *     while(条件不成立) {
 *         lock.wait();
 *     }
 *     //干活
 * }
 *
 * //另一个线程
 *
 * synchronized(lock) {
 *     lock.notifyAll();
 * }
 *
 **/
public class WaitNotifyThread {

    static final Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            synchronized (obj) {
                System.out.println("t1执行。。。");
                try {
                    obj.wait();  //让线程在obj一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t1其他代码。。。");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (obj) {
                System.out.println("t2执行。。。");
                try {
                    obj.wait();  //让线程在obj一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2其他代码。。。");
            }
        }, "t2").start();

        //主线程2秒后执行
        Thread.sleep(2000);
        System.out.println("唤醒obj上的其他线程");
        synchronized (obj) {
            obj.notify();  //唤醒obj上的其中一个线程
            //obj.notifyAll();  //唤醒obj上的其中所有线程
        }
    }
}
