package liqi.study.juc.day02;

import java.util.concurrent.*;

/**
 * @author: liqi
 * @create 2021-08-05 10:13 AM
 */

public class  CompletableFutureAPIDemo{

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        thenCompose() 自己解决
        CompletableFuture.supplyAsync(() -> {
            return 12;
        }).thenCompose(f -> {
            return CompletableFuture.supplyAsync(() -> {
                return f + 2;
            });
        });
    }
    /** 多次合并上一次计算结果 thenCombine(CompletionStage cs,BiFunction bf)  BiFunction 两个参数,上一次计算结果和本次计算结果的数据*/
    private static void demo7() {
        long startTime = System.currentTimeMillis();
        System.out.println(CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            return 20;
        }), (v1, v2) -> {
            return v1 + v2;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            return 30;
        }), (v1, v2) -> {
            return v1 + v2;
        }).join());
        long endTime = System.currentTimeMillis();
        System.out.println("用时：" + (endTime - startTime));
    }

    /** 对计算结果进行选取 applyToEither(CompletionStage cs,Function f) */
    private static void demo6() {
        System.out.println(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 12;
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        }), res -> {
            return res; // 选取两个线程计算的快的结果
        }).join());
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    /** 对计算结果进行消费 */
    private static void demo5() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(13, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50));
        /** thenAccept(Consumer c) 会对计算结果进行消费，导致 get()方法拿不到结果*/
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 10;
        }, executor).thenApply(f -> {
            return f + 2;
        }).thenApply(f -> {
            return f + 2;
        }).thenAccept(res -> {

        }).join()); // 无结果 null
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 10;
        }, executor).thenApply(f -> {
            return f + 2;
        }).thenApply(f -> {
            return f + 2;
        }).thenApply(res -> {
            return res + 2;
        }).join());// 有结果
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 10;
        }, executor).thenApply(f -> {
            return f + 2;
        }).thenApply(f -> {
            return f + 2;
        }).thenRun(() -> {

        }).join());// 无结果 null
        executor.shutdown();
    }

    /** 带 whenComplete 和 whenCompleteAsync 区别 */
    private static void demo4() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(13, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50));
        /**
         *  whenComplete 是执行前一个链的线程继续执行 whenComplete
         *  whenCompleteAsync 是把当前链交给线程池的线程来处理,该方法不指定线程池参数的话就是用默认的线程池
         *  后缀带 Async 的方法都是这样
         */
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "step1");
            return 12;
        },executor).thenApplyAsync(f -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "step2");
            return f + 2;
        },executor).thenApplyAsync(f -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "step3");
            return f + 2;
        },executor).whenCompleteAsync((v,e) -> {
            if (e == null) System.out.println(Thread.currentThread().getName() + "\t" + "result = " + v);
        },executor).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
        executor.shutdown();
    }

    /** handle()链式调用出错程序如何运行2 */
    private static void demo3() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(13, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50));
        CompletableFuture.supplyAsync(() -> {
            System.out.println("-------------step1");
            return 1;
        }, executor).handle((f,e) -> {
            System.out.println("-------------step2");
            return f + 2;
        }).handle((f,e) -> {
            /** 当前调用出错将会把异常传给下个调用链，使下一个链继续运行 */
            int i = 10 / 0;
            System.out.println("-------------step3");
            return f + 3;
        }).handle((f,e) -> {
            System.out.println("-------------step4");
            return f + 4;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("------result = " + v);
            }
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        }).join();
        executor.shutdown();
    }

    /** thenApply()链式调用出错程序如何运行1 */
    private static void demo2() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(13, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50));
        System.out.println(CompletableFuture.supplyAsync(() -> {
            System.out.println("-------------step1");
            return 1;
        }, executor).thenApply(f -> {
            System.out.println("-------------step2");
            return f + 2;
        }).thenApply(f -> {
            /** 链式调用,当前步骤出错，直接到 exceptionally 后面的调用不会发生*/
            int i = 10 / 0;
            System.out.println("-------------step3");
            return f + 3;
        }).thenApply(f -> {
            System.out.println("-------------step4");
            return f + 4;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("------result = " + v);
            }
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        }).join());
        executor.shutdown();
    }

    private static void demo1() throws InterruptedException, ExecutionException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(13, 20, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50));
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            return 1;
        },executor);
        // 没有计算出结果时，返回默认的 9999                                                          getNow(default result)
//        System.out.println(completableFuture.getNow(9999));

        // 阻塞方法,获取Lambda表达式返回值，过时不候，直接抛异常
//        System.out.println(completableFuture.get(2L,TimeUnit.SECONDS));
//        System.out.println(completableFuture.get());                                              get(Long l,TimeUnit time)


        // complete(Object o) 返回boolean,如果没计算完直接打断计算返回默认值，计算完就返回计算值，和 get()方法连用
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println(completableFuture.complete(123) + "\t" + completableFuture.get());

        /** 线程池必须关闭 */
        executor.shutdown();
    }

}
