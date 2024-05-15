package space.itzkana;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class TestGuavaRateLimiter {

    public static void main(String[] args) throws Exception {
        RateLimiter limiter = RateLimiter.create(5.0); // 200ms per permit
        /*for (int i = 0; i < 5; i++) {
            int count = 5;
            System.out.printf("RateLimiter#%d: %s\n", count, limiter.acquire(count));
        }*/

        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        System.out.printf("RateLimiter#10: %s\n", limiter.acquire(10));
        System.out.printf("RateLimiter#10: %s\n", limiter.acquire(10));
        System.out.println("End");
    }
}
