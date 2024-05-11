package space.itzkana.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;
import space.itzkana.annotation.IsNationalPhone;
import space.itzkana.enums.PhonePatternEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NationalPhoneValidator implements ConstraintValidator<IsNationalPhone, String> {

    private static final Map<String, Pattern> patternsCache;

    static {
        patternsCache = Collections.unmodifiableMap(Arrays.stream(PhonePatternEnum.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        enumItem -> Pattern.compile(enumItem.getRegularExp())
                )));
    }

    private Map<String, Pattern> patterns;

    @Override
    public void initialize(IsNationalPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        String[] accept = constraintAnnotation.accept();
        if (ObjectUtils.isEmpty(accept)) {
            return;
        }
        List<String> acceptNations = Arrays.asList(accept);
        patterns = new LinkedHashMap<>();
        patternsCache.keySet().stream()
                .filter(acceptNations::contains)
                .forEach(nationCode -> patterns.put(nationCode, patternsCache.get(nationCode)));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String removeEmptySpaces = value.replaceAll("\\s+", "");
        if (patterns == null) {
            patterns = patternsCache;
        }
        return patterns.values().stream().anyMatch(pattern -> pattern.matcher(removeEmptySpaces).matches());
    }
}
