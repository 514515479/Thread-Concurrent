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
public class GuardedSuspensionTimeOut {

    public static void main(String[] args) {
        GuardedObjectNew guardedObjectNew = new GuardedObjectNew();
        //输出结果的线程
        new Thread(() -> {
            //子线程执行下载
            List<String> response = downLoad();
            guardedObjectNew.complete(response);
        }).start();

        System.out.println("WAITING...");
        //主线程接收结果
        Object response = guardedObjectNew.get(1000);
        System.out.println(response);
    }

    //模拟下载
    private static List<String> downLoad() {
        List<String> list = Arrays.asList("A", "B", "C");
        return list;
    }
}

class GuardedObjectNew {

    private Object response;
    private static final Object lock = new Object();

    public Object get(long timeout) {
        synchronized (lock) {
            //1.记录开始时间
            long beginTime = System.currentTimeMillis();
            //2.已经花费的时间
            long timePassed = 0;
            //条件不满足则等待（还没获取到结果）
            while (response == null) {
                //4.还需要等待的时间，例如 timeout 为1000，经历了400，还需要等待600
                long waitTime = timeout - timePassed;
                System.out.println("还需要等待的时间" + waitTime);

                if (waitTime <= 0) {
                    System.out.println("break...");
                    break;
                }
                try {
                    System.out.println("等待中...");
                    lock.wait(waitTime);
                    System.out.println("结束等待");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //3.记录已经花费的时间，用于下次循环计算 还需要等待的时间 （timeout - 已经花费的时间）
                timePassed = System.currentTimeMillis() - beginTime;
                boolean flag = response == null;
                System.out.println("已经经历了" + timePassed + ";response is null:" + flag);
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (lock) {
            //条件满足，通知等待线程（已经得到结果）
            this.response = response;
            System.out.println("获取到的结果，返回");
            lock.notifyAll();
        }
    }
}
