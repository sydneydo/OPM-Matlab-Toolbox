package exportedAPI.opcatAPIx;

public interface IXCheckResult {
	public final static int RIGHT = 1;
	public final static int WRONG = -1;
	public final static int WARNING = 0;

	public int getResult();

	public String getMessage();
}
