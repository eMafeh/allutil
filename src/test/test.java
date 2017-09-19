package test;

import bean.Goods;
import bean.Price;
import bean.ShopCar;
import bean.user;
import util.coreutil.BeanSQLBuffer;
import util.threadutil.LoopThread;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class test {
    String oo = "oo";

    public void oo() {
        System.out.println(oo = oo + 1);
    }

//    public static void main(String[] args) throws IntrospectionException, InterruptedException {
//        main();
//    }

    public void abc() throws InterruptedException {
        test tes = new test();
        LoopTank l = new LoopTank() {
            @Override
            public void run() {
                System.out.println("1嘿嘿");
            }
        };

        LoopTank ll = test::d;

        LoopThread lt = LoopThread.getLoopThread();
        lt.addLoopTankBySec(new test()::oo, 1, 0);
        lt.addLoopTankBySec(l, 1, -1);
        lt.addLoopTankBySec(ll, 1, 0);
        LoopTank tt = new LoopTank() {
            @Override
            public void run() {
                System.out.println(lt.runTime());
            }
        };
        Thread.sleep(3000);
        lt.clear();
        System.out.println("清空了");
        lt.addLoopTankBySec(tes::oo, 1, 0);
        System.out.println(lt.addLoopTankBySec(tt, 1, 0));
        System.out.println(lt.addLoopTankByHour(tt, 1, 0));
        LoopThread ltt = LoopThread.getLoopThread();
        System.out.println(lt.addLoopTankBySec(ll, 1, System.currentTimeMillis() + 5000));
        System.out.println(lt.addLoopTankBySec(new LoopTank() {
            @Override
            public void run() {
                System.out.println("3哈哈");
            }
        }, 2, -1));
        Thread.sleep(7000);
        tes.oo = "pp";
        System.out.println("移出 :" + lt.removeLoopTank(tt));
        System.out.println("移出 :" + lt.removeLoopTank(tt));
        System.out.println("移出 :" + lt.removeLoopTank(ll));

        Thread.sleep(7000);
        System.out.println("停止前1 : " + System.currentTimeMillis());
        System.out.println(lt.shutdown());
        System.out.println("停止前2 : " + System.currentTimeMillis());
        System.out.println("准备停止了");
        Thread.sleep(3000);
        System.out.println("停止前3 : " + System.currentTimeMillis());
        System.out.println("结束了");
        LoopThread lttt = LoopThread.getLoopThread(3);
        lttt.addLoopTankBySec(l, 1, System.currentTimeMillis() + 5000);
        System.out.println(lttt.size());

        Thread.sleep(8000);
        System.out.println(lttt.size());
//		lttt.shutdown();

    }

    public static void d() {
        System.out.println("2嗯嗯");
    }

    public void bb() throws IOException {
        Set<Class<?>> classes = FindClass.getClasses("");
//		  Enumeration<URL> resources =
//		  Thread.currentThread().getContextClassLoader().getResources("");
        classes.stream().forEach(System.out::println);
    }

    // 真正的javaBean对象的操作
    public void o() {
        user user = new user();
        user.setUsername("钱睿");
        user qr = (bean.user) user.getBeanByMessage().get(0);
        user.getBeans();
        user.getBeans();
        for (int i = 0; i < 2; i++) {

            List<ShopCar> sonBean = qr.getSonBean();
            System.out.println(sonBean);
            for (ShopCar shopCar : sonBean) {
                System.out.println(shopCar.getMainBean(user.class).toString());
                System.out.println(shopCar.getMainBean(Goods.class).toString());
                System.out.println(shopCar.getMainBean(Price.class));
            }

        }

    }

    public void a() throws IntrospectionException {
        for (int i = 0; i < 10; i++) {

            BeanSQLBuffer utilBean = BeanSQLBuffer.getUtilBean(user.class);
            user user = (bean.user) utilBean.getBeans().get(0);

            BeanInfo beanInfo = Introspector.getBeanInfo(user.class);

            Arrays.asList(beanInfo.getPropertyDescriptors()).stream().map(a -> {
                try {
                    return a.getReadMethod().invoke(user) + a.getName();
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return a;
            }).forEach(System.out::println);

        }
    }
}
