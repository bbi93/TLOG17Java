/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelogger.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import timelogger.baseclasses.Task;
import timelogger.exceptions.EmptyTimeFieldException;

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
				endTime=endTime.minusMinutes(mod);
			} else {
				endTime=endTime.plusMinutes(15 - mod);
			}
		}
		return endTime;
	}

	public static boolean isSeparatedTime(Task t, Collection<Task> tasks) throws EmptyTimeFieldException {
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

	public static boolean isWeekday(LocalDate actualDay) {
		boolean notSaturday = actualDay.getDayOfWeek() != DayOfWeek.SATURDAY;
		boolean notSunday = actualDay.getDayOfWeek() != DayOfWeek.SUNDAY;
		return notSaturday && notSunday;
	}

	public static boolean isMultipleQuarterHour(long taskMinutes) {
		return taskMinutes % 15 == 0;
	}

}
