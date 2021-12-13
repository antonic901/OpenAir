package openair.service.interfaces;

import openair.dto.TimeSheetDayDTO;
import openair.model.Employee;
import openair.model.TimeSheetDay;
import java.util.List;

public interface ITimeSheetDay {
    TimeSheetDay addTimeSheetDay(TimeSheetDayDTO timeSheetDayDTO, Employee employee);
    TimeSheetDay getByTaskIdEmployeeId(Long taskId, Long employeeId);
}
