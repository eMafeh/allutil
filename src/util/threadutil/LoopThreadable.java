package util.threadutil;

public interface LoopThreadable {
	boolean removeLoopTank(Runnable runnable);

	boolean addLoopTankBySec(Runnable runnable, int step, long end);

	boolean addLoopTankByHour(Runnable runnable, int step, long end);

	String shutdown();

	long runTime();

	long runTime(Runnable runnable);

	void clear();

	int size();
}
