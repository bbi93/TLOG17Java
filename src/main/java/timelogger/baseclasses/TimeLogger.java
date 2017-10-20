package timelogger.baseclasses;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author bbi93
 */
@Getter
public class TimeLogger {

	private List<WorkMonth> months = new ArrayList<>();

	public void addMonth(WorkMonth wm) {
		if (isNewMonth(wm)) {
			months.add(wm);
		}
	}

	public boolean isNewMonth(WorkMonth wm) {
		return months.stream().filter(month -> month.getDate().equals(wm.getDate())).count() == 0;
	}
}
