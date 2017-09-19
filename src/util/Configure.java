package util;

public final class Configure {

	public static final String ROOTNAME = "root";// 数据库用户名
	public static final String PASSWORD = "102400";// 密码
	public static final String DATABASE = "taobao";// 数据库名
	public static final String URL = "JDBC:mysql://localhost:3306/" + DATABASE + "?useUnicode=true&characterEncoding=utf-8";

	public static final String DRIVER = "com.mysql.jdbc.Driver";// 驱动类名

	public static final int CONNUM = 2;// 连接池最大连接数
	public static final long LOSETIME = 10000l;// 连接池最大等待时间,毫秒

	public static final boolean LOGSERVAIC = false;// servaic 打印开启
	public static final boolean LOGDAO = false;// dao操作 打印开启
	public static final boolean LOGBEAN = false;// bean操作 打印开启
	public static final boolean LOGVIEW = false;// 前端操作 打印开启
	public static final int DELETEVALUE = 1;// 删除状态的值
	public static final String STATENAME = "cansee";// 状态字段名字
	public static final String IDNAME = "id";// id字段名字
}
