import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;


public class ListTargetsTest {

	@After
	public void clearDistancesAndPlanets() {
		PlanetWars.testsReset();
	}

	@Test
	public void testGetClosestTargets() {
		// Allied planet at (0, 0) with 20 ships stationed and a growth rate of 5 ships per turn
		// Neutral planet at (20, 20) with 10 ships stationed and a growth rate of 2 ships per turn
		// Enemy planet at (10, 10) with 10 ships stationed and a growth rate of 2 ships per turn
		// Enemy planet at (15, 15) with 10 ships stationed and a growth rate of 2 ships per turn
		PlanetWars pw = new PlanetWars("P 0 0 1 20 5\nP 20 20 0 10 2\nP 10 10 2 10 2\nP 15 15 2 10 2");

		List<Planet> closests = new ArrayList<Planet>();
		closests.add(pw.getPlanet(2));
		closests.add(pw.getPlanet(3));
		closests.add(pw.getPlanet(1));
		assertTrue("closest planets out of order", closests.equals(pw.getClosestTargets(pw.getPlanet(0))));
	}

	@Test
	public void testGetWeakestTargets() {
		// Allied planet at (0, 0) with 20 ships stationed and a growth rate of 5 ships per turn
		// Neutral planet at (20, 20) with 50 ships stationed and a growth rate of 2 ships per turn
		// Enemy planet at (10, 10) with 10 ships stationed and a growth rate of 2 ships per turn
		// Enemy planet at (15, 15) with 40 ships stationed and a growth rate of 2 ships per turn
		// Neutral planet at (12, 12) with 25 ships stationed and a growth rate of 2 ships per turn
		// Enemy planet at (20, 15) with 25 ships stationed and a growth rate of 2 ships per turn
		PlanetWars pw = new PlanetWars("P 0 0 1 20 5\nP 20 20 0 50 2\nP 10 10 2 10 2\nP 15 15 2 40 2\nP 12 12 0 25 2\nP 20 15 2 25 2");

		List<Planet> weakests = new ArrayList<Planet>();
		weakests.add(pw.getPlanet(2));
		weakests.add(pw.getPlanet(4));
		weakests.add(pw.getPlanet(5));
		weakests.add(pw.getPlanet(3));
		weakests.add(pw.getPlanet(1));
		assertTrue("weakests planets out of order", weakests.equals(pw.getWeakestTargets(pw.getPlanet(0))));
	}

	@Test
	public void testGetFastestGrowthTargets() {
		// Allied planet at (0, 0) with 20 ships stationed and a growth rate of 5 ships per turn
		// Neutral planet at (20, 20) with 20 ships stationed and a growth rate of 2 ships per turn
		// Enemy planet at (10, 10) with 20 ships stationed and a growth rate of 5 ships per turn
		// Enemy planet at (15, 15) with 20 ships stationed and a growth rate of 3 ships per turn
		// Neutral planet at (12, 12) with 20 ships stationed and a growth rate of 4 ships per turn
		// Enemy planet at (20, 15) with 20 ships stationed and a growth rate of 4 ships per turn
		PlanetWars pw = new PlanetWars("P 0 0 1 20 5\nP 20 20 0 20 2\nP 10 10 2 20 5\nP 15 15 2 20 3\nP 12 12 0 20 4\nP 20 15 2 20 4");

		List<Planet> fastests = new ArrayList<Planet>();
		fastests.add(pw.getPlanet(2));
		fastests.add(pw.getPlanet(4));
		fastests.add(pw.getPlanet(5));
		fastests.add(pw.getPlanet(3));
		fastests.add(pw.getPlanet(1));

		assertTrue("fastest growth planets out of order", fastests.equals(pw.getFastestGrowthTargets(pw.getPlanet(0))));
	}

}
