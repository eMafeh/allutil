package util.coreutil;


import util.Configure;
import util.coreutil.SqlPool.Order;
import util.level3_annotation.TableHelper;

public class SQLBuilder {
	private static SQLBuilder sqlBuilder = new SQLBuilder();

	private SQLBuilder() {
	}

	static SQLBuilder getsqlBuilder() {
		return sqlBuilder;
	}

	String beanByIdSql(Class<?> clazz) {
		String[] sqls = SqlPool.getSQL(Order.GETBEANBYID, clazz);
		if (sqls != null && sqls[0] != null)
			return sqls[0];
		TableHelper tableHelper = new TableHelper(clazz);
		String sql = "select " + CoreUtil.fieldToString(clazz) + " from `" + tableHelper.getTable() + "` where `"
				+ tableHelper.getIdField() + "`= ? and `" + tableHelper.getState() + "`!='" + Configure.DELETEVALUE
				+ "' ";
		// ≈≈–Ú
		if (tableHelper.getOrderByField() != null)
			sql += " ORDER BY `" + tableHelper.getOrderByField() + "`";
		SqlPool.setSQL(Order.GETBEANBYID, clazz, new String[] { sql });
		return sql;
	}

	String pageSql(Class<?> clazz) {
		String[] sqls = SqlPool.getSQL(Order.PAGEQUERY, clazz);
		if (sqls != null && sqls[0] != null)
			return sqls[0];
		TableHelper tableHelper = new TableHelper(clazz);
		String table = tableHelper.getTable();
		if (table == null)
			return null;
		String field = CoreUtil.fieldToString(clazz);
		String canseename = tableHelper.getState();
		String orderby = tableHelper.getOrderByField();
		String sqlleft = "select " + field + " from `" + table + "` where `" + canseename + "`!='"
				+ Configure.DELETEVALUE + "' " + (orderby == null ? "" : "order by `" + orderby + "`");
		SqlPool.setSQL(Order.PAGEQUERY, clazz, new String[] { sqlleft });
		return sqlleft;
	}

	String[] messageSql(Class<?> clazz) {
		String[] sqls = SqlPool.getSQL(Order.BYMESSAGE, clazz);
		if (sqls != null)
			return sqls;
		TableHelper tableHelper = new TableHelper(clazz);
		String field = CoreUtil.fieldToString(clazz);
		String sql = "select " + field + " from `" + tableHelper.getTable() + "` where `" + tableHelper.getState()
				+ "`!='" + Configure.DELETEVALUE + "'";
		String orderByField = tableHelper.getOrderByField();
		String orderBysql = orderByField == null ? "" : " ORDER BY `" + orderByField + "`";
		sqls = new String[] { sql, orderBysql };
		SqlPool.setSQL(Order.BYMESSAGE, clazz, sqls);
		return sqls;
	}

	<E extends BeanSQLBuffer> String[] updateSql(E newone) {
		String[] sqls = SqlPool.getSQL(Order.UPDATE, newone.getClass());
		if (sqls != null)
			return sqls;
		TableHelper tableHelper = new TableHelper(newone.getClass());
		String table = tableHelper.getTable();

		String sql = "UPDATE `" + table + "` SET ";
		String sqlid = " WHERE (`" + tableHelper.getIdField() + "`=?)";
		sqls = new String[] { sql, sqlid };
		SqlPool.setSQL(Order.UPDATE, newone.getClass(), sqls);
		return sqls;
	}
}
