/**
 * @Author: tobi
 * @Date: 2020/6/22 19:36
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
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("学习2秒...");
        }
    }

    public void sleep() {
        synchronized (sleepRoom) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("睡觉1秒...");
        }
    }
}
