package openair.repository;

import openair.model.Absence;
import openair.model.enums.Status;
import openair.utils.AbsenceInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findAllByEmployeeId(Long id);
    @Query(value = "select * " +
            "from absences a " +
            "where a.employee_id = ?1 AND " +
            "a.status like 'APPROVED' OR a.status like 'INPROCESS' " +
            "AND (?2 <= a.end_time) AND (a.start_time <= ?3) ", nativeQuery = true)
    Optional<Absence> findAllByEmployeeIdAndStatus(Long id, LocalDateTime start, LocalDateTime end);

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
