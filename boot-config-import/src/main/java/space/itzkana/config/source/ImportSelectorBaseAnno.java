package space.itzkana.config.source;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import space.itzkana.annotation.EnableAnimal;

import java.util.Map;

public class ImportSelectorBaseAnno implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        // 这里的importingClassMetadata，指的是使用@EnableAnimal的非注解类（由于注解可被继承）
        // 因为`AnnotationMetadata`是`Import`注解所在的类属性，如果所在类是注解类，则延伸至应用这个注解类的非注解类为止
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableAnimal.class.getName(), true);
        if (map != null) {
            String name = (String) map.get("name");
            return new String[]{
                    "space.itzkana.service." + name
            };
        }
        return new String[0];

    }
}
