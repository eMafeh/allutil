package reflect.ctrip_dalbaiji;

import java.lang.reflect.Field;

//short char boolean float double byte
enum SwitchWay {
    N, CalToDate, DateToCal, CalToTime, DateToTime, ToString, ToInt, ToLong, ToShort, ToDouble, ToBigDecimal;

    static SwitchWay getWay(Field sourceField, Field targetField)
            throws IllegalAccessException, InstantiationException {
        String typeName = sourceField.getType().getTypeName();
        String targetTypeName = targetField.getType().getTypeName();

        if (typeName.equals(targetTypeName))
            return N;

        if ("java.lang.String".equals(targetTypeName))
            return ToString;

        if ("long".equals(targetTypeName) || "java.lang.Long".equals(targetTypeName))
            return ToLong;

        if ("int".equals(targetTypeName) || "java.lang.Integer".equals(targetTypeName))
            return ToInt;

        if ("java.math.BigDecimal".equals(targetTypeName)) {
            return ToBigDecimal;
        }
        if ("short".equals(targetTypeName) || "java.lang.Short".equals(targetTypeName)) {
            return ToShort;
        }
        if ("double".equals(targetTypeName) || "java.lang.Double".equals(targetTypeName)) {
            return ToDouble;
        }

        if (!("java.util.Calendar".equals(typeName) || "java.sql.Timestamp".equals(typeName)
                || "java.util.Date".equals(typeName)))
            return N;
        if ("java.util.Calendar".equals(targetTypeName)) {
            if ("java.util.Calendar".equals(typeName))
                return N;
            if ("java.sql.Timestamp".equals(typeName) || "java.util.Date".equals(typeName))
                return DateToCal;
        }
        if ("java.sql.Timestamp".equals(targetTypeName)) {
            if ("java.util.Calendar".equals(typeName))
                return CalToTime;
            if ("java.sql.Timestamp".equals(typeName))
                return N;
            return DateToTime;
        }
        if ("java.util.Date".equals(targetTypeName)) {
            if ("java.util.Calendar".equals(typeName))
                return CalToDate;
            return N;
        }
        throw new IllegalAccessException("sourcefield : " + sourceField + " targetfield : " + targetField);
    }
}