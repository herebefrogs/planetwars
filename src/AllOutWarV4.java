import java.util.Iterator;
import java.util.List;


public class AllOutWarV4 implements Strategy {

	@Override
	public void doTurn(Planet m, PlanetWars pw) {
		List<Planet> closests = pw.listClosestPlanets(m);
		Iterator<Planet> it = closests.iterator();

		while (it.hasNext() && m.numShips > 0) {
			Planet c = it.next();

			// is this a planet I still need to capture?
			if (m.planetID != c.planetID && c.futureOwner != 1) {
				int d = pw.getDistance(m.planetID, c.planetID);

				// calculate the bare minimum of ships to capture the closest planet
				int ships = c.availableShips + 1 + ((c.futureOwner == 0) ? 0 : d * c.growthRate);

				// if we have enough ship...
				if (m.numShips >= ships) {
					// send the fleet
					pw.issueOrder(m, c, ships);
				}
			}
		}
	}

}
