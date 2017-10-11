package timelogger.baseclasses;

import java.time.LocalTime;
import lombok.Data;

/**
 *
 * @author bbi93
 */
@Data
public class Task {

	private String taskId;
	private LocalTime startTime;
	private LocalTime endTime;
	private String comment;

}
