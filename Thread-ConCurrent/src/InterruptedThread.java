/**
 * @Author: tobi
 * @Date: 2020/6/19 14:39
 *
 * 线程打断
 **/
public class InterruptedThread {
    public static void main(String[] args) throws InterruptedException {

        //打断正常的线程，打断标记为true，打断阻塞状态的线程（sleep，wait，join），打断标记会被清除，为false
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        Thread.sleep(2000);
        t1.interrupt();
        System.out.println(t1.getState());
        System.out.println(t1.isInterrupted());

    }
}
