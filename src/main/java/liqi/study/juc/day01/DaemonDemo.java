package liqi.study.juc.day01;

/**
 * @author: liqi
 * @create 2021-08-04 8:03 PM
 */

public class DaemonDemo {

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            System.out.println(Thread.currentThread().isDaemon()?"守护线程":"用户线程");
            while (true){

            }
        }, "a");
        // 设置为守护线程,守护线程不可单独存在，依赖于用户线程，当程序中用户线程全部结束，守护线程也会结束
        a.setDaemon(true);
        a.start();
        System.out.println(Thread.currentThread().getName() + "\t" + "task over");
    }
}
