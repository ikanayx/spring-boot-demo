package space.itzkana.async.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AsyncConfig implements WebMvcConfigurer {

    /**
     * 可全局设置DeferredResult请求的超时时间
     */
    @Override
    public void configureAsyncSupport(@NonNull AsyncSupportConfigurer configurer) {
        //WebMvcConfigurer.super.configureAsyncSupport(configurer);
        configurer.setDefaultTimeout(30000);
    }
}
