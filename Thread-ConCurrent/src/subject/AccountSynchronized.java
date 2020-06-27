package subject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tobi
 * @Date: 2020/6/27 15:54
 *
 * 线程不安全的账户取钱例子，Synchronized解决（有锁）
 **/
public interface AccountSynchronized {
    //获取余额
    Integer getBalance();
    //取钱
    void withdraw(Integer amount);

    public static void main(String[] args) {
        AccountSynchronized.demo(new AccountUnsafe(10000));
    }

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(AccountSynchronized accountSynchronized) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                accountSynchronized.withdraw(10);
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
        System.out.println("余额=" + accountSynchronized.getBalance() + "；花费时间=" + (end-start)/1000_000 + " ms");
    }
}

class AccountUnsafe implements AccountSynchronized {

    private Integer balance;

    public AccountUnsafe(Integer blance) {
        this.balance = blance;
    }

    //解决方法：取钱的方法加锁
    @Override
    //public synchronized Integer getBalance() {
    public Integer getBalance() {
        return this.balance;
    }

    @Override
    //public synchronized void withdraw(Integer amount) {
    public void withdraw(Integer amount) {
        this.balance -= amount;
    }
}