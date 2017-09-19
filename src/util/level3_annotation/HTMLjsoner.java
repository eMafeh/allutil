package util.level3_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import util.coreutil.BeanSQLBuffer;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HTMLjsoner {
	public static final int SHOW = 1;
	public static final int ALTER = 2;

	int show()default SHOW;
	Class<? extends BeanSQLBuffer> clazz()default BeanSQLBuffer.class;
	String showfield()default "";
	String name()default "";
}
