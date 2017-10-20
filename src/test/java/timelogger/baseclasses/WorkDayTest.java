package timelogger.baseclasses;

import java.time.LocalDate;
import java.time.LocalTime;
import timelogger.exceptions.NegativeMinutesOfWorkException;
import org.junit.Test;
import static org.junit.Assert.*;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.NoTaskDeclaredException;
import timelogger.exceptions.NotSeparatedTimesException;

/**
 *
 * @author bbi93
 */
public class WorkDayTest {

	@Test
	public void testGetExtraMinPerDay() throws Exception {
		WorkDay wd = new WorkDay(2017, 10, 19);
		wd.addTask(new Task("LT-0001", "07:30", "08:45", "test task"));
		long expResult = -375L;
		long result = wd.getExtraMinPerDay();
		assertEquals(expResult, result);
	}

	@Test
	public void testGetExtraMinPerDayWithNoTask() throws Exception {
		WorkDay wd = new WorkDay(2017, 10, 19);
		long expResult = wd.getRequiredMinPerDay() * (-1);
		long result = wd.getExtraMinPerDay();
		assertEquals(expResult, result);
	}

	@Test(expected = NegativeMinutesOfWorkException.class)
	public void testnegativeRequiredMinperDay() throws Exception {
		WorkDay wd = new WorkDay(2017, 10, 19);
		wd.setRequiredMinPerDay(-300);
	}

	@Test(expected = NegativeMinutesOfWorkException.class)
	public void testCreatingNegativeRequiredMinperDay() throws Exception {
		WorkDay wd = new WorkDay(-300, 2017, 10, 19);
	}

	@Test(expected = FutureWorkException.class)
	public void testSettingFutureWorkDay() throws Exception {
		WorkDay wd = new WorkDay(LocalDate.now());
		wd.setActualDay(LocalDate.now().plusDays(1));
	}

	@Test(expected = FutureWorkException.class)
	public void testCreatingFutureWorkDay() throws Exception {
		WorkDay wd = new WorkDay(LocalDate.now().plusDays(1));
	}

	@Test
	public void testGetSumPerDay() throws Exception {
		WorkDay wd = new WorkDay(LocalDate.now());
		wd.addTask(new Task("LT-0001", "07:30", "08:45", "test task"));
		wd.addTask(new Task("LT-0002", "08:45", "09:45", "test task"));
		long expResult = 135;
		assertEquals(expResult, wd.getSumPerDay());
	}

	@Test
	public void testSumPerDay() throws Exception {
		WorkDay wd = new WorkDay(LocalDate.now());
		long expResult = 0;
		assertEquals(expResult, wd.getSumPerDay());
	}

	@Test
	public void testendTimeOfTheLastTask() throws Exception {
		WorkDay wd = new WorkDay(LocalDate.now());
		wd.addTask(new Task("LT-0001", "07:30", "08:45", "test task"));
		wd.addTask(new Task("LT-0002", "09:30", "11:45", "test task"));
		LocalTime expResult = LocalTime.of(11, 45);
		assertEquals(expResult, wd.endTimeOfTheLastTask());
	}

	@Test(expected = NoTaskDeclaredException.class)
	public void testendTimeOfTheLastTaskWithNoTask() throws Exception {
		WorkDay wd = new WorkDay(LocalDate.now());
		LocalTime result = wd.endTimeOfTheLastTask();
	}

	@Test(expected = EmptyTimeFieldException.class)
	public void testendTimeOfTheLastTaskWithNoEndTime() throws Exception {
		WorkDay wd = new WorkDay(LocalDate.now());
		wd.addTask(new Task("LT-0001", "07:30", "", "test task"));
		LocalTime result = wd.endTimeOfTheLastTask();
	}

	@Test(expected = NotSeparatedTimesException.class)
	public void testNotSeparatedTimesException() throws Exception {
		WorkDay wd = new WorkDay(LocalDate.now());
		wd.addTask(new Task("LT-0001", "07:30", "08:45", "test task"));
		wd.addTask(new Task("LT-0002", "08:30", "09:45", "test task"));
	}

	@Test(expected = EmptyTimeFieldException.class)
	public void testWorkDayWithEmptyTask() throws Exception {
		WorkDay wd = new WorkDay();
		wd.addTask(new Task("LT-0001"));
		wd.getSumPerDay();
	}

}
