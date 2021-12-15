package openair.service;

import com.amazonaws.services.kms.model.NotFoundException;

import openair.dto.ExspenseReportDTO;
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

        List<ExpenseReport>  expenseReports = new ArrayList<>();

        for(ExpenseReport expenseReport : expenseReportRepository.findAll()) {

            if(expenseReport.getEmployee().getAdmin().getId().equals(id)) {

                expenseReports.add(expenseReport);
            }
        }
        return expenseReports;
    }

    @Override
    public ExpenseReport addReport(ExspenseReportDTO expenseReportDTO, Employee employee) throws NotFoundException{

        Optional<Project> projectOptional = projectRepository.findById(expenseReportDTO.getProjectId());

        if(!projectOptional.isPresent())
            throw new NotFoundException("Project with id " + expenseReportDTO.getProjectId().toString() + " does not exist.");

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
        expenseReport.setProject(projectOptional.get());
        expenseReport.setStatus(Status.INPROCESS);
        expenseReport.setTrackingNumber(expenseReportDTO.getTrackingNumber());
        expenseReport.setDocument(expenseReportDTO.getDocument());

        return expenseReportRepository.save(expenseReport);
    }

    @Override
    public ExpenseReport reviewReport(Long reportId, Status status) throws NotFoundException{

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
