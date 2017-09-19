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
	 * BeanSQLBuffer的Factory
	 * 
	 * 没有一个对象创建一个对象
	 * 
	 * 有对象直接从集合中拿出来
	 * 
	 * @param class
	 * @return 一个没有值的仅作为工具的Bean
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
	 * 根据表名获取数据的行数
	 * 
	 * 底层依靠table
	 * 
	 * rs自动关闭
	 * 
	 * @param E
	 *            extends Bean
	 * @return table下的数据条数int
	 */
	default int size() {
		TableHelper tableHelper = new TableHelper(getClass());
		return SQLUtil.size(tableHelper.getIdField(), tableHelper.getTable(), tableHelper.getState());
	}

	/**
	 * 分页获得对象
	 * 
	 * @param page
	 * @param item
	 * @return ArrayList<E extends Bean>对象集合
	 */
	@SuppressWarnings("unchecked")
	default <E extends BeanSQLBuffer> ArrayList<E> getBeans(int page, int item) {
		return (ArrayList<E>) BeanUtil.getBeans(getClass(), page, item);
	}

	/**
	 * 获得全部该类型对象
	 * 
	 * @param page
	 * @param item
	 * @return ArrayList<E>对象集合
	 */
	default <E extends BeanSQLBuffer> ArrayList<E> getBeans() {
		return getBeans(0, 0);
	}

	/**
	 * 根据Bean中非null字段
	 * 
	 * 查询数据库中完全相同的数据
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
	 * 根据id获得相应的Bean
	 * 
	 * 没有则返回null
	 * 
	 * b为true则一定从数据库查询,否则直接尝试从缓存中获取历史对象
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <E extends BeanSQLBuffer> E getBean(String id, boolean b) {
		return (E) BeanUtil.getBean(getClass(), id, b);
	}

	// 默认为false
	default <E extends BeanSQLBuffer> E getBean(String id) {
		return getBean(id, false);
	}

	/**
	 * 对继承了Bean的javabean
	 * 
	 * 直接向数据库插入增加和修改操作
	 * 
	 * @param E
	 *            extends Bean
	 * @return 成功操作的字段数量int
	 */
	default int BeanzsgNotDel() {
		return BeanzsgNotDel(null);
	}

	default int BeanzsgNotDel(ConnectionHandler con) {
		TableHelper tableHelper = new TableHelper(getClass());
		String table = tableHelper.getTable();
		/* 如果该Bean没有实现getTable,那么返回-1 */
		if (table == null)
			return -1;

		String id = getId();
		CoreUtil.LogBean("BeanzsgNotDel:id:" + id);
		if (id != null)
			/* id不为空,修改数据 */
			return BeanUtil.BeanUpdate(this, con==null?null:con.con);
		else {
			this.setId(UUID.randomUUID().toString());
			this.setCurrtime(new Date());
			/* id为空,添加记录 */
			return BeanUtil.BeanInsert(this, con==null?null:con.con) == true ? 1 : -1;
		}
	}

	/**
	 * 根据Bean对象删除某条记录
	 * 
	 * 依靠的还是id和table
	 * 
	 * @param
	 * 
	 * @return 删除成功1,删除失败-1
	 */
	default int delete() {
		return delete(null);
	}

	/**
	 * 带con的为事务操作,不带con为一般操作
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
	 * 事务相关,提供专用的连接,以及事务的三个操作
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
