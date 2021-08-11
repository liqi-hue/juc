package liqi.study.juc.day09;

import java.util.concurrent.TimeUnit;

/**
 * @author: liqi
 * @create 2021-08-10 3:23 PM
 */

public class ThreadLocalMapDemo {

    /**
     * 源码分析：
     * Thread 类中有一个字段 ThreadLocal.ThreadLocalMap threadLocals = null;
     * 当 ThreadLocal调用 get() 方法时，底层调用 ThreadLocalMap map = getMap(t);尝试获取当前线程的字段 threadLocals
     *      getMap(t) -->   return t.threadLocals;
     * 值为 null 时,以当前对象为键(ThreadLocal)创建一个 ThreadLocalMap
     *      createMap(t, value);    t.threadLocals = new ThreadLocalMap(this, firstValue);
     *
     */

    public static void main(String[] args) {

    }
}
