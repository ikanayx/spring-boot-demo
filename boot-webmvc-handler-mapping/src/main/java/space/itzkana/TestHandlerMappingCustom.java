package space.itzkana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestHandlerMappingCustom {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestHandlerMappingCustom.class);
        app.setRegisterShutdownHook(false);
        app.run(args);
    }
}
