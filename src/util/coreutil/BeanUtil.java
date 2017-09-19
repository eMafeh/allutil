package util.coreutil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.level3_annotation.TableHelper;

public class BeanUtil {

	/**
	 * 用rs生成Beans
	 * 
	 * 数据库不想被拿到的字段,对象中不要建立字段
	 * 
	 * 默认获得所有public字段的对象字段
	 * 
	 * 可以选择false,获得全字段对象
	 * 
	 * @param e
	 * @param rs
	 * @param b
	 * @return ArrayList<E>
	 */
	public static <E> ArrayList<E> getBeans(Class<E> clazz, ResultSet rs) {
		ArrayList<E> ES = new ArrayList<>();
		CoreUtil.LogBean("根据resultSet反射获取对象 : " + clazz);
		if (clazz == null||rs==null)
			return ES;
		Field[] df = clazz.getDeclaredFields();

		/* 循环修改字段为public */
		for (Field F : df)
			F.setAccessible(true);

		try {
			while (rs.next()) {
				/* 新的对象 */
				E O = clazz.newInstance();
				for (Field F : df)
					/* 每个字段都单独尝试 */
					try {
						Object v = rs.getObject(F.getName());
						if (v != null)
							F.set(O, v);
					} catch (Exception e2) {
					}
				/* 添加对象到集合 */
				ES.add(O);
				if (O instanceof BeanSQLBuffer)
					BeanPool.put((BeanSQLBuffer) O);// TODO 在缓存区中增加bean
				CoreUtil.LogBean("拿到一个bean:" + O);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {

			/* 关闭rs */
			SQLUtil.rsClose(rs);
		}
		CoreUtil.LogBean("result对象获取完成");
		return ES;
	}

	public static <E extends BeanSQLBuffer> ArrayList<E> getBeans(Class<E> clazz, int page, int item) {
		ResultSet rs = query(page, item, clazz);
		return getBeans(clazz, rs);
	}

	/**
	 * 根据id,对象类型
	 * 
	 * 查询某个对象
	 * 
	 * @param e
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends BeanSQLBuffer> E getBean(Class<E> clazz, String id, boolean b) {
		if (id == null)
			return null;
		if (!b) {
			BeanSQLBuffer bean = BeanPool.get(id);// TODO 直接拿缓存中的Bean
			if (bean != null)
				return (E) bean;
		}

		String sql = SQLBuilder.getsqlBuilder().beanByIdSql(clazz);
		if (sql == null)
			return null;

		CoreUtil.LogBean("根据id getBean : " + sql);
		ResultSet rs = SQLUtil.query(sql, id);
		ArrayList<E> beans = getBeans(clazz, rs);
		CoreUtil.LogBean("getBean成功");
		if (beans.isEmpty())
			return null;
		return beans.get(0);

	}

	/**
	 * 根据页数
	 * 
	 * 每页几条数据 查询的Bean
	 * 
	 * 返回相应的rs
	 * 
	 * @param E
	 *            extends Bean
	 * @param page
	 * @param num
	 * @return ResultSet
	 */
	static <E extends BeanSQLBuffer> ResultSet query(int page, int item, Class<E> clazz) {

		ResultSet rs = null;

		String sqlleft = SQLBuilder.getsqlBuilder().pageSql(clazz);
		if (sqlleft == null)
			return rs;

		String sql = sqlleft + (page <= 0 || item <= 0 ? "" : " limit ?,?");

		CoreUtil.LogDao("页码查询 rs :" + sql);
		/* 查询目标记录 */
		if (page <= 0 || item <= 0)
			rs = SQLUtil.query(sql);
		else
			rs = SQLUtil.query(sql, (page - 1) * item, item);
		return rs;
	}

	/**
	 * 根据Bean中非null字段,查询数据库中完全相同的数据
	 * 
	 * @param e
	 * @return
	 */
	public static <E extends BeanSQLBuffer> ArrayList<E> getBeanByMessage(E e, boolean b) {
		ArrayList<E> A = new ArrayList<>();
		if (e == null)
			return A;
		@SuppressWarnings("unchecked")
		Class<E> clazz = (Class<E>) e.getClass();

		String[] sqls = SQLBuilder.getsqlBuilder().messageSql(clazz);
		if (sqls == null)
			return A;

		String sql = sqls[0];
		String orderBysql = sqls[1];
		CoreUtil.LogBean("根据E里的条件获取对象 : ");
		Object[] os = null;
		Field[] df = clazz.getDeclaredFields();
		for (Field f : df) {
			f.setAccessible(true);
			try {
				Object o = f.get(e);
				if (o != null) {
					sql += " and `" + f.getName() + "`=?";
					os = CoreUtil.SAdd(o, os);
				}
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		sql = sql + orderBysql;
		CoreUtil.LogBean(sql);
		if (os == null)// 没有条件返回空
			return A;
		if (os.length == 1 && e.getId() != null) {// 只有id就根据id查询
			A.add(getBean(clazz, e.getId(), b));
			return A;
		}
		return getBeans(clazz, SQLUtil.query(sql, os));
	}

	/**
	 * 对确定table的Bean直接向数据库插入
	 * 
	 * @param E
	 *            extends Bean
	 * @return boolean
	 */
	static <E extends BeanSQLBuffer> boolean BeanInsert(E e, Connection conn) {
		if (e == null)
			return false;
		String table = new TableHelper(e.getClass()).getTable();
		/* 如果该Bean没有实现getTable,那么返回false */
		if (table == null)
			return false;

		List<Object> Arr = new ArrayList<>();

		/* 准备sql语句 */
		StringBuffer begin = new StringBuffer("INSERT INTO `");
		begin.append(table);
		begin.append("` (");
		StringBuffer field = new StringBuffer();
		StringBuffer value = new StringBuffer();
		CoreUtil.LogBean("数据库插入一个Bean : ");
		/* 反射获取全部private的字段 */
		Class<?> E = e.getClass();
		Field[] df = E.getDeclaredFields();
		for (Field f : df) {
			f.setAccessible(true);
			/**
			 * 每个字段 如果不为空 添加一个数据位
			 */
			try {
				Object o = f.get(e);
				if (o != null) {
					/* 字段名 */
					field.append(",`");
					field.append(f.getName());
					field.append("`");
					/* 空位 */
					value.append(",?");
					/* 字段值 */
					Arr.add(o);
				}
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}

		/* 字段全部为空,返回false */
		if (Arr.isEmpty()) {
			CoreUtil.LogBean("对象为空 : " + e);
			return false;
		}

		/* 拼装sql语句 */
		StringBuffer sql = begin.append(field.substring(1));
		sql.append(") VALUES (");
		sql.append(value.substring(1));
		sql.append(")");
		CoreUtil.LogBean(sql);
		int i = SQLUtil.zsg(sql.toString(), conn, Arr.toArray());
		return i > 0;
	}

	/**
	 * 对确定id和table的bean向数据库修改操作
	 * 
	 * @param E
	 *            extends Bean
	 * @return int
	 */
	static <E extends BeanSQLBuffer> int BeanUpdate(E newone, Connection conn) {
		CoreUtil.LogBean("数据库更新一个BeanUpdate:");
		if (newone == null)
			return -1;
		String id = newone.getId();
		BeanPool.remove(id);// TODO 移除对象

		String[] sqls = SQLBuilder.getsqlBuilder().updateSql(newone);
		if (sqls == null)
			return -1;
		String sql = sqls[0];
		String sqlid = sqls[1];

		Class<?> E = newone.getClass();
		Field[] df = E.getDeclaredFields();
		String dsql = "";
		Object[] obj = null;
		for (Field f : df) {
			f.setAccessible(true);
			// TODO 这里最好改进为getset方法去做
			// 直接塞值会导致没有办发增加功能
			/* 每个字段单独尝试 */
			try {
				Object o = f.get(newone);
				if (o != null) {
					dsql += ", `" + f.getName() + "`=? ";
					obj = CoreUtil.SAdd(o, obj);
				}
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		sql = sql + dsql.substring(1) + sqlid;
		obj = CoreUtil.SAdd(id, obj);
		CoreUtil.LogBean("sql语句为 : " + sql + "/r/n参数为 : ");
		CoreUtil.syso(obj, 2);

		return SQLUtil.zsg(sql, conn, obj);
	}

	static class BeanPool {
		/**
		 * 一级缓存区
		 */
		private static final Map<String, BeanSQLBuffer> BEANMAP = new HashMap<>();

		private static BeanSQLBuffer get(String id) {
			CoreUtil.LogBean("对象池拿到:" + id);
			return BEANMAP.get(id);
		}

		private static void put(BeanSQLBuffer bean) {
			if (bean != null && bean.getId() != null) {
				CoreUtil.LogBean("对象池放入:" + bean);
				BEANMAP.put(bean.getId(), bean);
			}
		}

		static void remove(String id) {
			if (id != null && BEANMAP.containsKey(id)) {
				CoreUtil.LogBean("对象池移除:" + id);
				BEANMAP.remove(id);
			}
		}

	}

}
