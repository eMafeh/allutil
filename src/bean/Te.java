package bean;

import util.coreutil.BeanSQLBuffer;

public class Te implements BeanSQLBuffer{
private String id;
private String ����;
private String fid;
@Override
public String getId() {
	return id;
}
@Override
public void setId(String id) {
	this.id=id;
	
}
@Override
public String toString() {
	return "Te [id=" + id + ", ����=" + ���� + ", fid=" + fid + "]";
}
public String get����() {
	return ����;
}
public void set����(String ����) {
	this.���� = ����;
}
public String getFid() {
	return fid;
}
public void setFid(String fid) {
	this.fid = fid;
}
}
