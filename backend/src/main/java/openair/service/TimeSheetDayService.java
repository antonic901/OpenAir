package openair.service;

import openair.dto.TimeSheetDayDTO;
import openair.model.Employee;
import openair.model.Task;
import openair.model.TimeSheetDay;
import openair.repository.TimeSheetDayRepository;
import openair.service.interfaces.ITimeSheetDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSheetDayService implements ITimeSheetDay {

    private TimeSheetDayRepository timeSheetDayRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    public TimeSheetDayService(TimeSheetDayRepository timeSheetDayRepository){
        this.timeSheetDayRepository = timeSheetDayRepository;
    }

    @Override
    public TimeSheetDay addTimeSheetDay(TimeSheetDayDTO timeSheetDayDTO, Employee employee) {
        Task task = taskService.findById(timeSheetDayDTO.getTaskId());

        TimeSheetDay timeSheetDay = new TimeSheetDay();

        timeSheetDay.setEmployee(employee);
        timeSheetDay.setTask(task);
        timeSheetDay.setDate(timeSheetDayDTO.getDate());
        timeSheetDay.setWorkTime(timeSheetDayDTO.getWorkTime());
        timeSheetDay.setApproved(false);

        return timeSheetDayRepository.save(timeSheetDay);
    }
}
