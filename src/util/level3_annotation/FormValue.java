package util.level3_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface FormValue {
	enum choose {
		METHOD(2), FIELD(1);
		private final int i;
		private choose(int i) {
			this.i = i;
		}
		public int value() {
			return i;
		}
	}

	String value();

	choose choose() default choose.FIELD;
}
