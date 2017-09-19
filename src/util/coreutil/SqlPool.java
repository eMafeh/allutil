package util.coreutil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SqlPool {
	enum Order {
		GETBEANBYID, PAGEQUERY, BYMESSAGE, UPDATE
	}

	private static final Map<Order, Map<Class<?>, String[]>> SQLMAP = new HashMap<>();

	static String[] getSQL(Order order, Class<?> clazz) {
		Map<Class<?>, String[]> map = SQLMAP.get(order);
		String[] S = map == null ? null : map.get(clazz);
		CoreUtil.LogBean("sql≥ÿƒ√µΩ : " +Arrays.toString(S));
		return S;
	}

	static void setSQL(Order order, Class<?> clazz, String[] sql) {
		if (!SQLMAP.containsKey(order))
			SQLMAP.put(order, new HashMap<>());
		SQLMAP.get(order).put(clazz, sql);
		CoreUtil.LogBean("sql≥ÿ∑≈»Î : " +Arrays.toString(sql));
	}
}
