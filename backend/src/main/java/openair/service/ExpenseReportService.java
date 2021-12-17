package openair.service;

import openair.dto.ExspenseReportDTO;
import openair.exception.NotFoundException;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Money;
import openair.model.Project;
import openair.model.enums.Currency;
import openair.model.enums.Status;
import openair.repository.EmployeeRepository;
import openair.repository.ExpenseReportRepository;
import openair.repository.ProjectRepository;
import openair.service.interfaces.IExpenseReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseReportService implements IExpenseReportService {

    private ExpenseReportRepository expenseReportRepository;
    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;
    private MoneyService moneyService;

    @Autowired
    public ExpenseReportService(ExpenseReportRepository expenseReportRepository, EmployeeRepository employeeRepository,
                                MoneyService moneyService, ProjectRepository projectRepository){
        this.expenseReportRepository = expenseReportRepository;
        this.employeeRepository = employeeRepository;
        this.moneyService = moneyService;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ExpenseReport> getAllByAdminId(Long id) {

        return expenseReportRepository.findAllByAdminId(id);

    }

    @Override
    public ExpenseReport addReport(ExpenseReport expenseReport) {

        expenseReport.setDateOfCreation(LocalDate.now());
        expenseReport.setStatus(Status.INPROCESS);
        expenseReport.getRefund().setDate(LocalDate.now());

        return expenseReportRepository.save(expenseReport);
    }

    @Override
    public ExpenseReport reviewReport(Long reportId, Status status) {

        Optional<ExpenseReport> expenseReportOpt = expenseReportRepository.findById(reportId);

        if(!expenseReportOpt.isPresent())
            throw  new NotFoundException("Expense report with id " + reportId.toString() + "does not exist.");

        //odobrim zahtev
        expenseReportOpt.get().setStatus(status);

        //pronadjem radnika da bi mu uvecala platu za onliko koliko je navedeno u reportu
        if(status == Status.APPROVED) {

            Employee employee = expenseReportOpt.get().getEmployee();

            if(expenseReportOpt.get().getRefund().getCurrency() == Currency.RSD) {

                //ako je u RSD konvertujemo u EUR i tek onda dodajemo na platu
                double converted = moneyService.convert(DateTimeFormatter.ofPattern("yyyy-MM-dd").
                        format(expenseReportOpt.get().getDateOfCreation()), Currency.RSD.toString(), Currency.EUR.toString(),
                               expenseReportOpt.get().getRefund().getQuantity());

                employee.setSalary(employee.getSalary() + converted);
            }
            else {
                employee.setSalary(employee.getSalary() + expenseReportOpt.get().getRefund().getQuantity());
            }

            employeeRepository.save(employee);
        }

        return expenseReportRepository.save(expenseReportOpt.get());
    }

}
