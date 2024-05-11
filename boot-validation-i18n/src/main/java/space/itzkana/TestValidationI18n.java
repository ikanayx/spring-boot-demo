package space.itzkana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestValidationI18n {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestValidationI18n.class);
        app.setRegisterShutdownHook(false);
        app.run(args);
    }
}
