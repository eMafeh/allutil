package lab;

import util.coreutil.BeanSQLBuffer;

import java.util.*;
import java.util.stream.Collectors;

public class ContactShip {
	// 项目下所有的class
	private static Set<Class<?>> classs = FindClass.getClasses("");
	// 带有contactline的class
	private static Set<Class<?>> clas = classs.stream()
			.filter(a -> Arrays.asList(a.getInterfaces()).stream().filter(b -> b == ContactLine.class).count() == 1)
			.collect(Collectors.toSet());

	// 拿到所有的连接关系
	private static OneShip[] twoships;
	
	private static Set<Class<? extends BeanSQLBuffer>> beans = new HashSet<>();
	
	private static List<OneShip[]> allroad = new ArrayList<>();
	
	static {
		
		Set<OneShip> ships = new HashSet<>();
		
		clas.stream().forEach(c -> Arrays.asList(c.getDeclaredFields()).stream()
				.filter(a -> a.getType() == OneShip.class).forEach(a -> {
					try {
						a.setAccessible(true);
						OneShip o = (OneShip) a.get(c.newInstance());
						boolean b = true;
						for (OneShip one : ships)
							if (o.getCone() == o.getCtwo()
									|| (one != o && one.getCone() == o.getCone() && one.getCtwo() == o.getCtwo())) {
								System.out.println(
										"已跳过,对象关系配置错误或重复 : " + c + " Parameter : " + o.getCone() + o.getCtwo());
								b = false;
								break;
							}
						if (b)
							ships.add(o);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}));
		// 确定有多少来回的路
		List<OneShip> ships2 = new ArrayList<>();
		ships.stream()
				.forEach(a -> ships2.add(new OneShip(a.getCtwo(), a.getMethodtwo(), a.getCone(), a.getMethodone())));
		ships2.addAll(ships);
		twoships = new OneShip[ships2.size()];
		ships2.toArray(twoships);
		// 确定总共有多少bean
		ships.stream().forEach(a -> {
			beans.add(a.getCone());
			beans.add(a.getCtwo());
		});
	}
//
//	public static void main(String[] args) {
//		System.out.println(classs);
//		System.out.println();
//		System.out.println(clas);
//		System.out.println(Arrays.toString(twoships));
//		buildroad();
//		allroad.stream().forEach(a -> {System.out.println("又一条路");Arrays.asList(a).stream().forEach(System.out::println);});
//		System.out.println(allroad.size());
//	}

	private static void buildroad() {
		for (OneShip s : twoships) {
			OneShip[] n = new OneShip[] { s };
			allroad.add(n);
			realBuild(n);
		}
	}

	private static void realBuild(OneShip[] road) {

		int l = road.length;
		Class<? extends BeanSQLBuffer> ther = road[l - 1].getCtwo();
		m: for (OneShip s : twoships) {
			if (s.getCone() == ther) {

				for (OneShip oneShip : road)
					if (oneShip.getCone() == s.getCtwo())
						continue m;

				OneShip[] n = new OneShip[l + 1];
				for (int i = 0; i < l; i++)
					n[i] = road[i];
				n[l] = s;

				allroad.add(n);
				realBuild(n);
			}
		}
	}
}
