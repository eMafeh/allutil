package util.level3_annotation;

import util.coreutil.BeanSQLBuffer;
import util.coreutil.CoreUtil;
import util.level2_bean.PageBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//import net.sf.json.JSONArray;

public class HTMLJSON {
	public static <E extends BeanSQLBuffer> String HTMLtable(List<E> list, PageBean<E> page) {
		if (list == null || list.isEmpty())
			return "";

		StringBuffer js = new StringBuffer();

		Class<? extends BeanSQLBuffer> clazz = list.get(0).getClass();
		String name = clazz.getSimpleName();
		Field[] df = clazz.getDeclaredFields();
		js.append("<table><tr>");
		for (Field f : df)
			if (f.isAnnotationPresent(HTMLjsoner.class)) {
				HTMLjsoner an = f.getAnnotation(HTMLjsoner.class);
				String fieldname = f.getName();
				if (!an.name().equals(""))
					fieldname = an.name();
				js.append("<th>");
				if (an.clazz() != BeanSQLBuffer.class)
					js.append(fieldname + ":" + an.showfield());
				else
					js.append(fieldname);
				js.append("</th>");
			}
		js.append("</tr>");
		if (page == null) {
			CoreUtil.LogService("HTMKJSON : pagebean是 null");
			page = PageBean.getPageBean(list);
			CoreUtil.LogService("pagebean自动生成" + page);
		}
		try {
			for (int i = 0; i < page.getSize(); i++)
				if (i >= page.getBegin() && i < page.getEnd()) {
					E e = list.get(i);
					js.append("<tr id=\"" + e.getId() + "\">");
					for (Field f : df)
						if (f.isAnnotationPresent(HTMLjsoner.class)) {
							f.setAccessible(true);
							Object value = f.get(e);
							HTMLjsoner an = f.getAnnotation(HTMLjsoner.class);
							if (an.clazz() != BeanSQLBuffer.class) {// 根据注解去获得相关id相关字段的值
								Field field = an.clazz().getDeclaredField(an.showfield());
								field.setAccessible(true);
								BeanSQLBuffer utilBean = BeanSQLBuffer.getUtilBean(an.clazz());
								BeanSQLBuffer bean = utilBean.getBean(f.get(e).toString());
								if (bean == null)
									value = "(已删除)";
								else
									value = field.get(bean);
							}
							if (f.getAnnotation(HTMLjsoner.class).show() == 1)
								js.append("<td><input type=\"text\" name=\"" + f.getName() + "\" value=\""
										+ (value == null ? "" : value) + "\" disabled /></td>");
							else if (f.getAnnotation(HTMLjsoner.class).show() == 2)
								js.append("<td><input type=\"text\" name=\"" + f.getName() + "\" value=\""
										+ (value == null ? "" : value) + "\" /></td>");
						}
					js.append("<td><input type=\"hidden\" name=\"bean\" value=\"" + name
							+ "\"><td><input type=\"hidden\" name=\"id\" value=\"" + e.getId()
							+ "\"><input type=\"button\" value=\"修改\" onclick=\"updata('" + e.getId()
							+ "');\"></td><td><input type=\"button\" value=\"冻结\"  onclick=\"del('" + e.getId() + "','"
							+ name + "');\"></td></tr>");
				}

		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}
		js.append("</table>");
		return js.toString();
	}

	public static <E extends BeanSQLBuffer> void JSONArrays(List<E> list, PageBean<E> page)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (list == null || list.isEmpty())
			return ;
		E e = list.get(0);
		Class<? extends BeanSQLBuffer> clazz = e.getClass();
		TableHelper tableHelper = new TableHelper(clazz);
		Field[] df = clazz.getDeclaredFields();
		List<String> Fieldname = new ArrayList<>();
		List<String> th = new ArrayList<>();
		Fieldname.add(tableHelper.getIdField());
		th.add("主键");
		for (Field f : df)
			if (f.isAnnotationPresent(HTMLjsoner.class)) {
				HTMLjsoner an = f.getAnnotation(HTMLjsoner.class);
				String fieldname = f.getName();
				Fieldname.add(fieldname);
				th.add(!an.name().equals("") ? an.name() : fieldname);
			}
		Object[] fdth = { Fieldname.toArray(), th.toArray() };
		List<Object[]> tds = new ArrayList<>();
		if (page == null){
			CoreUtil.LogService("HTMKJSON : pagebean是 null");
			page = PageBean.getPageBean(list);
			CoreUtil.LogService("pagebean自动生成" + page);
		}
		for (int i = 0; i < page.getSize(); i++)
			if (i >= page.getBegin() && i < page.getEnd()) {
				e = list.get(i);
				List<String> td = new ArrayList<>();
				td.add(e.getId());
				for (Field f : df)
					if (f.isAnnotationPresent(HTMLjsoner.class)) {
						f.setAccessible(true);
						Object value = f.get(e);
						HTMLjsoner an = f.getAnnotation(HTMLjsoner.class);
						if (an.clazz() != BeanSQLBuffer.class) {// 根据注解去获得相关id相关字段的值
							Class<? extends BeanSQLBuffer> claz = an.clazz();
							Field field = claz.getDeclaredField(an.showfield());
							field.setAccessible(true);
							BeanSQLBuffer bean = BeanSQLBuffer.getUtilBean(claz).getBean(f.get(e).toString());
							if (bean == null)
								value = "(已删除)";
							else
								value = field.get(bean);
						}
						td.add(value == null ? "" : value.toString());
					}
				tds.add(td.toArray());
			}
		Object[] l = { fdth, tds.toArray() };
//		return JSONArray.fromObject(l);
	}
}
