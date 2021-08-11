package liqi.study.juc.day03;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: liqi
 * @create 2021-08-05 8:41 PM
 */

public class SaleTicket {

    private int number = 50;
    Lock lock = new ReentrantLock(true);// 公平锁
//    Lock lock = new ReentrantLock();
    public void sale(){
        while (true){
            lock.lock();
            try{
                if (number > 0) {
                    number--;
                    System.out.println(Thread.currentThread().getName() + "\t" + "线程卖出第：" + (50 - number) + " 张票");
                }else break;
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                lock.unlock();
            }
        }
    }
}
class SaleDemo{

    public static void main(String[] args) {
        SaleTicket saleTicket = new SaleTicket();
        new Thread(() -> {
            saleTicket.sale();
        },"a").start();
        new Thread(() -> {
            saleTicket.sale();
        },"b").start();
        new Thread(() -> {
            saleTicket.sale();
        },"c").start();
        new Thread(() -> {
            saleTicket.sale();
        },"d").start();
    }
}
