package openair.service.interfaces;

import openair.dto.ExspenseReportDTO;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Project;

public interface IExpenseReportService {
    ExpenseReport addReport(ExspenseReportDTO expenseReportDTO, Employee employee, Project project);

    ExpenseReport approveReport(Long reportId);

    ExpenseReport rejectReport(Long reportId);
}
