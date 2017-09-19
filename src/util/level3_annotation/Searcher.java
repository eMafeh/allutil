package util.level3_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import util.coreutil.BeanSQLBuffer;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Searcher {
	public static final boolean LIKE = true;
	public static final boolean BE = false;

	boolean likeorbe()default BE;
	Class<? extends BeanSQLBuffer> clazz() default BeanSQLBuffer.class;
	String field() default "";
}
