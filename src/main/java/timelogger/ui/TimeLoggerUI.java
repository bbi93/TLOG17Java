package timelogger.ui;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import timelogger.exceptions.FutureWorkException;
import timelogger.baseclasses.Task;
import timelogger.baseclasses.TimeLogger;
import timelogger.baseclasses.WorkDay;
import timelogger.baseclasses.WorkMonth;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.InvalidTaskIdException;
import timelogger.exceptions.NegativeMinutesOfWorkException;
import timelogger.exceptions.NoTaskDeclaredException;
import timelogger.exceptions.NoTaskIdException;
import timelogger.exceptions.NotExpectedTimeOrderException;
import timelogger.exceptions.NotSeparatedTimesException;
import timelogger.exceptions.WeekendNotEnabledException;

/**
 *
 * @author bbi93
 */
public class TimeLoggerUI {

	private static Scanner sc = new Scanner(System.in);
	private static TimeLogger timelogger = new TimeLogger();

	public static void main(String[] args) {
		int option = 0;
		do {
			printMenu();
			option = askForInputIntegerWithConstraits("Enter the number of choosen option (0-10): ", 0, 10);
			doOption(option);
		} while (option != 0);
	}

	private static void printMenu() {
		System.out.println("***********************************************************\n"
			+ "0.  Exit" + "\n"
			+ "1.  List months" + "\n"
			+ "2.  List days" + "\n"
			+ "3.  List tasks" + "\n"
			+ "4.  Add new month" + "\n"
			+ "5.  Add day to a specific month" + "\n"
			+ "6.  Start a task for a day" + "\n"
			+ "7.  Finish a specific task" + "\n"
			+ "8.  Delete a task" + "\n"
			+ "9.  Modify task" + "\n"
			+ "10. Statistics" + "\n");
	}

