package reflect.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by qian_r on 2017/7/30.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueShip {
    String value() default "";

    String sourceName() default "";

    String targetName() default "";


//    Class<?> clazz() default ValueShip.class;
}
