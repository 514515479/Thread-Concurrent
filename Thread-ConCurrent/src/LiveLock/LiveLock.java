package LiveLock;

/**
 * @Author: tobi
 * @Date: 2020/6/22 20:33
 *
 * 活锁
 *     两个线程互相改变对方结束条件，最后谁也无法结束
 **/
public class LiveLock {

    static volatile int count = 10;

    static final Object lock = new Object();

    public static void main(String[] args) {

        //期望count减到0退出循环
        new Thread(() -> {
            while (count > 0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
                System.out.println("t1, count = " +count);
            }
        }, "t1").start();

        //期望count加到20退出循环
        new Thread(() -> {
            while (count < 20) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                System.out.println("t2, count = " +count);
            }
        }, "t2").start();
    }
}
