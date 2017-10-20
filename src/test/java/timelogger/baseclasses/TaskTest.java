package timelogger.baseclasses;

import java.time.LocalTime;
import timelogger.exceptions.NotExpectedTimeOrderException;
import timelogger.exceptions.EmptyTimeFieldException;
import org.junit.Test;
import static org.junit.Assert.*;
import timelogger.exceptions.InvalidTaskIdException;
import timelogger.exceptions.NoTaskIdException;

/**
 *
 * @author bbi93
 */
public class TaskTest {

	@Test(expected = NotExpectedTimeOrderException.class)
	public void testTaskCreationWithBadOrderedTimeValues() throws Exception {
		Task badTask = new Task("1542", "08:45", "07:30", "task comment");
	}

	@Test(expected = EmptyTimeFieldException.class)
	public void testTaskCreationWithEmptyTimeValues() throws Exception {
		Task badTask = new Task("1542", "07:45", "", "task comment");
	}

	@Test
	public void testGetMinPerTask() throws Exception {
		Task instance = new Task("LT-1111", "07:30", "08:45", "test task");
		long expResult = 75L;
		long result = instance.getMinPerTask();
		assertEquals(expResult, result);
	}

	@Test(expected = EmptyTimeFieldException.class)
	public void testGetMinPerTaskWithEmptyTimes() throws Exception {
		Task instance = new Task("LT-1111");
		instance.getMinPerTask();
	}

	@Test()
	public void testIsValidTaskId() throws Exception {
		Task instance = new Task("LT-1111", "08:30", "09:45", "test task");
		assertEquals(true, instance.isValidTaskId());
	}

	@Test(expected = InvalidTaskIdException.class)
	public void testIsValidTaskId2() throws Exception {
		Task instance = new Task("11111", "08:30", "09:45", "test task");
	}

	@Test(expected = InvalidTaskIdException.class)
	public void testIsValidTaskId3() throws Exception {
		Task instance = new Task("LT1313", "08:30", "09:45", "test task");
	}

	@Test(expected = InvalidTaskIdException.class)
	public void testIsValidTaskId4() throws Exception {
		Task instance = new Task("1111-LT-1313", "08:30", "09:45", "test task");
	}

	@Test(expected = InvalidTaskIdException.class)
	public void testIsValidTaskId5() throws Exception {
		Task instance = new Task("LT-13135", "08:30", "09:45", "test task");
	}

	@Test(expected = NoTaskIdException.class)
	public void testIsValidTaskIdEmptyId() throws Exception {
		Task instance = new Task("", "08:30", "09:45", "test task");
	}

	@Test(expected = NoTaskIdException.class)
	public void testIsValidTaskIdNullId() throws Exception {
		Task instance = new Task(null, "08:30", "09:45", "test task");
	}

	@Test
	public void testEmptyTaskComment() throws Exception {
		Task instance = new Task("LT-9832", "08:30", "09:45", "");
		assertEquals("", instance.getComment());
	}

	@Test
	public void testNullTaskComment() throws Exception {
		Task instance = new Task("LT-9832", "08:30", "09:45", null);
		assertEquals("", instance.getComment());
	}

	@Test
	public void testTaskEndTimeRoundNonRoundEnd() throws Exception {
		Task instance = new Task("LT-9832", "07:30", "07:50", "test comment");
		LocalTime expectedTime = LocalTime.of(7, 45);
		assertEquals(expectedTime, instance.getEndTime());
	}

	@Test
	public void testTaskEndTimeRoundNonRoundStart() throws Exception {
		Task instance = new Task("LT-9832", "07:35", "08:00", "test comment");
		LocalTime expectedTime = LocalTime.of(8, 5);
		assertEquals(expectedTime, instance.getEndTime());
	}

}
