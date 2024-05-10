package space.itzkana.config.source;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import space.itzkana.annotation.EnableAnimalRegister;
import space.itzkana.service.Tiger;

import java.util.Map;

public class ImportBdRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableAnimalRegister.class.getName(), true);
        if (map != null) {
            String name = (String) map.get("name");
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(Tiger.class)
                    // 增加构造参数
                    .addConstructorArgValue(name);
            // 注册Bean
            registry.registerBeanDefinition("serviceC", beanDefinitionBuilder.getBeanDefinition());
        } else {
            ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        }
    }
}
