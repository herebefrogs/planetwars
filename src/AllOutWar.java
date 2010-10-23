import java.util.List;


public class AllOutWar implements Strategy {

	@Override
	public void doTurn(Planet m, PlanetWars pw) {
		List<Planet> closests = pw.listClosestNotMyPlanets(m);

		if (!closests.isEmpty()) {
			Planet c = closests.get(0);
			int d = pw.getDistance(m.planetID, c.planetID);

			// send the bare minimum of ships to capture the closest
			// planet or all ships available if there isn't enough
			int ships = Math.min(m.numShips, c.numShips + ((c.owner == 0) ? c.growthRate : d * c.growthRate));

			// make it so!
			pw.issueOrder(m, c, ships);
		}
	}

}
