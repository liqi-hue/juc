package liqi.study.juc.day03;

import java.util.concurrent.TimeUnit;

public class Lock8Demo {

    /**
     * synchronized 加在普通方法上，锁对象是调用当前方法的对象，加在静态方法上，锁对象是当前类对象Class
     */
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            phone.sendEmail();
        }, "a").start();
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
//            phone.sendSMS();
            phone.hello();
        }, "b").start();
    }

}
