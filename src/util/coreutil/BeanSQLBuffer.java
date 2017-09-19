package util.coreutil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import util.Configure;
import util.level3_annotation.TableHelper;

public interface BeanSQLBuffer {

	public static class utilmap {
		private static final Map<String, BeanSQLBuffer> map = new HashMap<String, BeanSQLBuffer>();
	}

	String getId();
	void setId(String id);

	default void setCurrtime(Date currtime) {
	}

	default void setOfftime(Date offtime) {
	}


	/**
	 * BeanSQLBuffer��Factory
	 * 
	 * û��һ�����󴴽�һ������
	 * 
	 * �ж���ֱ�ӴӼ������ó���
	 * 
	 * @param class
	 * @return һ��û��ֵ�Ľ���Ϊ���ߵ�Bean
	 */
	@SuppressWarnings("unchecked")
	public static <E extends BeanSQLBuffer> E getUtilBean(Class<? extends BeanSQLBuffer> c) {
		if (c == null)
			return null;
		String name = c.getName();
		BeanSQLBuffer bean = utilmap.map.get(name);
		if (!utilmap.map.containsKey(name))
			try {
				bean = c.newInstance();
				utilmap.map.put(name, bean);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		return (E) bean;
	}

	@SuppressWarnings("unchecked")
	public static <E extends BeanSQLBuffer> E getUtilBean(String classname) {
		Class<? extends BeanSQLBuffer> c = null;
		if (classname == null)
			return null;
		try {
			c = (Class<? extends BeanSQLBuffer>) Class.forName(classname);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return getUtilBean(c);
	}

	/**
	 * ���ݱ�����ȡ���ݵ�����
	 * 
	 * �ײ�����table
	 * 
	 * rs�Զ��ر�
	 * 
	 * @param E
	 *            extends Bean
	 * @return table�µ���������int
	 */
	default int size() {
		TableHelper tableHelper = new TableHelper(getClass());
		return SQLUtil.size(tableHelper.getIdField(), tableHelper.getTable(), tableHelper.getState());
	}

	/**
	 * ��ҳ��ö���
	 * 
	 * @param page
	 * @param item
	 * @return ArrayList<E extends Bean>���󼯺�
	 */
	@SuppressWarnings("unchecked")
	default <E extends BeanSQLBuffer> ArrayList<E> getBeans(int page, int item) {
		return (ArrayList<E>) BeanUtil.getBeans(getClass(), page, item);
	}

	/**
	 * ���ȫ�������Ͷ���
	 * 
	 * @param page
	 * @param item
	 * @return ArrayList<E>���󼯺�
	 */
	default <E extends BeanSQLBuffer> ArrayList<E> getBeans() {
		return getBeans(0, 0);
	}

	/**
	 * ����Bean�з�null�ֶ�
	 * 
	 * ��ѯ���ݿ�����ȫ��ͬ������
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <E extends BeanSQLBuffer> ArrayList<E> getBeanByMessage(boolean b) {
		return (ArrayList<E>) BeanUtil.getBeanByMessage(this, b);
	}

	default <E extends BeanSQLBuffer> ArrayList<E> getBeanByMessage() {
		return getBeanByMessage(false);
	}

	/**
	 * ����id�����Ӧ��Bean
	 * 
	 * û���򷵻�null
	 * 
	 * bΪtrue��һ�������ݿ��ѯ,����ֱ�ӳ��Դӻ����л�ȡ��ʷ����
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <E extends BeanSQLBuffer> E getBean(String id, boolean b) {
		return (E) BeanUtil.getBean(getClass(), id, b);
	}

	// Ĭ��Ϊfalse
	default <E extends BeanSQLBuffer> E getBean(String id) {
		return getBean(id, false);
	}

	/**
	 * �Լ̳���Bean��javabean
	 * 
	 * ֱ�������ݿ�������Ӻ��޸Ĳ���
	 * 
	 * @param E
	 *            extends Bean
	 * @return �ɹ��������ֶ�����int
	 */
	default int BeanzsgNotDel() {
		return BeanzsgNotDel(null);
	}

	default int BeanzsgNotDel(ConnectionHandler con) {
		TableHelper tableHelper = new TableHelper(getClass());
		String table = tableHelper.getTable();
		/* �����Beanû��ʵ��getTable,��ô����-1 */
		if (table == null)
			return -1;

		String id = getId();
		CoreUtil.LogBean("BeanzsgNotDel:id:" + id);
		if (id != null)
			/* id��Ϊ��,�޸����� */
			return BeanUtil.BeanUpdate(this, con==null?null:con.con);
		else {
			this.setId(UUID.randomUUID().toString());
			this.setCurrtime(new Date());
			/* idΪ��,��Ӽ�¼ */
			return BeanUtil.BeanInsert(this, con==null?null:con.con) == true ? 1 : -1;
		}
	}

	/**
	 * ����Bean����ɾ��ĳ����¼
	 * 
	 * �����Ļ���id��table
	 * 
	 * @param
	 * 
	 * @return ɾ���ɹ�1,ɾ��ʧ��-1
	 */
	default int delete() {
		return delete(null);
	}

	/**
	 * ��con��Ϊ�������,����conΪһ�����
	 * 
	 * @param con
	 * @return
	 */
	default int delete(ConnectionHandler con) {
		return delete(Configure.DELETEVALUE, con);
	}

	default int delete(int i) {
		return delete(i, null);
	}

	default int delete(int i, ConnectionHandler con) {
		TableHelper tableHelper = new TableHelper(getClass());
		return SQLUtil.delete(getId(), tableHelper.getTable(), i, con==null?null:con.con, tableHelper.getState(),
				tableHelper.getIdField(), tableHelper.getOfftimeField());
	}

	public class ConnectionHandler {
		private Connection con;

		private ConnectionHandler(Connection con) {
			this.con = con;
		}

	}

	/**
	 * �������,�ṩר�õ�����,�Լ��������������
	 * 
	 * @return
	 * @throws SQLException
	 */
	static ConnectionHandler begincon() throws SQLException {
		Connection con = SQLUtil.connectPool.getcon();
		if (con != null)
			if (SQLUtil.connectPool.begin(con))
				return new ConnectionHandler(con);
			else
				SQLUtil.connectPool.rollback(con);
		return null;
	}

	static boolean commit(ConnectionHandler con) {
		boolean b = SQLUtil.connectPool.commit(con==null?null:con.con);
		con.con = null;
		return b;
	}

	static boolean rollback(ConnectionHandler con) {
		boolean b = SQLUtil.connectPool.rollback(con==null?null:con.con);
		con.con = null;
		return b;
	}

}
