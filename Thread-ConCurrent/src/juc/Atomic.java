package juc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    //带版本号的原子引用
    //AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) {
        //method1();
        //method2();
        //method3();
        method4();
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
        //带版本号的原子引用，可以识别到ABA的改动
        //int stamp = ref.getStamp();
        //System.out.println("A改成C：" + ref.compareAndSet(prev, "C", stamp, stamp + 1));
    }
    //AtomicReference的ABA问题，主线程感知不到别的线程修改过共享变量的值
    public static void other() {
        new Thread(() -> {
            System.out.println("A改成B：" + ref.compareAndSet(ref.get(), "B"));
            //带版本号的原子引用
            //int stamp = ref.getStamp();
            //System.out.println("A改成B：" + ref.compareAndSet(ref.get(), "B", stamp, stamp + 1));
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            System.out.println("B改成A：" + ref.compareAndSet(ref.get(), "A"));
            //带版本号的原子引用
            //int stamp = ref.getStamp();
            //System.out.println("A改成B：" + ref.compareAndSet(ref.get(), "B", stamp, stamp + 1));
        }).start();
    }

    //原子数组（保护数组里面的元素）
    public static void method3() {
        //线程不安全
        demo(
                ()->new int[10],
                (array)->array.length,
                (array, index) -> array[index]++,
                array-> System.out.println(Arrays.toString(array))
        );
        //线程安全
        demo(
                ()-> new AtomicIntegerArray(10),
                (array) -> array.length(),
                (array, index) -> array.getAndIncrement(index),
                array -> System.out.println(array)
        );
    }
    /**
     参数1，提供数组、可以是线程不安全数组或线程安全数组
     参数2，获取数组长度的方法
     参数3，自增方法，回传 array, index
     参数4，打印数组的方法
     */
    // supplier 提供者 无中生有 ()->结果
    // function 函数 一个参数一个结果 (参数)->结果 , BiFunction (参数1,参数2)->结果
    // consumer 消费者 一个参数没结果 (参数)->void, BiConsumer (参数1,参数2)->
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer ) {
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
            //每个线程对数组作 10000 次操作
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j%length);
                }
            }));
        }
        ts.forEach(Thread::start); // 启动所有线程
        ts.forEach(t -> {
            try {
                //等所有线程结束
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        printConsumer.accept(array);
    }

    //字段更新器（可以针对对象的某个域（Field）进行原子操作，只能配合 volatile 修饰的字段使用，否则会出现异常）
    public  static void method4() {
        Student stu = new Student();
        AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");
        //参数1：要修改的对象，参数2：字段的原始属性值，参数3：字段要修改的值
        System.out.println(updater.compareAndSet(stu, null, "张三"));
    }

}

class Student {
    //必须用volatile修饰
    volatile String name;

    @Override
    public String toString() {
        return "Student{" + "name'" + name + '\'' + '}';
    }
}
