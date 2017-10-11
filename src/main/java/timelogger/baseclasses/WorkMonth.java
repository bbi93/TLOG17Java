package timelogger.baseclasses;

import java.time.YearMonth;
import java.util.List;
import lombok.Data;

/**
 *
 * @author bbi93
 */
@Data
public class WorkMonth {

	private List<WorkDay> days;
	private YearMonth date;
	private long sumPerMonth;
	private long requiredMinPerMonth;
}
