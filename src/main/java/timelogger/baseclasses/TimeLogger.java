package timelogger.baseclasses;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author bbi93
 */
@Data
public class TimeLogger {

	private List<WorkMonth> months = new ArrayList<>();

	public void addMonth(WorkMonth wm) {
		if (isNewMonth(wm)) {
			months.add(wm);
		}
	}

	/**
	 *
	 * @param wm Workmonth to check.
	 * @return boolean Return treu, if workmonth list not contains workmonth with same value date field.
	 */
	public boolean isNewMonth(WorkMonth wm) {
		return months.stream().filter(month -> month.getDate().equals(wm.getDate())).count() == 0;
	}
}
