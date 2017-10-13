package timelogger.baseclasses;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.java.Log;

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
		this.actualDay = LocalDate.of(year, month, day);
	}

	public WorkDay(long requiredMinPerDay, int year, int month, int day) {
		this.setRequiredMinPerDay(requiredMinPerDay);
		this.actualDay = LocalDate.of(year, month, day);
	}

	public long getSumPerDay() {
		sumPerDay = 0;
		for (Task task : tasks) {
			sumPerDay += task.getMinPerTask();
		}
		return sumPerDay;
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

	public boolean isSeparatedTime(Task t) {
		for (Task task : tasks) {
			boolean tStartsBeforeTask = t.getStartTime().isBefore(task.getStartTime());
			boolean tEndsBeforeTask = t.getEndTime().isBefore(task.getStartTime());
			boolean tStartsAfterTask = t.getStartTime().isAfter(task.getEndTime());
			boolean tEndsAfterTask = t.getEndTime().isAfter(task.getEndTime());
			if (tStartsBeforeTask && !tEndsBeforeTask) {
				return false;
			}
			if (tStartsAfterTask && !tEndsAfterTask) {
				return false;
			}
			if (tStartsBeforeTask && tEndsAfterTask) {
				return false;
			}
			if (tStartsAfterTask && tEndsBeforeTask) {
				return false;
			}
		}
		return true;
	}

	public void addTask(Task t) {
		if (t.isMultipleQuarterHour() && isSeparatedTime(t)) {
			tasks.add(t);
		}
	}

	public boolean isWeekday() {
		boolean notSaturday = getActualDay().getDayOfWeek() != DayOfWeek.SATURDAY;
		boolean notSunday = getActualDay().getDayOfWeek() != DayOfWeek.SUNDAY;
		return notSaturday && notSunday;
	}

}
