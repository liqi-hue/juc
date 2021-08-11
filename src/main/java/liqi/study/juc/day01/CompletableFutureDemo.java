package liqi.study.juc.day01;

import java.util.concurrent.*;

/**
 * @author: liqi
 * @create 2021-08-04 9:00 PM
 */

public class CompletableFutureDemo {
    /**
     *  FutureTask的升级，增加了异步编排功能
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            return 12;
        }).thenApply(res -> {
            int i = 10 / 0;
            return res + 2;
        }).whenComplete((v, e) -> {
            if (e == null) {
                // 无异常时
                System.out.println("res = " + v);
            }
        }).exceptionally((e) -> {
//            e.printStackTrace();
            System.out.println("res = " + 1001);
            return 1001;
        });
    }

    public void demo(){
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "正在计算...");
            return 12;
        }).thenApply((v) -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return v + 1;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("计算结果为:" + v);
            }
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
        // get方法仍然会阻塞，如果一定要用，请把它放在最后面
        /** join()方法和get()方法一样会阻塞，只不过不会抛出异常 */
//        System.out.println(completableFuture.get());
        System.out.println(completableFuture.join());
        System.out.println(Thread.currentThread().getName() + "\t" + "main over...");

    }



    public void test() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 20, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50));
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "come in");
        });
        /** 可以使用自己定义的线程池 */
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "come in");
        }, executor);

        // CompletableFuture 有返回值方法
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            return 11;
        });

        CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> {
            return 12;
        }, executor);
        // CompletableFuture 实现了 Future接口,get()方法仍然是阻塞的
        System.out.println(future3.get());
        System.out.println(future4.get());
        executor.shutdown();

    }
}
