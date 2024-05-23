package space.itzkana.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TestAsync {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestAsync.class);
        app.setRegisterShutdownHook(false);
        app.run(args);
    }
}
