package vn.cloud.cardservice.validator;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoFunnyNamesValidator implements ConstraintValidator<NoFunnyNames, String> {

    private static final List<String> VULGAR_WORDS = Arrays.asList("fuck", "fuk", "fark", "dick", "d1ck", "d!ck", "cunt", "whore", "ass", "a55"); // not limited to these words, but this list is to be used as an example by the custom validator

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        for (String word : VULGAR_WORDS) {
            if (email.toLowerCase().contains(word)) {
                return false;
            }
        }
        return true;
    }
}
