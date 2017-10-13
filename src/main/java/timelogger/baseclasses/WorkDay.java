package timelogger.baseclasses;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.java.Log;
import timelogger.baseclasses.utils.Util;

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
		return tasks.stream().mapToLong(Task::getMinPerTask).sum();
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
		if (Util.isMultipleQuarterHour(t.getMinPerTask()) && Util.isSeparatedTime(t, this.getTasks())) {
			tasks.add(t);
		}
	}

}
