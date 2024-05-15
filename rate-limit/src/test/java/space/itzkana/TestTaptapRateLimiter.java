package space.itzkana;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import space.itzkana.ratelimit.service.LimitedService;
import space.itzkana.ratelimit.TestRateLimit;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TestRateLimit.class)
@Slf4j
public class TestTaptapRateLimiter {

    @Autowired
    LimitedService service;

    @Value("${extend.ratelimiter.bucket-capacity}")
    Integer capacity;

    @Value("${extend.ratelimiter.time-window-rate}")
    Integer timeWindowRate;

    @Test
    public void ctxLoaded() throws InterruptedException {

        //testReqOnce();

        // 并发请求(测试令牌桶)
        //testReqConcurrent1();

        // 并发请求(滑动时间窗口)
        testReqConcurrent2();
    }

    void testReqOnce() {
        // 正常请求
        service.sendVerifyCode1("13800138000");
        service.sendVerifyCode2("support@spring.org");
    }

    void testReqConcurrent1() throws InterruptedException {
        // 令牌桶回满令牌
        Thread.sleep(SECONDS.toMillis(1));
        int size = Optional.ofNullable(capacity).orElse(1) + 3;
        int successQty = reqConcurrent(size, "permits-bucket", () -> service.sendVerifyCode1("13800138000"));
        assertEquals(successQty, capacity);
    }

    void testReqConcurrent2() throws InterruptedException {
        int size = Optional.ofNullable(timeWindowRate).orElse(1) + 3;
        int successQty = reqConcurrent(size, "time-window", () -> service.sendVerifyCode2("support@spring.org"));
        assertEquals(successQty, timeWindowRate);
    }

    int reqConcurrent(Integer threadQty, String threadNamePrefix, Runnable handler) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(threadQty);
        AtomicInteger success = new AtomicInteger();
        try (var pool = Executors.newFixedThreadPool(threadQty, new NameThreadFactory(threadNamePrefix))) {
            for (int i = 0; i < threadQty; i++) {
                pool.submit(() -> {
                    try {
                        handler.run();
                        int successQty = success.addAndGet(1);
                        log.info("{} success qty: {}", threadNamePrefix, successQty);
                    } catch (Exception ignored) {
                    }
                    latch.countDown();
                });
            }
        }
        latch.await();
        return success.get();
    }

    static class NameThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;

        NameThreadFactory(String prefix) {
            group = Thread.currentThread().getThreadGroup();
            namePrefix = prefix + "-thread-#" + poolNumber.getAndIncrement();
        }

        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
