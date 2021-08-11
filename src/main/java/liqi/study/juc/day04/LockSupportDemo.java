package liqi.study.juc.day04;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: liqi
 * @create 2021-08-07 8:04 AM
 */

public class LockSupportDemo {

    /**
     *  LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。
     *  下面这句话，后面详细说LockSupport中的 park() 和 unpark() 的作用分别是阻塞线程和解除阻塞线程
     */
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    static Object object = new Object();

    public static void main(String[] args) {
        demo3();
    }
    /** LockSupport 实现线程的等待与唤醒 */
    private static void demo3() {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "--- come in");
            LockSupport.park();
            LockSupport.park();
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t" + "--- 被唤醒");
        }, "t1");
        t1.start();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "发出通知");
            // 给 t1 线程发一个通行证，两个线程执行顺序无关,只要线程有通行证，
            // 碰到 unpark(thread) 就不会停止，而是消费一张通行证，通行证最多一张，不能叠加
            LockSupport.unpark(t1);
        },"t2").start();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "发出通知");
            LockSupport.unpark(t1);
        },"t3").start();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "发出通知");
            LockSupport.unpark(t1);
        },"t4").start();
    }

    /** ReentrantLock 方法实现线程等待唤醒 */
    private static void demo2() {
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + "\t" + "--- come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "\t" + "--- 被唤醒");
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                lock.unlock();
            }
        },"t1").start();
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + "\t" + "--- 发出通知");
                condition.signal();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                lock.unlock();
            }
        },"t2").start();
    }

    /** synchronized 方法实现线程等待唤醒 */
    private static void demo1() {
        new Thread(() -> {
            synchronized (object){
                System.out.println(Thread.currentThread().getName() + "\t" + "---come in");
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "\t" + "---被唤醒");

        },"t1").start();
        new Thread(() -> {
            synchronized (object){
                object.notify();
                System.out.println(Thread.currentThread().getName() + "\t" + "---发出通知");
            }
        },"t2").start();
    }

}
