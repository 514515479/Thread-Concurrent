/**
 * @Author: tobi
 * @Date: 2020/6/26 15:35
 *
 * java内存模型 JMM
 * 1.原子性：保证指令不会受到上下文切换的影响。
 * 2.可见性：保证指令不会受到cpu缓存的影响。
 * 3.有序性：保证指令不会收到cpu并行优化的影响。
 *
 * 可见性的例子。
 * 解决：
 *     1.volatile修饰成员变量。
 *     2.加synchronized（线程在加锁时，先清空工作内存，在主内存中拷贝最新变量的副本到工作内存，执行完代码，将更改后的共享变量的值刷新到主内存中，释放互斥锁）
 *
 **/
public class TestVolatile {

    static boolean flag = true;
    //static volatile boolean flag = true;  加了volatile的变量，就不能从缓存中读取了，必须从主内存读取最新的值
    public static void main(String[] args) {
        new Thread(() -> {
            //频繁从主内存中读取flag的值，jit会将值缓存到自己工作内存的高速缓存中，减少访问，提高效率
            while (flag) {
                //println里面加了synchronized，所有能起到停止的作用
                //System.out.println("do something...");
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //main修改了flag的值并同步到主内存，但是上面的线程是从自己工作内存的高速缓存中读取flag的值，结果永远是旧值，所以无法停止
        flag = false;
        System.out.println("falg已改为false...");
    }
}
