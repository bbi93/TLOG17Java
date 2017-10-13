/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelogger.baseclasses;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author precognox
 */
public class WorkMonthTest {

	private static WorkMonth workMonth;

	public WorkMonthTest() {
	}

	@BeforeClass
	public static void setUpClass() {
		workMonth = new WorkMonth(2017, 10);
		workMonth.addWorkDay(new WorkDay(2017, 10, 12));
		workMonth.addWorkDay(new WorkDay(2017, 10, 13));
	}

	@Test
	public void testGetExtraMinPerMonth() {
		Task task=new Task("LT-1414", "09:00", "16:30", "test task");
		for (WorkDay workDay : workMonth.getDays()) {
			workDay.addTask(task);
		}
		long expResult = 0L;
		long result = workMonth.getExtraMinPerMonth();
		assertEquals(expResult, result);
	}

	@Test
	public void testIsNewDate() {
		WorkDay wd = new WorkDay(2017, 10, 13);
		boolean expResult = false;
		boolean result = workMonth.isNewDate(wd);
		assertEquals(expResult, result);
	}

	@Test
	public void testIsSameMonthPass() {
		WorkDay wd = new WorkDay(2017, 10, 1);
		boolean expResult = true;
		boolean result = workMonth.isSameMonth(wd);
		assertEquals(expResult, result);
	}

	@Test
	public void testIsSameMonthFail() {
		WorkDay wd = new WorkDay(2017, 9, 1);
		boolean expResult = false;
		boolean result = workMonth.isSameMonth(wd);
		assertEquals(expResult, result);
	}

	@Test
	public void testAddWorkDay_WorkDay_boolean() {
		boolean isWeekendEnabled = true;
		int beforeAdd = workMonth.getDays().size();
		workMonth.addWorkDay(new WorkDay(2017, 10, 14), isWeekendEnabled);
		int afterAdd = workMonth.getDays().size();
		assertEquals(beforeAdd + 1, afterAdd);
	}

	@Test
	public void testAddWorkDay_WorkDay() {
		int beforeAdd = workMonth.getDays().size();
		workMonth.addWorkDay(new WorkDay(2017, 10, 14));
		int afterAdd = workMonth.getDays().size();
		assertEquals(beforeAdd, afterAdd);

	}

}
