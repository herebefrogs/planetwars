import java.util.List;


public class AllOutWarV3 implements Strategy {

	@Override
	public void doTurn(Planet m, PlanetWars pw) {
		List<Planet> closests = pw.listClosestNotMyPlanets(m);

		if (!closests.isEmpty()) {
			Planet c = closests.get(0);
			int d = pw.getDistance(m.planetID, c.planetID);

			// calculate the bare minimum of ships to capture the closest planet
			int ships = c.numShips + 1 + ((c.owner == 0) ? 0 : d * c.growthRate);

			// if we have enough ship, send the fleet
			if (m.numShips >= ships) {
				pw.issueOrder(m, c, ships);
			}
		}
	}

}
