package com.zj.jvmproblemsdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
class TestController {
    private ExecutorService threadPool = Executors.newFixedThreadPool(100);
    private static Object lock = new Object();
    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    private Object lock1 = new Object();
    private Object lock2 = new Object();

    /**
     * 模拟一个线程死锁的请求
     *
     * @return
     */
    @GetMapping("deadLock")
    public String deadLock() throws Exception {

        Thread t1 = new Thread(() -> {
            logger.info("线程1开始工作，先获取锁1");
            synchronized (lock1) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("线程1获得锁1，尝试获得锁2");

                synchronized (lock2) {
                    logger.info("线程1获得锁2成功");
                }
            }

        });

        Thread t2 = new Thread(() -> {
            logger.info("线程2开始工作，先获取锁2");
            synchronized (lock2) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    logger.info("线程2获得锁1成功");
                }
            }


        });

        t1.setName("my-thread-1");
        t2.setName("my-thread-2");
        t1.join();
        t2.join();
        t1.start();
        t2.start();


        return "success";
    }

    /**
     * 测试
     */
    @GetMapping({"/upuTest"})
    public void test() {
        for (int i = 0; i < 100; ++i) {
            this.threadPool.execute(() -> {
                logger.info("加法线程开始工作");
                long sum = 0L;
                Object var2 = lock;
                synchronized (lock) {


                    try {
                        while (true) {
                            sum += 0L;
                        }
                    } finally {
                        ;
                    }
                }
            });
        }

    }
}
