package reflect.tostring;

import reflect.core.ValueEncode;

/**
 * Created by QianRui on 2017/9/11.
 */
class DetailBuild {
    static void xmlstring(String field, Object value, StringBuilder result) {
        result.append("<").append(field).append(">").append(value).append("</").append(field).append(">");
    }

    static void xmlbefore(String field, Object value, StringBuilder result) {
        result.append("<").append(field).append(">");
    }

    static void xmlafter(String field, Object value, StringBuilder result) {
        result.append("</").append(field).append(">");
    }

    static void kvstring(String field, Object value, StringBuilder result) {
        result.append(result.length() == 0 ? "" : "&").append(ValueEncode.httpencode(field)).append("=").append(ValueEncode.httpencode(value));
    }

    static void donot(String field, Object value, StringBuilder result) {
    }
}
