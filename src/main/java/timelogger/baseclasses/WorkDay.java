package timelogger.baseclasses;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

/**
 *
 * @author bbi93
 */
@Data
public class WorkDay {

	private List<Task> tasks;
	private long requiredMinPerDay;
	private LocalDate actualDay;
	private long sumPerDay;
}
