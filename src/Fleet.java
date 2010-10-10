public class Fleet implements Comparable<Fleet>, Cloneable {
	// Initializes a fleet.
	public Fleet(int owner, int numShips, int sourcePlanet, int destinationPlanet, int totalTripLength,	int turnsRemaining) {
		this.owner = owner;
		this.numShips = numShips;
		this.sourcePlanet = sourcePlanet;
		this.destinationPlanet = destinationPlanet;
		this.totalTripLength = totalTripLength;
		this.turnsRemaining = turnsRemaining;
	}

	// Initializes a fleet.
	public Fleet(int owner, int numShips) {
		this.owner = owner;
		this.numShips = numShips;
		this.sourcePlanet = -1;
		this.destinationPlanet = -1;
		this.totalTripLength = -1;
		this.turnsRemaining = -1;
	}

	public void removeShips(int amount) {
		numShips -= amount;
	}

	// Subtracts one turn remaining. Call this function to make the fleet get
	// one turn closer to its destination.
	public void timeStep() {
		if (turnsRemaining > 0) {
			--turnsRemaining;
		} else {
			turnsRemaining = 0;
		}
	}

	@Override
	public int compareTo(Fleet o) {
		Fleet f = o;
		return this.numShips - f.numShips;
	}

	public int owner;
	public int numShips;
	public int sourcePlanet;
	public int destinationPlanet;
	public int totalTripLength;
	public int turnsRemaining;

	private Fleet(Fleet _f) {
		owner = _f.owner;
		numShips = _f.numShips;
		sourcePlanet = _f.sourcePlanet;
		destinationPlanet = _f.destinationPlanet;
		totalTripLength = _f.totalTripLength;
		turnsRemaining = _f.turnsRemaining;
	}
	@Override
	public Object clone() {
		return new Fleet(this);
	}

	@Override
	public String toString() {
		return "F " + owner + " " + numShips + " " + sourcePlanet + " " + destinationPlanet + " " + totalTripLength + " " + turnsRemaining;
	}
}
