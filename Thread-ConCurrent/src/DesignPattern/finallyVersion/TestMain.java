package DesignPattern.finallyVersion;

/**
 * @Author: tobi
 * @Date: 2020/6/22 15:28
 **/
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }

        Thread.sleep(2000);

        for (Integer id : MailBox.getIds()) {
            new Postman(id, "内容" + id).start();
        }
    }
}
