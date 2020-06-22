package DesignPattern;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: tobi
 * @Date: 2020/6/21 21:58
 *
 * 同步模式之保护性暂停，用在一个线程等待另一个线程的执行结果。
 * 要点：
 * 有一个结果需要从一个线程传递到另一个线程，让他们关联同一个GuardedObject
 * JDK中，join、Future的实现，采用的就是此模式。
 **/
public class GuardedSuspension {

    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        //输出结果的线程
        new Thread(() -> {
            //子线程执行下载
            List<String> response = downLoad();
            guardedObject.complete(response);
        }).start();

        System.out.println("WAITING...");
        //主线程接收结果
        Object response = guardedObject.get();
        System.out.println(response);
    }

    //模拟下载
    private static List<String> downLoad() {
        List<String> list = Arrays.asList("A", "B", "C");
        return list;
    }
}

class GuardedObject {

    private Object response;
    private static final Object lock = new Object();

    public Object get() {
        synchronized (lock) {
            //条件不满足则等待（还没获取到结果）
            while (response == null) {
                try {
                    System.out.println("thread waiting...");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (lock) {
            //条件满足，通知等待线程（已经得到结果）
            this.response = response;
            System.out.println("get response, return");
            lock.notifyAll();
        }
    }
}
