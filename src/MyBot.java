import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class MyBot {
	/**
	 * Log file for game state and orders in debug mode
	 */
	private static FileWriter debugLog = null;

	public static void doTurn(PlanetWars pw) {
		// analyze game state
		for (Planet p : pw.getPlanets()) {
			// assign a strategy to my planets
			if (p.owner == 1 && p.strategy == null) {
				p.strategy = new AllOutWarV3();
			}
			// calculate available ships
			int enemyShips = 0;
			for (Fleet f : pw.getEnemyFleets(p)) {
				enemyShips += f.numShips;
			}
			int myShips = 0;
			for (Fleet f : pw.getMyFleets(p)) {
				myShips += f.numShips;
			}
			switch (p.owner) {
				case 0:
					p.availableShips = Math.abs(p.numShips - enemyShips) - myShips;
					break;
				case 1:
					p.availableShips = p.numShips + myShips - enemyShips;
					break;
				case 2:
					p.availableShips = p.numShips + enemyShips - myShips;
					break;
			}
			log("P " + p.planetID + " " + p.owner + " " + p.availableShips);
		}

		// issue orders
		for (Planet m : pw.getMyPlanets()) {
			m.strategy.doTurn(m, pw);
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

		String line = null;
		String message = "";
		int turn = 1;
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

			while ((line = r.readLine()) != null) {
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

