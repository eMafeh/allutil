package util.level2_bean;

import util.coreutil.BeanSQLBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MainBean extends BeanSQLBuffer {

	String getSelfId(BeanSQLBuffer sonbean);

	default <E extends BeanSQLBuffer> List<E> getSonBean(Class<? extends BeanSQLBuffer> clazz) {
		return getSonBean(clazz, false);
	}

	default <E extends BeanSQLBuffer> List<E> getSonBean(Class<? extends BeanSQLBuffer> clazz, boolean choose) {
		ArrayList<E> rightbeans = new ArrayList<>();
		E bean = BeanSQLBuffer.getUtilBean(clazz);
		if (bean == null)// 错误的bean.class,直接返回空集合
			return rightbeans;
		System.out.println(sons.sonmap);
		Map<Class<? extends BeanSQLBuffer>, Map<String, Object>> allsons = sons.sonmap.get(this.getClass());
		boolean allsonsnotnull = false;
		if (allsons != null) {
			allsonsnotnull = true;
			if (!choose) {
				Map<String, Object> sonbeans = allsons.get(clazz);
				if (sonbeans != null)
					if (System.currentTimeMillis() - (Long) sonbeans.get(sons.GETTIME) > sons.INVALIDTIME)
						sons.sonmap.remove(this);
					else {
						@SuppressWarnings("unchecked")
						List<String> sonid = (List<String>) sonbeans.get(sons.SONID);
						for (String id : sonid) {
							E sonbean = bean.getBean(id);
							if (sonbean != null && getId().equals(getSelfId(sonbean)))
								rightbeans.add(sonbean);
						}
						return rightbeans;
					}
			}
		}
		Map<String, Object> newsons = new HashMap<>();// 子map, 放着时间和id
		List<String> sonid = new ArrayList<>();

		List<E> beans = bean.getBeans();
		for (E son : beans) {
			if (getId().equals(getSelfId(son)))
				rightbeans.add(son);
			sonid.add(son.getId());
		}
		newsons.put(sons.SONID, sonid);
		newsons.put(sons.GETTIME, System.currentTimeMillis());
		if (allsonsnotnull)
			allsons.put(clazz, newsons);
		else {
			Map<Class<? extends BeanSQLBuffer>, Map<String, Object>> newallsons = new HashMap<>();// 子map,String
			newallsons.put(clazz, newsons);
			sons.sonmap.put(this.getClass(), newallsons);
		}
		return rightbeans;

	}

	static class sons {
		private static final long INVALIDTIME = 1 << 18;// 超过262秒,不到5分钟
		private static final String GETTIME = "time";
		private static final String SONID = "sonbean";
		private static final Map<Class<? extends BeanSQLBuffer>, Map<Class<? extends BeanSQLBuffer>, Map<String, Object>>> sonmap = new HashMap<>();
	}
}
