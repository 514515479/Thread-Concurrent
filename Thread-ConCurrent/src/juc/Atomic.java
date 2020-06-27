package juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tobi
 * @Date: 2020/6/27 17:30
 *
 * 原子类
 *     原子整数
 *     原子引用
 *     原子数组
 *     字段更新器
 *     原子累加器
 **/
public class Atomic {
    public static void main(String[] args) {
        AtomicInteger i= new AtomicInteger(0);
        System.out.println(i.incrementAndGet()); //++i   1
        System.out.println(i.getAndIncrement()); //i++   2
    }
}
