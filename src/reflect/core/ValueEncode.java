package reflect.core;

import org.apache.catalina.util.URLEncoder;

import java.nio.charset.Charset;

/**
 * Created by QianRui on 2017/9/10.
 */
public class ValueEncode {

    public static String httpencode(Object value) {
        return value == null ? null : URLEncoder.DEFAULT.encode(value.toString(), Charset.defaultCharset());
    }
}
