package space.itzkana.async.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import space.itzkana.async.service.AsyncService;
import space.itzkana.async.service.SlowService;

import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SpringMvcAsync {
    private final AsyncService asyncService;
    private final SlowService slowService;

    /**
     * 不足之处：不能单独处理超时和异常情况，如果设置了全局异常处理器则会被拦截并处理
     *
     * @return Async回调
     */
    @GetMapping("async/callable")
    public Callable<String> reqCallable() {
        Callable<String> callable = () -> {
            //slowService.invokeVerySlow(10);
            slowService.invokeWithException(10);
            return "handler output: callable ok";
        };
        log.info("exit reqCallable method invoked. request thread: {}", Thread.currentThread().getName());
        return callable;
    }

    /**
     * 和Servlet提供的AsyncContext相似
     *
     * @return Async任务
     */
    @GetMapping("async/webAsyncTask")
    public WebAsyncTask<String> reqWebAsyncTask() {
        // 指定线程池executor的beanName
        WebAsyncTask<String> webAsyncTask = new WebAsyncTask<>(SECONDS.toMillis(30), "customAsyncExecutor", () -> {
            String handleThread = Thread.currentThread().getName();
            slowService.invokeVerySlow(10);
            log.info("exit anonymous async method invoked. handle thread: {}", handleThread);
            return "handler output: webAsyncTask ok";
        });
        webAsyncTask.onCompletion(() -> log.info("webAsyncTask completed"));
        webAsyncTask.onTimeout(() -> {
            log.warn("webAsyncTask timeout");
            return "webAsyncTask timeout";
        });
        webAsyncTask.onError(() -> {
            log.error("webAsyncTask error."); // 这里拿不到异常内容
            return "webAsyncTask error";
        });
        log.info("exit reqWebAsyncTask method invoked. request thread: {}", Thread.currentThread().getName());
        return webAsyncTask;
    }

    /**
     * 可将DeferredResult缓存起来，由服务端决定何时setResult返回给客户端
     */
    @GetMapping("async/deferred")
    public DeferredResult<String> reqDeferredResult(@RequestParam(required = false, defaultValue = "1") int expect) {
        int timeoutSeconds = 10;

        DeferredResult<String> deferredResult = new DeferredResult<>(SECONDS.toMillis(timeoutSeconds));
        deferredResult.onError((e) -> log.error("deferred request failed.", e));
        deferredResult.onTimeout(() -> {
            log.warn("deferred request timeout");
            deferredResult.setResult("deferred request timeout, return default response: 0");
        });
        deferredResult.onCompletion(() -> log.info("exit deferred result")); // 无论超时还是正常放回都会触发

        if (expect == 1) {
            asyncService.invokeAsync(timeoutSeconds - 1, deferredResult);
        } else {
            asyncService.invokeAsync(timeoutSeconds + 1, null);
        }
        return deferredResult;
    }
}
