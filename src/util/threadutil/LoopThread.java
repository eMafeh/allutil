package util.threadutil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class LoopThread implements LoopThreadable {
	private boolean flag;
	private Future<String> future;
	private int outtimes;
	private int times;
	private ExecutorService ne = Executors.newSingleThreadExecutor();
	private final Callable<String> c = new Callable<String>() {
		@Override
		public String call() throws Exception {
			while (flag) {
				onehour();
			}
			return "LoopTank正常结束了";
		}
	};

	private long BEGIN;
	private static final LoopThread LOOPTHREAD = new LoopThread();

	private LoopThread() {
	}

	public static LoopThread getLoopThread() {
		return getLoopThread(10);
	}

	public synchronized static LoopThread getLoopThread(int outtimes) {
		if (LOOPTHREAD.future == null || LOOPTHREAD.ne.isShutdown()) {
			LOOPTHREAD.BEGIN = System.currentTimeMillis();
			LOOPTHREAD.outtimes = outtimes;
			LOOPTHREAD.flag = true;
			LOOPTHREAD.clear();
			LOOPTHREAD.ne = Executors.newSingleThreadExecutor();
			LOOPTHREAD.future = LOOPTHREAD.ne.submit(LOOPTHREAD.c);
		}
		return LOOPTHREAD;
	}

	private void onehour() {
		LoopTanker.LoopTankerStream(LoopTanker.hourtank);
		long end = System.currentTimeMillis() + 3600000;
		while (end > System.currentTimeMillis() && flag)
			oneSecond();

	}

	private void oneSecond() {
		try {
			LoopTanker.LoopTankerStream(LoopTanker.secondtank);
			ne.awaitTermination(1, TimeUnit.SECONDS);
			if (size() != 0) {
				times = 0;
				return;
			}
			times++;
			if (times >= outtimes) {
				flag = false;
				ne.shutdown();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	private static class LoopTanker implements Runnable {
		private Long begin;
		private Long end;
		private int step;
		private Long last;
		private Runnable runnable;
		private static final Set<LoopTanker> secondtank = new HashSet<>();
		private static final Set<LoopTanker> hourtank = new HashSet<>();

		private LoopTanker(Long end, int step, Runnable runnable) {
			last = begin = System.currentTimeMillis();
			this.end = end;
			this.step = step;
			this.runnable = runnable;
		}

		private static void LoopTankerStream(Set<LoopTanker> set) {
			long now = System.currentTimeMillis();
			Set<LoopTanker> s = new HashSet<>();
			set.stream().filter(a -> (a.end > 0 && a.end < now)).forEach(a -> s.add(a));
			set.removeAll(s);

			set.stream().filter(a -> ((a.last + a.step) <= now)).forEach(a -> {
				a.run();
				a.last = now;
			});
		}

		private static LoopTanker getLoopTanker(Runnable runnable) {
			long now = System.currentTimeMillis();
			LoopTanker lt = null;
			for (LoopTanker loopTanker : hourtank) {
				if (loopTanker.end > 0 && loopTanker.end < now) {
					hourtank.remove(loopTanker);
					continue;
				}

				if (loopTanker.runnable == runnable) {
					lt = loopTanker;
					break;
				}
			}
			for (LoopTanker loopTanker : secondtank) {
				if (loopTanker.end > 0 && loopTanker.end < now) {
					secondtank.remove(loopTanker);
					continue;
				}

				if (loopTanker.runnable == runnable) {
					lt = loopTanker;
					break;
				}
			}
			return lt;
		}

		@Override
		public void run() {
			runnable.run();
		}
	}

	@Override
	public synchronized boolean removeLoopTank(Runnable runnable) {
		for (LoopTanker loopTanker : LoopTanker.hourtank)
			if (loopTanker.runnable == runnable)
				return LoopTanker.hourtank.remove(loopTanker);

		for (LoopTanker loopTanker : LoopTanker.secondtank)
			if (loopTanker.runnable == runnable)
				return LoopTanker.secondtank.remove(loopTanker);

		return false;
	}

	@Override
	public boolean addLoopTankBySec(Runnable runnable, int second, long end) {
		return addLoopTank(runnable, second, end, 1000, LoopTanker.secondtank);
	}

	@Override
	public boolean addLoopTankByHour(Runnable runnable, int hour, long end) {
		return addLoopTank(runnable, hour, end, 3600000, LoopTanker.hourtank);
	}

	private synchronized boolean addLoopTank(Runnable runnable, int hour, long end, int d, Set<LoopTanker> set) {
		if (hour <= 0)
			return false;
		if (end > 0 && end < System.currentTimeMillis())
			return false;
		if (LoopTanker.getLoopTanker(runnable) != null)
			return false;
		LoopTanker l = new LoopTanker(end, hour * d, runnable);
		return set.add(l);
	}

	@Override
	public String shutdown() {
		flag = false;
		clear();
		ne.shutdown();
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long runTime() {
		return System.currentTimeMillis() - BEGIN;
	}

	@Override
	public long runTime(Runnable runnable) {
		LoopTanker loopTanker = LoopTanker.getLoopTanker(runnable);
		if (loopTanker == null)
			return -1;
		return System.currentTimeMillis() - loopTanker.begin;
	}

	@Override
	public synchronized void clear() {
		LoopTanker.hourtank.clear();
		LoopTanker.secondtank.clear();
	}

	@Override
	public int size() {
		return LoopTanker.hourtank.size() + LoopTanker.secondtank.size();
	}

}
