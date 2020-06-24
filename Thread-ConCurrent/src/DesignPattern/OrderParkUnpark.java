package DesignPattern;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author: tobi
 * @Date: 2020/6/24 19:08
 *
 * 同步模式之顺序控制
 * 固定运行顺序，比如先2后1打印（park/unpark）
 *
 **/
public class OrderParkUnpark {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("等待t2执行...");
            //当没有“许可”时，线程暂停运行，有“许可”时，用掉这个“许可”，线程恢复运行
            LockSupport.park();
            System.out.println("t1执行...");
        }, "t1");

        t1.start();

        new Thread(() -> {
            System.out.println("t2执行...");
            //给t1发放“许可”，调用多次unpark(t1)也只发放一个“许可”
            LockSupport.unpark(t1);
        }, "t2").start();
    }
}
