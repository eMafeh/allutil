package reflect.ctrip_dalbaiji;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


class SetValueFactory {

    // 这部分后续改进为switch工厂包含在映射中
    static void setValue(Object source, Object target, Field sourceField, Field targetField, SwitchWay way)
            throws IllegalAccessException, InstantiationException {
        Object o = sourceField.get(source);
        if (o == null)
            return;

        switch (way) {
            case N:
                targetField.set(target, o);
                break;
            case DateToCal:
                Calendar instance = Calendar.getInstance();
                instance.setTime((Date) o);
                targetField.set(target, instance);
                break;
            case CalToTime:
                targetField.set(target, new Timestamp(((Calendar) o).getTime().getTime()));
                break;
            case DateToTime:
                targetField.set(target, new Timestamp(((Date) o).getTime()));
                break;
            case CalToDate:
                targetField.set(target, ((Calendar) o).getTime());
                break;
            case ToString:
                targetField.set(target, o.toString());
                break;
            case ToInt:
                targetField.set(target, Integer.parseInt(o.toString()));
                break;
            case ToLong:
                targetField.set(target, Long.parseLong(o.toString()));
                break;
            case ToBigDecimal:
                targetField.set(target, new BigDecimal(o.toString()));
                break;
            case ToShort:
                targetField.set(target, Short.parseShort(o.toString()));
                break;
            case ToDouble:
                targetField.set(target, Double.parseDouble(o.toString()));
                break;
            default:
                break;
        }

    }

}
