package DeadLock;

/**
 * @Author: tobi
 * @Date: 2020/6/22 19:47
 *
 * 死锁现象
 *     一个线程持有A锁，等待B锁；另一个线程持有B锁，等待A锁。互相持有对方需要的锁都不释放，互相等待。
 *
 * 问题排查：
 * 1.jps显示java进程id，查找出当前进程id。
 * 2.jstack id 显示死锁
 *
 * 结果：
 * Found one Java-level deadlock:
 * =============================
 * "死锁B":
 *   waiting to lock monitor 0x000000001c7f0ee8 (object 0x000000076b79e300, a java.lang.Object),
 *   which is held by "死锁A"
 * "死锁A":
 *   waiting to lock monitor 0x000000001c7f22d8 (object 0x000000076b79e310, a java.lang.Object),
 *   which is held by "死锁B"
 *
 * Java stack information for the threads listed above:
 * ===================================================
 * "死锁B":
 *         at DeadLock.DeadLock.lambda$main$1(DeadLock.java:39)
 *         - waiting to lock <0x000000076b79e300> (a java.lang.Object)
 *         - locked <0x000000076b79e310> (a java.lang.Object)
 *         at DeadLock.DeadLock$$Lambda$2/990368553.run(Unknown Source)
 *         at java.lang.Thread.run(Thread.java:748)
 * "死锁A":
 *         at DeadLock.DeadLock.lambda$main$0(DeadLock.java:24)
 *         - waiting to lock <0x000000076b79e310> (a java.lang.Object)
 *         - locked <0x000000076b79e300> (a java.lang.Object)
 *         at DeadLock.DeadLock$$Lambda$1/2003749087.run(Unknown Source)
 *         at java.lang.Thread.run(Thread.java:748)
 *
 * Found 1 deadlock.
 *
 * 或者jconsole
 *
 * jps/jconsole不显示java进程或者
 * 1。通常是没有权限引起的。
 *
 * C:\Users\【用户名】\AppData\Local\Temp\hsperfdata_【用户名】 
 * 在该目录下存放所有本地java进程文件。 
 *
 * 举例我的：
 *
 * C:\Users\51451\AppData\Local\Temp\hsperfdata_51451
 *
 * 试试能不能新建文件之类的，不能，则右键---属性--安全--编辑--给当前账号添加完全控制的权限，重新启动项目即可。
 **/
public class DeadLock {
    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();

        new Thread(() -> {
            synchronized (A) {
                System.out.println("持有A锁...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    System.out.println("持有B锁...");
                    System.out.println("t1操作...");
                }
            }
        }, "死锁A").start();

        new Thread(() -> {
            synchronized (B) {
                System.out.println("持有B锁...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A) {
                    System.out.println("持有A锁...");
                    System.out.println("t2操作...");
                }
            }
        }, "死锁B").start();
    }
}
