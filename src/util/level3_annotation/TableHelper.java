package util.level3_annotation;

import util.Configure;

public class TableHelper {
	Class<?> clazz;
	TableValue tv;

	public TableHelper(Class<?> c) {
		clazz = c;
		tv = c.getDeclaredAnnotation(TableValue.class);
	}

	public String getTable() {
		return tv == null || "".equals(tv.table()) ? clazz.getSimpleName() : tv.table();
	}

	public String getOrderByField() {
		return tv == null || "".equals(tv.orderbyField()) ? null : tv.orderbyField();
	}

	public String getOfftimeField() {
		return tv == null || "".equals(tv.OfftimeField()) ? null : tv.OfftimeField();
	}

	public String getIdField() {
		return tv == null || "".equals(tv.IdField()) ? Configure.IDNAME : tv.IdField();
	}

	public String getState() {
		return tv == null || "".equals(tv.stateField()) ? Configure.STATENAME : tv.stateField();
	}
}
