package reflect;

import org.junit.Test;
import reflect.ctrip_dalbaiji.DalPlayWithBaiji;
import reflect.testpojo.FuncUser;
import reflect.testpojo.FuncUser2;
import reflect.testpojo.FuncUser3;
import reflect.tostring.BackValue;

import java.util.*;


public class TestTemp {
    @Test
    public void vackvalue() {

        FuncUser2 funcUser3 = new FuncUser2();
        funcUser3.setFunId("1");
        funcUser3.setUsername(2);
        funcUser3.setTest(1l);
        FuncUser funcUser = new FuncUser();
        funcUser.setFunId(1);
        funcUser3.setFuncUser(funcUser);

        System.out.println(funcUser3.getClass().getSuperclass().getDeclaredFields().length);

        String getkeyvalue = BackValue.getkeyvalue(funcUser3);
        System.out.println(getkeyvalue);
        String xml = BackValue.getXML(funcUser3);
        System.out.println(xml);
    }

    private static void test2() {

        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(null);
        System.out.println(arrayList);
    }

    static void test() {
        Map<String, String> h = new HashMap<>();
        h.put("1", "2");
        h.keySet().stream().forEach(a -> System.out.println(h.get(a)));
    }

    static void test3() {
        HashMap<String, Set<Integer>> hashMap = new HashMap<>();
        for (int j = 0; j < 5; j++) {

            for (int i = 0; i < 5; i++) {
                hashMap.computeIfAbsent(j + "", k -> new HashSet<>()).add(i);
            }
        }
        System.out.println(hashMap);
    }

    public static void TEST_PALY() throws IllegalAccessException, InstantiationException {
        FuncUser3 funcUser3 = new FuncUser3();
        funcUser3.setFunId("1");
        funcUser3.setUsername(2);
        System.out.println(funcUser3);
        FuncUser2 funcUser2 = DalPlayWithBaiji.typeTo(funcUser3, FuncUser2.class);
        System.out.println(funcUser2);
        FuncUser funcUser = DalPlayWithBaiji.typeTo(funcUser2, FuncUser.class);
        System.out.println(funcUser);
        for (int i = 0; i < 10; i++) {
            funcUser3 = DalPlayWithBaiji.typeTo(funcUser, FuncUser3.class);
            System.out.println(funcUser3);
            funcUser2 = DalPlayWithBaiji.typeTo(funcUser3, FuncUser2.class);
            System.out.println(funcUser2);
            funcUser = DalPlayWithBaiji.typeTo(funcUser2, FuncUser.class);
            System.out.println(funcUser);
        }

    }
}
