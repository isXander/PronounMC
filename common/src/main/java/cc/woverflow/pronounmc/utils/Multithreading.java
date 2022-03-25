package cc.woverflow.pronounmc.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Multithreading {
    private static AtomicInteger counter = new AtomicInteger(0);
    public static ThreadPoolExecutor POOL = new ThreadPoolExecutor(20, 20, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), (r) -> new Thread(r, "PronounMC " + counter.incrementAndGet()));

    public static void runAsync(Runnable runnable) {
        POOL.execute(runnable);
    }
}
