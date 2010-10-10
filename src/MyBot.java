import java.io.FileWriter;
import java.io.IOException;


public class MyBot {
	/**
	 * Log file for game state and orders in debug mode
	 */
	private static FileWriter debugLog = null;

	public static void doTurn(PlanetWars pw) {
		// (1) If we currently have a fleet in flight, just do nothing.
		if (pw.getMyFleets().size() >= 1) {
			return;
		}
		// (2) Find my strongest planet.
		Planet source = null;
		double sourceScore = Double.MIN_VALUE;
		for (Planet p : pw.getMyPlanets()) {
			double score = p.numShips;
			if (score > sourceScore) {
				sourceScore = score;
				source = p;
			}
		}
		// (3) Find the weakest enemy or neutral planet.
		Planet dest = null;
		double destScore = Double.MIN_VALUE;
		for (Planet p : pw.getNotMyPlanets()) {
			double score = 1.0 / (1 + p.numShips);
			if (score > destScore) {
				destScore = score;
				dest = p;
			}
		}
		// (4) Send half the ships from my strongest planet to the weakest
		// planet that I do not own.
		if (source != null && dest != null) {
			int numShips = source.numShips / 2;
			pw.issueOrder(source, dest, numShips);
		}
	}

	/**
	 * Open log file in debug mode
	 */
	private static void openDebugLog(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if ((args[i].equals("--debug") || args[i].equals("-d"))) {
				try {
					debugLog = new FileWriter("build/debugLog.txt");
				} catch (IOException ioe) {
				}
			}
		}
	}

	/**
	 * Close log file in debug mode
	 */
	private static void closeDebugLog() {
		if (debugLog != null) {
			try {
				debugLog.flush();
				debugLog.close();
			} catch (IOException ioe) {
			}
			debugLog = null;
		}
	}

	public static void log(String s) {
		if (debugLog != null) {
			try {
				debugLog.write(s + "\n");
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) {
		openDebugLog(args);

		String line = "";
		String message = "";
		int c;
		int turn = 1;
		try {
			while ((c = System.in.read()) >= 0) {
				switch (c) {
					case '\n':
						if (line.equals("go")) {
							log("Turn " + turn++);
							long t = System.currentTimeMillis();

							PlanetWars pw = new PlanetWars(message);

							log("parsed game data in " + (System.currentTimeMillis() - t) + "ms");
							t = System.currentTimeMillis();

							doTurn(pw);

							log("issued orders in " + (System.currentTimeMillis() - t) + "ms");
							pw.finishTurn();
							message = "";
						} else {
							message += line + "\n";
						}
						line = "";
						break;
					default:
						line += (char)c;
						break;
				}
			}
		} catch (Exception e) {
			log(e.toString());
			for (StackTraceElement s : e.getStackTrace()) {
				log(s.toString());
			}
		} finally {
			closeDebugLog();
		}
	}
}

