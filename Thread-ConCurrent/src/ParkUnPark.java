import java.util.concurrent.locks.LockSupport;

/**
 * @Author: tobi
 * @Date: 2020/6/22 19:08
 *
 * park &  unpark
 *
 * park之前调用了unpark，park会直接往下执行
 *
 **/
public class ParkUnPark {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("开始...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("park...");
            //WAITING状态
            //当没有“许可”时，线程暂停运行，有“许可”时，用掉这个“许可”，线程恢复运行
            LockSupport.park();
            System.out.println("恢复");
        });
        t1.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("unpark...");
        //给t1发放“许可”，调用多次unpark(t1)也只发放一个“许可”
        LockSupport.unpark(t1);
    }
}
