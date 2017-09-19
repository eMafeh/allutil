package reflect.tostring;

import reflect.core.FieldSet;

/**
 * Created by QianRui on 2017/9/11.
 */
class ResultBuilder {

    static <E> String getString(E e, SupportWay way) {
        StringBuilder result = new StringBuilder();
        buildResult(e, result, way);
        return result.toString();
    }

    private static <E> void buildResult(E e, StringBuilder result, SupportWay way) {
        if (e == null) return;
        FieldSet[] biSets = FieldSet.getBiSets(e.getClass());
        for (FieldSet biSet : biSets) {
            try {
                if (!biSet.isInit()) {
                    if (!biSet.isIgnore())
                        way.getGoString().build(biSet.getSourceName(), biSet.getField().get(e), result);
                } else {
                    way.getBefore().build(biSet.getSourceName(), biSet.getField().get(e), result);
                    buildResult(biSet.getField().get(e), result, way);
                    way.getAfter().build(biSet.getSourceName(), biSet.getField().get(e), result);
                }
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }
}
