package space.itzkana.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import space.itzkana.validator.NationalPhoneValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = {NationalPhoneValidator.class})
public @interface IsNationalPhone {
    String[] accept() default {};

    String message() default "Invalid national phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
