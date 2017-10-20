package timelogger.utils;

import java.time.LocalTime;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;
import timelogger.baseclasses.Task;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.NotExpectedTimeOrderException;

/**
 *
 * @author bbi93
 */
public class UtilTest {

	@Test
	public void testRoundToMultipleQuarterHour() throws Exception {
		LocalTime startTime = LocalTime.parse("07:30");
		LocalTime endTime = LocalTime.parse("07:50");
		LocalTime expResult = LocalTime.parse("07:45");
		LocalTime result = Util.roundToMultipleQuarterHour(startTime, endTime);
		assertEquals(expResult, result);
	}

	@Test
	public void testRoundToMultipleQuarterHour2() throws Exception {
		LocalTime startTime = LocalTime.parse("07:30");
		LocalTime endTime = LocalTime.parse("07:55");
		LocalTime expResult = LocalTime.parse("08:00");
		LocalTime result = Util.roundToMultipleQuarterHour(startTime, endTime);
		assertEquals(expResult, result);
	}

	@Test
	public void testIsMultipleQuarterHour() throws Exception {
		LocalTime startTime = LocalTime.parse("07:30");
		LocalTime endTime = LocalTime.parse("07:55");
		boolean expResult = false;
		boolean result = Util.isMultipleQuarterHour(startTime, endTime);
		assertEquals(expResult, result);
	}

	@Test
	public void testIsMultipleQuarterHour2() throws Exception {
		LocalTime startTime = LocalTime.parse("07:30");
		LocalTime endTime = LocalTime.parse("07:45");
		boolean expResult = true;
		boolean result = Util.isMultipleQuarterHour(startTime, endTime);
		assertEquals(expResult, result);
	}

	@Test(expected = EmptyTimeFieldException.class)
	public void testIsMultipleQuarterHourEmptyTimeFieldException() throws Exception {
		LocalTime startTime = null;
		LocalTime endTime = LocalTime.parse("07:55");
		boolean expResult = false;
		boolean result = Util.isMultipleQuarterHour(startTime, endTime);
		assertEquals(expResult, result);
	}

	@Test(expected = NotExpectedTimeOrderException.class)
	public void testIsMultipleQuarterHourNotExpectedTimeOrderException() throws Exception {
		LocalTime startTime = LocalTime.parse("08:30");
		LocalTime endTime = LocalTime.parse("07:45");
		boolean expResult = true;
		boolean result = Util.isMultipleQuarterHour(startTime, endTime);
		assertEquals(expResult, result);
	}

}
