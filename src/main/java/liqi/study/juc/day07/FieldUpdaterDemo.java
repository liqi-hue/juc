package liqi.study.juc.day07;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author: liqi
 * @create 2021-08-08 9:35 PM
 */

public class FieldUpdaterDemo {

}

class BankAccount{


    /**
     * 以一种线程安全的方式操作非线程安全对象内的某些字段
     */
    private String name = "ccb";
    // 更新的对象字段修饰符必须是 public volatile
    public volatile int balance = 10;
    /**
     * 因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须
     * 使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。
     */
    AtomicIntegerFieldUpdater fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(BankAccount.class,"balance");

    public void transfer(BankAccount bankAccount){
        fieldUpdater.incrementAndGet(bankAccount);
    }

}