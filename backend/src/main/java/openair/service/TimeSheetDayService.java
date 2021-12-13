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

    private TaskService taskService;

    @Autowired
    public TimeSheetDayService(TimeSheetDayRepository timeSheetDayRepository, TaskService taskService){
        this.timeSheetDayRepository = timeSheetDayRepository;
        this.taskService = taskService;
    }

    @Override
    public TimeSheetDay addTimeSheetDay(TimeSheetDayDTO timeSheetDayDTO, Employee employee) {
        Task task = taskService.findById(timeSheetDayDTO.getTaskId());

        TimeSheetDay timeSheetDay = new TimeSheetDay();

        timeSheetDay.setEmployee(employee);
        timeSheetDay.setTask(task);
        timeSheetDay.setDate(timeSheetDayDTO.getDate().toLocalDate());
        timeSheetDay.setWorkTime(timeSheetDayDTO.getWorkTime());

        return timeSheetDayRepository.save(timeSheetDay);
    }
}
