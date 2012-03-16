package foocityBackend;

public class FooLogger {
	public final static int RELEASE_LOG = 0;
	public final static int ERROR_LOG = 1;
	public final static int WARN_LOG = 2;
	public final static int INFO_LOG = 3;

	// set MAX_ERROR to the level of logging that you would like to display
	public final static int MAX_ERROR = RELEASE_LOG;

	public static void releaseLog(String Message) {
		Log(Message, RELEASE_LOG);
	}

	public static void errorLog(String Message) {
		Log(Message, ERROR_LOG);
	}

	public static void warnLog(String Message) {
		Log(Message, WARN_LOG);
	}

	public static void infoLog(String Message) {
		Log(Message, INFO_LOG);
	}

	static void Log(String Message, int errorLevel) {
		if (errorLevel <= MAX_ERROR) {
			System.out.println(Message);
		}
	}
	
	public static void printMetric(FooCityManager map, int metric){
		for (int y = 0; y < map.getMapArea().getHeight(); y++) {
			for (int x = 0; x < map.getMapArea().getWidth(); x++){
				infoLog(Integer.toString(map.getTileMetrics(x, y, metric)) + " ");
			}
			infoLog("\n");
		}
	}
}
