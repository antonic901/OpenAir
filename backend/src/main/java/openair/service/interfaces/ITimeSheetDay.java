package openair.service.interfaces;

import openair.dto.TimeSheetDayDTO;
import openair.model.Employee;
import openair.model.TimeSheetDay;

public interface ITimeSheetDay {
    TimeSheetDay addTimeSheetDay(TimeSheetDayDTO timeSheetDayDTO, Employee employee);
}
