package space.itzkana.starter.ref;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestCustomStarter {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestCustomStarter.class);
        app.run(args);
    }
}
