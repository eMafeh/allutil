package reflect.ctrip_dalbaiji;

import reflect.core.FieldSet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// cache field ships,use BiClass get cache
class ValueChangeHelper {
    private static final Map<BiClass, ValueChangeHelper[]> FIELDSHIP = new HashMap<>();
    private Field sourceField;
    private Field targetField;
    private SwitchWay way;

    private ValueChangeHelper(Field sourceField, Field targetField) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        try {
            way = SwitchWay.getWay(sourceField, targetField);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    static ValueChangeHelper[] getValueChangeHelper(BiClass biClass) {
        return FIELDSHIP.computeIfAbsent(biClass, a -> {
            FieldSet[] sourceBiSets = FieldSet.getBiSets(biClass.getSourceClass());
            FieldSet[] targetFieldSets = FieldSet.getBiSets(biClass.getTargetClass());
            List<ValueChangeHelper> list = new ArrayList<>();
            for (FieldSet sourceFieldSet : sourceBiSets)
                for (FieldSet targetFieldSet : targetFieldSets)
                    if (sourceFieldSet.getSourceName().equals(targetFieldSet.getTargetName())
                            && !Modifier.isFinal(targetFieldSet.getField().getModifiers())) {
                        list.add(new ValueChangeHelper(sourceFieldSet.getField(), targetFieldSet.getField()));
                        break;
                    }

            return list.toArray(new ValueChangeHelper[list.size()]);
        });
    }

    Field getSourceField() {
        return sourceField;
    }

    Field getTargetField() {
        return targetField;
    }

    SwitchWay getWay() {
        return way;
    }
}