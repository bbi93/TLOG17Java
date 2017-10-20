package timelogger.baseclasses;

import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author precognox
 */
public class TimeLoggerTest {

	public TimeLoggerTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@Test
	public void testAddMonth() {
		System.out.println("addMonth");
		WorkMonth wm = null;
		TimeLogger instance = new TimeLogger();
		instance.addMonth(wm);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsNewMonth() {
		System.out.println("isNewMonth");
		WorkMonth wm = new WorkMonth(0, 0);
		TimeLogger instance = new TimeLogger();
		boolean expResult = false;
		boolean result = instance.isNewMonth(wm);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

}
