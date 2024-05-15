package space.itzkana.openapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestOpenapi {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TestOpenapi.class);
        application.setRegisterShutdownHook(false);
        application.run(args);
    }
}
