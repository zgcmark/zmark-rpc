package com.zmark.remoting.provider.netty;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author zhengguangchen
 */

public class RequestPool {
    private static ExecutorService executorService;

    public static void submit(Runnable task) {
        if (executorService == null) {
            synchronized (RequestPool.class) {
                if (executorService == null) {
                    ThreadFactory threadFactory =
                        new ThreadFactoryBuilder().setNameFormat("aiopPool-thread-%d").build();
                    int core = Runtime.getRuntime().availableProcessors();
                    executorService = new ThreadPoolExecutor(core, core * 4, 30, TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<Runnable>(core * 100), threadFactory,
                        new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        executorService.submit(task);
    }
}
