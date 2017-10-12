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

	public Task(String taskId, LocalTime startTime, LocalTime endTime, String comment) {
		this.taskId = taskId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.comment = comment;
	}

	public Task(String taskId, int startHour, int endHour, String comment) {
		this.taskId = taskId;
		int startHourNumber = startHour / 100;
		int startMinuteNumber = startHour - startHourNumber;
		this.startTime = LocalTime.of(startHourNumber, startMinuteNumber);
		int endHourNumber = endHour / 100;
		int endMinuteNumber = endHour - endHourNumber;
		this.endTime = LocalTime.of(endHourNumber, endMinuteNumber);
		this.comment = comment;
	}

	public Task(String taskId, String startTimeString, String endTimeString, String comment) {
		this.taskId = taskId;
		this.startTime = LocalTime.parse(startTimeString);
		this.endTime = LocalTime.parse(endTimeString);
		this.comment = comment;
	}

	public long getMinPerTask() {
		long endTimeInMinutes = (endTime.getHour() * 60) + (endTime.getMinute());
		long startTimeInMinutes = (startTime.getHour() * 60) + (startTime.getMinute());
		return endTimeInMinutes - startTimeInMinutes;
	}

	public boolean isValidTaskId() {
		Collection<Pattern> patternCollection = new LinkedList<>();
		patternCollection.add(Pattern.compile("LT-\\d{4}"));
		patternCollection.add(Pattern.compile("\\d{4}"));

		for (Pattern pattern : patternCollection) {

			Matcher matcher = pattern.matcher(taskId);

			if (matcher.find()) {
				if ((matcher.start() == 0) && (matcher.end() == taskId.length())) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isMultipleQuarterHour() {
		return getMinPerTask() % 15 == 0;
	}

}
