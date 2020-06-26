package DesignPattern;

/**
 * @Author: tobi
 * @Date: 2020/6/26 20:47
 *
 * 双重检验的单例模式
 **/
public class Singleton {
    //加volatile防止指令重排
    private volatile static Singleton INSTANCE = null;

    public Singleton () {}

    public Singleton getInstance() {
        //这个if是为了在单例对象已经创建的情况下，不用进入synchronized，synchronized耗性能
        if (INSTANCE == null) {
            synchronized (Singleton.class) {
                //假设有两个线程A、B。
                //两个同时通过了第一个if（同时判断INSTANCE==null）。
                //A获取了锁，判断INSTANCE==null，实例化了Singleton，释放了锁，
                //B等待A释放了锁，B获取锁了，如果没有第二个判断，那么B还会去new Singleton()，再创建一个实例。
                if (INSTANCE == null) {
                    return new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}
