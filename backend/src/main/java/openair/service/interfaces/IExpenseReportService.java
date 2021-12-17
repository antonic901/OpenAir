package openair.service.interfaces;

import openair.model.ExpenseReport;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.enums.Status;

import java.util.List;

public interface IExpenseReportService {
    List<ExpenseReport> getAllByAdminId(Long id);
    ExpenseReport addReport(ExpenseReport expenseReport);
    ExpenseReport reviewReport(Long reportId, Status status);
}
