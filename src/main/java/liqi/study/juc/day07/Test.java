package liqi.study.juc.day07;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author: liqi
 * @create 2021-08-09 8:50 PM
 */

public class Test {
    @org.junit.Test
    public void test(){
        LongAdder add1;
        LongAdder add2;
        add1 = add2 = null;
        add1 = new LongAdder();
        System.out.println(add1);
        System.out.println(add2);
    }
}
