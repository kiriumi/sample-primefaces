package validation.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import validation.AvirableCharValidator;
import validation.constraints.AvirableChar.List;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = {AvirableCharValidator.class})
public @interface AvirableChar {

    String message() default "{validation.constraints.AvirableChar.message}";

    boolean allowLf() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        AvirableChar[] value();
    }

}
