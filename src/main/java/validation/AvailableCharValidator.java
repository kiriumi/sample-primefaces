package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import util.StringJisUtils;
import validation.constraints.AvailableChar;

public class AvailableCharValidator implements ConstraintValidator<AvailableChar, String> {

    private boolean allowNewLine;

    @Override
    public void initialize(AvailableChar constraintAnnotation) {
        this.allowNewLine = constraintAnnotation.allowNewLine();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringJisUtils.isJisX0201_0208(value, allowNewLine);
    }

}
