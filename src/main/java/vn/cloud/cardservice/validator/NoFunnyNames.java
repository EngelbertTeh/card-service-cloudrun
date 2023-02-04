package vn.cloud.cardservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoFunnyNamesValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoFunnyNames {

    String message() default "Contains vulgar words";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}