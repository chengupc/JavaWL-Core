package com.chengch.inspur.JavaWL.Core.ThreadDemo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;


public class Demo1 {
    public static void main(String[] args) {
//单线程，无返回值，无优雅退出
        //        ExecutorService pool = Executors.newSingleThreadExecutor();
        //        for(int i=1;i<=10;i++){
        //            pool.execute(new Runnable() {
        //                public void run() {
        //                    System.out.println(Thread.currentThread().getName());
        //                    try {
        //                        Thread.sleep(1000);
        //                    } catch (InterruptedException e) {
        //                        e.printStackTrace();
        //                    }
        //                }
        //
        //            });
        //            System.out.println(Thread.currentThread().getName()+" over ");
        //        }
        //        pool.shutdown();
// 多线程，有返回值，hoot退出
        //线程池 线程大小
        int threadNum = 2;
        ExecutorService exec = Executors.newFixedThreadPool(threadNum,
                new ThreadFactoryBuilder().setNameFormat("work-threadt-%d").build());
        //回调
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            exec.shutdown();
            try {
                while (!exec.awaitTermination(5, TimeUnit.MINUTES)) {

                }
            } catch (Exception e) {

            }
        }));

        //提交线程
        List<Future<String>> futures = new ArrayList<>(threadNum);
        for(int i=0; i< threadNum;i++){
            futures.add((Future<String>) exec.submit(new CheckWorker()));
        }
        for (Future<String> f : futures) {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static class CheckWorker implements Serializable,Callable<String> {


        @Override
        public String call() throws Exception {
            return Thread.currentThread().getName();
        }
    }
}
