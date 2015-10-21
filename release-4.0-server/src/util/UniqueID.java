package util;

public class UniqueID {
	static long current = System.currentTimeMillis();

	static public synchronized long get() {
		return current++;
	}
}
