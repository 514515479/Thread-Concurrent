package juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tobi
 * @Date: 2020/6/27 16:19
 *
 * 线程不安全的账户取钱例子，AtomicInteger，cas解决（无锁）这个效率高一些（适合线程数少，多核cpu的场景）
 *
 * 为什么无锁效率高
 *     1.无锁情况下，即使重试失败，线程始终在高速运行，而synchronized会让线程在没有获得锁的时候，发生上下文切换，进入阻塞。
 *     2.但无锁情况下，因为线程要保持运行，需要额外 CPU 的支持，CPU 在这里就好比高速跑道，没有额外的跑道，线程想高速运行也无从谈起，
 *       虽然不会进入阻塞，但由于没有分到时间片，仍然会进入可运行状态，还是会导致上下文切换。（线程数不大于CPU核心数）
 *
 *
 * cas的底层是lock cmpxchg指令（X86架构）在cpu的指令级别实现原子性。
 * 在单核CPU和多核CPU下都能够保证。
 *
 * cas和volatile
 * cas必须配合volatile才能获取到共享变量的最新值，来实现“比较并交换”（利用了volatile的可见性）
 * 例如AtomicInteger保存值的value属性，就用volatile修饰。
 **/
public interface AccountCAS {
    //获取余额
    Integer getBalance();
    //取钱
    void withdraw(Integer amount);

    public static void main(String[] args) {
        AccountCAS.demo(new AccountSafe(10000));
    }

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(AccountCAS accountCAS) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                accountCAS.withdraw(10);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                //主线程等待所有线程执行结束
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println("余额=" + accountCAS.getBalance() + "；花费时间=" + (end-start)/1000_000 + " ms");
    }
}

class AccountSafe implements AccountCAS {

    private AtomicInteger balance;

    public AccountSafe(Integer blance) {
        this.balance = new AtomicInteger(blance);
    }

    @Override
    public Integer getBalance() {
        return this.balance.get();
    }

    //解决方法：AtmoicInteger，cas
    @Override
    public void withdraw(Integer amount) {
        //先用prev对比最新的余额，然后修改，如果prev和最新的余额一致，则修改成功，否则失败
        //修改成功就退出无限循环，否则继续cas直到成功
        while (true) {
            //获取最新的余额
            int prev = this.balance.get();
            //要修改的余额
            int next = prev - amount;
            if(this.balance.compareAndSet(prev, next)) {
                break;
            }
        }

        // 可以简化为下面的方法
        //balance.addAndGet(-amount);
    }
}

