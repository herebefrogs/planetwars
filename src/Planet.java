public class Planet implements Cloneable {
	// Initializes a planet.
	public Planet(int planetID, int owner, int numShips, int growthRate, double x, double y) {
		this.planetID = planetID;
		this.owner = owner;
		this.numShips = numShips;
		this.growthRate = growthRate;
		this.x = x;
		this.y = y;
	}

	public void setOwner(int newOwner) {
		this.owner = newOwner;
	}

	public void setNumShips(int newNumShips) {
		this.numShips = newNumShips;
	}

	public void addShips(int amount) {
		numShips += amount;
	}

	public void removeShips(int amount) {
		numShips -= amount;
	}

	public int planetID;
	public int owner;
	public int numShips;
	public int growthRate;
	public double x, y;
	public Strategy strategy;

	private Planet (Planet _p) {
		planetID = _p.planetID;
		owner = _p.owner;
		numShips = _p.numShips;
		growthRate = _p.growthRate;
		x = _p.x;
		y = _p.y;
	}

	@Override
	public Object clone() {
		return new Planet(this);
	}

	@Override
	public String toString() {
		return "P " + x + " " + y  + " " + owner + " " + numShips + " " + growthRate + " #" + planetID;
	}
}
