package DesignPattern;

/**
 * @Author: tobi
 * @Date: 2020/6/24 18:52
 *
 * 同步模式之顺序控制
 * 固定运行顺序，比如先2后1打印（wait/notify）
 *
 * wait/notify很麻烦（park/unpark方便）：
 *  * 1.要保证先wait在notify，否则wait永远唤不醒，所以使用了标记
 *  * 2.如果有其他线程错误唤醒了，不满足条件需要重新等待，所以使用了while防止假唤醒
 *  * 3.wait的线程可能不止一个，使用了notifyAll
 **/
public class OrderWaitNotify {

    //用来同步的对象
    private static Object obj = new Object();
    //标记t2是否执行过
    private static boolean t2Runed = false;

    public static void main(String[] args) {
        new Thread(() -> {
           synchronized (obj) {
               //如果t2没有执行过
               while(!t2Runed) { //while防止假唤醒（被其他线程唤醒）
                   try {
                       System.out.println("等待t2执行");
                       obj.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
               System.out.println("t1执行");
           }
        }, "t1").start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (obj) {
                System.out.println("t2执行");
                t2Runed = true;
                obj.notifyAll();
            }
        }, "t2").start();
    }
}
