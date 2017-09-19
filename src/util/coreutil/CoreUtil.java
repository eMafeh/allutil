package util.coreutil;

import util.Configure;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CoreUtil {

	/* 控制台打印控制 */
	public static <E> void LogView(E e) {
		if (Configure.LOGVIEW)
			System.out.println(e);
	}

	/* 控制台打印控制 */
	public static <E> void LogDao(E e) {
		if (Configure.LOGDAO)
			System.out.println(e);
	}

	/* 控制台打印控制 */
	public static <E> void LogService(E e) {
		if (Configure.LOGSERVAIC)
			System.out.println(e);
	}

	/* 控制台打印控制 */
	public static <E> void LogBean(E e) {
		if (Configure.LOGBEAN)
			System.out.println(e);
	}

	/*
	 * 对于表单中某一条信息 尝试用bean中的类型 存入String[]的表单信息
	 */
	public static Object mapValue(Map<String, String[]> map, String name, Class<?> type) {
		String[] S = map.get(name);
		if (S == null)
			return null;
		if (S.length == 0 || S[0] == null || S[0].length() == 0)
			return null;
		if (S.length == 1)
			return realClassValue(type, S[0]);
		return Arrays.asList(S);
	}

	public static <E extends Collection<?>, V> boolean isSame(E e, V v) {
		if (e == null || v == null) {
			System.out.println("isSame存在空输入");
			return false;
		}
		for (Object i : e)
			if (v.equals(i))
				return true;
		return false;
	}

	public static String[] SAdd(String s, String[] S) {
		if (S == null) {
			String[] str = { s };
			return str;
		}
		int l = S.length;
		String[] str = new String[l + 1];
		for (int i = 0; i < l; i++)
			str[i] = S[i];
		str[l] = s;
		return str;
	}

	public static Object[] SAdd(Object s, Object[] S) {
		if (S == null) {
			Object[] str = { s };
			return str;
		}
		int l = S.length;
		Object[] str = new Object[l + 1];
		for (int i = 0; i < l; i++)
			str[i] = S[i];
		str[l] = s;
		return str;
	}

	public static Object realClassValue(Class<?> type, String s) {
		Object o = null;
		String Fname = type.getTypeName();
		switch (Fname) {
		case "java.lang.Long":
			o = Long.parseLong(s);
			break;
		case "java.lang.Integer":
			o = Integer.parseInt(s);
			break;
		case "java.lang.Double":
			o = Double.parseDouble(s);
			break;
		case "java.math.BigDecimal":
			o = BigDecimal.valueOf(Double.parseDouble(s));
			// 注意,这里有个bug,提交的数据,会缺失小数点后的0
			break;
		case "java.lang.String":
			o = s;
			break;

		case "java.util.Date":
			s = s.replace('T', ' ');
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			try {
				o = sdf.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

		return o;
	}

	public static <E> void syso(E e, int w) {
		if (e instanceof Map<?, ?>) {
			Map<?, ?> m = (Map<?, ?>) e;
			Set<?> ks = m.keySet();
			for (Object o : ks)
				syso(m.get(o), w);
		} else if (e instanceof int[])
			for (Object o : (int[]) e)
				syso(o, w);
		else if (e instanceof String[])
			for (Object o : (String[]) e)
				syso(o, w);
		else if (e instanceof Object[])
			for (Object o : (Object[]) e)
				syso(o, w);
		else
			switch (w) {
			case 1:
				LogDao(e);
				break;
			case 2:
				LogBean(e);
				break;
			case 3:
				LogService(e);
				break;
			default:
				break;
			}
	}

	public static <E> String fieldToString(Class<E> e) {
		if (e == null)
			return null;
		StringBuffer s = new StringBuffer();
		Field[] df = e.getDeclaredFields();
		for (Field field : df) {
			s.append(",`");
			s.append(field.getName());
			s.append("`");
		}
		if (s.length() > 0)
			return s.substring(1);
		return "";
	}
}
