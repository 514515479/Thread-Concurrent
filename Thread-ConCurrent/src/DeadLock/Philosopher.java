package DeadLock;

/**
 * @Author: tobi
 * @Date: 2020/6/22 20:18
 *
 * 哲学家（哲学家就餐）
 **/
public class Philosopher extends Thread{
    Chopstock left;
    Chopstock right;

    public Philosopher(String name, Chopstock left, Chopstock right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    public void eat() {
        System.out.println(Thread.currentThread().getName() + "吃饭中...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            //获得左手筷子
            synchronized (left) {
                //获得右手筷子
                synchronized (right) {
                    //吃饭
                    this.eat();
                }
                //放下右手筷子
            }
            //放下左手筷子
        }
    }
}
