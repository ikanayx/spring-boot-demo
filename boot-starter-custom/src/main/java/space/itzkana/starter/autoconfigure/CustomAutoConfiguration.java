package space.itzkana.starter.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.itzkana.starter.service.CustomService;
import space.itzkana.starter.service.CustomServiceImpl;

@Configuration
public class CustomAutoConfiguration {

    @Bean
    public CustomService customService() {
        return new CustomServiceImpl();
    }
}
