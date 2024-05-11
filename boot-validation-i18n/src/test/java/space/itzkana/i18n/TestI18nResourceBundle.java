package space.itzkana.i18n;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class TestI18nResourceBundle {

    public static void main(String[] args) {
        String key = "user.info.phone.length-not-match";
        Stream.of(Locale.SIMPLIFIED_CHINESE, Locale.of("en", "US"))
                .forEach(locale -> {
                    String val = ResourceBundle.getBundle("validation", locale).getString(key);
                    System.out.printf("%s: %s%n", locale.getDisplayName(), val);
                });
    }
}
