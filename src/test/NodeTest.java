package test;

import bean.Te;
import util.coreutil.BeanSQLBuffer;
import util.coreutil.SQLUtil;
import util.threadutil.LoopThread;

import java.util.*;
import java.util.stream.Collectors;

public class NodeTest {
	static Set<Te> beans;
	static Te utilBean;
	static Random random = new Random();
	static {
		SQLUtil.zsg("use qqtest", null);
		utilBean = BeanSQLBuffer.getUtilBean(Te.class);
	}

//	public static void main(String[] args) {
//System.out.println(Integer.toBinaryString(16));
//		b();
//		a();
//		try{String s = null;System.out.println(s.toString());}catch(NullPointerException e){
//			System.out.println(1);
//		}catch(Exception e){System.out.println(2);}
//		List<NodeTest> a = new ArrayList<>();
//		a = new ArrayList<>(new HashSet<>(a));
//		a = a.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
		// System.out.println(((NodeTest)null).c("12"));
//	}

	public String c(String s) {
		char[] c = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean b = false;
		for (int i = 0; i < c.length; i++) {
			if (b)
				if (c[i] != ' ')
					sb.append(' ');
				else
					continue;

			if (!(b = c[i] == ' ')) {
				sb.append(c[i]);

			}
		}
		return sb.toString();
	}

	public static void b() {
		int i = 0;
		class ti {
			int i;
		}
		ti ti = new ti();
		System.out.println(System.currentTimeMillis());
		LoopThread loopThread = LoopThread.getLoopThread();
		LoopTank loopTank = new LoopTank() {

			@Override
			public void run() {
				System.out.println(ti.i);
			}
		};
		loopThread.addLoopTankBySec(loopTank, 5, 0);
		for (; i < 1000; i++) {
			ti.i = i;
			ArrayList<Te> b = utilBean.getBeans();
			Te te = b.get(random.nextInt(b.size()));
			Te t = new Te();
			t.setFid(te.getId());
			t.set名字(te.get名字() + random.nextInt(10));
			t.BeanzsgNotDel();
		}
		System.out.println(System.currentTimeMillis());
		loopThread.removeLoopTank(loopTank);
		System.out.println(System.currentTimeMillis());
	}

	public static void a() {

		ArrayList<Te> b = utilBean.getBeans();
		beans = b.stream().collect(Collectors.toSet());
		List<Te> list = b.stream().filter(a -> a.getFid() == null).collect(Collectors.toList());
		// Te texun = utilBean.getBean("15");
		long time = System.currentTimeMillis();
		for (Te te : list) {
			System.out.println(te.get名字());
			showname(te, te.get名字().length() + 1);
		}
		System.out.println(System.currentTimeMillis() - time);
	}

	public static void showname(Te te, int length) {
		List<Te> collect = beans.stream().filter(a -> te.getId().equals(a.getFid())).collect(Collectors.toList());
		if (collect.size() == 0) {
			System.out.println();
			return;
		}
		beans.remove(collect);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(" ");
		}
		for (int i = 0; i < collect.size(); i++) {
			Te te2 = collect.get(i);
			if (i != 0)
				System.out.print(sb);
			System.out.print("-" + te2.get名字());
			showname(te2, length + 2 * te2.get名字().length() + 1);
		}
	}
}
