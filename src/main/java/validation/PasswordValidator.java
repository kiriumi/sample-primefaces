package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import util.StringJisUtils;
import validation.constraints.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private String regexp;
    private int min;
    private int max;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches(regexp) && StringJisUtils.isJisX0201_0208(value) && min <= value.length() && value.length() <= max;
    }

}
