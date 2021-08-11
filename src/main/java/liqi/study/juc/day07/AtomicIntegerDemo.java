package liqi.study.juc.day07;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author: liqi
 * @create 2021-08-08 8:56 PM
 */

public class AtomicIntegerDemo {


    public static void main(String[] args) {
//        AtomicIntegerArray integerArray = new AtomicIntegerArray(5);

    }

    private static void demo() {
        // 模拟50个线程并发计算,使用 CountDownLatch 发出通知
        CountDownLatch count = new CountDownLatch(50);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        atomicInteger.incrementAndGet();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    // 每完成一个线程，计数器减一
                    count.countDown();
                }

            },"t1").start();
        }
        try {
            // 所有线程计算完成，主线程放行
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + atomicInteger.get());
    }
}
