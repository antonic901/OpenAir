package openair.service;

import openair.exception.NotFoundException;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.enums.Currency;
import openair.model.enums.Status;
import openair.repository.EmployeeRepository;
import openair.repository.ExpenseReportRepository;
import openair.service.interfaces.IExpenseReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExpenseReportService implements IExpenseReportService {

    private final ExpenseReportRepository expenseReportRepository;
    private final EmployeeRepository employeeRepository;
    private final MoneyService moneyService;

    @Autowired
    public ExpenseReportService(ExpenseReportRepository expenseReportRepository, EmployeeRepository employeeRepository,
                                MoneyService moneyService){
        this.expenseReportRepository = expenseReportRepository;
        this.employeeRepository = employeeRepository;
        this.moneyService = moneyService;
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

        ExpenseReport expenseReport = expenseReportRepository.findById(reportId).orElseThrow(() -> new NotFoundException("Expense report with id " + reportId + "does not exist."));

        //odobrim zahtev
        expenseReport.setStatus(status);

        //pronadjem radnika da bi mu uvecala platu za onliko koliko je navedeno u reportu
        if(status == Status.APPROVED) {

            Employee employee = expenseReport.getEmployee();

            if(expenseReport.getRefund().getCurrency() == Currency.RSD) {

                //ako je u RSD konvertujemo u EUR i tek onda dodajemo na platu
                double converted = moneyService.convert(DateTimeFormatter.ofPattern("yyyy-MM-dd").
                        format(expenseReport.getDateOfCreation()), Currency.RSD.toString(), Currency.EUR.toString(),
                        expenseReport.getRefund().getQuantity());

                employee.setSalary(employee.getSalary() + converted);
            }
            else {
                employee.setSalary(employee.getSalary() + expenseReport.getRefund().getQuantity());
            }

            employeeRepository.save(employee);
        }

        return expenseReportRepository.save(expenseReport);
    }

}
