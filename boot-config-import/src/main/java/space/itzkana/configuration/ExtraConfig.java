package space.itzkana.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.itzkana.service.Animal;
import space.itzkana.service.Cat;

@Configuration
public class ExtraConfig {
    //@org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    @Bean
    public Animal animal() {
        return new Cat();
    }
}
