/**
 * @Author: tobi
 * @Date: 2020/6/19 14:50
 **/

/**
 * 两阶段终止模式
 **/
public class TwoPhaseTermination {
    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.start();

        Thread.sleep(3500);

        //优雅停止
        monitor.stop();

    }
}

//监控
class Monitor {
    //监控线程
    private Thread monitor;

    //启动监控线程
    public void start() {
        monitor = new Thread(() -> {
            //循环查看打断标记
            while (true) {
                Thread currentThread = Thread.currentThread();
                if (currentThread.isInterrupted()) {
                    System.out.println("料理后事");
                    break;
                }

                try {
                    Thread.sleep(2000);
                    System.out.println("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //如果是在sleep中被打断，打断标记会为false，这里需要重新设置打断标记
                    currentThread.interrupt();
                }
            }
        });
        monitor.start();
    }
    //终止监控线程
    public void stop() {
        monitor.interrupt();
    }

}
