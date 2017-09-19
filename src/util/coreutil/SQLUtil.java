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
	 * ������con,ֻ��һ��
	 */
	private static Connection con;

	/**
	 * ��query���ú�,Ҫ���ⲿ�ֶ��ر�
	 */

	private static PreparedStatement pst;

	/**
	 * Ϊcon��ʼ��
	 */
	static {
		CoreUtil.LogDao("���ݿ����Ӵ�����ʼ");
		try {
			Class.forName(Configure.DRIVER);
			con = DriverManager.getConnection(Configure.URL, Configure.ROOTNAME, Configure.PASSWORD);
			for (int i = 0; i < Configure.CONNUM; i++) {
				Connection con = DriverManager.getConnection(Configure.URL, Configure.ROOTNAME,
						Configure.PASSWORD);
				connectPool.conpool.add(con);
			}
			CoreUtil.LogDao("���ݿ�����ȫ�������ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ������query, ����rs��Դͷ
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
	 * �������ݿ����ӵ�Ψһ���
	 */
	private synchronized static Object pstdid(InputStream is, long size, String sql, Object... A) throws SQLException {
		CoreUtil.LogDao("���ݿ��sql��� : " + sql);
		CoreUtil.LogDao("���ݿ�Ĳ��� : ");
		CoreUtil.syso(A, 1);
		/* �ر���һ��pst */
		if (pst != null && !pst.isClosed())
			pst.close();
		/* ���ɻ������ */
		pst = con.prepareStatement(sql);
		if ("s".equalsIgnoreCase(sql.charAt(0) + "")) {
			int i = 1;
			if (A != null)
				for (Object o : A)
					pst.setObject(i++, o);
			ResultSet rs = pst.executeQuery();
			CoreUtil.LogDao("���ݿ���ҳɹ�");
			return rs;
		} else {
			int i = didzsg(is, size, pst, A);
			return i;
		}
	}

	/*
	 * ��ɾ�ĵ����
	 */
	private static int didzsg(InputStream is, long size, PreparedStatement pstt, Object... A) throws SQLException {
		int t = -1;// �������
		int i = 1;// ��������

		/* ������� */
		if (A != null)
			for (Object o : A)
				pstt.setObject(i++, o);

		/* ��������������� */
		if (is != null && size != 0)
			pstt.setBinaryStream(i, is, size);

		/* ���в��� */
		t = pstt.executeUpdate();
		CoreUtil.LogDao("���ݿ�updata:�ɹ�");
		return t;
	}

	/**
	 * ������zsg
	 * 
	 * @param sql
	 * @param A
	 * @param is
	 * @param size
	 * @return int
	 */
	public static int zsg(InputStream is, long size, String sql, Connection conn, Object... A) {
		try {
			if (conn == null) // û��ר������,ʹ�ù��õ�
				return (int) pstdid(is, size, sql, A);
			// TODO ����ģʽ,����begin��commit
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
	 * �ٲ�����zsg����,���ӳ��ṩ
	 * 
	 * @param sql
	 * @param A
	 * @return int
	 */
	public static int zsg(String sql, Connection con, Object... A) {
		return zsg(null, 0, sql, con, A);
	}

	/**
	 * ����rs�Զ���ȡ����
	 * 
	 * rs�Զ��ر�
	 * 
	 * @param rs
	 * @return int
	 */
	public static int size(ResultSet rs) {

		CoreUtil.LogDao("��ȡsize:");
		/* rsΪ��,ֱ�ӷ���0 */
		if (rs == null)
			return 0;
		int size = 0;
		try {
			/* ָ�뵽���һ��,��ȡ�������� */
			rs.last();
			size = rs.getRow();
			CoreUtil.LogDao("��ȡsize:" + size + "�ɹ�");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			/* �ر�rs */
			rsClose(rs);
		}
		return size;
	}

	public static int size(String column, String table, String canseename) {
		CoreUtil.LogDao("��ȡsize : " + table + "�ֶ��� : " + column);
		String sql = "select COUNT(`" + column + "`) from `" + table + "` where `"+canseename+"`!=" + Configure.DELETEVALUE;
		return zsg(sql, null);
	}

	/**
	 * �ر�rs�Ĺ�������
	 * 
	 * @param rs
	 */
	public static void rsClose(ResultSet rs) {
		try {
			/* �ǿ�,��û�йر� */
			if (rs != null && (!rs.isClosed()))
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����id,table,�ɼ���ɾ��ĳ����¼
	 * 
	 * @param
	 * 
	 * @return int
	 */
	public static int delete(String id, String table, int c, Connection conn, String canseename, String idname,
			String offtimename) {

		/* �����Beanû��ʵ��getTable,����û��id��Ϣ,��ô����false */
		if (isEmpty(table) || isEmpty(id))
			return -1;
		BeanUtil.BeanPool.remove(id);// TODO �ӻ������Ƴ�
		String sql = "update `" + table + "` set `" + canseename + "`='" + c + "' where `" + idname + "`=?";
		CoreUtil.LogDao("��ɾ����sql��乹��Ϊ : " + sql);
		int i = zsg(sql, conn, id);
		if (!isEmpty(offtimename)) {
			sql = "update `" + table + "` set `" + offtimename + "`=? where `" + idname + "`=?";
			CoreUtil.LogDao("���Ը��¼�ɾ����ʱ�� : " + sql);
			zsg(sql, conn, new Date(), id);
		}
		CoreUtil.LogDao("��ɾ�����");
		return i;
	}

	/**
	 * �÷����Ѿ���ʱ
	 * 
	 * ���ǲ�ʱҲ�����õ�
	 * 
	 * ��ʱ��������
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
			/* ����ResultSetMetaData */
			A.add(rsmd);

			int a = rsmd.getColumnCount();
			boolean[] nul = new boolean[a];

			for (int i = 1; i <= a; i++) {
				field.add(rsmd.getColumnName(i));
				nul[i - 1] = rsmd.isNullable(i) == 1;
				if (rsmd.isAutoIncrement(i))
					/* �õ������� String */
					A.add(rsmd.getColumnName(i));
			}
			/* =========�õ������ֶ�����˳��ArralList */
			A.add(field);
			/* ======�õ������ֶ��Ƿ�����ǿ�ֵboolea[] */
			A.add(nul);
			while (rs.next()) {
				HashMap<String, Object> hm = new HashMap<>();
				for (int i = 1; i <= a; i++)
					hm.put(rsmd.getColumnName(i), rs.getObject(i));
				/* ===�õ����е���Ϣ,ÿһ����һ��hashmap */
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
		// TODO �߳ǳ��ṩ�̵߳ķ���,�Դ�����
		private static List<Connection> conpool = new Vector<>();
		private static List<Connection> conpoolout = new Vector<>();

		public synchronized static Connection getcon() throws SQLException {
			CoreUtil.LogDao("���ӳ�ʣ������" + conpool.size());
			Connection c = null;
			long begintime = System.currentTimeMillis();
			while (true) {// û�������˾�ѭ���ȴ�����һ���ͷ�
				if (conpool.size() > 0) {
					c = conpool.remove(0);
					conpoolout.add(c);
					break;
				}
				if (System.currentTimeMillis() - begintime > Configure.LOSETIME)// �������ʱ��,����null
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
					CoreUtil.LogDao("����begin:�ɹ�");
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
					CoreUtil.LogDao("����rollback:�ɹ�");
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
					CoreUtil.LogDao("����commit:�ɹ�");
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

			CoreUtil.LogDao("���ӳػ�һ������ : " + con);
			conpoolout.remove(con);
			try {
				if (!con.isClosed()) {
					conpool.add(con);
					CoreUtil.LogDao("���ӳػ�һ�����ӳɹ� : " + con);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			CoreUtil.LogDao("�����ѹر� : " + con);
			try {
				con = DriverManager.getConnection(Configure.URL, Configure.ROOTNAME, Configure.PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conpool.add(con);
			CoreUtil.LogDao("���ӳػ�һ�����ӳɹ� : " + con);

		}
	}

}
