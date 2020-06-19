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

    static final Object object = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
           for (int i = 0; i < 5000; i++) {
               synchronized (object) {
                   counter++;
               }
           }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (object) {
                    counter--;
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(counter);
    }
}
