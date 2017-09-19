package reflect.ctrip_dalbaiji;

import java.util.ArrayList;
import java.util.List;

public class DalPlayWithBaiji {


    // source value insert target;
    public static <E, T> T typeTo(E e, Class<T> t) throws IllegalAccessException, InstantiationException {
        if (e == null || t == null)
            return null;

        T tInstance = t.newInstance();

        //source and target for biclass
        BiClass biClass = BiClass.getBiClass(e.getClass(), t);

        //getValueHelper
        ValueChangeHelper[] valueChangeHelpers = ValueChangeHelper.getValueChangeHelper(biClass);
        for (ValueChangeHelper valueChangeHelper : valueChangeHelpers)
            //setValue
            SetValueFactory.setValue(e, tInstance, valueChangeHelper.getSourceField(), valueChangeHelper.getTargetField(), valueChangeHelper.getWay());

        return tInstance;
    }

    // sourceList value insert targetList ;
    public static <E, T> List<T> typeToList(List<E> e, Class<T> t)
            throws IllegalAccessException, InstantiationException {
        List<T> ts = new ArrayList<>();
        if (e == null || e.size() == 0 || t == null)
            return ts;
        for (E source : e)
            ts.add(typeTo(source, t));
        return ts;
    }

}