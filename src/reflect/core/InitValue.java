package reflect.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by QianRui on 2017/9/10.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InitValue {
    //whether recursive? default true;
    boolean value() default true;

    boolean ignore() default false;
}
