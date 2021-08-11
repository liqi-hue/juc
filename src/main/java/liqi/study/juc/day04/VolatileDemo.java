package liqi.study.juc.day04;

import java.util.concurrent.TimeUnit;

/**
 * @author: liqi
 * @create 2021-08-07 3:50 PM
 */

public class VolatileDemo {

    /**
     *  内存屏障（也称内存栅栏，内存栅障，屏障指令等，是一类同步屏障指令，
     *  是CPU或编译器在对内存随机访问的操作中的一个同步点，
     *  使得此点之前的所有读写操作都执行后才可以开始执行此点之后的操作），避免代码重排序。
     *  内存屏障其实就是一种JVM指令，Java内存模型的重排规则会要求Java编译器在生成JVM指令时插入特定的内存屏障指令，
     *  通过这些内存屏障指令，volatile实现了Java内存模型中的可见性和有序性，但volatile无法保证原子性。
     *  内存屏障之前的所有写操作都要回写到主内存，内存屏障之后的所有读操作都能获得内存屏障之前的所有写操作的最新结果(实现了可见性)。
     *  因此重排序时，不允许把内存屏障之后的指令重排序到内存屏障之前。一句话：对一个 volatile 域的写,
     *  happens-before 于任意后续对这个 volatile 域的读，也叫写后读。
     */
//    static boolean flag = true;
    static volatile boolean flag = true;
    static int i = 10;
    public static void main(String[] args) {
        seeDemo();
    }

    //
    private static void seeDemo() {

        new Thread(() -> {
            while (flag){

            }
        },"t1").start();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        flag = false;
    }
}
