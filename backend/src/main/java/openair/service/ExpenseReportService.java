package openair.service;

import openair.dto.ExspenseReportDTO;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Money;
import openair.model.Project;
import openair.model.enums.Currency;
import openair.model.enums.Status;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.ExpenseReportRepository;
import openair.service.interfaces.IExpenseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseReportService implements IExpenseReportService {

    private ExpenseReportRepository expenseReportRepository;
    private EmployeeRepository employeeRepository;
    private AdminRepository adminRepository;

    private MoneyService moneyService;

    @Autowired
    public ExpenseReportService(ExpenseReportRepository expenseReportRepository, EmployeeRepository employeeRepository, AdminRepository adminRepository, MoneyService moneyService){
        this.expenseReportRepository = expenseReportRepository;
        this.employeeRepository = employeeRepository;
        this.adminRepository = adminRepository;
        this.moneyService = moneyService;
    }

    @Override
    public List<ExpenseReport> getAllByAdminId(Long id) {
        List<ExpenseReport>  expenseReports = new ArrayList<ExpenseReport>();
        for(ExpenseReport expenseReport : expenseReportRepository.findAll()) {
            if(expenseReport.getEmployee().getAdmin().getId().equals(id)) {
                expenseReports.add(expenseReport);
            }
        }
        return expenseReports;
    }

    @Override
    public ExpenseReport addReport(ExspenseReportDTO expenseReportDTO, Employee employee, Project project) {
        ExpenseReport expenseReport = new ExpenseReport();

        Money money = new Money();
        money.setCurrency(expenseReportDTO.getRefund().getCurrency());
        money.setDate(LocalDate.now());
        money.setQuantity(expenseReportDTO.getRefund().getQuantity());


        expenseReport.setDescription(expenseReportDTO.getDescription());
        expenseReport.setEmployee(employee);
        expenseReport.setRefund(money);
        expenseReport.setName(expenseReportDTO.getName());
        expenseReport.setDateOfCreation(LocalDate.now());
        expenseReport.setProject(project);
        expenseReport.setStatus(Status.INPROCESS);
        expenseReport.setTrackingNumber(expenseReportDTO.getTrackingNumber());
        expenseReport.setDocument(expenseReportDTO.getDocument());

        return expenseReportRepository.save(expenseReport);
    }

    @Override
    public ExpenseReport reviewReport(Long reportId, Status status) {
        ExpenseReport expenseReport = expenseReportRepository.findById(reportId).get();

        //odobrim zahtev
        expenseReport.setStatus(status);

        //pronadjem radnika da bi mu uvecala platu za onliko koliko je navedeno u reportu
        if(status == Status.APPROVED) {
            Employee employee = expenseReport.getEmployee();
            //TODO treba podrzati konverziju ukoliko su valute ralicite

            if(expenseReport.getRefund().getCurrency() == Currency.RSD) {
                //ako je u RSD konvertujemo u EUR i tek onda dodajemo na platu
                double converted = moneyService.convert(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(expenseReport.getDateOfCreation()), Currency.RSD.toString(), Currency.EUR.toString(), expenseReport.getRefund().getQuantity());
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
