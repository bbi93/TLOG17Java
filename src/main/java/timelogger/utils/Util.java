package timelogger.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import timelogger.baseclasses.Task;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.NotExpectedTimeOrderException;

/**
 *
 * @author bbi93
 */
public class Util {

	public static LocalTime roundToMultipleQuarterHour(LocalTime startTime, LocalTime endTime) {
		int timeDiffInMinutes = (endTime.toSecondOfDay() / 60) - (startTime.toSecondOfDay() / 60);
		if (!isMultipleQuarterHour(timeDiffInMinutes)) {
			long mod = timeDiffInMinutes % 15;
			if (mod < 8) {
				endTime = endTime.minusMinutes(mod);
			} else {
				endTime = endTime.plusMinutes(15 - mod);
			}
		}
		return endTime;
	}

	public static boolean isSeparatedTime(Task t, Collection<Task> tasks) throws EmptyTimeFieldException {
		for (Task task : tasks) {
			//if task starts when other task starts
			if (t.getStartTime().equals(task.getStartTime())) {
				return false;
			}
			//if task ends when other task ends
			if (t.getEndTime().equals(task.getEndTime())) {
				return false;
			}
			//if starttime inside other task
			if (t.getStartTime().isAfter(task.getStartTime()) && t.getStartTime().isBefore(task.getEndTime())) {
				return false;
			}
			//if endtime inside other task
			if (t.getEndTime().isAfter(task.getStartTime()) && t.getEndTime().isBefore(task.getEndTime())) {
				return false;
			}
			//if task is around of other task
			if (t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getEndTime())) {
				return false;
			}
		}
		return true;
	}

	public static boolean isWeekday(LocalDate actualDay) {
		boolean notSaturday = actualDay.getDayOfWeek() != DayOfWeek.SATURDAY;
		boolean notSunday = actualDay.getDayOfWeek() != DayOfWeek.SUNDAY;
		return notSaturday && notSunday;
	}

	public static boolean isMultipleQuarterHour(long taskMinutes) {
		return taskMinutes % 15 == 0;
	}

	public static boolean isMultipleQuarterHour(LocalTime startTime, LocalTime endTime) throws NotExpectedTimeOrderException, EmptyTimeFieldException {
		if (startTime == null || endTime == null) {
			throw new EmptyTimeFieldException("Some time field is not setted.");
		} else if (startTime.isBefore(endTime)) {
			int taskMinutes = (endTime.toSecondOfDay() / 60) - (startTime.toSecondOfDay() / 60);
			return Util.isMultipleQuarterHour(taskMinutes);
		} else {
			throw new NotExpectedTimeOrderException("Bad time order.");
		}
	}

}
