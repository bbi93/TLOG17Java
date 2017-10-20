package timelogger.baseclasses;

import java.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.WeekendNotEnabledException;

/**
 *
 * @author bbi93
 */
public class WorkMonthTest {

	@Test
	public void testgetSumPerMonth() throws Exception {
		Task task1 = new Task("2154", "07:30", "08:45", "comment");
		Task task2 = new Task("2155", "08:45", "09:45", "comment");
		WorkDay wd1 = new WorkDay(420);
		wd1.addTask(task1);
		WorkDay wd2 = new WorkDay(420, LocalDate.of(2017, 10, 2));
		wd2.addTask(task2);
		WorkMonth wm = new WorkMonth(2017, 10);
		wm.addWorkDay(wd1);
		wm.addWorkDay(wd2);
		long expected = 135;
		assertEquals(expected, wm.getSumPerMonth());
	}

	@Test
	public void testgetSumPerMonthWithZeroDay() throws Exception {
		WorkMonth wm = new WorkMonth(2017, 10);
		long expected = 0;
		assertEquals(expected, wm.getSumPerMonth());
	}

	@Test
	public void testgetExtraMinPerMonth() throws Exception {
		Task task1 = new Task("2154", "07:30", "08:45", "comment");
		Task task2 = new Task("2155", "08:45", "09:45", "comment");
		WorkDay wd1 = new WorkDay(420);
		wd1.addTask(task1);
		WorkDay wd2 = new WorkDay(420, LocalDate.of(2017, 10, 2));
		wd2.addTask(task2);
		WorkMonth wm = new WorkMonth(2017, 10);
		wm.addWorkDay(wd1);
		wm.addWorkDay(wd2);
		long expected = -705;
		assertEquals(expected, wm.getExtraMinPerMonth());
	}

	@Test
	public void testgetExtraMinPerMonthWithZeroDay() throws Exception {
		WorkMonth wm = new WorkMonth(2017, 10);
		long expected = 0;
		assertEquals(expected, wm.getExtraMinPerMonth());
	}

	@Test
	public void testgetRequiredMinPerMonth() throws Exception {
		WorkMonth wm = new WorkMonth(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
		wm.addWorkDay(new WorkDay(420));
		wm.addWorkDay(new WorkDay(420));
		long expected = 840;
		assertEquals(expected, wm.getRequiredMinPerMonth());
	}

	@Test
	public void testgetRequiredMinPerMonthZero() throws Exception {
		WorkMonth wm = new WorkMonth(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
		long expected = 0;
		assertEquals(expected, wm.getRequiredMinPerMonth());
	}

	@Test(expected = WeekendNotEnabledException.class)
	public void testsumDayAndMonth() throws Exception {
		Task task1 = new Task("2154", "07:30", "08:45", "comment");
		WorkDay wd1 = new WorkDay(LocalDate.of(2016, 8, 28));
		wd1.addTask(task1);
		WorkMonth wm = new WorkMonth(2016, 8);
		wm.addWorkDay(wd1);
	}

	@Test(expected = EmptyTimeFieldException.class)
	public void testgetSumPerMonthWithEmptyTime() throws Exception {
		Task task1 = new Task("2154", "07:30", "08:45", "comment");
		Task task2 = new Task("2155", "08:45", "", "comment");
		WorkDay wd1 = new WorkDay(420);
		wd1.addTask(task1);
		WorkDay wd2 = new WorkDay(420, LocalDate.of(2017, 10, 2));
		wd2.addTask(task2);
		WorkMonth wm = new WorkMonth(2017, 10);
		wm.addWorkDay(wd1);
		wm.addWorkDay(wd2);
		wm.getSumPerMonth();
	}

	@Test(expected = EmptyTimeFieldException.class)
	public void testgetExtraMinPerMonthWithEmptyTime() throws Exception {
		Task task1 = new Task("2154", "07:30", "08:45", "comment");
		Task task2 = new Task("2155", "08:45", "", "comment");
		WorkDay wd1 = new WorkDay(420);
		wd1.addTask(task1);
		WorkDay wd2 = new WorkDay(420, LocalDate.of(2017, 10, 2));
		wd2.addTask(task2);
		WorkMonth wm = new WorkMonth(2017, 10);
		wm.addWorkDay(wd1);
		wm.addWorkDay(wd2);
		wm.getExtraMinPerMonth();
	}

}
