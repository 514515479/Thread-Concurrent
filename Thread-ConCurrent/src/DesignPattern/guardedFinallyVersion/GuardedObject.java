package DesignPattern.guardedFinallyVersion;

/**
 * @Author: tobi
 * @Date: 2020/6/22 15:14
 **/
public class GuardedObject {

    private int id;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    private Object response;

    public Object get(long timeout) {
        synchronized (this) {
            //1.记录开始时间
            long beginTime = System.currentTimeMillis();
            //2.已经花费的时间
            long timePassed = 0;
            //条件不满足则等待（还没获取到结果）
            while (response == null) {
                //4.还需要等待的时间，例如 timeout 为1000，经历了400，还需要等待600
                long waitTime = timeout - timePassed;

                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //3.记录已经花费的时间，用于下次循环计算 还需要等待的时间 （timeout - 已经花费的时间）
                timePassed = System.currentTimeMillis() - beginTime;
                boolean flag = response == null;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
