package liqi.study.juc.day03;

import java.util.concurrent.TimeUnit;

/**
 * @author: liqi
 * @create 2021-08-05 6:53 PM
 */

class Phone {

    public synchronized void sendEmail(){
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("---------send email----------");
    }

    public synchronized void sendSMS(){
        System.out.println("----------send SMS-----------");
    }

    public void hello(){
        System.out.println("----------hello-----------");
    }
}
