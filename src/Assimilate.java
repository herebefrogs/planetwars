import java.util.Iterator;
import java.util.List;


public class Assimilate implements Strategy {

	@Override
	public void doTurn(Planet m, PlanetWars pw) {
		List<Planet> closests = pw.listClosestNotMyPlanets(m);
		Iterator<Planet> it = closests.iterator();

		while (it.hasNext() && m.numShips > 0) {
			Planet c = it.next();

			// planet hasn't been captured by me yet
			if (c.futureOwner != 1 && c.availableShips >= 0) {
				int d = pw.getDistance(m.planetID, c.planetID);

				// send the bare minimum of ships to capture the closest
				// planet or all ships available if there isn't enough
				int ships = Math.min(m.numShips, c.availableShips + 1 + ((c.futureOwner == 0) ? 0 : d * c.growthRate));

				// make it so!
				pw.issueOrder(m, c, ships);
			}
		}
	}

}
