package juc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: tobi
 * @Date: 2020/6/27 17:30
 *
 * 原子类
 *     原子整数
 *     原子引用
 *     原子数组
 *     字段更新器
 *     原子累加器
 **/
public class Atomic {

    private static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) {
        //method1();
        method2();
    }

    //原子整数
    public static void method1() {
        AtomicInteger i= new AtomicInteger(0);
        System.out.println(i.incrementAndGet()); //++i   1
        System.out.println(i.getAndIncrement()); //i++   2
        //让i乘以10                        读取到     要修改的值（i乘以10的结果）
        System.out.println(i.updateAndGet(value -> value * 10));
    }

    //原子引用AtomicReference
    public static void method2() {
        System.out.println("开始执行...");
        String prev = ref.get();
        other();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("A改成C：" + ref.compareAndSet(prev, "C"));
    }
    //AtomicReference的ABA问题，主线程感知不到别的线程修改过共享变量的值
    public static void other() {
        new Thread(() -> {
            System.out.println("A改成B：" + ref.compareAndSet(ref.get(), "B"));
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            System.out.println("B改成A：" + ref.compareAndSet(ref.get(), "A"));
        }).start();
    }
}
