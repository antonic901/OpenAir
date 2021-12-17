package openair.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import openair.dto.TimeSheetDayDTO;
import openair.model.Employee;
import openair.model.Task;
import openair.model.TimeSheetDay;

public class TimeSheetDayTestData {
    public static List<TimeSheetDay> createTimeSheetDayList() {
        List<TimeSheetDay> timeSheetDayList = new ArrayList<TimeSheetDay>();

        //  id, localDate, workTime, employee, Task
        Employee employee = AbsenceTestData.createEmployee();
        Task task = TaskTestData.createTask();

        timeSheetDayList.add(new TimeSheetDay(1L, LocalDate.now(), 14, employee, task));
        timeSheetDayList.add(new TimeSheetDay(2L, LocalDate.now(), 14, employee, task));
        timeSheetDayList.add(new TimeSheetDay(3L, LocalDate.now(), 14, employee, task));
        timeSheetDayList.add(new TimeSheetDay(4L, LocalDate.now(), 14, employee, task));

        return timeSheetDayList;
    }

    public static TimeSheetDay createTimeSheetDay() {
        TimeSheetDay timeSheetDay = new TimeSheetDay();
        timeSheetDay.setWorkTime(15);
        Task task = new Task();
        task.setId(1L);
        timeSheetDay.setTask(task);
        timeSheetDay.setDate(LocalDate.now());

        return timeSheetDay;
    }


}
