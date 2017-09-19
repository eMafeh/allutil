package util.coreutil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import util.Configure;

public class SQLUtil {

	/**
	 * 公共的con,只有一个
	 */
	private static Connection con;

	/**
	 * 在query调用后,要在外部手动关闭
	 */

	private static PreparedStatement pst;

	/**
	 * 为con初始化
	 */
	static {
		CoreUtil.LogDao("数据库连接创建开始");
		try {
			Class.forName(Configure.DRIVER);
			con = DriverManager.getConnection(Configure.URL, Configure.ROOTNAME, Configure.PASSWORD);
			for (int i = 0; i < Configure.CONNUM; i++) {
				Connection con = DriverManager.getConnection(Configure.URL, Configure.ROOTNAME,
						Configure.PASSWORD);
				connectPool.conpool.add(con);
			}
			CoreUtil.LogDao("数据库连接全部创建成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 基本的query, 所有rs的源头
	 * 
	 * @param sql
	 * @param A
	 * @return ResultSet
	 */
	public static ResultSet query(String sql, Object... A) {
		try {
			return (ResultSet) pstdid(null, 0, sql, A);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 公共数据库连接的唯一入口
	 */
	private synchronized static Object pstdid(InputStream is, long size, String sql, Object... A) throws SQLException {
		CoreUtil.LogDao("数据库的sql语句 : " + sql);
		CoreUtil.LogDao("数据库的参数 : ");
		CoreUtil.syso(A, 1);
		/* 关闭上一个pst */
		if (pst != null && !pst.isClosed())
			pst.close();
		/* 生成基本语句 */
		pst = con.prepareStatement(sql);
		if ("s".equalsIgnoreCase(sql.charAt(0) + "")) {
			int i = 1;
			if (A != null)
				for (Object o : A)
					pst.setObject(i++, o);
			ResultSet rs = pst.executeQuery();
			CoreUtil.LogDao("数据库查找成功");
			return rs;
		} else {
			int i = didzsg(is, size, pst, A);
			return i;
		}
	}

	/*
	 * 增删改的入口
	 */
	private static int didzsg(InputStream is, long size, PreparedStatement pstt, Object... A) throws SQLException {
		int t = -1;// 操作结果
		int i = 1;// 参数数量

		/* 传入参数 */
		if (A != null)
			for (Object o : A)
				pstt.setObject(i++, o);

		/* 传入二进制流参数 */
		if (is != null && size != 0)
			pstt.setBinaryStream(i, is, size);

		/* 进行操作 */
		t = pstt.executeUpdate();
		CoreUtil.LogDao("数据库updata:成功");
		return t;
	}

	/**
	 * 基本的zsg
	 * 
	 * @param sql
	 * @param A
	 * @param is
	 * @param size
	 * @return int
	 */
	public static int zsg(InputStream is, long size, String sql, Connection conn, Object... A) {
		try {
			if (conn == null) // 没有专用连接,使用公用的
				return (int) pstdid(is, size, sql, A);
			// TODO 事务模式,接受begin和commit
			PreparedStatement p = conn.prepareStatement(sql);
			int i = didzsg(is, size, p, A);
			p.close();
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 少参数的zsg方法,连接池提供
	 * 
	 * @param sql
	 * @param A
	 * @return int
	 */
	public static int zsg(String sql, Connection con, Object... A) {
		return zsg(null, 0, sql, con, A);
	}

	/**
	 * 根据rs自动获取行数
	 * 
	 * rs自动关闭
	 * 
	 * @param rs
	 * @return int
	 */
	public static int size(ResultSet rs) {

		CoreUtil.LogDao("获取size:");
		/* rs为空,直接返回0 */
		if (rs == null)
			return 0;
		int size = 0;
		try {
			/* 指针到最后一条,获取该行行数 */
			rs.last();
			size = rs.getRow();
			CoreUtil.LogDao("获取size:" + size + "成功");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			/* 关闭rs */
			rsClose(rs);
		}
		return size;
	}

	public static int size(String column, String table, String canseename) {
		CoreUtil.LogDao("获取size : " + table + "字段名 : " + column);
		String sql = "select COUNT(`" + column + "`) from `" + table + "` where `"+canseename+"`!=" + Configure.DELETEVALUE;
		return zsg(sql, null);
	}

	/**
	 * 关闭rs的公共方法
	 * 
	 * @param rs
	 */
	public static void rsClose(ResultSet rs) {
		try {
			/* 非空,且没有关闭 */
			if (rs != null && (!rs.isClosed()))
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据id,table,可见性删除某条记录
	 * 
	 * @param
	 * 
	 * @return int
	 */
	public static int delete(String id, String table, int c, Connection conn, String canseename, String idname,
			String offtimename) {

		/* 如果该Bean没有实现getTable,或者没有id信息,那么返回false */
		if (isEmpty(table) || isEmpty(id))
			return -1;
		BeanUtil.BeanPool.remove(id);// TODO 从缓存区移除
		String sql = "update `" + table + "` set `" + canseename + "`='" + c + "' where `" + idname + "`=?";
		CoreUtil.LogDao("假删除的sql语句构建为 : " + sql);
		int i = zsg(sql, conn, id);
		if (!isEmpty(offtimename)) {
			sql = "update `" + table + "` set `" + offtimename + "`=? where `" + idname + "`=?";
			CoreUtil.LogDao("尝试更新假删除的时间 : " + sql);
			zsg(sql, conn, new Date(), id);
		}
		CoreUtil.LogDao("假删除完成");
		return i;
	}

	/**
	 * 该方法已经过时
	 * 
	 * 但是不时也许还能用到
	 * 
	 * 暂时放在这里
	 * 
	 * @param re
	 * @return ArrayList<Object>
	 */
	@Deprecated
	public static ArrayList<Object> showRe(ResultSet rs) {
		ArrayList<Object> A = new ArrayList<>();
		ArrayList<String> field = new ArrayList<>();
		ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData();
			/* 放入ResultSetMetaData */
			A.add(rsmd);

			int a = rsmd.getColumnCount();
			boolean[] nul = new boolean[a];

			for (int i = 1; i <= a; i++) {
				field.add(rsmd.getColumnName(i));
				nul[i - 1] = rsmd.isNullable(i) == 1;
				if (rsmd.isAutoIncrement(i))
					/* 拿到主键名 String */
					A.add(rsmd.getColumnName(i));
			}
			/* =========拿到所有字段名的顺序ArralList */
			A.add(field);
			/* ======拿到所有字段是否可以是空值boolea[] */
			A.add(nul);
			while (rs.next()) {
				HashMap<String, Object> hm = new HashMap<>();
				for (int i = 1; i <= a; i++)
					hm.put(rsmd.getColumnName(i), rs.getObject(i));
				/* ===拿到所有的信息,每一条是一个hashmap */
				A.add(hm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return A;
	}

	static <O> boolean isEmpty(O o) {
		return o == null || o.equals("");
	}

	public static class connectPool {
		// TODO 线城池提供线程的方法,自带管理
		private static List<Connection> conpool = new Vector<>();
		private static List<Connection> conpoolout = new Vector<>();

		public synchronized static Connection getcon() throws SQLException {
			CoreUtil.LogDao("连接池剩余数量" + conpool.size());
			Connection c = null;
			long begintime = System.currentTimeMillis();
			while (true) {// 没有连接了就循环等待到上一个释放
				if (conpool.size() > 0) {
					c = conpool.remove(0);
					conpoolout.add(c);
					break;
				}
				if (System.currentTimeMillis() - begintime > Configure.LOSETIME)// 超出最大时间,返回null
					return null;
			}
			return c;
		}

		public static boolean begin(Connection con) {
			if (con != null) {
				try {
					PreparedStatement p = con.prepareStatement("begin");
					p.executeUpdate();
					p.close();
					CoreUtil.LogDao("事务begin:成功");
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

		public static boolean rollback(Connection con) {
			if (con != null) {
				try {
					con.rollback();
					CoreUtil.LogDao("事务rollback:成功");
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					back(con);
				}
			}
			return false;
		}

		public static boolean commit(Connection con) {
			if (con != null) {
				try {
					con.commit();
					CoreUtil.LogDao("事务commit:成功");
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					back(con);
				}
			}
			return false;

		}

		private synchronized static void back(Connection con) {

			CoreUtil.LogDao("连接池还一个连接 : " + con);
			conpoolout.remove(con);
			try {
				if (!con.isClosed()) {
					conpool.add(con);
					CoreUtil.LogDao("连接池还一个连接成功 : " + con);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			CoreUtil.LogDao("连接已关闭 : " + con);
			try {
				con = DriverManager.getConnection(Configure.URL, Configure.ROOTNAME, Configure.PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conpool.add(con);
			CoreUtil.LogDao("连接池还一个连接成功 : " + con);

		}
	}

}
