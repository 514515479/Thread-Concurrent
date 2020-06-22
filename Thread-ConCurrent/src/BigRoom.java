/**
 * @Author: tobi
 * @Date: 2020/6/22 19:36
 *
 * 多把锁
 * 将锁的粒度细分
 *     好处：增加并发度。
 *     坏处：如果一个线程需要同时获取多把锁，容易造成死锁。
 **/
public class BigRoom {

    public static void main(String[] args) {
        Rooms rooms = new Rooms();
        new Thread(() -> {
            rooms.study();
        }, "学习线程").start();

        new Thread(() -> {
            rooms.sleep();
        }, "睡觉线程").start();
    }
}

class Rooms {
    private static final Object studyRoom = new Object();
    private static final Object sleepRoom = new Object();

    public void study() {
        synchronized (studyRoom) {
            System.out.println("学习2秒...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sleep() {
        synchronized (sleepRoom) {
            System.out.println("睡觉1秒...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
