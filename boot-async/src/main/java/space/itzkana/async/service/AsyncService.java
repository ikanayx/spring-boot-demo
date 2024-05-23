package space.itzkana.async.service;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class AsyncService {

    @Async
    @SneakyThrows
    public void invokeAsync(int runSeconds, DeferredResult<String> deferredResult) {
        Thread.sleep(SECONDS.toMillis(runSeconds));
        if (deferredResult != null) {
            deferredResult.setResult("async service set deferred result as: ok");
        }
    }
}
