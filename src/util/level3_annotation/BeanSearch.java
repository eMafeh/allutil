package util.level3_annotation;

import util.coreutil.BeanSQLBuffer;
import util.coreutil.BeanUtil;
import util.coreutil.CoreUtil;
import util.coreutil.SQLUtil;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BeanSearch {
	public static <E extends BeanSQLBuffer> ArrayList<E> search(String message, Class<E> clazz, Integer... cansee) {
		if (clazz == null)
			return new ArrayList<>();
		TableHelper tableHelper = new TableHelper(clazz);
		Field[] df = clazz.getDeclaredFields();
		StringBuffer sql = new StringBuffer("select * from `" + tableHelper.getTable() + "`where (");
		StringBuffer term = new StringBuffer();
		if (message == null || message.equals(""))
			message = "%";

		/* 构建模糊查询的sql语句 */
		List<String> list = new ArrayList<>();
		for (Field f : df) {
			if (f.isAnnotationPresent(Searcher.class)) {
				Searcher an = f.getAnnotation(Searcher.class);

				if (an.likeorbe()) {
					term.append("or `" + f.getName() + "` like ? ");
					list.add("%" + message + "%");
					ArrayList<? extends BeanSQLBuffer> beans = beans(an, message, "like");
					if (beans != null)
						for (BeanSQLBuffer b : beans) {
							term.append("or `" + f.getName() + "` like ? ");
							list.add("%" + b.getId() + "%");
						}
				} else {
					term.append("or `" + f.getName() + "` = ? ");
					list.add(message);
					ArrayList<? extends BeanSQLBuffer> beans = beans(an, message, "=");
					if (beans != null)
						for (BeanSQLBuffer b : beans) {
							term.append("or `" + f.getName() + "` = ? ");
							list.add(b.getId());
						}

				}
			}
		}
		if (term.length() > 0)
			sql.append(term.substring(2) + "or");

		sql.append(" 1=2 )");

		if (cansee != null && cansee.length != 0) {
			sql.append("and (");
			for (Integer i : cansee)
				sql.append("`cansee`='" + i + "' or");
			sql.append(" 1=2 )");
		} /*
			 * else sql.append("and `cansee`!=1");
			 */
		String orderByField = tableHelper.getOrderByField();
		if (orderByField != null)
			sql.append(" order by " + orderByField);
		CoreUtil.LogService(sql);
		CoreUtil.LogService(list);
		ResultSet rs = SQLUtil.query(sql.toString(), list.toArray());
		return BeanUtil.getBeans(clazz, rs);

	}

	private static ArrayList<? extends BeanSQLBuffer> beans(Searcher an, String message, String likeorbe) {
		ArrayList<? extends BeanSQLBuffer> beans = null;
		Class<? extends BeanSQLBuffer> clazz = an.clazz();
		if (clazz != BeanSQLBuffer.class) {
			message = "=".equals(likeorbe) ? message : "%" + message + "%";
			TableHelper tableHelper = new TableHelper(clazz);
			String table = tableHelper.getTable();
			Field field = null;
			try {
				field = clazz.getDeclaredField(an.field());
			} catch (NoSuchFieldException | SecurityException e1) {
				e1.printStackTrace();
			}
			if (field != null) {
				String mainsql = "select * from `" + table + "` where `" + an.field() + "` " + likeorbe + " ?";
				ResultSet rs = SQLUtil.query(mainsql, message);
				beans = BeanUtil.getBeans(clazz, rs);
			}
		}
		if (beans != null)
			CoreUtil.LogService("search:" + beans);
		return beans;
	}
}
