package liqi.study.juc.day06;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author: liqi
 * @create 2021-08-08 10:42 AM
 */

public class CASDemo {
    static AtomicInteger atomicInteger = new AtomicInteger(5);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100,1);
    public static void main(String[] args) {
        /**
         * ABA 问题的解决 -->> 增加版本号 AtomicStampedReference
         */
        new Thread(() -> {
            // 获取版本号
            int stamp = atomicStampedReference.getStamp();
            Integer reference = atomicStampedReference.getReference();
            while (!atomicStampedReference.compareAndSet(reference,101,stamp,stamp+1)){

            }
        },"t1").start();
        new Thread(() -> {
            // 获取版本号
            int stamp = atomicStampedReference.getStamp();
            Integer reference = atomicStampedReference.getReference();
            atomicStampedReference.compareAndSet(reference,101,stamp,stamp+1);
        },"t2").start();
    }

    private static void demo() {
        System.out.println(atomicInteger.compareAndSet(5,2021));
        System.out.println(atomicInteger.compareAndSet(2021,2021));
    }
}
