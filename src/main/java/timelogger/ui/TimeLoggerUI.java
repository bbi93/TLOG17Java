/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelogger.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import timelogger.baseclasses.Task;
import timelogger.baseclasses.TimeLogger;
import timelogger.baseclasses.WorkDay;
import timelogger.baseclasses.WorkMonth;

/**
 *
 * @author bbi93
 */
public class TimeLoggerUI {

	private static TimeLogger timelogger = new TimeLogger();

	public static void main(String[] args) {
		int option = 0;
		do {
			printMenu();
			option = askForInputOption("Enter the number of choosen option (0-10): ");
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

	private static int askForInputOption(String ask) {
		System.out.println(ask);
		Scanner sc = new Scanner(System.in);
		sc.findInLine(Pattern.compile("(\\d+)"));
		return Integer.parseInt(sc.match().group(1));
	}

	private static int askForInputInteger(String ask) {
		System.out.println(ask);
		Scanner sc = new Scanner(System.in);
		sc.findInLine(Pattern.compile("(\\d+)"));
		return Integer.parseInt(sc.match().group(1));
	}

	private static double askForInputDouble(String ask) {
		System.out.println(ask);
		Scanner sc = new Scanner(System.in);
		sc.findInLine(Pattern.compile("(\\d+\\.\\d+)"));
		return Double.parseDouble(sc.match().group(1));
	}

	private static String askForInputString(String ask) {
		try {
			System.out.println(ask);
			Scanner sc = new Scanner(System.in);
			return sc.nextLine();
		} catch (NoSuchElementException nsee) {
			System.err.println(nsee.getMessage());
			return "";
		}
	}

	private static boolean askForInputboolean(String ask) {
		try {
			System.out.println(ask);
			Scanner sc = new Scanner(System.in);
			return sc.nextBoolean();
		} catch (NoSuchElementException nsee) {
			System.err.println(nsee.getMessage());
			return false;
		}
	}

	private static void doOption(int option) {
		switch (option) {
			case 0:
				break;
			case 1:
				listMonths();
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
				System.err.println("There's no option like: " + option + " !");
				break;
		}
	}

	private static int listMonths() {
		List<WorkMonth> months = timelogger.getMonths();
		months.stream().forEach((month) -> {
			System.out.println(months.indexOf(month)
				+ ". " + month.getDate().getYear()
				+ "-" + month.getDate().getMonth());
		});
		return months.size();
	}

	private static void listDays() {
		if (listMonths() > 0) {
			int rowNumber = askForInputOption("Select one by row number: ");
			List<WorkDay> days = timelogger.getMonths().get(rowNumber).getDays();
			days.stream().forEach((day) -> {
				System.out.println(days.indexOf(day)
					+ ". " + day.getActualDay().getYear()
					+ "-" + day.getActualDay().getMonthValue()
					+ "-" + day.getActualDay().getDayOfMonth());
			});
		} else {
			System.err.println("No month found!");
		}
	}

	private static void listTasks() {
		if (timelogger.getMonths().size() > 0) {
			int monthNumber = askForInputInteger("Input month number: ");
			WorkMonth monthWithMonthNumber = timelogger.getMonths().stream().filter((month) -> month.getDate().getMonthValue() == monthNumber).findFirst().get();
			if (monthWithMonthNumber.getDays().size() > 0) {
				int dayNumber = askForInputInteger("Input day number: ");
				WorkDay dayWithDayNumber = monthWithMonthNumber.getDays().stream().filter((day) -> day.getActualDay().getDayOfMonth() == dayNumber).findFirst().get();
				dayWithDayNumber.getTasks().stream().forEach((task) -> {
					System.out.println(task.toString());
				});
			} else {
				System.err.println("No day available in this month.");
			}
		} else {
			System.err.println("No month available.");
		}
	}

	private static void addNewMonth() {
		int yearNumber = askForInputInteger("Input year: ");
		int monthNumber = askForInputInteger("Input month number: ");
		timelogger.addMonth(new WorkMonth(LocalDate.now().getYear(), monthNumber));
	}

	private static void addDayToMonth() {
		if (listMonths() > 0) {
			int rowNumber = askForInputOption("Select month by row number: ");
			WorkMonth month = timelogger.getMonths().get(rowNumber);
			if (month != null) {
				int dayNumber = askForInputInteger("Input day: ");
				double requiredWorkingHours = askForInputDouble("Input working hours (default=7.5): ");
				if (requiredWorkingHours <= 0) {
					month.addWorkDay(new WorkDay(month.getDate().getYear(), rowNumber, dayNumber));
				} else {
					month.addWorkDay(new WorkDay(Math.round(requiredWorkingHours * 60), month.getDate().getYear(), rowNumber, dayNumber));
				}

			}
		}
	}

	private static void startTaskForDay() {
		if (timelogger.getMonths().size() > 0) {
			int monthNumber = askForInputInteger("Input month number: ");
			WorkMonth monthWithMonthNumber = timelogger.getMonths().stream().filter((month) -> month.getDate().getMonthValue() == monthNumber).findFirst().get();
			if (monthWithMonthNumber.getDays().size() > 0) {
				int dayNumber = askForInputInteger("Input day number: ");
				WorkDay dayWithDayNumber = monthWithMonthNumber.getDays().stream().filter((day) -> day.getActualDay().getDayOfMonth() == dayNumber).findFirst().get();
				String taskId = askForInputString("Input task id: ");
				String taskComment = askForInputString("Input task comment: ");
				Task lastTask = dayWithDayNumber.getLatestTaskOfDay();
				String taskStartTime;
				if (!lastTask.equals(null)) {
					taskStartTime = askForInputString("Input task start (default:" + lastTask.getEndTime().getHour() + ":" + lastTask.getEndTime().getMinute() + "): ");
					if (taskStartTime.isEmpty()) {
						taskStartTime = lastTask.getEndTime().getHour() + ":" + lastTask.getEndTime().getMinute();
					}
				} else {
					taskStartTime = askForInputString("Input task start (format HH:MM):");
					if (taskStartTime.isEmpty()) {
						taskStartTime = "00:00";
					}
				}
				Task newTask = new Task(taskId);
				newTask.setStartTime(taskStartTime);
				newTask.setComment(taskComment);
				dayWithDayNumber.addTask(newTask);
			} else {
				System.err.println("No day available in this month.");
			}
		} else {
			System.err.println("No month available.");
		}
	}

	private static void finishTaskDay() {
		if (timelogger.getMonths().size() > 0) {
			int monthNumber = askForInputInteger("Input month number: ");
			WorkMonth monthWithMonthNumber = timelogger.getMonths().stream().filter((month) -> month.getDate().getMonthValue() == monthNumber).findFirst().get();
			if (monthWithMonthNumber.getDays().size() > 0) {
				int dayNumber = askForInputInteger("Input day number: ");
				WorkDay dayWithDayNumber = monthWithMonthNumber.getDays().stream().filter((day) -> day.getActualDay().getDayOfMonth() == dayNumber).findFirst().get();
				List<Task> endlessTasks = dayWithDayNumber.getTasks().stream().filter((task) -> Objects.isNull(task.getEndTime())).collect(Collectors.toList());
				endlessTasks.stream().forEach((task) -> {
					System.out.println(task.toString());
				});
				String taskId = askForInputString("Input task id: ");
				Task choosenTask = endlessTasks.stream().filter((task) -> task.getTaskId().equals(taskId)).findFirst().get();
				if (!choosenTask.equals(null)) {
					choosenTask.setEndTime(askForInputString("Input task start (format HH:MM):"));
				} else {
					System.err.println("No unfinished task available in this workday.");
				}
			} else {
				System.err.println("No day available in this month.");
			}
		} else {
			System.err.println("No month available.");
		}
	}

	private static void deleteTask() {
		if (timelogger.getMonths().size() > 0) {
			int monthNumber = askForInputInteger("Input month number: ");
			WorkMonth monthWithMonthNumber = timelogger.getMonths().stream().filter((month) -> month.getDate().getMonthValue() == monthNumber).findFirst().get();
			if (monthWithMonthNumber.getDays().size() > 0) {
				int dayNumber = askForInputInteger("Input day number: ");
				WorkDay dayWithDayNumber = monthWithMonthNumber.getDays().stream().filter((day) -> day.getActualDay().getDayOfMonth() == dayNumber).findFirst().get();
				String taskId = askForInputString("Input task id: ");
				Task choosenTask = dayWithDayNumber.getTasks().stream().filter((task) -> task.getTaskId().equals(taskId)).findFirst().get();
				if (!choosenTask.equals(null)) {
					if (askForInputboolean("Are you sure to delete this task? (true/false):")) {
						for (WorkMonth month : timelogger.getMonths()) {
							for (WorkDay day : month.getDays()) {
								for (Task task : day.getTasks()) {
									if (task.getTaskId().equals(choosenTask.getTaskId())) {
										day.getTasks().remove(task);
									}
								}
							}
						}
					}
				} else {
					System.err.println("No task available to delete.");
				}
			} else {
				System.err.println("No day available in this month.");
			}
		} else {
			System.err.println("No month available.");
		}
	}

	private static void modifyTask() {
		if (timelogger.getMonths().size() > 0) {
			int monthNumber = askForInputInteger("Input month number: ");
			WorkMonth monthWithMonthNumber = timelogger.getMonths().stream().filter((month) -> month.getDate().getMonthValue() == monthNumber).findFirst().get();
			if (monthWithMonthNumber.getDays().size() > 0) {
				int dayNumber = askForInputInteger("Input day number: ");
				WorkDay dayWithDayNumber = monthWithMonthNumber.getDays().stream().filter((day) -> day.getActualDay().getDayOfMonth() == dayNumber).findFirst().get();
				String taskId = askForInputString("Input task id: ");
				Task choosenTask = dayWithDayNumber.getTasks().stream().filter((task) -> task.getTaskId().equals(taskId)).findFirst().get();
				if (!choosenTask.equals(null)) {
					for (WorkMonth month : timelogger.getMonths()) {
						for (WorkDay day : month.getDays()) {
							for (Task task : day.getTasks()) {
								if (task.getTaskId().equals(choosenTask.getTaskId())) {
									String newTaskId = askForInputString("Input task's new id: ");
									newTaskId = newTaskId.isEmpty() ? choosenTask.getTaskId() : newTaskId;
									String newTaskComment = askForInputString("Input task's new comment: ");
									newTaskComment = newTaskComment.isEmpty() ? choosenTask.getComment() : newTaskComment;
									String newTaskStartTime = askForInputString("Input task's new start time (format HH:MM):");
									newTaskStartTime = newTaskStartTime.isEmpty() ? choosenTask.getStartTime().toString() : newTaskStartTime;
									String newTaskEndTime = askForInputString("Input task's new end time (format HH:MM):");
									newTaskEndTime = newTaskEndTime.isEmpty() ? choosenTask.getEndTime().toString() : newTaskEndTime;
									task.setTaskId(newTaskId);
									task.setComment(newTaskComment);
									task.setStartTime(newTaskStartTime);
									task.setEndTime(newTaskEndTime);
								}throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
							}
						}
					}
				} else {
					System.err.println("No task available to delete.");
				}
			} else {
				System.err.println("No day available in this month.");
			}
		} else {
			System.err.println("No month available.");
		}
	}

	private static void printStatistics() {
		if (timelogger.getMonths().size() > 0) {
			int monthNumber = askForInputInteger("Input month number: ");
			WorkMonth monthWithMonthNumber = timelogger.getMonths().stream().filter((month) -> month.getDate().getMonthValue() == monthNumber).findFirst().get();
			System.out.println(monthNumber+
				". month has "+monthWithMonthNumber.getDays().size()+" workdays. Required mins/Sum mins/Extra mins "+
				monthWithMonthNumber.getRequiredMinPerMonth()+"/"+
				monthWithMonthNumber.getSumPerMonth()+"/"+
				monthWithMonthNumber.getExtraMinPerMonth());
			System.out.println("Days statistics:");
			for (WorkDay workDay : monthWithMonthNumber.getDays()) {
				System.out.println(workDay.toStatistics());
			}
		}
		else {
			System.err.println("No month available.");
		}
	}

}
