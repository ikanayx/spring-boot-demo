package space.itzkana.annotation;

import org.springframework.context.annotation.Import;
import space.itzkana.config.source.ImportBdRegistrar;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ImportBdRegistrar.class)
public @interface EnableAnimalRegister {
    String name() default "";
}
