package reflect.ctrip_dalbaiji;

import java.util.ArrayList;
import java.util.List;

// source and terget define BiClass
class BiClass {
    private static final List<BiClass> BICLASS = new ArrayList<>();

    private Class<?> sourceClass;
    private Class<?> targetClass;

    private BiClass(Class<?> sourceClass, Class<?> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    static <E, T> BiClass getBiClass(Class<E> source, Class<T> target) {
        for (BiClass biClass : BICLASS)
            if (biClass.sourceClass.equals(source) && biClass.targetClass.equals(target))
                return biClass;
        synchronized (BiClass.class) {
            for (BiClass biClass : BICLASS)
                if (biClass.sourceClass.equals(source) && biClass.targetClass.equals(target))
                    return biClass;
            BiClass biClass = new BiClass(source, target);
            BICLASS.add(biClass);
            return biClass;
        }

    }

    Class<?> getSourceClass() {
        return sourceClass;
    }

    Class<?> getTargetClass() {
        return targetClass;
    }

}