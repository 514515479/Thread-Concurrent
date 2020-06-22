package DesignPattern.guardedFinallyVersion;

/**
 * @Author: tobi
 * @Date: 2020/6/22 15:23
 *
 * 寄信人
 **/
public class Postman extends Thread{

    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = MailBox.getGuardedObject(id);
        System.out.println("送信 id = " + guardedObject.getId());
        guardedObject.complete(mail);
    }
}
