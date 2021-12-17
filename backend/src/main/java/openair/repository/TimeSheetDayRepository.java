package openair.repository;

import openair.model.Employee;
import openair.model.TimeSheetDay;
import openair.utils.PdfTableDataOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeSheetDayRepository extends JpaRepository<TimeSheetDay, Long> {

    TimeSheetDay findByEmployeeIdAndTaskIdAndDate(Long id, Long taskId, LocalDate date);

    @Query(value = "Select u.name as \"Employee\", p.name as \"Project\", sum(tsd.work_time) as \"Work_time\", p.project_type as \"Project_type\" " +
            "From time_sheet_day tsd, tasks t, projects p, users u " +
            "where tsd.task_id = t.id " +
            "and u.id = tsd.employee_id " +
            "and p.id = t.project_id " +
            "group by u.name, t.project_id, p.name, p.project_type",
            nativeQuery = true)
    List<PdfTableDataOutput> getDataForPdf();

    @Query(value = "select tsd.date " +
            "from time_sheet_day tsd " +
            "where tsd.employee_id = ?1 AND " +
            "(extract(month from tsd.date) = ?2 AND " +
            "extract(year from tsd.date) = ?3)", nativeQuery = true)
    List<LocalDate> findAllOfCurrentMonth(Long employeeId, int value, int year);
}
