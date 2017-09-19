package reflect.tostring;

/**
 * Created by QianRui on 2017/9/10.
 */
public class BackValue {
    public static <E> String getkeyvalue(E e) {
        return ResultBuilder.getString(e, SupportWay.KEYVALUE);
    }

    public static <E> String getXML(E e) {

        return ResultBuilder.getString(e, SupportWay.XML);
    }


}
