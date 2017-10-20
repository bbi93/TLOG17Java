package timelogger.baseclasses;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.NegativeMinutesOfWorkException;
import timelogger.exceptions.NoTaskDeclaredException;
import timelogger.exceptions.NotSeparatedTimesException;
import timelogger.utils.Util;

/**
 *
 * @author bbi93
 */
@Getter
public class WorkDay {

	private static final long DEFAULT_REQUIRED_MIN_PER_DAY = 450;

	private List<Task> tasks = new ArrayList<>();
	private long requiredMinPerDay;
	private LocalDate actualDay;
	private long sumPerDay;

	public WorkDay() throws NegativeMinutesOfWorkException, FutureWorkException {
		this(LocalDate.now());
	}

	public WorkDay(LocalDate actualDay) throws NegativeMinutesOfWorkException, FutureWorkException {
		this(DEFAULT_REQUIRED_MIN_PER_DAY, actualDay);
	}

	public WorkDay(long requiredMinPerDay) throws NegativeMinutesOfWorkException, FutureWorkException {
		this(requiredMinPerDay, LocalDate.now());
	}

	public WorkDay(int year, int month, int day) throws NegativeMinutesOfWorkException, DateTimeException, FutureWorkException {
		this(DEFAULT_REQUIRED_MIN_PER_DAY, LocalDate.of(year, month, day));
	}

	public WorkDay(long requiredMinPerDay, int year, int month, int day) throws NegativeMinutesOfWorkException, DateTimeException, FutureWorkException {
		this(requiredMinPerDay, LocalDate.of(year, month, day));
	}

	public WorkDay(long requiredMinPerDay, LocalDate actualDay) throws NegativeMinutesOfWorkException, FutureWorkException {
		this.setRequiredMinPerDay(requiredMinPerDay);
		this.setActualDay(actualDay);
	}

	public long getSumPerDay() throws EmptyTimeFieldException {
		long daySum = 0;
		for (Task task : tasks) {
			try {
				daySum += task.getMinPerTask();
			} catch (EmptyTimeFieldException ex) {
				throw new EmptyTimeFieldException(task.getTaskId() + " (" + task.getStartTime() + ") task has unsetted time fields.");
			}
		}
		return daySum;
	}

	public void setRequiredMinPerDay(long requiredMinPerDay) throws NegativeMinutesOfWorkException {
		if (requiredMinPerDay >= 0) {
			this.requiredMinPerDay = requiredMinPerDay;
		} else {
			throw new NegativeMinutesOfWorkException("Required minutes cannot be negative.");
		}
	}

	public void setActualDay(LocalDate actualDay) throws FutureWorkException {
		if (LocalDate.now().isBefore(actualDay)) {
			throw new FutureWorkException("");
		} else {
			this.actualDay = actualDay;
		}
	}

	public void setActualDay(int year, int month, int day) throws DateTimeException, FutureWorkException {
		this.setActualDay(LocalDate.of(year, month, day));
	}

	public long getExtraMinPerDay() throws EmptyTimeFieldException {
		return getSumPerDay() - getRequiredMinPerDay();
	}

	public void addTask(Task t) throws NotSeparatedTimesException, EmptyTimeFieldException {
		if (Util.isSeparatedTime(t, this.getTasks())) {
			tasks.add(t);
		} else {
			throw new NotSeparatedTimesException("Task has time conflict with other task in workday.");
		}
	}

	public Task getLatestTaskOfDay() throws NoTaskDeclaredException {
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
		throw new NoTaskDeclaredException("No task in this workday.");
	}

	public LocalTime endTimeOfTheLastTask() throws EmptyTimeFieldException, NoTaskDeclaredException {
		Task task = this.getLatestTaskOfDay();
		return task.getEndTime();
	}

	public String toStatistics() throws EmptyTimeFieldException {
		return actualDay + " Task number:" + tasks.size() + " Logged time:" + getSumPerDay() + " Extra time:" + getExtraMinPerDay() + ".";
	}

}
