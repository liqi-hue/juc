package liqi.study.juc.day07;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: liqi
 * @create 2021-08-09 8:30 AM
 */

public class LongAdderAPIDemo {

    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        longAdder.increment();
        longAdder.increment();
        System.out.println(longAdder.longValue());
        // 左边 x 为累加的值，右边 y 为 accumulate(1) 参数值
        LongAccumulator longAccumulator = new LongAccumulator((x,y) -> x + y,0L);
        longAccumulator.accumulate(1);
        longAccumulator.accumulate(2);
        longAccumulator.accumulate(3);
        System.out.println(longAccumulator.longValue());// 返回累加结果
    }
}
