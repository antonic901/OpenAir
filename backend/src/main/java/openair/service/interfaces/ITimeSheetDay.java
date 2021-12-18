package openair.service.interfaces;

import openair.model.Employee;
import openair.model.TimeSheetDay;

public interface ITimeSheetDay {
    TimeSheetDay addTimeSheetDay(TimeSheetDay timeSheetDay, Employee employee);
    TimeSheetDay getByTaskIdEmployeeId(Long taskId, Long employeeId);
}
