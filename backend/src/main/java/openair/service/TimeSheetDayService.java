package openair.service;

import openair.exception.ResourceConflictException;
import openair.model.Employee;
import openair.model.TimeSheetDay;
import openair.repository.TimeSheetDayRepository;
import openair.service.interfaces.ITimeSheetDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TimeSheetDayService implements ITimeSheetDay {

    private TimeSheetDayRepository timeSheetDayRepository;

    @Autowired
    public TimeSheetDayService(TimeSheetDayRepository timeSheetDayRepository){
        this.timeSheetDayRepository = timeSheetDayRepository;

    }

    @Override
    public TimeSheetDay addTimeSheetDay(TimeSheetDay timeSheetDay, Employee employee) {

        //za employee-a i task proveriti da li je kreiran vec timeSheetDay za datum
        TimeSheetDay exist = timeSheetDayRepository.findByEmployeeIdAndTaskIdAndDate(
                employee.getId(),timeSheetDay.getTask().getId(),timeSheetDay.getDate());

        if(exist != null)
            throw new ResourceConflictException(exist.getId(),"Task is already logged for " + exist.getDate().toString() + ".");

        return timeSheetDayRepository.save(timeSheetDay);
    }

}
