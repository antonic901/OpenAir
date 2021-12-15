package openair.service.interfaces;

import openair.dto.ExspenseReportDTO;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Project;
import openair.model.enums.Status;

import java.util.List;

public interface IExpenseReportService {
    List<ExpenseReport> getAllByAdminId(Long id);
    ExpenseReport addReport(ExspenseReportDTO expenseReportDTO, Employee employee);
    ExpenseReport reviewReport(Long reportId, Status status);
}
