import java.util.Iterator;
import java.util.List;


public class AllOutWarV5 implements Strategy {

	@Override
	public void doTurn(Planet m, PlanetWars pw) {
		List<Planet> closests = pw.listFastestGrowthPlanets(m);
		Iterator<Planet> it = closests.iterator();

		while (it.hasNext() && m.numShips > 0) {
			Planet c = it.next();

			// is this a planet I still need to capture?
			if (c.futureOwner != 1 && c.availableShips >= 0) {
				int d = pw.getDistance(m.planetID, c.planetID);

				// calculate the bare minimum of ships to capture the closest planet
				int ships = c.availableShips + 1 + ((c.futureOwner == 0) ? 0 : d * c.growthRate);

				// if we have enough ship...
				if (m.numShips >= ships) {
					// ... send the fleet
					if (pw.issueOrder(m, c, ships)) {
						// account for ships that just left...
						m.removeShips(ships);
						// add my fleet
						pw.addFleet(m, c, ships);
						// update available ships for destination planet
						pw.updateAvailableShips(c);
					}
				}
			}
		}
	}

}
