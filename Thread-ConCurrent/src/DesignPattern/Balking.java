package DesignPattern;

/**
 * @Author: tobi
 * @Date: 2020/6/26 16:07
 *
 * 同步模式之Balking
 * Balking（犹豫）模式用在一个线程发现另外一个线程或者本线程已经做了某件相同的事，那本线程就无需再做，直接结束返回。
 **/
public class Balking {
    //用来表示是否已经有线程执行过了
    private boolean flag;

    public void start() {
        //synchronized防止多线程下，多个线程都读取到flag = false
        synchronized (this) {
            //已经执行过了，直接结束返回
            if (flag) {
                return;
            }
            flag = true;
            //如果没有，执行操作
            // do something...
        }
    }
}
