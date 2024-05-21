package space.itzkana.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Controller
@RequestMapping("emitter")
@Slf4j
public class SseEmitterController {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    @GetMapping(path = "subscribe")
    public SseEmitter subscribe(
            @RequestParam(required = false, defaultValue = "default") String id,
            @RequestParam(required = false, defaultValue = "3") Integer reconnectAfter
    ) throws IOException {

        int reconnectDelta = reconnectAfter;
        SseEmitter emitter = new SseEmitter(SECONDS.toMillis(reconnectDelta));

        // 声明自动重连的时间，客户端EventSource拥有自动重连功能
        emitter.send(SseEmitter.event()
                .reconnectTime(SECONDS.toMillis(reconnectDelta))
                .data("reconnect while lost after %d seconds.".formatted(reconnectDelta))
                .build());
        emitter.onTimeout(() -> {
            emitterMap.remove(id);
            log.error("sseEmitter:subscribe timeout.");
        });

        emitter.onCompletion(() -> log.info("sseEmitter:subscribed."));
        emitter.onError((throwable) -> {
            emitterMap.remove(id);
            log.error("sseEmitter:subscribe hasError.", throwable);
        });

        emitterMap.put(id, emitter);
        return emitter;
    }

    @ResponseBody
    @PostMapping("broadcast")
    public String broadcast(@RequestParam(required = false, defaultValue = "default") String id, String msg) throws IOException {
        if (msg != null && !msg.trim().isEmpty()) {
            SseEmitter sseEmitter = emitterMap.get(id);
            if (sseEmitter != null) {
                sseEmitter.send(msg);
            }
            return "发送成功";
        }
        return "未发送任何内容";
    }

    @ResponseBody
    @PostMapping("unsubscribe")
    public String unsubscribe(@RequestParam(required = false, defaultValue = "default") String id) {
        SseEmitter sseEmitter = emitterMap.get(id);
        if (sseEmitter != null) {
            sseEmitter.complete();
            emitterMap.remove(id);
        }
        return "订阅已取消";
    }
}
