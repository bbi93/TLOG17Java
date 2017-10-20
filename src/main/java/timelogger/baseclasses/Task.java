package timelogger.baseclasses;

import timelogger.exceptions.NoTaskIdException;
import timelogger.exceptions.InvalidTaskIdException;
import timelogger.exceptions.ImpossibleTimeException;
import timelogger.exceptions.NotExpectedTimeOrderException;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.utils.Util;

/**
 *
 * @author bbi93
 */
public class Task {

	private String taskId;
	private LocalTime startTime;
	private LocalTime endTime;
	private String comment;

	public Task(String taskId) throws Exception {
		this.setTaskId(taskId);
	}

	public Task(String taskId, int startHour, int endHour, String comment) throws Exception {
		this.setTaskId(taskId);
		int startHourNumber = startHour / 100;
		int startMinuteNumber = startHour - startHourNumber;
		this.setStartTime(startHourNumber, startMinuteNumber);
		int endHourNumber = endHour / 100;
		int endMinuteNumber = endHour - endHourNumber;
		this.setEndTime(endHourNumber, endMinuteNumber);
		this.setComment(comment);
	}

	public Task(String taskId, String startTimeString, String endTimeString, String comment) throws Exception {
		this.setTaskId(taskId);
		this.setStartTime(startTimeString);
		this.setEndTime(endTimeString);
		this.setComment(comment);
	}

	public Task(String taskId, LocalTime startTime, LocalTime endTime, String comment) throws Exception {
		this.setTaskId(taskId);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setComment(comment);
	}

	@Override
	public String toString() {
		return "taskId=" + taskId + ", startTime=" + startTime + ", endTime=" + endTime + ", comment=" + comment;
	}

	public long getMinPerTask() throws EmptyTimeFieldException {
		long startTimeInMinutes = (this.getStartTime().getHour() * 60) + (this.getStartTime().getMinute());
		long endTimeInMinutes = (this.getEndTime().getHour() * 60) + (this.getEndTime().getMinute());
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

	public void setTaskId(String taskId) throws InvalidTaskIdException, NoTaskIdException {
		this.taskId = taskId;
		if (this.taskId != null) {
			if (this.taskId.isEmpty()) {
				throw new NoTaskIdException("Task's id cannot be empty.");
			} else if (isValidTaskId()) {
			} else {
				throw new InvalidTaskIdException("Task id is invalid.");
			}
		} else {
			throw new NoTaskIdException("Task's id cannot be null.");
		}

	}

	private void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setStartTime(int hour, int min) throws EmptyTimeFieldException, ImpossibleTimeException {
		String startTimeString = formatStringFromIntegerTime(hour, min);
		this.setStartTime(startTimeString);
	}

	public void setStartTime(String startTime) throws EmptyTimeFieldException {
		if (!startTime.isEmpty()) {
			this.setStartTime(LocalTime.parse(startTime));
		} else {
			throw new EmptyTimeFieldException("Start time field cannot be empty.");
		}
	}

	private void setEndTime(LocalTime endTime) throws EmptyTimeFieldException {
		this.endTime = Util.roundToMultipleQuarterHour(this.getStartTime(), endTime);
	}

	public void setEndTime(int hour, int min) throws NotExpectedTimeOrderException, EmptyTimeFieldException, ImpossibleTimeException {
		String endTimeString = formatStringFromIntegerTime(hour, min);
		this.setEndTime(endTimeString);
	}

	public void setEndTime(String endTimeString) throws NotExpectedTimeOrderException, EmptyTimeFieldException {
		if (!endTimeString.isEmpty()) {
			LocalTime endTimeLocal = LocalTime.parse(endTimeString);
			if (endTimeLocal.isAfter(this.getStartTime())) {
				this.setEndTime(endTimeLocal);
			} else {
				throw new NotExpectedTimeOrderException("Task cannot be ended before it starts.");
			}
		} else {
			throw new EmptyTimeFieldException("End time field cannot be empty.");
		}
	}

	public void setComment(String comment) {
		if (comment == null || comment.isEmpty()) {
			this.comment = "";
		} else {
			this.comment = comment;
		}
	}

	private String formatStringFromIntegerTime(int hour, int min) throws ImpossibleTimeException {
		if (hour >= 0 && hour < 24 && min >= 0 && min < 60) {
			StringBuilder sb = new StringBuilder(5);
			if (hour < 10) {
				sb.append("0");
			}
			sb.append(hour);
			sb.append(":");
			if (min < 10) {
				sb.append("0");
			}
			sb.append(min);
			return sb.toString();
		} else {
			throw new ImpossibleTimeException("Bad integer value for hour or min.");
		}
	}

	public String getTaskId() {
		return this.taskId;
	}

	public LocalTime getStartTime() throws EmptyTimeFieldException {
		if (this.startTime != null) {
			return this.startTime;
		} else {
			throw new EmptyTimeFieldException("Starttime not setted yet.");
		}
	}

	public LocalTime getEndTime() throws EmptyTimeFieldException {
		if (this.endTime != null) {
			return this.endTime;
		} else {
			throw new EmptyTimeFieldException("Endtime not setted yet.");
		}
	}

	public String getComment() {
		return this.comment;
	}

}
