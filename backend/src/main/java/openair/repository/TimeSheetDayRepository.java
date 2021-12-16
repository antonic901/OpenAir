package openair.repository;

import openair.model.Employee;
import openair.model.TimeSheetDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSheetDayRepository extends JpaRepository<TimeSheetDay, Long> {

    List<TimeSheetDay> findAllByEmployeeId(Long employeeId);

    TimeSheetDay findByEmployeeIdAndTaskIdAndDate(Long id, Long taskId, LocalDateTime date);
}
