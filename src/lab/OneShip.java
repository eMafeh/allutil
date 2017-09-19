package lab;

import util.coreutil.BeanSQLBuffer;

public class OneShip {
private Class<? extends BeanSQLBuffer> cone;
private String methodone;
private Class<? extends BeanSQLBuffer> ctwo;
private String methodtwo;
public OneShip(Class<? extends BeanSQLBuffer> cone, String methodone, Class<? extends BeanSQLBuffer> ctwo, String methodtwo) {
	this.cone = cone;
	this.methodone = methodone;
	this.ctwo = ctwo;
	this.methodtwo = methodtwo;
}
@Override
public String toString() {
	return "OneShip [cone=" + cone + ", methodone=" + methodone + ", ctwo=" + ctwo + ", methodtwo=" + methodtwo + "]";
}
public Class<? extends BeanSQLBuffer> getCone() {
	return cone;
}
public void setCone(Class<? extends BeanSQLBuffer> cone) {
	this.cone = cone;
}
public String getMethodone() {
	return methodone;
}
public void setMethodone(String methodone) {
	this.methodone = methodone;
}
public Class<? extends BeanSQLBuffer> getCtwo() {
	return ctwo;
}
public void setCtwo(Class<? extends BeanSQLBuffer> ctwo) {
	this.ctwo = ctwo;
}
public String getMethodtwo() {
	return methodtwo;
}
public void setMethodtwo(String methodtwo) {
	this.methodtwo = methodtwo;
}
}
