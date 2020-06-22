package DeadLock;

/**
 * @Author: tobi
 * @Date: 2020/6/22 20:16
 *
 * 筷子（哲学家就餐）
 **/
public class Chopstock {
    private String name;

    public Chopstock(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" + this.name + "}";
    }
}
