package liqi.study.juc.day03;

/**
 * @author: liqi
 * @create 2021-08-05 8:06 PM
 */

public class SynchronizedByteCodeDemo {

    /**
     *  synchronized 字节码角度分析
     */

    final Object object = new Object();
    public void m1(){
        synchronized (object){
            System.out.println(Thread.currentThread().getName() + "\t" + "come in");
        }
    }
}
