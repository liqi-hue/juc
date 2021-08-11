package liqi.study.juc.day09;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * @author: liqi
 * @create 2021-08-10 4:27 PM
 */

public class ReferenceDemo {

    public static void main(String[] args) {
        Object obj = new Object(){
            @Override
            protected void finalize() throws Throwable {
                System.out.println("---------gc,finalized invoked-------");
            }
        };
        PhantomReference<Object> phantomReference = new PhantomReference<>(obj,new ReferenceQueue<>());
    }
    /**
     *   虚引用需要java.lang.ref.PhantomReference类来实现。 顾名思义，就是形同虚设，与其他几种引用都不同，虚引用并不会决定对象的生命周期。
     *   如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收，它不能单独使用也不能通过它访问对象，
     *   虚引用必须和引用队列 (ReferenceQueue)联合使用。 虚引用的主要作用是跟踪对象被垃圾回收的状态。 仅仅是提供了一种确保对象被 finalize以后，
     *   做某些事情的机制。 PhantomReference的get方法总是返回null，因此无法访问对应的引用对象。其意义在于：说明一个对象已经进入finalization阶段，
     *   可以被gc回收，用来实现比finalization机制更灵活的回收操作。 换句话说，设置虚引用关联的唯一目的，
     *   就是在这个对象被收集器回收的时候收到一个系统通知或者后续添加进一步的处理。
     */



    /**
     *  软引用 SoftReference，内存足够不会被回收，内存不够就会被回收,常用在缓存中
     *  弱引用 WeakReference，gc() 运行就会被回收
     */

    /**弱引用应用场景
     * 假如有一个应用需要读取大量的本地图片:如果每次读取图片都从硬盘读取则会严重影响性能,
     * 如果一次性全部加载到内存中又可能造成内存溢出。 此时使用软引用可以解决这个问题。
     * 设计思路是：用一个HashMap来保存图片的路径和相应图片对象关联的软引用之间的映射关系，
     * 在内存不足时，JVM会自动回收这些缓存图片对象所占用的空间，从而有效地避免了OOM的问题。 Map> imageCache = new HashMap>();
     */
    private static void demo2() {
        Object obj = new Object(){
            @Override
            protected void finalize() throws Throwable {
                System.out.println("---------gc,finalized invoked-------");
            }
        };
        WeakReference<Object> weakReference = new WeakReference<>(obj);
        System.out.println("gc, before---" + weakReference.get());
        obj = null;
        System.gc();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    // 强引用
    private static void demo1() {
        Object obj = new Object(){
            @Override
            protected void finalize() throws Throwable {
                System.out.println("---------gc,finalized invoked-------");
            }
        };
        System.out.println("before gc------------" + obj);
        obj = null;
        System.gc();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("after gc-------------" + obj);
    }


}
