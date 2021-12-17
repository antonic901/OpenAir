package openair.repository;

import openair.model.Absence;
import openair.model.enums.Status;
import openair.utils.AbsenceInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findAllByEmployeeId(Long id);
    List<Absence> findAllByAdminId(Long id);

    @Query(value = "select a.start_time as Start_date, a.end_time as End_date " +
            "from absences a " +
            "where a.employee_id = ?1 AND " +
            "a.status like 'APPROVED' AND " +
            "(extract(month from a.start_time) = ?2 AND " +
            "extract(year from a.start_time) = ?3)", nativeQuery = true)
    //does not need to end this month
    List<AbsenceInterface> findAllOfCurrentMonth(Long employeeId, int month, int year);


}
