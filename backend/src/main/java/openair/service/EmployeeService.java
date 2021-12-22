package openair.service;

import openair.model.Mail;
import openair.exception.NotFoundException;
import openair.model.Employee;
import openair.repository.AbsenceRepository;
import openair.repository.EmployeeRepository;
import openair.repository.TimeSheetDayRepository;
import openair.service.interfaces.IEmployeeService;

import openair.utils.AbsenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final MailService mailService;
    private final TimeSheetDayRepository timeSheetDayRepository;
    private final AbsenceRepository absenceRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, MailService mailService,
                           TimeSheetDayRepository timeSheetDayRepository,
                           AbsenceRepository absenceRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.mailService = mailService;
        this.timeSheetDayRepository = timeSheetDayRepository;
        this.absenceRepository = absenceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Employee add(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        employee.setFreeDays(8);
        employee.setDateOfHiring(LocalDate.now());

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> findAllEmployeesByAdminId(Long id) {
        return employeeRepository.findAllByAdminId(id);
    }

    @Override
    public Employee findByUsername(String name) {
        return employeeRepository.findByUsername(name).orElseThrow(() -> new NotFoundException("Employee with username " + name + " does not exist."));
    }

    @Override
    public Employee findEmployeeById(Long employeeID) {
        return employeeRepository.findById(employeeID).orElseThrow(() -> new NotFoundException(employeeID,"Employee with id " + employeeID + " does not exist."));
    }

    //At 00:00:00am, on the 1st day, every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public List<Employee> increaseEmployeeFreeDays() {

        List<Employee> employees = employeeRepository.findAll();

        for(Employee employee: employees){
            employee.setFreeDays(
                    (int) (employee.getFreeDays() + increaseByHowMuch(employee.getDateOfHiring())));
        }

        return employeeRepository.saveAll(employees);
    }

    private long increaseByHowMuch(LocalDate dateOfHiring){
        long increaseBy = 2;
        long numOfYearsInCompany = java.time.temporal.ChronoUnit.YEARS.between(dateOfHiring, LocalDate.now());

        if (numOfYearsInCompany % 5 == 0)
            increaseBy += numOfYearsInCompany / 5;

        return increaseBy;
    }

    //Every month on the last weekday, at noon
    @Scheduled(cron = "0 0 12 LW * ?")
    public void sendReminder() {

        List<LocalDate> workDayList = findWorkDaysOfMonth();
        List<Employee> employeeList = employeeRepository.findAll();

        for (Employee employee : employeeList) {

            StringBuilder emailContent = new StringBuilder("Dear, you forgot to fill in your working hours for: ");
            int detector = 0;

            List<LocalDate> filledDates = timeSheetDayRepository.findAllOfCurrentMonth(employee.getId(), LocalDateTime.now().getMonth().getValue(),LocalDateTime.now().getYear());
            List<LocalDate> absentDates = findAbsentDates(employee.getId());

            for (LocalDate workDate : workDayList) {
                //date filled or employee on vacation
                if (!filledDates.contains(workDate) && !absentDates.contains(workDate)) {
                    detector = 1;
                    emailContent.append(workDate.toString()).append(" ");
                }
            }

            //do not send mail if everything is filled
            if (detector != 0) sendMail(employee.getEmail(), emailContent + ". Please do it before end of the month.");

        }
    }

    private List<LocalDate> findAbsentDates(Long employeeId){

        List<LocalDate> absentDays = new ArrayList<>();

        //vacation starts in current month
        List<AbsenceInterface> absences = absenceRepository.findAllOfCurrentMonth(employeeId, LocalDateTime.now().getMonth().getValue(),LocalDateTime.now().getYear());
        
        //extract dates to list
        for (AbsenceInterface absence : absences) {
            absentDays.addAll(findAllDatesBetweenTwoDates(absence.getStartDate(),
                    absence.getEndDate()));

        }

        //remove dates from next month, when vacation ends in next month
        absentDays.removeIf(date -> date.getMonth() != LocalDate.now().getMonth());

        return absentDays;
    }

    private List<LocalDate> findAllDatesBetweenTwoDates(LocalDate start, LocalDate end){

        List<LocalDate> totalDates = new ArrayList<>();

        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }

        return totalDates;
    }

    private List<LocalDate> findWorkDaysOfMonth(){

        List<LocalDate> workDayList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        Month month = currentDate.getMonth();
        int year = currentDate.getYear();

        IntStream.rangeClosed(1, YearMonth.of(year, month).lengthOfMonth())
                .mapToObj(day -> LocalDate.of(year, month, day))
                .filter(date -> date.getDayOfWeek() == DayOfWeek.MONDAY ||
                        date.getDayOfWeek() == DayOfWeek.TUESDAY ||
                        date.getDayOfWeek() == DayOfWeek.WEDNESDAY ||
                        date.getDayOfWeek() == DayOfWeek.THURSDAY ||
                        date.getDayOfWeek() == DayOfWeek.FRIDAY)
                .forEach(workDayList::add);

        return workDayList;
    }

    private void sendMail(String emailAddress, String emailContent){
        Mail mail = new Mail();

        mail.setContentType("REMINDER");
        mail.setMailSubject("Monthly reminder to log your working hours");
        mail.setMailContent(emailContent);
        mail.setMailTo(emailAddress);

        mailService.sendMail(mail);
    }
}
