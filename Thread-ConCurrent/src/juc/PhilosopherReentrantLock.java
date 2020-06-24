package juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tobi
 * @Date: 2020/6/24 16:33
 *
 * ReentrantLock解决哲学家就餐问题
 **/
public class PhilosopherReentrantLock {
    public static void main(String[] args) {
        Chopstock1 c1 = new Chopstock1("筷子1");
        Chopstock1 c2 = new Chopstock1("筷子2");
        Chopstock1 c3 = new Chopstock1("筷子3");
        Chopstock1 c4 = new Chopstock1("筷子4");
        Chopstock1 c5 = new Chopstock1("筷子5");

        new Philosopher1("苏格拉底", c1, c2).start();
        new Philosopher1("帕拉图", c2, c3).start();
        new Philosopher1("亚里士多德", c3, c4).start();
        new Philosopher1("赫拉克利特", c4, c5).start();
        new Philosopher1("阿基米德", c5, c1).start();
    }
}

class Chopstock1 extends ReentrantLock {
    private String name;

    public Chopstock1(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" + this.name + "}";
    }
}

class Philosopher1 extends Thread{
    Chopstock1 left;
    Chopstock1 right;

    public Philosopher1(String name, Chopstock1 left, Chopstock1 right) {
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
            if (left.tryLock()) {
                //获得右手筷子
                try {
                    if (right.tryLock()) {
                        //吃饭
                        this.eat();
                    }
                } finally {
                    //没获取到右手筷子，就放下左手筷子
                    left.unlock();
                }
            }
        }
    }
}
