package timelogger.baseclasses;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bbi93
 */
public class TaskTest {

	public TaskTest() {
	}

	@Test
	public void testGetMinPerTask() {
		Task instance = new Task("LT-1111", "08:30", "09:45", "test task");
		long expResult = 75L;
		long result = instance.getMinPerTask();
		assertEquals(expResult, result);
	}

	@Test
	public void testIsValidTaskId() {
		Task instance = new Task("LT-1111", "08:30", "09:45", "test task");
		boolean expResult = true;
		boolean result = instance.isValidTaskId();
		assertEquals(expResult, result);
	}

	@Test
	public void testIsValidTaskId2() {
		Task instance = new Task("11111", "08:30", "09:45", "test task");
		boolean expResult = false;
		boolean result = instance.isValidTaskId();
		assertEquals(expResult, result);
	}

	@Test
	public void testIsValidTaskId3() {
		Task instance = new Task("LT1313", "08:30", "09:45", "test task");
		boolean expResult = false;
		boolean result = instance.isValidTaskId();
		assertEquals(expResult, result);
	}

	@Test
	public void testIsValidTaskId4() {
		Task instance = new Task("1111-LT-1313", "08:30", "09:45", "test task");
		boolean expResult = false;
		boolean result = instance.isValidTaskId();
		assertEquals(expResult, result);
	}

	@Test
	public void testIsValidTaskId5() {
		Task instance = new Task("LT-13135", "08:30", "09:45", "test task");
		boolean expResult = false;
		boolean result = instance.isValidTaskId();
		assertEquals(expResult, result);
	}

	@Test
	public void testIsMultipleQuarterHour() {
		Task instance = new Task("LT-1111", "08:30", "09:45", "test task");
		boolean expResult = true;
		boolean result = instance.isMultipleQuarterHour();
		assertEquals(expResult, result);
	}

	@Test
	public void testIsMultipleQuarterHour2() {
		Task instance = new Task("LT-1111", "08:30", "09:40", "test task");
		boolean expResult = false;
		boolean result = instance.isMultipleQuarterHour();
		assertEquals(expResult, result);
	}

}
