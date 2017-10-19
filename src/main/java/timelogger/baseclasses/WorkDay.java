package timelogger.baseclasses;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.extern.java.Log;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.utils.Util;

/**
 *
 * @author bbi93
 */
@Log
@Getter
public class WorkDay {

	private List<Task> tasks = new ArrayList<>();
	private long requiredMinPerDay;
	private LocalDate actualDay;
	private long sumPerDay;

	public WorkDay(LocalDate actualDay) {
		this.setRequiredMinPerDay();
		this.setActualDay(actualDay);
	}

	public WorkDay(long requiredMinPerDay, LocalDate actualDay) {
		this.setRequiredMinPerDay(requiredMinPerDay);
		this.setActualDay(actualDay);
	}

	public WorkDay(int year, int month, int day) {
		this.setRequiredMinPerDay();
		this.setActualDay(year, month, day);
	}

	public WorkDay(long requiredMinPerDay, int year, int month, int day) {
		this.setRequiredMinPerDay(requiredMinPerDay);
		this.setActualDay(year, month, day);
	}

	public long getSumPerDay() {
		long daySum = 0;
		for (Task task : tasks) {
			try {
				daySum += task.getMinPerTask();
			} catch (EmptyTimeFieldException ex) {
				Logger.getLogger(WorkDay.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return daySum;
	}

	private void setRequiredMinPerDay() {
		this.setRequiredMinPerDay(450);
	}

	private void setRequiredMinPerDay(long requiredMinPerDay) {
		this.requiredMinPerDay = requiredMinPerDay;
	}

	private void setActualDay(LocalDate actualDay) {
		this.actualDay = actualDay;
	}

	private void setActualDay(int year, int month, int day) {
		try {
			this.actualDay = LocalDate.of(year, month, day);
		} catch (DateTimeException ex) {
			log.warning(ex.getMessage());
			this.actualDay = LocalDate.now();
		}
	}

	public long getExtraMinPerDay() {
		return getSumPerDay() - getRequiredMinPerDay();
	}

	public void addTask(Task t) {
		try {
			if (Util.isMultipleQuarterHour(t.getMinPerTask()) && Util.isSeparatedTime(t, this.getTasks())) {
				tasks.add(t);
			}
		} catch (EmptyTimeFieldException ex) {
			Logger.getLogger(WorkDay.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Task getLatestTaskOfDay() {
		List<Task> tasksInDay = this.getTasks();
		if (tasksInDay.size() > 0) {
			Task lastTask = tasksInDay.get(0);
			for (Task task : tasksInDay) {
				try {
					if (task.getEndTime().isAfter(lastTask.getEndTime())) {
						lastTask = task;
					}
				} catch (EmptyTimeFieldException ex) {
					Logger.getLogger(WorkDay.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			return lastTask;
		}
		return null;
	}

	public String toStatistics() {
		return actualDay + " Task number:" + tasks.size() + " Logged time:" + getSumPerDay() + " Extra time:" + getExtraMinPerDay() + ".";
	}

}
