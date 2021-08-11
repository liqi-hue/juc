package liqi.study.juc.day07;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: liqi
 * @create 2021-08-09 9:12 AM
 */

public class LongAdder_AccumulateDemo {

    /**
     *  LongAdder 十倍的性能提升, LongAdder 已经可以替代 AtomicLong 了
     *
     *  LongAdder的基本思路就是分散热点，将value值分散到一个Cell数组中，不同线程会命中到数组的不同槽中，
     *  各个线程只对自己槽中的那个值进行CAS操作，这样热点就被分散了，冲突的概率就小很多。
     *  如果要获取真正的long值，只要将各个槽中的变量值累加返回。
     *  sum()会将所有Cell数组中的value和base累加作为返回值，核心的思想就是将之前AtomicLong一个value的更新压力分散到多个value中去，
     *  从而降级更新热点。Value = base + Cell[]
     *
     *  小总结：
     *  LongAdder在无竞争的情况，跟AtomicLong一样，对同一个base进行操作，当出现竞争关系时则是采用化整为零的做法，
     *  从空间换时间，用一个数组cells，将一个value拆分进这个数组cells。
     *  多个线程需要同时对value进行操作时候，可以对线程id进行hash得到hash值，再根据hash值映射到这个数组cells的某个下标，
     *  再对该下标所对应的值进行自增操作。当所有线程操作完毕，将数组cells的所有值和无竞争值base都加起来作为最终结果。
     *
     *  LongAdder add(1) 方法调用流程：
     *  if 短路或判断
     *      ((cs = cells) != null || !casBase(b = base, b + x))      cells 数组为空就调用 casBase(e,v),不为空进 if,跳过了 casBase(e,v)
     *  if短路或判断：
     *      1,cs == null                                        如果数组为空，直接扩容初始长度 2
     *      2,(m = cs.length - 1) < 0                           m 为数组下标最大值
     *      3,(c = cs[getProbe() & m]) == null  判断当前下标元素是否为null getProbe() 方法为计算当前线程编号并 & m 算出数组索引值，好对其 cas 修改, 为null则初始化
     *      4,!(uncontended = c.cas(v = c.value, v + x))        修改该下标的值,修改成功结束操作，不成功数组扩容
     *      5,if进去则执行：
     *      longAccumulate(x, null, uncontended);
     *          for(;;)
     *          三大分支：
     *          if ((as = cells) != null && (n = as.length) > 0)                数组初始化完成状态
     *
     *          else if (cellsBusy == 0 && cells == as && casCellsBusy())       数组未初始化状态
     *              casCellsBusy() 通过 cas 把 CELLSBUSY 的值从0 改为 1 即有锁状态
     *              对数组进行初始化，长度为2,并根据与运算对数组索引进行赋值设置(写操作)  rs[h & 1] = new Cell(x);
     *
     *          else if (casBase(v = base, ((fn == null) ? v + x : fn.applyAsLong(v, x)))       数组正在初始化状态
     *              执行 casBase(e,v)
     */

    private static final int THREAD_SIZE = 50;
    private static final int _1W = 10000;

    public static void main(String[] args) {
       new LongAdder_AccumulateDemo().test();
    }

    // 短路或测试
    public void test(){
        // || 为短路或 test1()为true test2()不再调用
        if (test1() || test2()){

        }
    }

    public boolean test1(){
        System.out.println("test1");
        return true;
    }

    public boolean test2(){
        System.out.println("test2");
        return false;
    }

    private static void extracted() {
        ClickNumber clickNumber = new ClickNumber();
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_SIZE);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < THREAD_SIZE; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100 * _1W; j++) {
                    clickNumber.add_synchronized(); // 1074 ms
                }
                countDownLatch.countDown();
            },"t" + i).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.print("用时：" + (endTime - startTime) + "\t");
        System.out.println("synchronized 计算结果:" + clickNumber.getNum());

        CountDownLatch countDownLatch2 = new CountDownLatch(THREAD_SIZE);
        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_SIZE; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100 * _1W; j++) {
                    clickNumber.add_atomic_integer(); // 885 ms
                }
                countDownLatch2.countDown();
            },"t" + i).start();
        }
        try {
            countDownLatch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime2 = System.currentTimeMillis();
        System.out.print("用时：" + (endTime2 - startTime2) + "\t");
        System.out.println("atomic_integer 计算结果:" + clickNumber.getAtomic_num().get());

        CountDownLatch countDownLatch3 = new CountDownLatch(THREAD_SIZE);
        long startTime3 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_SIZE; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100 * _1W; j++) {
                    clickNumber.add_atomic_long(); // 826 ms
                }
                countDownLatch3.countDown();
            },"t" + i).start();
        }
        try {
            countDownLatch3.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime3 = System.currentTimeMillis();
        System.out.print("用时：" + (endTime3 - startTime3) + "\t");
        System.out.println("atomic_long 计算结果:" + clickNumber.getAtomicLong());

        CountDownLatch countDownLatch4 = new CountDownLatch(THREAD_SIZE);
        long startTime4 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_SIZE; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100 * _1W; j++) {
                    clickNumber.add_long_adder(); // 70 ms 几乎十倍的性能提升
                }
                countDownLatch4.countDown();
            },"t" + i).start();
        }
        try {
            countDownLatch4.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime4 = System.currentTimeMillis();
        System.out.print("用时：" + (endTime4 - startTime4) + "\t");
        System.out.println("long_adder 计算结果:" + clickNumber.getLongAdder().longValue());

        CountDownLatch countDownLatch5 = new CountDownLatch(THREAD_SIZE);
        long startTime5 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_SIZE; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100 * _1W; j++) {
                    clickNumber.add_long_accumulator(); // 66 ms
                }
                countDownLatch5.countDown();
            },"t" + i).start();
        }
        try {
            countDownLatch5.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime5 = System.currentTimeMillis();
        System.out.print("用时：" + (endTime5 - startTime5) + "\t");
        System.out.println("long_accumulator 计算结果:" + clickNumber.getLongAccumulator());
    }

}
class ClickNumber{

    private int num = 0;
    private AtomicInteger atomic_num = new AtomicInteger(0);
    private AtomicLong atomicLong = new AtomicLong(0);
    private LongAdder longAdder = new LongAdder();
    private LongAccumulator longAccumulator = new LongAccumulator((x,y) -> x + y,0);
    public synchronized void add_synchronized(){
        num++;
    }

    public void add_atomic_integer(){
        atomic_num.incrementAndGet();
    }
    public void add_atomic_long(){
        atomicLong.incrementAndGet();
    }
    public void add_long_adder(){
        longAdder.increment();
    }
    public void add_long_accumulator(){
        longAccumulator.accumulate(1);
    }





    public int getNum() {
        return num;
    }

    public AtomicInteger getAtomic_num() {
        return atomic_num;
    }

    public AtomicLong getAtomicLong() {
        return atomicLong;
    }

    public LongAdder getLongAdder() {
        return longAdder;
    }

    public LongAccumulator getLongAccumulator() {
        return longAccumulator;
    }


}
