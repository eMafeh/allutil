package util.level3_annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import util.coreutil.CoreUtil;

public interface FormGetBean {
	/**
	 * 用一个表单
	 * 
	 * 获取一个指定对象的新对象
	 * 
	 * 根据private在内全部字段尝试存入信息
	 * 
	 * 没有值则该字段不操作
	 * 
	 * @param map
	 * @param e
	 * @param b
	 * @return E
	 */
	default <E> E setBean(ServletRequest arg0, Class<E> clazz) {
		E o = null;
		CoreUtil.LogView("尝试注入 : "+clazz);
		if (arg0 == null)
			return o;
		Map<String, String[]> map = arg0.getParameterMap();
		if (map == null)
			return o;
		try {
			o = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		if (o == null)
			return o;

		List<String> mlist = new ArrayList<>();
		Field[] df = clazz.getDeclaredFields();
		for (Field field : df) {
			String fname = field.getName();
			if (field.isAnnotationPresent(FormValue.class)) {
				FormValue an = field.getAnnotation(FormValue.class);
				if (an.choose() != null && !(an.choose().value() == 2)) {
					mlist.add(an.value());
					fname = "";
				} else
					fname = an.value();
			}
			field.setAccessible(true);
			Object value;
			try {
				value = CoreUtil.mapValue(map, fname, field.getType());
				field.set(o, value);
				CoreUtil.LogView(clazz+" 中,field: "+field+" 已经注入");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Method[] md = clazz.getMethods();
		for (Method method : md)
			if (CoreUtil.isSame(mlist, method.getName())) {
				mlist.remove(method.getName());
				Parameter[] ps = method.getParameters();
				List<Object> pv = new ArrayList<>();
				for (Parameter parameter : ps) {
					Class<?> type = parameter.getType();
					String name = parameter.getName();
					if (parameter.isAnnotationPresent(FormValue.class))
						name = parameter.getAnnotation(FormValue.class).value();
					Object value = CoreUtil.mapValue(map, name, type);
					pv.add(value);
				}

				try {
					method.invoke(o, pv.toArray());
					CoreUtil.LogView(clazz+" 中,method : "+method+" 已经注入");
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		if (!mlist.isEmpty())
			CoreUtil.LogView("request中这些method没找到(" + clazz + ") : " + mlist);
		return o;
	}

	

}
