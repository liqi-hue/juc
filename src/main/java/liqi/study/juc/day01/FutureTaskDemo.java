package liqi.study.juc.day01;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: liqi
 * @create 2021-08-04 8:20 PM
 */

public class FutureTaskDemo {

    /**
     * Future接口定义了操作异步任务执行一些方法，如获取异步任务的执行结果、取消任务的执行、
     * 判断任务是否被取消、判断任务执行是否完毕等。 Callable接口中定义了需要有返回的任务需要实现的方法。
     * 比如主线程让一个子线程去执行任务，子线程可能比较耗时，启动子线程开始执行任务后，主线程就去做其他事情了，
     * 过了一会才去获取子任务的执行结果。
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "耗时方法---> come in");
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            return 1024;
        });
        new Thread(futureTask,"a").start();
        System.out.println("主线程干活...");
        // get()方法不管是否计算完成都会阻塞，是阻塞的方法,推荐把这个方法放在代码最后面
//        System.out.println(futureTask.get());
        // 过时不候策略,调用超时直接报错
//        futureTask.get(2L,TimeUnit.SECONDS);

        /** 用轮询代替阻塞 */
        while (true){
            if (futureTask.isDone()){
                System.out.println("分线程计算完成");
                break;
            }else {
                System.out.println(Thread.currentThread().getName() + "\t" +"正在获取计算结果...");
            }
        }
    }
}
