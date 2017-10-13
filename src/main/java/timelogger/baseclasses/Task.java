package timelogger.baseclasses;

import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;

/**
 *
 * @author bbi93
 */
@Getter
public class Task {

	private String taskId;
	private LocalTime startTime;
	private LocalTime endTime;
	private String comment;

	public Task(String taskId) {
		setTaskId(taskId);
	}

	public Task(String taskId, int startHour, int endHour, String comment) {
		setTaskId(taskId);
		int startHourNumber = startHour / 100;
		int startMinuteNumber = startHour - startHourNumber;
		setStartTime(startHourNumber, startMinuteNumber);
		int endHourNumber = endHour / 100;
		int endMinuteNumber = endHour - endHourNumber;
		setEndTime(endHourNumber, endMinuteNumber);
		setComment(comment);
	}

	public Task(String taskId, String startTimeString, String endTimeString, String comment) {
		setTaskId(taskId);
		setStartTime(startTimeString);
		setEndTime(endTimeString);
		setComment(comment);
	}

	public long getMinPerTask() {
		long endTimeInMinutes = (endTime.getHour() * 60) + (endTime.getMinute());
		long startTimeInMinutes = (startTime.getHour() * 60) + (startTime.getMinute());
		return endTimeInMinutes - startTimeInMinutes;
	}

	private boolean isValidLTTaskId() {
		Matcher matcher = Pattern.compile("LT-\\d{4}").matcher(taskId);
		if (matcher.find()) {
			if ((matcher.start() == 0) && (matcher.end() == taskId.length())) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidRedmineTaskId() {
		Matcher matcher = Pattern.compile("\\d{4}").matcher(taskId);
		if (matcher.find()) {
			if ((matcher.start() == 0) && (matcher.end() == taskId.length())) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidTaskId() {
		if (isValidLTTaskId() || isValidRedmineTaskId()) {
			return true;
		}
		return false;
	}

	public boolean isMultipleQuarterHour() {
		return getMinPerTask() % 15 == 0;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setStartTime(int hour, int min) {
		this.startTime = LocalTime.of(hour, min);
	}

	public void setStartTime(String startTime) {
		this.startTime = LocalTime.parse(startTime);
	}

	public void setEndTime(int hour, int min) {
		this.endTime = LocalTime.of(hour, min);
	}

	public void setEndTime(String endTime) {
		this.endTime = LocalTime.parse(endTime);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
