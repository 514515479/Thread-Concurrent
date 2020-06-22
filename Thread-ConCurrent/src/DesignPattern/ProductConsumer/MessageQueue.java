package DesignPattern.ProductConsumer;

import java.util.LinkedList;

/**
 * @Author: tobi
 * @Date: 2020/6/22 16:26
 *
 * 消息阻塞队列
 **/
public class MessageQueue {

    //存放message的队列
    private LinkedList<Message> queue;
    //队列存放上限
    private int maxLimit;

    public MessageQueue(int maxLimit) {
        this.maxLimit = maxLimit;
        queue = new LinkedList<>();
    }

    public Message take() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                System.out.println("没有存货了，等待put中...");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //队列中有存货
            Message messge = queue.removeFirst();
            queue.notifyAll();
            return messge;
        }
    }

    public void put(Message message) {
        synchronized (queue) {
            while (queue.size() == maxLimit) {
                System.out.println("队列已达上限，等待取货...");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //队列中没满
            queue.addLast(message);
            queue.notifyAll();
        }
    }
}
