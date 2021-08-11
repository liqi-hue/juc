package liqi.study.juc.day06;

/**
 * @author: liqi
 * @create 2021-08-08 9:40 AM
 */

public class SingletionPattern {

    // 单例模式，双重检查防止指令重排
    static volatile SingletionPattern person;

    private SingletionPattern() {
    }

    public static SingletionPattern getInstance(){
        if (person == null){
            synchronized (SingletionPattern.class){
                if (person == null){
                    person = new SingletionPattern();
                }
            }
        }
        return person;
    }


}
class Singleton2{

    private Singleton2() {
    }

    public static Singleton2 getInstance(){
        return SingletonDemo.singleton;
    }

    /**
     *  静态内部类实现单例模式
     */
    private static class SingletonDemo{
        public static Singleton2 singleton = new Singleton2();
    }
}
