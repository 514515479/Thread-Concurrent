package DesignPattern.finallyVersion;

/**
 * @Author: tobi
 * @Date: 2020/6/22 15:19
 *
 * 收信人
 **/
public class People extends Thread{

    @Override
    public void run() {
        //收信
        GuardedObject guardedObject = MailBox.createGuardedObject();
        System.out.println("开始收信，id = " + guardedObject.getId());
        Object mail = guardedObject.get(5000);
        System.out.println("收到信，id = " + guardedObject.getId() + "内容：" + mail);
    }
}
