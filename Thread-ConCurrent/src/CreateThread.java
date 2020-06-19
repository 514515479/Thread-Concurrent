import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: tobi
 * @Date: 2020/6/18 17:16
 **/

/**
 * 创建线程
 **/
public class CreateThread {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //  1.Thread
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "-running");
        }).start();
        System.out.println(Thread.currentThread().getName() + "-running");


        //  2.Runnable
        /*Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "111-running");
            }
        };*/
        Runnable r = () -> System.out.println(Thread.currentThread().getName() + "-running");
        //r.run();  直接调用run不会起线程，会在主线程执行
        new Thread(r).start();
        System.out.println(Thread.currentThread().getName() + "-running");

        //  3.Callable
        FutureTask<Integer> future = new FutureTask<Integer>(() -> {
            System.out.println(Thread.currentThread().getName() + "111-running");
            return 100;
        });
        new Thread(future).start();
        System.out.println(future.get());
    }
}
