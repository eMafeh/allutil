package reflect.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//package field to FieldSet
public class FieldSet {
    private static final Map<Class<?>, FieldSet[]> MAPS = new HashMap<>();
    private Field field;
    private String sourceName;
    private String targetName;
    private boolean init;
    private boolean ignore;


    private FieldSet(Field field) {
        this.field = field;
        field.setAccessible(true);
        doInitAgain();
        doValueShip();
    }

    private void doInitAgain() {
        InitValue initAgain = field.getAnnotation(InitValue.class);
        if (initAgain == null)
            return;
        if (initAgain.value()) init = ignore = true;
        else {
            init = false;
            ignore = initAgain.ignore();
        }

    }

    private void doValueShip() {
        ValueShip valueMap = field.getAnnotation(ValueShip.class);
        if (valueMap == null) {
            sourceName = targetName = field.getName();
        } else {
            sourceName = "".equals(valueMap.sourceName())
                    ? "".equals(valueMap.value()) ? field.getName() : valueMap.value() : valueMap.sourceName();
            targetName = "".equals(valueMap.targetName())
                    ? "".equals(valueMap.value()) ? field.getName() : valueMap.value() : valueMap.targetName();
        }
    }

    public static FieldSet[] getBiSets(Class<?> clazz) {
        return MAPS.computeIfAbsent(clazz, a -> {
            List<FieldSet> fieldSets = new ArrayList<>();
            setBiSets(clazz, fieldSets);
            return fieldSets.toArray(new FieldSet[fieldSets.size()]);
        });
    }

    private static void setBiSets(Class<?> clazz, List<FieldSet> fieldSets) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
            fieldSets.add(new FieldSet(field));
        if (!clazz.getSuperclass().equals(Object.class))
            setBiSets(clazz.getSuperclass(), fieldSets);
    }

    public Field getField() {
        return field;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public boolean isInit() {
        return init;
    }

    public boolean isIgnore() {
        return ignore;
    }
}