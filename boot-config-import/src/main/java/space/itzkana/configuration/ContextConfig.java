package space.itzkana.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import space.itzkana.annotation.EnableAnimalDeferred;
import space.itzkana.config.source.ImportSelectorSimple;
import space.itzkana.service.Animal;
import space.itzkana.service.Bear;

/**
 * 同样是Animal接口的实现， @Import引入的实现类，优先级比当前配置类内声明的实现类要高
 * 附加条件：当前配置类需使用 @ConditionalOnMissingBean，否则会覆盖@Import的对象
 */
//@Import(ExtraConfig.class)

//可直接import目标类（比较少见的做法）
// 注册的beanName是类全限定名称，不是cat，也不是animal
//@Import(space.itzkana.service.Cat.class)

//可import实现了ImportSelector的类
// 注册的beanName是类全限定名称，不是cat，也不是animal
//@Import(ImportSelectorSimple.class)

//配合Enable注解动态import
// 注册的beanName是类全限定名称，不是cat，也不是animal
//@EnableAnimal(name = "Cat")

//Deferred意味着最后加载,此时优先级变为最低,会先注册当前配置类声明的bean，再注册deferred声明的bean
// 此时由于deferred#importSelector只能返回类全限定名，不能指定ConditionalOnMissingBean，生成的beanName也是类全限定名称
// 解析器会将两个bean同时注册到BeanRegistry内
// getBean环节时
// - 如果使用类型获取，则会报错，可使用@Primary指示优先使用对象；
// - 如果使用beanName获取，则能正常获取，且可获取到两个bean实例；
// - 同理，使用@Resource注入依赖可正常运行，使用@Autowired注入依赖则会报错
//@EnableAnimalDeferred(name = "Cat")

//使用ImportBeanDefinitionRegistrar，自定义BeanDefinition
// Register的初始化优先级在@Bean之后，因此会被当前配置类声明的bean覆盖，如果要生效则必须将当前配置类声明的bean去除
//@EnableAnimalRegister(name = "tiger")
@Configuration
public class ContextConfig {
    /**
     * 如果不添加@ConditionalOnMissingBean
     * - Import的是Configuration时，bean定义会被覆盖
     * - Import的是接口另一个实现类时，报错，spring不能从2个实现中选择
     * - Import的是ImportSelector时，报错，错误同上
     */
    @ConditionalOnMissingBean
    @Bean
    //@Primary
    public Animal animal() {
        return new Bear();
    }
}
