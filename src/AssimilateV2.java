import java.util.Iterator;
import java.util.List;


public class AssimilateV2 implements Strategy {
	public static Planet commonTarget = null;

	@Override
	public void doTurn(Planet m, PlanetWars pw) {
		List<Planet> closests = pw.listClosestNotMyPlanets(m);
		Iterator<Planet> it = closests.iterator();

		while (it.hasNext() && m.numShips > 0) {

			if (commonTarget == null || commonTarget.futureOwner == 1) {
				commonTarget = it.next();
			}

			// planet hasn't been captured by me yet
			if (commonTarget.futureOwner != 1) {
				int d = pw.getDistance(m.planetID, commonTarget.planetID);

				// send the bare minimum of ships to capture the closest
				// planet or all ships available if there isn't enough
				int ships = Math.min(m.numShips, commonTarget.availableShips + 1 + ((commonTarget.futureOwner == 0) ? 0 : d * commonTarget.growthRate));

				// make it so!
				if (pw.issueOrder(m, commonTarget, ships)) {
					// account for ships that just left...
					m.removeShips(ships);
					// add my fleet
					pw.addFleet(m, commonTarget, ships);
					// update available ships for destination planet
					pw.updateAvailableShips(commonTarget);
				}
			}
		}
	}

}
