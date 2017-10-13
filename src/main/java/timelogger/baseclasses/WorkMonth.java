package timelogger.baseclasses;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import timelogger.baseclasses.utils.Util;

/**
 *
 * @author bbi93
 */
@Getter
public class WorkMonth {

	private List<WorkDay> days=new ArrayList<>();
	private YearMonth date;
	private long sumPerMonth;
	private long requiredMinPerMonth;

	public WorkMonth(int year, int month) {
		this.date=YearMonth.of(year, month);
	}

	public long getExtraMinPerMonth() {
		return days.stream().mapToLong(WorkDay::getExtraMinPerDay).sum();
	}

	public boolean isNewDate(WorkDay wd) {
		return days.stream().filter(d -> d.getActualDay().isEqual(wd.getActualDay())).count() == 0;
	}

	public boolean isSameMonth(WorkDay wd) {
		return date.getMonth() == wd.getActualDay().getMonth();
	}

	public void addWorkDay(WorkDay wd, boolean isWeekendEnabled) {
		if (!days.contains(wd)) {
			if (isSameMonth(wd)) {
				if ((!Util.isWeekday(wd.getActualDay()) && isWeekendEnabled) || Util.isWeekday(wd.getActualDay())) {
					days.add(wd);
				}
			}
		}
	}

	public void addWorkDay(WorkDay wd) {
		addWorkDay(wd, false);
	}
}
