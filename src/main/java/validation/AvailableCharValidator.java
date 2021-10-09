package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import util.StringJisUtils;
import validation.constraints.AvirableChar;

public class AvirableCharValidator implements ConstraintValidator<AvirableChar, String> {

    private boolean allowLf;

    @Override
    public void initialize(AvirableChar constraintAnnotation) {
        this.allowLf = constraintAnnotation.allowLf();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringJisUtils.isJisX0201_0208(value, allowLf);
    }

}
