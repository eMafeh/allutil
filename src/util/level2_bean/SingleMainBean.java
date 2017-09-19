package util.level2_bean;

import java.util.List;

import util.coreutil.BeanSQLBuffer;

public interface SingleMainBean extends MainBean {
	Class<? extends BeanSQLBuffer> getSonClass();

	default <E extends BeanSQLBuffer> List<E> getSonBean() {
		return getSonBean(false);
	}
	default <E extends BeanSQLBuffer> List<E> getSonBean(boolean b) {
		return getSonBean(getSonClass(),b);
	}

}
