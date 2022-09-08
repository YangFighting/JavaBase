package com.yang;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author zhangyang03
 * @Description
 * @create 2022-09-08 20:04
 */
public class FutureTaskTest {

    public static void useFutureTaskTest() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        FutureTask<String> stringFutureTask1 = new FutureTask<>(() -> {
            Thread.sleep(1000);
            return "t1 over";
        });
        new Thread(stringFutureTask1).start();

        FutureTask<String> stringFutureTask2 = new FutureTask<>(() -> {
            Thread.sleep(3 * 1000);
            return "t2 over";
        });
        new Thread(stringFutureTask2).start();

        System.out.println(stringFutureTask1.get());
        System.out.println(stringFutureTask2.get());
        long end = System.currentTimeMillis();

        System.out.println("useFuture 时间：" + (end - start));
    }


    public static void useTreadTest() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t1.join();

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        t2.join();

        long end = System.currentTimeMillis();
        System.out.println("useTread 时间：" + (end - start));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*
            使用 Tread 时，t1 需要 1s ,t2 需要 3s, 总共需要4s
        */
        useTreadTest();
        /*
            使用 Future 时，t1 需要 1s ,t2 需要 3s, 两个任务异步进行，总共需要3s
        */
        useFutureTaskTest();
    }
}
