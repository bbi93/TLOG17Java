package timelogger.baseclasses;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bbi93
 */
public class TimeLoggerTest {

	@Test
	public void testAddMonth() {
		WorkMonth wm = new WorkMonth(2017, 11);
		WorkMonth wm2 = new WorkMonth(2017, 11);
		TimeLogger instance = new TimeLogger();
		int expResult = 1;
		instance.addMonth(wm);
		instance.addMonth(wm2);
		assertEquals(expResult, instance.getMonths().size());
	}

}
