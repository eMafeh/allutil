package util;

public final class Configure {

	public static final String ROOTNAME = "root";// ���ݿ��û���
	public static final String PASSWORD = "102400";// ����
	public static final String DATABASE = "taobao";// ���ݿ���
	public static final String URL = "JDBC:mysql://localhost:3306/" + DATABASE + "?useUnicode=true&characterEncoding=utf-8";

	public static final String DRIVER = "com.mysql.jdbc.Driver";// ��������

	public static final int CONNUM = 2;// ���ӳ����������
	public static final long LOSETIME = 10000l;// ���ӳ����ȴ�ʱ��,����

	public static final boolean LOGSERVAIC = false;// servaic ��ӡ����
	public static final boolean LOGDAO = false;// dao���� ��ӡ����
	public static final boolean LOGBEAN = false;// bean���� ��ӡ����
	public static final boolean LOGVIEW = false;// ǰ�˲��� ��ӡ����
	public static final int DELETEVALUE = 1;// ɾ��״̬��ֵ
	public static final String STATENAME = "cansee";// ״̬�ֶ�����
	public static final String IDNAME = "id";// id�ֶ�����
}
