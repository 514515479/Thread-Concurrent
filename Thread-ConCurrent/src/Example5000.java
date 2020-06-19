/**
 * @Author: tobi
 * @Date: 2020/6/19 21:22
 **/

/**
 * 线程不安全的例子（使用synchronized解决）
 *
 * Java中对静态变量的自增/自减不是原子操作
 * 字节码中 i++/i--是四条指令 （i为静态变量）
 *
 * 1.getstatic i  获取静态变量i的值
 * 2.iconst_1     准备常量1
 * 3.iadd/isub    自增/自减
 * 4.putstatic_i  将修改后的i值存入静态变量i中
 *
 * 这8条指令单线程下顺序执行，多线程下可能会交错执行
 *
 **/
public class Example5000 {

    static int counter = 0;

    static final Object room = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (room) {
                for (int i = 0; i < 5000; i++) {
                    counter++;
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (room) {
                for (int i = 0; i < 5000; i++) {
                    counter--;
                }
            }
        });

        t1.start();
        t2.start();
//        while (true) {
//            Thread.State t1State = t1.getState();
//            Thread.State t2State = t2.getState();
//            System.out.println("t1  " + t1State);
//            System.out.println("t2  " + t2State);
//            if (t1State.equals(Thread.State.TERMINATED) && t2State.equals(Thread.State.TERMINATED)) {
//                break;
//            }
//        }
        t1.join();
        t2.join();

        System.out.println(counter);
    }
}
