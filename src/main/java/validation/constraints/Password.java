package validation.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import validation.PasswordValidator;
import validation.constraints.Password.List;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
public @interface Password {

    String message() default "{validation.constraints.Password.message}";

    String regexp() default "^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$";

    int min() default 8;

    int max() default 128;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        Password[] value();
    }

}
