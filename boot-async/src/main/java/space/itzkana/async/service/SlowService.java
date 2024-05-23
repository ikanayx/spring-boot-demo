package space.itzkana.async.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@Slf4j
public class SlowService {

    @SneakyThrows
    public void invokeVerySlow(int seconds) {
        Thread.sleep(SECONDS.toMillis(seconds));
        log.info("SlowService#invokeVerySlow method completed.");
    }
    public void invokeWithException(int seconds) throws Exception {
        Thread.sleep(SECONDS.toMillis(seconds));
        log.warn("SlowService#invokeVerySlow method throw RuntimeException.");
        throw new RuntimeException("some exception");
    }
}
