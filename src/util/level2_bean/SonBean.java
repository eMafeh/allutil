package util.level2_bean;

import util.coreutil.BeanSQLBuffer;

public interface SonBean extends BeanSQLBuffer {
	String getMainId(Class<? extends BeanSQLBuffer> c);

	default <E extends BeanSQLBuffer> E getMainBean(Class<? extends BeanSQLBuffer> c) {
		return getMainBean(c, false);
	}

	default <E extends BeanSQLBuffer> E getMainBean(Class<? extends BeanSQLBuffer> c, boolean b) {
		E bean = BeanSQLBuffer.getUtilBean(c);
		if (bean == null)
			return null;
		return bean.getBean(getMainId(c), b);
	}
}
