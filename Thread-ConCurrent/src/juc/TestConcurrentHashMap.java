package juc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: tobi
 * @Date: 2020/6/30 20:36
 *
 * ConcurrentHashMap中线程不安全的例子
 **/
public class TestConcurrentHashMap {

    private static ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String,Integer>();

    public static void main(String[] args) throws InterruptedException {

        map.put("key", 0);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                synchronized (map) {
                    //不加synchronized的话，get和put各自是线程安全的，但是组合起来就不是了
                    int key = map.get("key") + 1;
                    map.put("key", key);
                }
            }).start();
        }
        Thread.sleep(1000);
        System.out.println(map.get("key"));
    }
}
