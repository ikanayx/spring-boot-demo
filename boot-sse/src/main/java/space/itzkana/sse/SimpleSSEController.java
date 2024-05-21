package space.itzkana.sse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Controller
@Slf4j
public class SimpleSSEController {

    private final Map<String, PrintWriter> responseMap = new ConcurrentHashMap<>();

    private void writeOutput(String id, String msg, boolean closeConnect) {
        PrintWriter pw = responseMap.get(id);
        if (pw != null) {
            pw.write(msg);
            pw.flush(); // 刷新输出流，立刻发送给客户端
            if (closeConnect) {
                responseMap.remove(id).close();
            }
        }
    }

    @GetMapping(path = "subscribe")
    public WebAsyncTask<Void> subscribe(@RequestParam(required = false, defaultValue = "default") String id, HttpServletResponse response) {

        WebAsyncTask<Void> wat = new WebAsyncTask<>(SECONDS.toMillis(120), () -> asyncHandler(id, response));
        wat.onCompletion(() -> log.info("webAsyncTask:subscribe exec completely."));
        wat.onTimeout(() -> {
            responseMap.remove(id).close();
            log.error("webAsyncTask:subscribe timeout.");
            return null;
        });
        wat.onError(() -> {
            responseMap.remove(id).close();
            log.error("webAsyncTask:subscribe hasError.");
            return null;
        });

        return wat;
    }

    @ResponseBody
    @PostMapping("broadcast")
    public String broadcast(@RequestParam(required = false, defaultValue = "default") String id, String msg) {
        if (msg != null && !msg.trim().isEmpty()) {
            writeOutput(id, msg, false);
            return "发送成功";
        }
        return "未发送任何内容";
    }

    @ResponseBody
    @PostMapping("unsubscribe")
    public String unsubscribe(@RequestParam(required = false, defaultValue = "default") String id) {
        writeOutput(id, "取消订阅", true);
        return "订阅已取消";
    }

    public Void asyncHandler(String id, HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        responseMap.put(id, response.getWriter());
        writeOutput(id, "订阅成功", false);
        while (responseMap.containsKey(id)) {
            //noinspection BusyWait
            Thread.sleep(SECONDS.toMillis(1));
            writeOutput(id, "\n%d".formatted(System.currentTimeMillis()), false);
        }
        return null;
    }
}
