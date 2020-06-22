package DesignPattern.guardedFinallyVersion;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @Author: tobi
 * @Date: 2020/6/22 15:06
 *
 * 中间解耦类
 **/

public class MailBox {

    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();

    private static int id = 1;

    //产生唯一id
    private static synchronized int gerenateId() {
        return id++;
    }

    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(gerenateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}
