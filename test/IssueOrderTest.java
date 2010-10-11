import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class IssueOrderTest {
	MemoryStream ms;

	@Before
	public void captureSystemOut() throws Exception {
		ms = new MemoryStream(System.out);
		System.setOut(ms);
	}

	@After
	public void restoreSystemOut() throws Exception {
		System.setOut(ms.getPrintStream());
	}

	@Test
	public void testDontOwnPlanet() throws IOException {
		// Enemy planet at (10, 10) with 20 ships stationed and a growth rate of 5 ships per turn
		// Neutral planet at (15, 15) with 10 ships stationed and a growth rate of 2 ships per turn
		PlanetWars pw = new PlanetWars("P 10 10 2 20 5\nP 15 15 0 10 2");
		pw.issueOrder(0, 1, 20);

		String order = ms.read();
		assertTrue("don't own source planet: " + order, order.isEmpty());
	}

	@Test
	public void testNotEnoughShipOnPlanet() {
		// Allied planet at (10, 10) with 20 ships stationed and a growth rate of 5 ships per turn
		// Neutral planet at (15, 15) with 10 ships stationed and a growth rate of 2 ships per turn
		PlanetWars pw = new PlanetWars("P 10 10 1 20 5\nP 15 15 0 10 2");
		pw.issueOrder(0, 1, 30);

		String order = ms.read();
		assertTrue("not enough ships on source planet: " + order, order.isEmpty());
	}

	@Test
	public void testValidOrder() {
		// Allied planet at (10, 10) with 20 ships stationed and a growth rate of 5 ships per turn
		// Neutral planet at (15, 15) with 10 ships stationed and a growth rate of 2 ships per turn
		PlanetWars pw = new PlanetWars("P 10 10 1 20 5\nP 15 15 0 10 2");
		pw.issueOrder(0, 1, 20);

		String order = ms.read();
		assertTrue("valid order: " + order, order.equals("0 1 20"));
	}

	/**
	 * Capture any String written to this PrintStream
	 * @author jerome
	 */
	class MemoryStream extends PrintStream {
		/**
		 * Memory buffer to capture Strings
		 */
		StringBuffer sb = new StringBuffer();

		MemoryStream(PrintStream out) {
			super(out);
		}

		@Override
		public void println(String x) {
			sb.append(x);
			super.println(x);
		}

		/**
		 * Return captured String and clear memory buffer
		 * @return
		 */
		public String read() {
			String s = sb.toString();
			sb.delete(0, sb.length());
			return s;
		}

		/**
		 * Return original PrintStream
		 * @return
		 */
		public PrintStream getPrintStream() {
			return (PrintStream) out;
		}
	};
}
