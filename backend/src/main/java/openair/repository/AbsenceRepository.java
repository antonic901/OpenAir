package openair.repository;

import openair.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findAllByEmployeeId(Long id);
    List<Absence> findAllByAdminId(Long id);
}
