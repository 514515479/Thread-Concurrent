package DeadLock;

/**
 * @Author: tobi
 * @Date: 2020/6/22 20:22
 *
 * 哲学家就餐问题
 **/
public class TestMain {
    public static void main(String[] args) {

        Chopstock c1 = new Chopstock("筷子1");
        Chopstock c2 = new Chopstock("筷子2");
        Chopstock c3 = new Chopstock("筷子3");
        Chopstock c4 = new Chopstock("筷子4");
        Chopstock c5 = new Chopstock("筷子5");

        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("帕拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
    }
}
