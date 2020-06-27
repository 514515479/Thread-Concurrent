package subject;

/**
 * @Author: tobi
 * @Date: 2020/6/24 21:04
 *
 * 同步模式之交替输出（wait/notify）
 * 线程1输出a 5次，线程2输出b 5次，线程3输出c 5次
 * 总结果输出abcabcabcabcabc
 *
 * 输出内容         等待标记         下一个标记
 *   a               1               2
 *   b               2               3
 *   c               3               1
 **/
public class SyncWaitNotify {
    //标记
    private int flag;
    //循环次数
    private int loopNumber;

    public SyncWaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(int waitFlag, int nextFlag, String str) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                //当前标记和等待标记一致时才输出
                while (this.flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                //输出后，标记变为传入的下一个标记（下一个标记与下一次输出传入等待标记一致，这样下次才能输出）
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        SyncWaitNotify syncWaitNotify = new SyncWaitNotify(1, 5);
        new Thread(() -> {
            syncWaitNotify.print(1, 2, "a");
        }).start();
        new Thread(() -> {
            syncWaitNotify.print(2, 3, "b");
        }).start();
        new Thread(() -> {
            syncWaitNotify.print(3, 1, "c");
        }).start();
    }
}
