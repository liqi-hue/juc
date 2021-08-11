package liqi.study.juc.day03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: liqi
 * @create 2021-08-06 8:21 AM
 */

public class ThreadInterrupt {

    /** 三种优雅中断线程：
     *  首先一个线程不应该由其他线程来强制中断或停止，而是应该由线程自己自行停止。
     *  所以，Thread.stop, Thread.suspend, Thread.resume 都已经被废弃了。
     *  其次在Java中没有办法立即停止一条线程，然而停止线程却显得尤为重要，如取消一个耗时操作。
     *  因此，Java提供了一种用于停止线程的机制——中断。 中断只是一种协作机制，
     *  Java没有给中断增加任何语法，中断的过程完全需要程序员自己实现。
     *  若要中断一个线程，你需要手动调用该线程的 interrupt 方法，
     *  该方法也仅仅是将线程对象的中断标识设成 true；接着你需要自己写代码不断地检测当前线程的标识位，
     *  如果为true，表示别的线程要求这条线程中断，此时究竟该做什么需要你自己写代码实现。
     *  每个线程对象中都有一个标识，用于表示线程是否被中断；该标识位为 true 表示中断，为false表示未中断；
     *  通过调用线程对象的 interrupt 方法将该线程的标识位设为 true；可以在别的线程中调用，也可以在自己的线程中调用。
     *
     *   public void interrupt()实例方法，实例方法interrupt()仅仅是设置线程的中断状态为true，不会停止线程。
     *
     *   public static boolean interrupted()静态方法，Thread.interrupted();
     *   判断线程是否被中断，并清除当前中断状态这个方法做了两件事：1 返回当前线程的中断状态2 将当前线程的中断状态设为 false
     *   这个方法有点不好理解，因为连续调用两次的结果可能不一样。
     *
     *   public boolean isInterrupted()实例方法，判断当前线程是否被中断（通过检查中断标志位）
     */

    private static volatile boolean isStop = false;
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        demo5();

    }

    private static void demo5() {
        // interrupted() 静态方法，返回当前线程中断标志位，并把中断标志位设置为 false
        System.out.println(Thread.interrupted());   // false
        System.out.println(Thread.interrupted());   // false
        Thread.currentThread().interrupt();
        System.out.println(Thread.interrupted());   // true
        System.out.println(Thread.interrupted());   // false
    }

    /**
     * 当线程调用 wait() join() sleep() 方法时，对这个线程进行打断时
     * 该线程会抛出 InterruptedException 异常,立刻结束该方法，并清除线程中断标志位，改为 false,
     */
    private static void demo4() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()){
                    break;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    // 线程中断标志位会清除到默认 false
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t");
            }
        }, "thread-1");
        thread.start();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        new Thread(() -> {
            thread.interrupt();
        },"thread-2").start();
    }

    /** 通过 interrupt() 实现线程中断 */
    private static void demo3() {
        Thread thread1 = new Thread(() -> {
            while (true){
                // 返回当前线程中断的标志位
                if (Thread.currentThread().isInterrupted()){
                    System.out.println("thread1被打断...");
                    break;
                }
                System.out.println("hello thread1");
            }
        }, "thread1");
        thread1.start();
        try { TimeUnit.MILLISECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        new Thread(() -> {
            // 把线程中断标志位设置为 true
            thread1.interrupt();
        },"thread-1").start();
    }


    /** 通过 原子 变量实现线程中断 */
    private static void demo2() throws InterruptedException {
        new Thread(() -> {
            while (true){
                if (atomicBoolean.get()){
                    System.out.println("线程结束。。");
                    break;
                }
                System.out.println("hello atomicBoolean");
            }
        },"thread-1").start();
        Thread.sleep(200);
        new Thread(() -> {
            atomicBoolean.set(true);
        },"thread-2").start();
    }



    /** 通过 volatile 变量实现线程中断 */
    private static void demo1() throws InterruptedException {
        new Thread(() -> {
            while (true){
                if (isStop){
                    System.out.println("线程结束。。");
                    break;
                }
                System.out.println("hello world");
            }
        },"thread-1").start();
        Thread.sleep(200);
        new Thread(() -> {
            isStop = true;
        },"thread-2").start();
    }
}
