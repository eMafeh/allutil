package bean;

import util.coreutil.BeanSQLBuffer;

public class Te implements BeanSQLBuffer{
private String id;
private String 名字;
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
	return "Te [id=" + id + ", 名字=" + 名字 + ", fid=" + fid + "]";
}
public String get名字() {
	return 名字;
}
public void set名字(String 名字) {
	this.名字 = 名字;
}
public String getFid() {
	return fid;
}
public void setFid(String fid) {
	this.fid = fid;
}
}
