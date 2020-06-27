package subject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tobi
 * @Date: 2020/6/27 16:19
 *
 * 线程不安全的账户取钱例子，AtomicInteger，cas解决（无锁）这个效率高一些
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
    }
}