	private static int askForInputInteger(String ask) {
		System.out.println(ask);
		int returnValue = Integer.MIN_VALUE;
		try {
			returnValue = sc.nextInt();
		} catch (InputMismatchException ime) {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "Not float or double in input!", ime);
			askForInputInteger(ask);
		} catch (NoSuchElementException | IllegalStateException ex) {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No input entered, or cannot be read input!", ex);
			askForInputInteger(ask);
		}
		return returnValue;
	}

	private static int askForInputIntegerWithConstraits(String ask, int minValue, int maxValue) {
		int returnValue = askForInputInteger(ask);
		if (returnValue >= minValue && returnValue <= maxValue) {
			return returnValue;
		} else {
			return askForInputIntegerWithConstraits(ask, minValue, maxValue);
		}
	}

	private static double askForInputDouble(String ask) {
		System.out.println(ask);
		double returnValue = Double.MIN_VALUE;
		try {
			returnValue = sc.nextDouble();
		} catch (InputMismatchException ime) {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "Not float or double in input!", ime);
			askForInputDouble(ask);
		} catch (NoSuchElementException | IllegalStateException ex) {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No input entered, or cannot be read input!", ex);
			askForInputDouble(ask);
		}
		return returnValue;
	}

	private static double askForInputDoubleWithConstraits(String ask, double minValue, double maxValue) {
		double returnValue = askForInputDouble(ask);
		if (returnValue >= minValue && returnValue <= maxValue) {
			return returnValue;
		} else {
			return askForInputDoubleWithConstraits(ask, minValue, maxValue);
		}
	}

	private static String askForInputString(String ask) {
		System.out.println(ask);
		String returnValue = "";
		try {
			return sc.nextLine();
		} catch (IllegalStateException ise) {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "Cannot be read input!", ise);
			askForInputString(ask);
		} catch (NoSuchElementException nse) {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No input entered!", nse);
		}
		return returnValue;
	}

	private static String askForInputStringWithFormatConstrait(String ask, String expectedFormat) {
		String returnValue;
		Matcher matcher;
		do {
			returnValue = askForInputString(ask);
			matcher = Pattern.compile(expectedFormat).matcher(returnValue);
		} while (!matcher.find() || !(matcher.start() == 0 && matcher.end() == returnValue.length()) || returnValue.isEmpty());
		return returnValue;
	}

	private static boolean askForInputboolean(String ask) {
		System.out.println(ask);
		boolean returnValue = false;
		try {
			return sc.nextBoolean();
		} catch (InputMismatchException ime) {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "Not valid boolean in input!", ime);
			askForInputboolean(ask);
		} catch (NoSuchElementException | IllegalStateException ex) {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No input entered, or cannot be read input!", ex);
			askForInputboolean(ask);
		}
		return returnValue;
	}

	private static void doOption(int option) {
		switch (option) {
			case 0:
				break;
			case 1:
				listMonths(timelogger.getMonths());
				break;
			case 2:
				listDays();
				break;
			case 3:
				listTasks();
				break;
			case 4:
				addNewMonth();
				break;
			case 5:
				addDayToMonth();
				break;
			case 6:
				startTaskForDay();
				break;
			case 7:
				finishTaskDay();
				break;
			case 8:
				deleteTask();
				break;
			case 9:
				modifyTask();
				break;
			case 10:
				printStatistics();
				break;
			default:
				Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, new StringBuilder().append("There's no option like: ").append(option).append(" !").toString());
				break;
		}
	}

	/**
	 * Print Timelogger's workmonths.
	 */
	private static void listMonths(List<WorkMonth> months) {
		if (monthsNumber(months) > 0) {
			months.stream().forEach((month) -> {
				System.out.println(months.indexOf(month)
					+ ". " + month.getDate().getYear()
					+ "-" + month.getDate().getMonth());
			});
		} else {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No month available.");
		}
	}

	/**
	 * Return count of Timelogger's workmonths.
	 *
	 * @param months
	 * @return int Number of listed months. This information can be useful to other methods.
	 */
	private static int monthsNumber(List<WorkMonth> months) {
		return months.size();
	}

	/**
	 * Print selected workmonth's workdays.
	 */
	private static void listDays() {
		WorkMonth wm = selectMonthByRowNumber(timelogger.getMonths());
		if (wm != null) {
			List<WorkDay> days = wm.getDays();
			days.stream().forEach((day) -> {
				System.out.println(days.indexOf(day)
					+ ". " + day.getActualDay().getYear()
					+ "-" + day.getActualDay().getMonthValue()
					+ "-" + day.getActualDay().getDayOfMonth());
			});
		} else {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No month available.");
		}
	}

	/**
	 *
	 * @param wmList List of workmonths which available to choose from.
	 * @return WorkMonth The choosen workmonth.
	 */
	private static WorkMonth selectMonthByRowNumber(List<WorkMonth> wmList) {
		if (monthsNumber(wmList) > 0) {
			listMonths(wmList);
			int rowNumber = askForInputIntegerWithConstraits("Select month by row number: ", 0, monthsNumber(wmList) - 1);
			if (rowNumber < monthsNumber(wmList)) {
				return wmList.get(rowNumber);
			}
		}
		return null;
	}

	/**
	 * Ask month and day then list tasks of day.
	 */
	private static void listTasks() {
		WorkMonth wm = selectMonthByRowNumber(timelogger.getMonths());
		if (wm != null) {
			WorkDay wd = selectDayByRowNumber(wm.getDays());
			if (wd != null) {
				wd.getTasks().stream().forEach((task) -> {
					System.out.println(task.toString());
				});
			} else {
				Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No day available.");
			}
		} else {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No month available.");
		}
	}

	/**
	 *
	 * @param wmList List of workmonths which available to choose from.
	 * @return WorkMonth The choosen workmonth.
	 */
	private static WorkDay selectDayByRowNumber(List<WorkDay> wdList) {
		if (daysNumber(wdList) > 0) {
			listDays();
			int rowNumber = askForInputIntegerWithConstraits("Select day by row number: ", 0, daysNumber(wdList) - 1);
			if (rowNumber < daysNumber(wdList)) {
				return wdList.get(rowNumber);
			}
		}
		return null;
	}

	/**
	 * Return count of specific workmonth's workdays.
	 *
	 * @param days
	 * @return int Number of listed days. This information can be useful to other methods.
	 */
	private static int daysNumber(List<WorkDay> days) {
		return days.size();
	}

	private static void addNewMonth() {
		int yearNumber = askForInputIntegerWithConstraits("Input year: ", 1970, LocalDate.now().getYear());
		int monthNumber = askForInputIntegerWithConstraits("Input month number: ", 1, 12);
		timelogger.addMonth(new WorkMonth(yearNumber, monthNumber));
	}

	private static void addDayToMonth() {
		WorkMonth month = selectMonthByRowNumber(timelogger.getMonths());
		if (month != null) {
			int dayNumber = askForInputIntegerWithConstraits("Input day: ", 1, month.getDate().getMonth().maxLength());
			double requiredWorkingHours = askForInputDoubleWithConstraits("Input working hours (default=7.5): ", 1 / 60, 24 * 60);
			try {
				if (requiredWorkingHours <= 0) {
					month.addWorkDay(new WorkDay(month.getDate().getYear(), month.getDate().getMonthValue(), dayNumber));
				} else {
					month.addWorkDay(new WorkDay(Math.round(requiredWorkingHours * 60), month.getDate().getYear(), month.getDate().getMonthValue(), dayNumber));
				}
			} catch (NegativeMinutesOfWorkException | DateTimeException | FutureWorkException | WeekendNotEnabledException ex) {
				Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
			}
		}
	}

	private static void startTaskForDay() {
		WorkMonth wm = selectMonthByRowNumber(timelogger.getMonths());
		if (wm != null) {
			WorkDay wd = selectDayByRowNumber(wm.getDays());
			if (wd != null) {
				Task taskToStart = new Task();
				//aks for taskId field
				while (taskToStart.getTaskId() == null) {
					try {
						taskToStart.setTaskId(askForInputString("Input task id: "));
					} catch (InvalidTaskIdException | NoTaskIdException ex) {
						Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
					}
				}
				//ask for comment field
				while (taskToStart.getComment() == null) {
					taskToStart.setComment(askForInputString("Input task comment: "));
				}
				//ask for start time field
				boolean isCorrectstartTime = false;
				while (!isCorrectstartTime) {
					Task lastTaskOfDay = null;
					String taskStartTime = null;
					try {
						lastTaskOfDay = wd.getLatestTaskOfDay();
					} catch (NoTaskDeclaredException ex) {
						Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.INFO, "Cant find 'last task'.", ex);
					}
					try {
						if (lastTaskOfDay != null) {
							taskStartTime = askForInputStringWithFormatConstrait("Input task start (default:"
								+ lastTaskOfDay.getEndTime().getHour()
								+ ":"
								+ lastTaskOfDay.getEndTime().getMinute()
								+ "): ", "\\d{2}:\\d{2}");
							if (taskStartTime.isEmpty()) {
								taskStartTime = lastTaskOfDay.getEndTime().getHour() + ":" + lastTaskOfDay.getEndTime().getMinute();

							}
						} else {
							taskStartTime = askForInputStringWithFormatConstrait("Input task start (format HH:MM):", "\\d{2}:\\d{2}");
							if (taskStartTime.isEmpty()) {
								taskStartTime = "00:00";
							}
						}
						taskToStart.setStartTime(taskStartTime);
						isCorrectstartTime = true;
					} catch (EmptyTimeFieldException ex) {
						Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
					}
				}
				//add just created task to workday
				try {
					wd.addTask(taskToStart);
				} catch (NotSeparatedTimesException | EmptyTimeFieldException ex) {
					Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
				}
			} else {
				Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No day available.");
			}
		} else {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No month available.");
		}

	}

	private static void finishTaskDay() {
		Task choosenTask = chooseExistingTask();
		if (!choosenTask.equals(null)) {
			try {
				choosenTask.setEndTime(askForInputString("Input task end time (format HH:MM):"));
			} catch (NotExpectedTimeOrderException | EmptyTimeFieldException ex) {
				Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No task has benn finished!", ex);
			}
		} else {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No unfinished task available in this workday.");
		}
	}

	private static void deleteTask() {
		Task choosenTask = chooseExistingTask();
		if (!choosenTask.equals(null)) {
			if (askForInputboolean("Are you sure to delete this task? (true/false):")) {
				for (WorkMonth month : timelogger.getMonths()) {
					for (WorkDay day : month.getDays()) {
						for (Task task : day.getTasks()) {
							if (task.equals(choosenTask)) {
								day.getTasks().remove(task);
							}
						}
					}
				}
			}
		} else {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No task available to delete.");
		}
	}

	private static void modifyTask() {
		Task choosenTask = chooseExistingTask();
		if (!choosenTask.equals(null)) {
			for (WorkMonth month : timelogger.getMonths()) {
				for (WorkDay day : month.getDays()) {
					for (Task task : day.getTasks()) {
						if (task.equals(choosenTask)) {
							try {
								String newTaskId = askForInputString("Input task's new id: ");
								newTaskId = newTaskId.isEmpty() ? choosenTask.getTaskId() : newTaskId;
								String newTaskComment = askForInputString("Input task's new comment: ");
								newTaskComment = newTaskComment.isEmpty() ? choosenTask.getComment() : newTaskComment;
								String newTaskStartTime = askForInputString("Input task's new start time (format HH:MM):");
								newTaskStartTime = newTaskStartTime.isEmpty() ? choosenTask.getStartTime().toString() : newTaskStartTime;
								String newTaskEndTime = askForInputString("Input task's new end time (format HH:MM):");
								newTaskEndTime = newTaskEndTime.isEmpty() ? choosenTask.getEndTime().toString() : newTaskEndTime;
								try {
									task.setTaskId(newTaskId);
								} catch (InvalidTaskIdException | NoTaskIdException ex) {
									Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
								}
								task.setComment(newTaskComment);
								try {
									task.setStartTime(newTaskStartTime);
								} catch (EmptyTimeFieldException ex) {
									Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
								}
								try {
									task.setEndTime(newTaskEndTime);
								} catch (NotExpectedTimeOrderException | EmptyTimeFieldException ex) {
									Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
								}
							} catch (EmptyTimeFieldException ex) {
								Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
							}
						}
					}
				}
			}
		} else {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No task available to modify.");
		}
	}

	private static void printStatistics() {
		if (timelogger.getMonths().size() > 0) {
			int monthNumber = askForInputInteger("Input month number: ");
			WorkMonth monthWithMonthNumber = timelogger.getMonths().stream().filter((month) -> month.getDate().getMonthValue() == monthNumber).findFirst().get();
			try {
				System.out.println(monthNumber
					+ ". month has " + monthWithMonthNumber.getDays().size() + " workdays. Required mins/Sum mins/Extra mins "
					+ monthWithMonthNumber.getRequiredMinPerMonth() + "/"
					+ monthWithMonthNumber.getSumPerMonth() + "/"
					+ monthWithMonthNumber.getExtraMinPerMonth());

			} catch (EmptyTimeFieldException ex) {
				Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, ex);
			}
			System.out.println("Days statistics:");
			for (WorkDay workDay : monthWithMonthNumber.getDays()) {
				try {
					System.out.println(workDay.toStatistics());
				} catch (EmptyTimeFieldException ex) {
					Logger.getLogger(TimeLoggerUI.class
						.getName()).log(Level.WARNING, null, ex);
				}
			}
		} else {
			System.err.println("No month available.");
		}
	}

	private static Task chooseExistingTask() {
		Task choosenTask = null;
		WorkMonth wm = selectMonthByRowNumber(timelogger.getMonths());
		if (wm != null) {
			WorkDay wd = selectDayByRowNumber(wm.getDays());
			if (wd != null) {
				List<Task> endlessTasks = new ArrayList<>();
				for (Task task : wd.getTasks()) {
					try {
						task.getEndTime();
					} catch (EmptyTimeFieldException e) {
						endlessTasks.add(task);
					}
				}
				endlessTasks.stream().forEach((task) -> {
					System.out.println(task.toString());
				});
				String taskId = askForInputString("Input task id: ");
				LocalTime startTimeofChoosenTask = null;
				while (startTimeofChoosenTask == null) {
					startTimeofChoosenTask = LocalTime.parse(askForInputStringWithFormatConstrait("Input task start (format HH:MM):", "\\d{2}:\\d{2}"));
				}
				for (Task endlessTask : endlessTasks) {
					boolean isTaskIdSame = taskId.equals(endlessTask.getTaskId());
					boolean isStartTimeSame = false;
					try {
						if (endlessTask.getStartTime().equals(startTimeofChoosenTask)) {
							isStartTimeSame = true;
						}
					} catch (EmptyTimeFieldException etfe) {
						Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, null, etfe);
					}
					if (isTaskIdSame && isStartTimeSame) {
						choosenTask = endlessTask;
						break;
					}
				}
			} else {
				Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No day available.");
			}
		} else {
			Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.WARNING, "No month available.");
		}
		return choosenTask;
	}

}
