/**
 * @Author: tobi
 * @Date: 2020/6/19 22:41
 **/

/**
 * 面向对象的写法
 * class Test {
 *     public synchronized void increment() {
 *
 *     }
 * }
 *
 * 等价于
 *
 * class Test {
 *     public void increment() {
 *         synchronized (this) {
 *
 *         }
 *     }
 * }
 **/

/**
 * class Test {
 *     public synchronized static void increment() {
 *
 *     }
 * }
 *
 * 等价于
 *
 * class Test {
 *     public static void increment() {
 *         synchronized (Test.class) {
 *
 *         }
 *     }
 * }
 **/

public class Example5000New {

    public static void main(String[] args) throws InterruptedException {

        Room room = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(room.getCounter());
    }
}

class Room {
    private int counter = 0;

    public synchronized void increment() {
        counter++;
    }

    public synchronized void decrement() {
        counter--;
    }

    public synchronized int getCounter() {
        return counter;
    }
}


