// Contestants do not need to worry about anything in this file. This is just
// helper code that does the boring stuff for you, so you can focus on the
// interesting stuff. That being said, you're welcome to change anything in
// this file if you know what you're doing.

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PlanetWars {
	/**
	 * Store all distances between planets (1st index is source ID, 2nd index is destination ID)
	 */
	private static int[][] distances;

	/**
	 * List of planets
	 */
	private static final ArrayList<Planet> planets = new ArrayList<Planet>();
	/**
	 * List of fleets
	 */
	private ArrayList<Fleet> fleets;

	/**
	 * Update list of planets and fleets from the game state passed in argument
	 * @param gameStateString
	 */
	public PlanetWars(String gameStateString) {
		fleets = new ArrayList<Fleet>();
		parseGameState(gameStateString);
		if (distances == null) {
			initDistances();
		}
	}

	// Returns the number of planets. Planets are numbered starting with 0.
	public int numPlanets() {
		return planets.size();
	}

	// Returns the planet with the given planet_id. There are NumPlanets()
	// planets. They are numbered starting at 0.
	public Planet getPlanet(int planetID) {
		return planets.get(planetID);
	}

	// Returns the number of fleets.
	public int numFleets() {
		return fleets.size();
	}

	// Returns the fleet with the given fleet_id. Fleets are numbered starting
	// with 0. There are NumFleets() fleets. fleet_id's are not consistent from
	// one turn to the next.
	public Fleet getFleet(int fleetID) {
		return fleets.get(fleetID);
	}

	// Returns a list of all the planets.
	public List<Planet> getPlanets() {
		return planets;
	}

	// Return a list of all the planets owned by the current player. By
	// convention, the current player is always player number 1.
	public List<Planet> getMyPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
			if (p.owner == 1) {
				r.add(p);
			}
		}
		return r;
	}

	// Return a list of all neutral planets.
	public List<Planet> getNeutralPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
			if (p.owner == 0) {
				r.add(p);
			}
		}
		return r;
	}

	// Return a list of all the planets owned by rival players. This excludes
	// planets owned by the current player, as well as neutral planets.
	public List<Planet> getEnemyPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
			if (p.owner >= 2) {
				r.add(p);
			}
		}
		return r;
	}

	// Return a list of all the planets that are not owned by the current
	// player. This includes all enemy planets and neutral planets.
	public List<Planet> getNotMyPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
			if (p.owner != 1) {
				r.add(p);
			}
		}
		return r;
	}

	/**
	 * Order planets by smallest distance
	 * @author jerome
	 */
	private class DistanceComparator implements Comparator<Planet> {
		private Planet src;
		DistanceComparator(final Planet src) {
			this.src = src;
		}
		@Override
		public int compare(Planet p1, Planet p2) {
			int d1 = getDistance(src.planetID, p1.planetID);
			int d2 = getDistance(src.planetID, p2.planetID);
			return d1 < d2 ? -1 : d1 == d2 ? 0 : 1;
		}
	}
	/**
	 * Order planets by smallest number of ships (then by smallest distance)
	 * @author jerome
	 */
	private class ShipsComparator implements Comparator<Planet> {
		private Planet src;
		ShipsComparator(final Planet src) {
			this.src = src;
		}
		@Override
		public int compare(Planet p1, Planet p2) {
			int d1 = getDistance(src.planetID, p1.planetID);
			int d2 = getDistance(src.planetID, p2.planetID);
			return p1.numShips < p2.numShips ? -1 : (p1.numShips == p2.numShips ? (d1 < d2 ? -1 : d1 == d2 ? 0 : 1) : 1);
		}

	}
	/**
	 * Order planets by fastest growth factor (then by smallest distance)
	 * @author jerome
	 */
	private class GrowthComparator implements Comparator<Planet> {
		private Planet src;
		GrowthComparator(final Planet src) {
			this.src = src;
		}
		@Override
		public int compare(Planet p1, Planet p2) {
			int d1 = getDistance(src.planetID, p1.planetID);
			int d2 = getDistance(src.planetID, p2.planetID);
			return p1.growthRate > p2.growthRate ? -1 : (p1.growthRate == p2.growthRate ? (d1 < d2 ? -1 : d1 == d2 ? 0 : 1) : 1);
		}

	}
	/**
	 * Sort planets according to comparator provided
	 * @param planets
	 * @param cmp
	 * @return
	 */
	private List<Planet> sortPlanets(final List<Planet> planets, final Comparator<Planet> cmp) {
		Collections.sort(planets, cmp);
		return planets;
	}
	/**
	 * Return neutral and enemy planets ordered by shortest distance to source planet
	 * @param s
	 * @return
	 */
	public List<Planet> listClosestNotMyPlanets(final Planet src) {
		return sortPlanets(getNotMyPlanets(), new DistanceComparator(src));
	}
	/**
	 * Return neutral planets ordered by shortest distance to source planet
	 * @param s
	 * @return
	 */
	public List<Planet> listClosestNeutralPlanets(final Planet src) {
		return sortPlanets(getNeutralPlanets(), new DistanceComparator(src));
	}
	/**
	 * Return enemy planets ordered by shortest distance to source planet
	 * @param s
	 * @return
	 */
	public List<Planet> listClosestEnemyPlanets(final Planet src) {
		return sortPlanets(getEnemyPlanets(), new DistanceComparator(src));
	}

	/**
	 * Return neutral and enemy planets ordered by smallest number of ships.
	 * If 2 planets have the same number of ships, the closest to the source planet is returned first
	 * @param s
	 * @return
	 */
	public List<Planet> listWeakestNotMyPlanets(final Planet src) {
		return sortPlanets(getNotMyPlanets(), new ShipsComparator(src));
	}
	/**
	 * Return neutral planets ordered by smallest number of ships.
	 * If 2 planets have the same number of ships, the closest to the source planet is returned first
	 * @param s
	 * @return
	 */
	public List<Planet> listWeakestNeutralPlanets(final Planet src) {
		return sortPlanets(getNeutralPlanets(), new ShipsComparator(src));
	}
	/**
	 * Return enemy planets ordered by smallest number of ships.
	 * If 2 planets have the same number of ships, the closest to the source planet is returned first
	 * @param s
	 * @return
	 */
	public List<Planet> listWeakestEnemylPlanets(final Planet src) {
		return sortPlanets(getEnemyPlanets(), new ShipsComparator(src));
	}
	/**
	 * Return neutral and enemy planets ordered by highest growth factor.
	 * If 2 planets have the same growth factor, the closest to the source planet is returned first
	 * @param s
	 * @return
	 */
	public List<Planet> listFastestGrowthNotMyPlanets(final Planet src) {
		return sortPlanets(getNotMyPlanets(), new GrowthComparator(src));
	}
	/**
	 * Return neutral planets ordered by highest growth factor.
	 * If 2 planets have the same growth factor, the closest to the source planet is returned first
	 * @param s
	 * @return
	 */
	public List<Planet> listFastestGrowthNeutralPlanets(final Planet src) {
		return sortPlanets(getNeutralPlanets(), new GrowthComparator(src));
	}
	/**
	 * Return enemy planets ordered by highest growth factor.
	 * If 2 planets have the same growth factor, the closest to the source planet is returned first
	 * @param s
	 * @return
	 */
	public List<Planet> listFastestGrowthEnemyPlanets(final Planet src) {
		return sortPlanets(getEnemyPlanets(), new GrowthComparator(src));
	}
	// Return a list of all the fleets.
	public List<Fleet> getFleets() {
		List<Fleet> r = new ArrayList<Fleet>();
		for (Fleet f : fleets) {
			r.add(f);
		}
		return r;
	}

	// Return a list of all the fleets owned by the current player.
	public List<Fleet> getMyFleets() {
		List<Fleet> r = new ArrayList<Fleet>();
		for (Fleet f : fleets) {
			if (f.owner == 1) {
				r.add(f);
			}
		}
		return r;
	}

	// Return a list of all the fleets owned by enemy players.
	public List<Fleet> getEnemyFleets() {
		List<Fleet> r = new ArrayList<Fleet>();
		for (Fleet f : fleets) {
			if (f.owner != 1) {
				r.add(f);
			}
		}
		return r;
	}

	/**
	 * Returns the distance between two planets
	 * @param sourcePlanet
	 * @param destinationPlanet
	 * @return
	 */
	public int getDistance(int sourcePlanet, int destinationPlanet) {
		return distances[sourcePlanet][destinationPlanet];
	}

	/**
	 * Sends an order to the game engine. An order is composed of a source
	 * planet number, a destination planet number, and a number of ships. A
	 * few things to keep in mind:
	 * - you can issue many orders per turn if you like.
	 * - the planets are numbered starting at zero, not one.
	 * - you must own the source planet. If you break this rule, the game
	 *   engine kicks your bot out of the game instantly.
	 *   you can't move more ships than are currently on the source planet.
	 * - the ships will take a few turns to reach their destination. Travel
	 *   is not instant. See the Distance() function for more info.
	 * @param sourcePlanet
	 * @param destinationPlanet
	 * @param numShips
	 * @return true if order is valid & accepted, false otherwise
	 */
	public boolean issueOrder(int sourcePlanet, int destinationPlanet,	int numShips) {
		Planet source = getPlanet(sourcePlanet);
		if (source.owner != 1) {
			MyBot.log("skipping order: " + sourcePlanet + " " + destinationPlanet + " " + numShips + ": source planet is owned by " + source.owner);
		} else if (source.numShips < numShips) {
			MyBot.log("skipping order: " + sourcePlanet + " " + destinationPlanet + " " + numShips + ": source planet only have " + source.numShips + " ships");
		} else {
			System.out.println("" + sourcePlanet + " " + destinationPlanet + " " +	numShips);
			System.out.flush();
			return true;
		}
		return false;
	}

	/**
	 * Sends an order to the game engine. An order is composed of a source
	 * planet number, a destination planet number, and a number of ships. A
	 * few things to keep in mind:
	 * - you can issue many orders per turn if you like.
	 * - the planets are numbered starting at zero, not one.
	 * - you must own the source planet. If you break this rule, the game
	 *   engine kicks your bot out of the game instantly.
	 * - you can't move more ships than are currently on the source planet.
	 * - the ships will take a few turns to reach their destination. Travel
	 *   is not instant. See the Distance() function for more info.	 * @param source
	 * @param dest
	 * @param numShips
	 * @return true if order is valid & accepted, false otherwise
	 */
	public boolean issueOrder(Planet source, Planet dest, int numShips) {
		return issueOrder(source.planetID, dest.planetID, numShips);
	}

	// Sends the game engine a message to let it know that we're done sending
	// orders. This signifies the end of our turn.
	public void finishTurn() {
		System.out.println("go");
		System.out.flush();
	}

	// Returns true if the named player owns at least one planet or fleet.
	// Otherwise, the player is deemed to be dead and false is returned.
	public boolean isAlive(int playerID) {
		for (Planet p : planets) {
			if (p.owner == playerID) {
				return true;
			}
		}
		for (Fleet f : fleets) {
			if (f.owner == playerID) {
				return true;
			}
		}
		return false;
	}

	// If the game is not yet over (ie: at least two players have planets or
	// fleets remaining), returns -1. If the game is over (ie: only one player
	// is left) then that player's number is returned. If there are no
	// remaining players, then the game is a draw and 0 is returned.
	public int winner() {
		Set<Integer> remainingPlayers = new TreeSet<Integer>();
		for (Planet p : planets) {
			remainingPlayers.add(p.owner);
		}
		for (Fleet f : fleets) {
			remainingPlayers.add(f.owner);
		}
		switch (remainingPlayers.size()) {
			case 0:
				return 0;
			case 1:
				return ((Integer)remainingPlayers.toArray()[0]).intValue();
			default:
				return -1;
		}
	}

	// Returns the number of ships that the current player has, either located
	// on planets or in flight.
	public int numShips(int playerID) {
		int numShips = 0;
		for (Planet p : planets) {
			if (p.owner == playerID) {
				numShips += p.numShips;
			}
		}
		for (Fleet f : fleets) {
			if (f.owner == playerID) {
				numShips += f.numShips;
			}
		}
		return numShips;
	}

	/**
	 * Parses the game state
	 * @param s game state
	 * @return 1 on success, 0 on failure
	 */
	private int parseGameState(String s) {
		int planetID = 0;
		String[] lines = s.split("\n");
		for (int i = 0; i < lines.length; ++i) {
			String line = lines[i];
			int commentBegin = line.indexOf('#');
			if (commentBegin >= 0) {
				line = line.substring(0, commentBegin);
			}
			if (line.trim().length() == 0) {
				continue;
			}
			String[] tokens = line.split(" ");
			if (tokens.length == 0) {
				continue;
			}
			if (tokens[0].equals("P")) {
				if (tokens.length != 6) {
					return 0;
				}
				double x = Double.parseDouble(tokens[1]);
				double y = Double.parseDouble(tokens[2]);
				int owner = Integer.parseInt(tokens[3]);
				int numShips = Integer.parseInt(tokens[4]);
				int growthRate = Integer.parseInt(tokens[5]);
				Planet p;

				if (planets.size() == planetID) {
					// the planet hasn't been added to the list yet
					p = new Planet(planetID, owner, numShips, growthRate, x, y);
					p.strategy = new AllOutWarV3();
					planets.add(p);
				} else {
					// update planet data (x, y and growth rate won't change between turns)
					p = planets.get(planetID);
					p.setOwner(owner);
					p.setNumShips(numShips);
				}
				planetID++;
				MyBot.log(p.toString());
			} else if (tokens[0].equals("F")) {
				if (tokens.length != 7) {
					return 0;
				}
				int owner = Integer.parseInt(tokens[1]);
				int numShips = Integer.parseInt(tokens[2]);
				int source = Integer.parseInt(tokens[3]);
				int destination = Integer.parseInt(tokens[4]);
				int totalTripLength = Integer.parseInt(tokens[5]);
				int turnsRemaining = Integer.parseInt(tokens[6]);
				Fleet f = new Fleet(owner, numShips, source, destination, totalTripLength, turnsRemaining);
				fleets.add(f);
				MyBot.log(f.toString());
			} else {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * Initialize distances map
	 */
	private void initDistances() {
		long t = System.currentTimeMillis();

		int n = numPlanets();
		distances = new int[n][n];

		// for all source planets...
		for (int s = 0; s < n; s++) {
			// for all destination planets...
			for (int d = s; d < n; d++) {
				distances[s][d] = calculateDistance(s, d);
				distances[d][s] = distances[s][d];
			}
		}

		MyBot.log("initialized distances in " + (System.currentTimeMillis() - t) + "ms");
	}

	/**
	 * Returns the distance between two planets, rounded up to the next highest integer.
	 * This is the number of discrete time steps it takes to get between the two planets.
	 * 
	 * @param sourcePlanet
	 * @param destinationPlanet
	 * @return
	 */
	private int calculateDistance(int sourcePlanet, int destinationPlanet) {
		Planet source = planets.get(sourcePlanet);
		Planet destination = planets.get(destinationPlanet);
		double dx = source.x - destination.x;
		double dy = source.y - destination.y;
		return (int)Math.ceil(Math.sqrt(dx * dx + dy * dy));
	}

	/**
	 * A way for JUnit tests to clear the distances and planets lists
	 */
	static void testsReset() {
		distances = null;
		planets.clear();
	}
}
