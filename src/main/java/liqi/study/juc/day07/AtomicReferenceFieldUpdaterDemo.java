package liqi.study.juc.day07;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author: liqi
 * @create 2021-08-09 8:09 AM
 */

public class AtomicReferenceFieldUpdaterDemo {

    public static void main(String[] args) {
        MyVar myVar = new MyVar();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                myVar.init(myVar);
            },"t" + i).start();
        }
    }
}
class MyVar{
    // 更新的对象字段修饰符必须是 public volatile
    public volatile Boolean isInit = Boolean.FALSE;

    AtomicReferenceFieldUpdater fieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MyVar.class,Boolean.class,"isInit");

    /**  AtomicReferenceFieldUpdater案例：
     *  多线程并发调用一个类的初始化方法，如果未被初始化过，将执行初始化工作，要求只能初始化一次
     */

    public void init(MyVar myVar){
        if (fieldUpdater.compareAndSet(myVar,Boolean.FALSE,Boolean.TRUE)){
            System.out.println(Thread.currentThread().getName() + "\t" + "---start init");
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t" + "---end init");
        }else {
            System.out.println(Thread.currentThread().getName() + "\t" + "---抢夺失败");
        }
    }


}
