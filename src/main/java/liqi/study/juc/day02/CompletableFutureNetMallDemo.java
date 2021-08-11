package liqi.study.juc.day02;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: liqi
 * @create 2021-08-05 8:19 AM
 */

public class CompletableFutureNetMallDemo {
    /** 需求：查出所有商城某个商品价钱，并汇总
     *  经常出现在等待某条 SQL 执行完成后，再继续执行下一条 SQL ，而这两条 SQL 本身是并无关系的，
     *  可以同时进行执行的。我们希望能够两条 SQL 同时进行处理，而不是等待其中的某一条 SQL 完成后，
     *  再继续下一条。同理，对于分布式微服务的调用，按照实际业务，如果是无关联step by step的业务，
     *  可以尝试是否可以多箭齐发，同时调用。 我们去比同一个商品在各个平台上的价格，
     *  要求获得一个清单列表，1 step by step，查完京东查淘宝，查完淘宝查天猫...... 2 all   一口气同时查询。。。。。
     */
    // 模拟三个商城
    static List<NetMall> list = Arrays.asList(
       new NetMall("jd"),
       new NetMall("pdd"),
       new NetMall("tmall"),
       new NetMall("dangdang"),
       new NetMall("taobao")
    );

    // 一步一步计算
    public static List<String> getPriceByStep(List<NetMall> netMalls,String productName){
        return netMalls.stream().map(netMall -> {/** 格式化字符串 */
            return String.format(productName + " in %s price is %.2f",netMall.getMallName(),netMall.getPrice(productName));
        }).collect(Collectors.toList());
    }
    // 异步计算
    public static List<String> getPriceByAsync(List<NetMall> netMalls,String productName){
        return netMalls.stream()
                .map(netMall -> {
                    return CompletableFuture.supplyAsync(() -> {
                        return String.format(productName + " in %s price is %.2f",netMall.getMallName(),netMall.getPrice(productName));
                    });
                }).collect(Collectors.toList())
                .stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        List<String> mysql = getPriceByStep(list, "mysql");
        for (String s : mysql) {
            System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("getPriceByStep 花费时间：" + (endTime - startTime));
        long startTime2 = System.currentTimeMillis();
        List<String> mysql1 = getPriceByAsync(list, "mysql");
        for (String s : mysql1) {
            System.out.println(s);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("getPriceByAsync 花费时间：" + (endTime2 - startTime2));
    }
}
class NetMall{
    // 模拟网站
    private String mallName;

    public String getMallName() {
        return mallName;
    }

    public NetMall(String mallName){
        this.mallName = mallName;
    }
    public double getPrice(String mallName){
        // 模拟延迟
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        // 高并发下随机生成数,(0,1)之间,加一个字符，会把它转为ASC码对应数字
        return ThreadLocalRandom.current().nextDouble() + mallName.charAt(0);
    }
}