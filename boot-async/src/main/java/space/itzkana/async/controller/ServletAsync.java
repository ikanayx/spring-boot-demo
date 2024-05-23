package space.itzkana.async.controller;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class ServletAsync {

    @GetMapping("async/servlet")
    public void reqAsync(HttpServletRequest request) throws InterruptedException {
        AsyncContext ctx = request.startAsync();
        ctx.addListener(new ServletAsyncListener());
        ctx.setTimeout(TimeUnit.SECONDS.toMillis(30));
        ctx.start(() -> {
            String handleThread = Thread.currentThread().getName();
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                ctx.getResponse().setCharacterEncoding("UTF-8");
                ctx.getResponse().setContentType("text/html;charset=UTF-8");
                ctx.getResponse().getWriter().println("handler output: servlet asyncContext ok");
                ctx.complete(); // 必须调用complete()释放资源
                log.info("exit anonymous async method invoked. handle thread: {}", handleThread);
            } catch (InterruptedException e) {
                log.warn("thread {}, interrupted", handleThread, e);
            } catch (IOException e) {
                log.warn("thread {}, io exception", handleThread, e);
            }
        });
        log.info("exit reqAsync method invoked. request thread: {}", Thread.currentThread().getName());
    }

    public static class ServletAsyncListener implements AsyncListener {
        private void writeOutput(String outputMsg, ServletResponse response) throws IOException {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println(outputMsg);
        }

        @Override
        public void onComplete(AsyncEvent asyncEvent) throws IOException {
            log.info("servlet async completed");
            writeOutput("完成回调", asyncEvent.getSuppliedResponse());
            //writeOutput("追加完成", asyncEvent.getAsyncContext().getResponse());
        }

        @Override
        public void onTimeout(AsyncEvent asyncEvent) throws IOException {
            log.warn("servlet async timeout");
            writeOutput("处理超时", asyncEvent.getSuppliedResponse());
        }

        @Override
        public void onError(AsyncEvent asyncEvent) throws IOException {
            log.error("servlet async error", asyncEvent.getThrowable());
            writeOutput("处理出错", asyncEvent.getSuppliedResponse());
        }

        @Override
        public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
            log.info("servlet async start");
        }
    }
}
