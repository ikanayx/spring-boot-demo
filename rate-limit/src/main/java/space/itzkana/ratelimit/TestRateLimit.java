package space.itzkana.ratelimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestRateLimit {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestRateLimit.class);
        app.setRegisterShutdownHook(false);
        app.run(args);
    }
}
