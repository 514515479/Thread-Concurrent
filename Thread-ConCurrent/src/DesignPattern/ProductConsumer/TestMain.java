package DesignPattern.ProductConsumer;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: tobi
 * @Date: 2020/6/22 16:43
 **/
public class TestMain {
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);
        //4个生产者，生产数据
        for(int i = 0; i < 40; i++) {
            int id = i;
            new Thread(() -> {
                System.out.println("数据放入queue，id=" + id);
                messageQueue.put(new Message(id, downLoad()));
            }, "生产者" + i).start();
        }

        //1个消费者，消费数据
        new Thread(() -> {
            while (true) {
                Message message = messageQueue.take();
                List<String> response = (List<String>) message.getMessage();
                System.out.println("获取内容：id=" + message.getId() + "; message=" + response);
            }
        }, "消费者").start();
    }

    //模拟下载
    private static List<String> downLoad() {
        List<String> list = Arrays.asList("A", "B", "C");
        return list;
    }
}
