package liqi.study.juc.day06;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: liqi
 * @create 2021-08-08 2:28 PM
 */

public class MyCASDemo {

    // 手写自旋锁
    private AtomicReference<Thread> source = new AtomicReference<>();
    public void lock(){
        do{

        }while (!source.compareAndSet(null,Thread.currentThread()));
        System.out.println(Thread.currentThread().getName() + "\t" + "成功持有锁");
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
    }
    public void unlock(){
        source.compareAndSet(Thread.currentThread(),null);
        System.out.println(Thread.currentThread().getName() + "\t" + "成功释放锁");
    }

    public static void main(String[] args) {
        MyCASDemo casDemo = new MyCASDemo();
        new Thread(() -> {
            casDemo.lock();
            casDemo.unlock();
        },"t1").start();
        new Thread(() -> {
            casDemo.lock();
            casDemo.unlock();
        },"t2").start();
        new Thread(() -> {
            casDemo.lock();
            casDemo.unlock();
        },"t3").start();
        new Thread(() -> {
            casDemo.lock();
            casDemo.unlock();
        },"t4").start();
    }
}
