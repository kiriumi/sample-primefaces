package token;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited // WeldClientProxy化されてもアノテーションを取得できるようにするため
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TokenCheck {
}
