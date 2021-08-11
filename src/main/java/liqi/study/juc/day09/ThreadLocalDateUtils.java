package liqi.study.juc.day09;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: liqi
 * @create 2021-08-10 9:40 AM
 */

public class ThreadLocalDateUtils {

    /**
     *  SimpleDateFormat 线程不安全，不要定义为 static,否则要加锁
     */


    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Date parse(String date) throws ParseException{
        return sdf.parse(date);
    }

    public static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static final Date parse2(String date){
        try {
            return THREAD_LOCAL.get().parse(date);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            THREAD_LOCAL.remove();
        }
        return null;
    }
    // SimpleDateFormat不安全模拟
    // Exception in thread "0" Exception in thread "1" java.lang.NumberFormatException: multiple points
    private static void demo1() {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(parse("2021-11-11 11:11:11"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }



    public static void main(String[] args) throws ParseException {
       /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2021-8-10 09:41:12");
        System.out.println(date);*/
        demo3();
    }
    /**
     *  推荐解决方案 ThreadLocal
     */
    private static void demo3() {
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                System.out.println(parse2("2021-11-11 11:11:11"));
            },String.valueOf(i)).start();
        }
    }

    // SimpleDateFormat 不安全问题解决 一
    private static void demo2() {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                // 设置为局部变量
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    System.out.println(sdf.parse("2021-11-11 11:11:11"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sdf = null;
            },String.valueOf(i)).start();
        }
    }

}
