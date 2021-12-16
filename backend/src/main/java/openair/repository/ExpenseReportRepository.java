package openair.repository;

import openair.model.ExpenseReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {

    Optional<ExpenseReport> findByProjectIdAndEmployeeId(Long projectId, Long employeeId);
}
