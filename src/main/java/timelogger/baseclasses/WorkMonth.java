package timelogger.baseclasses;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.WeekendNotEnabledException;
import timelogger.utils.Util;

/**
 *
 * @author bbi93
 */
@Getter
public class WorkMonth {

	private List<WorkDay> days = new ArrayList<>();
	private YearMonth date;
	private long sumPerMonth;
	private long requiredMinPerMonth;
	private boolean isWeekendEnabled;

	public WorkMonth(int year, int month) {
		this(year, month, false);
	}

	public WorkMonth(int year, int month, boolean isWeekendEnabled) {
		this.date = YearMonth.of(year, month);
		this.isWeekendEnabled = isWeekendEnabled;
	}

	public long getExtraMinPerMonth() throws EmptyTimeFieldException {
		long extramin = 0;
		for (WorkDay day : days) {
			extramin += day.getExtraMinPerDay();
		}
		return extramin;
	}

	public long getSumPerMonth() throws EmptyTimeFieldException {
		long monthsum = 0;
		for (WorkDay day : days) {
			monthsum += day.getSumPerDay();
		}
		return monthsum;
	}

	public long getRequiredMinPerMonth() {
		long requiredMonthSum = 0;
		for (WorkDay day : days) {
			requiredMonthSum += day.getRequiredMinPerDay();
		}
		return requiredMonthSum;
	}

	public boolean isNewDate(WorkDay wd) {
		return days.stream().filter(d -> d.getActualDay().isEqual(wd.getActualDay())).count() == 0;
	}

	public boolean isSameMonth(WorkDay wd) {
		return date.getMonth() == wd.getActualDay().getMonth();
	}

	public void addWorkDay(WorkDay wd) throws WeekendNotEnabledException {
		if (!days.contains(wd)) {
			if (isSameMonth(wd)) {
				if ((!Util.isWeekday(wd.getActualDay()) && isWeekendEnabled) || Util.isWeekday(wd.getActualDay())) {
					days.add(wd);
				} else {
					throw new WeekendNotEnabledException("Given workday is on weekend, but it's not");
				}
			}
		}
	}
}
