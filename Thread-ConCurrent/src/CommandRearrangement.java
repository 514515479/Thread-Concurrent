/**
 * @Author: tobi
 * @Date: 2020/6/26 17:03
 *
 * 指令重排
 *     JVM在不影响正确性的前提下，可以调整语句的执行顺序。
 *     CPU一般采用流水线来执行指令。
 *     一个指令的执行被分成：
 *         取指令 - 指令译码 - 执行指令 - 内存访问 - 数据写回。
 *     在不改变程序结果的前提下，这些指令的各个阶段可以通过重排序和组合来实现指令级并行，可以优化效率。
 *
 * 指令重排例子（单线程下没问题，多线程会有问题）
 * 情况1：线程1先执行，这时flag=false，进入else结果是1。
 * 情况2：线程2先执行num=2，还没来得及执行flag=true，切换到线程1，进入else结果是1。
 * 情况3：线程2执行到flag=true，切换到线程1，进入if结果是4。
 * 情况4：发生指令重排，num = 2和flag = true，先执行flag = true（method2的两条指令顺序变化不影响结果），切换到线程1，num为0，
 *       再切换到线程2，结果是2.
 **/
public class CommandRearrangement {

    private static int num = 0;

    private static boolean flag = false;
    //解决方法：用volatile修饰成员变量，可以防止指令重排
    //private volatile static boolean flag = false;

    public static void main(String[] args) {
        IResult iResult = new IResult();
        new Thread(() -> {
            method1(iResult);
        }).start();

        new Thread(() -> {
            method2(iResult);
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("i = " + iResult.r);
    }

    private static void method1(IResult iResult) {
        if (flag) {
            iResult.r = num + num;
        } else {
            iResult.r = 1;
        }
    }

    private static void method2(IResult iResult) {
        num = 2;
        flag = true;
    }
}

class IResult {
    public Integer r;

    public IResult() {

    }
}
