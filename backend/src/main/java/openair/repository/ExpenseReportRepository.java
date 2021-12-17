package openair.repository;

import openair.model.ExpenseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {

    Optional<ExpenseReport> findByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    @Query(value = "Select ep.id, ep.date_of_creation, ep.description, ep.name, ep.money_id, " +
            "ep.status, ep.tracking_number, ep.document, ep.project_id, ep.employee_id " +
            "From users u, expensereports ep " +
            "where u.id = ep.employee_id and u.admin_id=?1",
            nativeQuery = true
    )
    List<ExpenseReport> findAllByAdminId(Long id);
}
