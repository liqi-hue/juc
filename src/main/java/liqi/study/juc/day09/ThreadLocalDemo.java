package liqi.study.juc.day09;

/**
 * @author: liqi
 * @create 2021-08-10 8:42 AM
 */

public class ThreadLocalDemo {

    /**
     * 是什么：
     * ThreadLocal提供线程局部变量。这些变量与正常的变量不同，因为每一个线程在访问ThreadLocal实例的时候
     * （通过其get或set方法）都有自己的、独立初始化的变量副本。ThreadLocal实例通常是类中的 私有静态字段，
     * 使用它的目的是希望将状态（例如，用户ID或事务ID）与线程关联起来。
     *
     * 能干嘛：
     *  实现每一个线程都有自己专属的本地变量副本(自己用自己的变量不麻烦别人，不和其他人共享，人人有份，人各一份)，
     *  主要解决了让每个线程绑定自己的值，通过使用get()和set()方法，
     *  获取默认值或将其值更改为当前线程所存的副本的值从而避免了线程安全问题。
     */
    public static void main(String[] args) {
        demo2();
    }

    // ThreadLocal 测试
    private static void demo2() {
        House house = new House();
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    house.saleHouse();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + " 线程卖出：" + house.threadLocal.get());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                // 必须进行 remove()
                house.threadLocal.remove();
            }
        },"t1").start();
        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    house.saleHouse();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + " 线程卖出：" + house.threadLocal.get());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                house.threadLocal.remove();
            }
        },"t2").start();
        new Thread(() -> {
            try {
                for (int i = 0; i < 8; i++) {
                    house.saleHouse();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + " 线程卖出：" + house.threadLocal.get());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                house.threadLocal.remove();
            }
        },"t3").start();
        System.out.println(Thread.currentThread().getName() + "\t" + " 线程卖出：" + house.threadLocal.get());
    }

    // ThreadLocal 初始化两种方式
    private static void demo1() {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
            @Override
            protected Integer initialValue() {
                return 0;
            }
        };
        // Supplier 接口
        ThreadLocal<Integer> threadLocal1 = ThreadLocal.withInitial(() -> 0);
    }
}
class House{

    ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

    public void saleHouse(){
        Integer value = threadLocal.get();
        ++value;
        threadLocal.set(value);
    }
}
