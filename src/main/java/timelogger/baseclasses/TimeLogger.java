/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelogger.baseclasses;

import java.util.List;
import lombok.Data;

/**
 *
 * @author bbi93
 */
@Data
public class TimeLogger {

	private List<WorkMonth> months;
}
