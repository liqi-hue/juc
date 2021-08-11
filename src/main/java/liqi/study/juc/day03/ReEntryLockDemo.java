package liqi.study.juc.day03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: liqi
 * @create 2021-08-05 10:06 PM
 */

public class ReEntryLockDemo {
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        demo2();
    }
    /** 死锁演示 */
    private static void demo2() {
        /**
         * 查死锁：打开终端 jps 查看进程号， jstack 进程号，看某进程是否出现死锁
         * 查死锁：打开 jconsole 定位线程
         */
        A a = new A();
        B b = new B();
        new Thread(() -> {
            synchronized (a){
                System.out.println(Thread.currentThread().getName() + "\t" + "尝试获取锁 B...");
                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
                synchronized (b){
                    System.out.println();
                }
            }
        },"thread-1").start();
        new Thread(() -> {
            synchronized (b){
                System.out.println(Thread.currentThread().getName() + "\t" + "尝试获取锁 A...");
                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
                synchronized (a){

                }
            }
        },"thread-2").start();
    }

    private static void demo1() {
        /** 可重入锁细节 */
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + "\t" + "come in");
                lock.lock();
                try{
                    System.out.println(Thread.currentThread().getName() + "\t" + "come in");
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
//                    lock.unlock();    // 当前线程如果没有把锁释放完，其他线程不能获取此锁，都会阻塞
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                lock.unlock();
            }
        },"thread-1").start();
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + "\t" + "come in");
                lock.lock();
                try{
                    System.out.println(Thread.currentThread().getName() + "\t" + "come in");
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    lock.unlock();
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                lock.unlock();
            }
        },"thread-2").start();
    }

}
