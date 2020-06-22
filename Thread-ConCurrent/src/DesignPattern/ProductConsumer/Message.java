package DesignPattern.ProductConsumer;

/**
 * @Author: tobi
 * @Date: 2020/6/22 16:21
 *
 * 消息
 **/
public class Message {

    private int id;
    private Object message;

    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return this.id;
    }

    public Object getMessage() {
        return this.message;
    }
}
