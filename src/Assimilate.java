import java.util.Iterator;
import java.util.List;


public class Assimilate implements Strategy {

	@Override
	public void doTurn(Planet m, PlanetWars pw) {
		List<Planet> closests = pw.listClosestNotMyPlanets(m);
		Iterator<Planet> it = closests.iterator();

		while (it.hasNext() && m.numShips > 0) {
			Planet c = it.next();

			// planet hasn't been captured yet
			if (c.availableShips >= 0) {
				int d = pw.getDistance(m.planetID, c.planetID);

				// send the bare minimum of ships to capture the closest
				// planet or all ships available if there isn't enough
				int ships = Math.min(m.numShips, c.numShips + 1 + ((c.owner == 0) ? 0 : d * c.growthRate));

				// make it so!
				if (pw.issueOrder(m, c, ships)) {
					// account for ships that just left...
					m.removeShips(ships);
					// ... and ships that will arrive
					c.availableShips -= ships;
				}
			}
		}
	}

}
