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
	 * ��rs����Beans
	 * 
	 * ���ݿⲻ�뱻�õ����ֶ�,�����в�Ҫ�����ֶ�
	 * 
	 * Ĭ�ϻ������public�ֶεĶ����ֶ�
	 * 
	 * ����ѡ��false,���ȫ�ֶζ���
	 * 
	 * @param e
	 * @param rs
	 * @param b
	 * @return ArrayList<E>
	 */
	public static <E> ArrayList<E> getBeans(Class<E> clazz, ResultSet rs) {
		ArrayList<E> ES = new ArrayList<>();
		CoreUtil.LogBean("����resultSet�����ȡ���� : " + clazz);
		if (clazz == null||rs==null)
			return ES;
		Field[] df = clazz.getDeclaredFields();

		/* ѭ���޸��ֶ�Ϊpublic */
		for (Field F : df)
			F.setAccessible(true);

		try {
			while (rs.next()) {
				/* �µĶ��� */
				E O = clazz.newInstance();
				for (Field F : df)
					/* ÿ���ֶζ��������� */
					try {
						Object v = rs.getObject(F.getName());
						if (v != null)
							F.set(O, v);
					} catch (Exception e2) {
					}
				/* ��Ӷ��󵽼��� */
				ES.add(O);
				if (O instanceof BeanSQLBuffer)
					BeanPool.put((BeanSQLBuffer) O);// TODO �ڻ�����������bean
				CoreUtil.LogBean("�õ�һ��bean:" + O);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {

			/* �ر�rs */
			SQLUtil.rsClose(rs);
		}
		CoreUtil.LogBean("result�����ȡ���");
		return ES;
	}

	public static <E extends BeanSQLBuffer> ArrayList<E> getBeans(Class<E> clazz, int page, int item) {
		ResultSet rs = query(page, item, clazz);
		return getBeans(clazz, rs);
	}

	/**
	 * ����id,��������
	 * 
	 * ��ѯĳ������
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
			BeanSQLBuffer bean = BeanPool.get(id);// TODO ֱ���û����е�Bean
			if (bean != null)
				return (E) bean;
		}

		String sql = SQLBuilder.getsqlBuilder().beanByIdSql(clazz);
		if (sql == null)
			return null;

		CoreUtil.LogBean("����id getBean : " + sql);
		ResultSet rs = SQLUtil.query(sql, id);
		ArrayList<E> beans = getBeans(clazz, rs);
		CoreUtil.LogBean("getBean�ɹ�");
		if (beans.isEmpty())
			return null;
		return beans.get(0);

	}

	/**
	 * ����ҳ��
	 * 
	 * ÿҳ�������� ��ѯ��Bean
	 * 
	 * ������Ӧ��rs
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

		CoreUtil.LogDao("ҳ���ѯ rs :" + sql);
		/* ��ѯĿ���¼ */
		if (page <= 0 || item <= 0)
			rs = SQLUtil.query(sql);
		else
			rs = SQLUtil.query(sql, (page - 1) * item, item);
		return rs;
	}

	/**
	 * ����Bean�з�null�ֶ�,��ѯ���ݿ�����ȫ��ͬ������
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
		CoreUtil.LogBean("����E���������ȡ���� : ");
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
		if (os == null)// û���������ؿ�
			return A;
		if (os.length == 1 && e.getId() != null) {// ֻ��id�͸���id��ѯ
			A.add(getBean(clazz, e.getId(), b));
			return A;
		}
		return getBeans(clazz, SQLUtil.query(sql, os));
	}

	/**
	 * ��ȷ��table��Beanֱ�������ݿ����
	 * 
	 * @param E
	 *            extends Bean
	 * @return boolean
	 */
	static <E extends BeanSQLBuffer> boolean BeanInsert(E e, Connection conn) {
		if (e == null)
			return false;
		String table = new TableHelper(e.getClass()).getTable();
		/* �����Beanû��ʵ��getTable,��ô����false */
		if (table == null)
			return false;

		List<Object> Arr = new ArrayList<>();

		/* ׼��sql��� */
		StringBuffer begin = new StringBuffer("INSERT INTO `");
		begin.append(table);
		begin.append("` (");
		StringBuffer field = new StringBuffer();
		StringBuffer value = new StringBuffer();
		CoreUtil.LogBean("���ݿ����һ��Bean : ");
		/* �����ȡȫ��private���ֶ� */
		Class<?> E = e.getClass();
		Field[] df = E.getDeclaredFields();
		for (Field f : df) {
			f.setAccessible(true);
			/**
			 * ÿ���ֶ� �����Ϊ�� ���һ������λ
			 */
			try {
				Object o = f.get(e);
				if (o != null) {
					/* �ֶ��� */
					field.append(",`");
					field.append(f.getName());
					field.append("`");
					/* ��λ */
					value.append(",?");
					/* �ֶ�ֵ */
					Arr.add(o);
				}
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}

		/* �ֶ�ȫ��Ϊ��,����false */
		if (Arr.isEmpty()) {
			CoreUtil.LogBean("����Ϊ�� : " + e);
			return false;
		}

		/* ƴװsql��� */
		StringBuffer sql = begin.append(field.substring(1));
		sql.append(") VALUES (");
		sql.append(value.substring(1));
		sql.append(")");
		CoreUtil.LogBean(sql);
		int i = SQLUtil.zsg(sql.toString(), conn, Arr.toArray());
		return i > 0;
	}

	/**
	 * ��ȷ��id��table��bean�����ݿ��޸Ĳ���
	 * 
	 * @param E
	 *            extends Bean
	 * @return int
	 */
	static <E extends BeanSQLBuffer> int BeanUpdate(E newone, Connection conn) {
		CoreUtil.LogBean("���ݿ����һ��BeanUpdate:");
		if (newone == null)
			return -1;
		String id = newone.getId();
		BeanPool.remove(id);// TODO �Ƴ�����

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
			// TODO ������øĽ�Ϊgetset����ȥ��
			// ֱ����ֵ�ᵼ��û�а췢���ӹ���
			/* ÿ���ֶε������� */
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
		CoreUtil.LogBean("sql���Ϊ : " + sql + "/r/n����Ϊ : ");
		CoreUtil.syso(obj, 2);

		return SQLUtil.zsg(sql, conn, obj);
	}

	static class BeanPool {
		/**
		 * һ��������
		 */
		private static final Map<String, BeanSQLBuffer> BEANMAP = new HashMap<>();

		private static BeanSQLBuffer get(String id) {
			CoreUtil.LogBean("������õ�:" + id);
			return BEANMAP.get(id);
		}

		private static void put(BeanSQLBuffer bean) {
			if (bean != null && bean.getId() != null) {
				CoreUtil.LogBean("����ط���:" + bean);
				BEANMAP.put(bean.getId(), bean);
			}
		}

		static void remove(String id) {
			if (id != null && BEANMAP.containsKey(id)) {
				CoreUtil.LogBean("������Ƴ�:" + id);
				BEANMAP.remove(id);
			}
		}

	}

}
