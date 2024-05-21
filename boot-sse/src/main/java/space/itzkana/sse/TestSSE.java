package space.itzkana.sse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestSSE {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestSSE.class);
        app.setRegisterShutdownHook(false);
        app.run(args);
    }
}
