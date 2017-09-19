package util.level2_bean;

import util.coreutil.BeanSQLBuffer;

public interface SingleSonBean extends SonBean {
	String getMainId();

	Class<? extends BeanSQLBuffer> getMainClass();

	@Override
	default String getMainId(Class<? extends BeanSQLBuffer> c) {
		return getMainId();
	}

	default <E extends BeanSQLBuffer> E getMainBean(boolean b) {
		E bean = BeanSQLBuffer.getUtilBean(getMainClass());
		if (bean == null)
			return null;
		return bean.getBean(getMainId(getMainClass()), b);
	}

	default <E extends BeanSQLBuffer> E getMainBean() {
		return getMainBean(false);
	}
}
