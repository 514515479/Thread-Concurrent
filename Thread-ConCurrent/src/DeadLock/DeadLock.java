package DeadLock;

/**
 * @Author: tobi
 * @Date: 2020/6/22 19:47
 *
 * 死锁现象
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
        }).start();

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
        }).start();
    }
}
