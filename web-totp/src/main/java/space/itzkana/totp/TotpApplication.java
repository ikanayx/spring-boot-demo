package space.itzkana.totp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TotpApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TotpApplication.class);
        app.setRegisterShutdownHook(false);
        app.run(args);
    }
}
