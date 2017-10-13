/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelogger.baseclasses;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author bbi93
 */
public class WorkDayTest {

	private static WorkDay workDay;

	public WorkDayTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		workDay = new WorkDay(300, 2017, 10, 12);
		workDay.addTask(new Task("LT-0001", "08:30", "09:45", "test task"));
		workDay.addTask(new Task("LT-0002", "09:45", "10:00", "test task 2"));
	}

	@Test
	public void testGetSumPerDay() {
		long expResult = 90L;
		long result = workDay.getSumPerDay();
		assertEquals(expResult, result);
	}

	@Test
	public void testGetExtraMinPerDay() {
		long expResult = -210L;
		long result = workDay.getExtraMinPerDay();
		assertEquals(expResult, result);
	}

	@Test
	public void testAddTask() {
		int beforeAdd=workDay.getTasks().size();
		Task t = new Task("LT-0001", "10:00", "11:00", "must add");
		workDay.addTask(t);
		int afterAdd=workDay.getTasks().size();
		assertEquals(beforeAdd+1, afterAdd);
	}
	@Test
	public void testAddTask2() {
		int beforeAdd=workDay.getTasks().size();
		Task t = new Task("LT-0001", "10:00", "11:01", "mustn't add, not quarter");
		workDay.addTask(t);
		int afterAdd=workDay.getTasks().size();
		assertEquals(beforeAdd, afterAdd);
	}

	@Test
	public void testAddTask3() {
		int beforeAdd=workDay.getTasks().size();
		Task t = new Task("LT-0001", "08:00", "09:00", "mustn't add not unique intervallum");
		workDay.addTask(t);
		int afterAdd=workDay.getTasks().size();
		assertEquals(beforeAdd, afterAdd);
	}

}
