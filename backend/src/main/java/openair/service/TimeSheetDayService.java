package openair.service;

import liquibase.pro.packaged.T;
import openair.dto.TimeSheetDayDTO;
import openair.exception.NotFoundException;
import openair.exception.ResourceConflictException;
import openair.model.Employee;
import openair.model.Task;
import openair.model.TimeSheetDay;
import openair.repository.TimeSheetDayRepository;
import openair.service.interfaces.ITimeSheetDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        //za employee-a i task proveriti da li je kreiran vec timeSheetDay za datum
        TimeSheetDay exist = timeSheetDayRepository.findByEmployeeIdAndTaskIdAndDate(
                employee.getId(),timeSheetDayDTO.getTaskId(),timeSheetDayDTO.getDate().toLocalDate());

        if(exist != null)
            throw new ResourceConflictException(exist.getId(),"Task is already logged for " + exist.getDate().toString() + ".");

        Task task = taskService.findById(timeSheetDayDTO.getTaskId());

        TimeSheetDay timeSheetDay = new TimeSheetDay();

        timeSheetDay.setEmployee(employee);
        timeSheetDay.setTask(task);
        timeSheetDay.setDate(timeSheetDayDTO.getDate().toLocalDate());
        timeSheetDay.setWorkTime(timeSheetDayDTO.getWorkTime());

        return timeSheetDayRepository.save(timeSheetDay);
    }

    @Override
    public TimeSheetDay getByTaskIdEmployeeId(Long taskId, Long employeeId) {
        for(TimeSheetDay timeSheetDay : timeSheetDayRepository.findAll()) {
            if(timeSheetDay.getTask().getId() == taskId && timeSheetDay.getEmployee().getId() == employeeId)
                return timeSheetDay;
        }

        return null;
    }
}
