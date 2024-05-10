package space.itzkana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestBootProcess {

    public static void main(String[] args) {
        var app = new SpringApplication(TestBootProcess.class);
        app.setRegisterShutdownHook(false);
        app.run(args);
    }

}
