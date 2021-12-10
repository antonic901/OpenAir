package openair.service;

import openair.dto.ExspenseReportDTO;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Project;
import openair.model.enums.Status;
import openair.repository.ExpenseReportRepository;
import openair.service.interfaces.IExpenseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseReportService implements IExpenseReportService {

    private ExpenseReportRepository expenseReportRepository;

    @Autowired
    public ExpenseReportService(ExpenseReportRepository expenseReportRepository){
        this.expenseReportRepository = expenseReportRepository;
    }

    @Override
    public ExpenseReport addReport(ExspenseReportDTO expenseReportDTO, Employee employee, Project project) {
        ExpenseReport expenseReport = new ExpenseReport();

        expenseReport.setDescription(expenseReportDTO.getDescription());
        expenseReport.setEmployee(employee);
        expenseReport.setRefund(expenseReportDTO.getRefund());
        expenseReport.setName(expenseReportDTO.getName());
        expenseReport.setDateOfCreation(expenseReportDTO.getDateOfCreation());
        expenseReport.setProject(project);
        expenseReport.setStatus(Status.INPROCESS);
        expenseReport.setTrackingNumber(expenseReportDTO.getTrackingNumber());

        return expenseReportRepository.save(expenseReport);
    }
}
