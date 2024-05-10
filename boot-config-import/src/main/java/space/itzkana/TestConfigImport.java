package space.itzkana;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import space.itzkana.configuration.ContextConfig;
import space.itzkana.service.Animal;
import space.itzkana.service.Cat;

public class TestConfigImport {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
        Animal animal;

        // System.out.println("animal is " + context.getBean(Animal.class).getName());
        System.out.println("animal1 is " + ((Animal) context.getBean(Cat.class.getName())).getName());
        System.out.println("animal2 is " + ((Animal) context.getBean("animal")).getName());
    }
}
