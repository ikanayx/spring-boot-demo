package space.itzkana.ratelimit.service;

import com.taptap.ratelimiter.annotation.RateLimit;
import com.taptap.ratelimiter.model.Mode;
import jodd.util.RandomString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LimitedService {

    final RandomString rand = RandomString.get();

    @RateLimit(
            mode = Mode.TOKEN_BUCKET,
            rate = 1,
            rateExpression = "${extend.ratelimiter.permits-rate}",
            bucketCapacityExpression = "${extend.ratelimiter.bucket-capacity}",
            keys = "#phoneNumber"
            //, fallbackFunction = "rateLimitExceedHandler" // 如果定义了fallbackFunction将不会抛出异常
    )
    public void sendVerifyCode1(String phoneNumber) {
        String code = rand.randomAlpha(6);
        log.info("Send verification code {} to phone {}", code, phoneNumber);
    }

    @RateLimit(
            mode = Mode.TIME_WINDOW,
            rate = 1,
            rateExpression = "${extend.ratelimiter.time-window-rate}",
            rateInterval = "5s",
            keys = "#emailAddress"
            //, fallbackFunction = "rateLimitExceedHandler" // 如果定义了fallbackFunction将不会抛出异常
    )
    public void sendVerifyCode2(String emailAddress) {
        String code = rand.randomAlpha(6);
        log.info("Send verification code {} to email {}", code, emailAddress);
    }

    @SuppressWarnings("unused")
    public void rateLimitExceedHandler(String arg) {
        log.warn("exceed rate limit. args: {}", arg);
    }
}
