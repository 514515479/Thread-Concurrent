/**
 * @Author: tobi
 * @Date: 2020/6/20 14:50
 **/

import java.util.ArrayList;
import java.util.List;

/**
 * 局部变量例子
 *
 * 共享变量List add方法的线程安全问题
 * 原因：
 *     线程1执行add操作，时间片结束，size还没修改，别的线程执行add后；线程1重新执行，修改size，最终size加1其实应该是加2的
 *
 * 把list改为局部变量后就没有问题了
 **/
public class UnsafeList {

    public static void main(String[] args) {
        ThreadUnsafe thread1 = new ThreadUnsafe();
        //线程不安全
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                thread1.method1(200);
            }).start();
        }

        //线程安全
        ThreadSafe thread2 = new ThreadSafe();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                thread2.method1(200);
            }).start();
        }
    }
}

class ThreadUnsafe {
    List<String> list = new ArrayList<>();
    public void method1(int cnt) {
        for (int i = 0; i < cnt; i++) {
            method2();
            method3();
        }
    }

    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }
}

class ThreadSafe {

    public void method1(int cnt) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            method2(list);
            method3(list);
        }
    }

    private void method2(List<String> list) {
        list.add("1");
    }

    private void method3(List<String> list) {
        list.remove(0);
    }
}