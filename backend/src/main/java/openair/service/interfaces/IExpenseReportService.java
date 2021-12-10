package openair.service.interfaces;

import openair.dto.ExspenseReportDTO;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Project;
import openair.model.enums.Status;

public interface IExpenseReportService {
    ExpenseReport addReport(ExspenseReportDTO expenseReportDTO, Employee employee, Project project);

    ExpenseReport reviewReport(Long reportId, Status status);
}
