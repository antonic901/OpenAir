package openair.service;

import openair.model.Mail;
import openair.exception.NotFoundException;
import openair.model.Employee;
import openair.model.TimeSheetDay;
import openair.repository.EmployeeRepository;
import openair.repository.TimeSheetDayRepository;
import openair.service.interfaces.IEmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;
    private MailService mailService;
    private TimeSheetDayRepository timeSheetDayRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, MailService mailService,
                           TimeSheetDayRepository timeSheetDayRepository) {
        this.employeeRepository = employeeRepository;
        this.mailService = mailService;
        this.timeSheetDayRepository = timeSheetDayRepository;
    }

    @Override
    public Employee add(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findByUsername(String name) {

        Optional<Employee> employeeOptional = employeeRepository.findByUsername(name);

        if(!employeeOptional.isPresent())
            throw new NotFoundException("Employee with username " + name + " does not exist.");

        return employeeOptional.get();
    }

    @Override
    public Employee findEmployeeById(Long employeeID) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeID);

        if(!employeeOptional.isPresent())
            throw new NotFoundException("Employee with id " + employeeID.toString() + " does not exist.");

        return employeeOptional.get();
    }


    //Svakog prvog u mesecu se poveca broj slobodnih dana za 2, +1 na svakih 5 godina zaposlenja
    //At 00:00:00am, on the 1st day, every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public List<Employee> increaseEmployeeFreeDays() {

        List<Employee> employeeList = employeeRepository.findAll();

        for (int i = 0; i < employeeList.size(); i++) {
            employeeList.get(i).setFreeDays((int) (employeeList.get(i).getFreeDays() +
                    increaseByHowMuch(employeeList.get(i).getDateOfHiring())));
        }

        return employeeRepository.saveAll(employeeList);
    }

    private long increaseByHowMuch(LocalDate dateOfHiring){
        long numOfYearsInCompany = 0;
        long increaseBy = 2;

        //koliko je godina u firmi
        numOfYearsInCompany = java.time.temporal.ChronoUnit.YEARS.between(dateOfHiring, LocalDate.now());

        //ako je tu 5,10,15... godina vec dobija dodatne dane na svakih 5 godina jedan vise
        if (numOfYearsInCompany % 5 == 0) {
            increaseBy += numOfYearsInCompany / 5;
        }

        return increaseBy;
    }

    //Every month on the last weekday, at noon
    @Scheduled(cron = "0 0 12 LW * ?")
    public void sendReminder() {

        //pronadjem sve radne dane u mesecu
        List<LocalDate> workDayList = findWorkDaysOfMonth();

        //lista svih radnika
        List<Employee> employeeList = employeeRepository.findAll();

        for (int i = 0; i < employeeList.size(); i++) {

            String emailContent = "Dear, you forgot to fill in your working hours for: ";
            int detector = 0;

            //za svakog radnika pronadjem listu timeSheetDays
            List<TimeSheetDay> timeSheetDays = timeSheetDayRepository.findAllByEmployeeId(employeeList.get(i).getId());

            //povadim datume za koje je kreirao timeSheetDays
            List<LocalDate> filledDates = findFilledDates(timeSheetDays);

            //proverim da li za svaki radni dan u mesecu ima kreiran timeSheetDay
            for(int j = 0; j < workDayList.size(); j++){
                if(!filledDates.contains(workDayList.get(j))){
                    detector = 1;
                    emailContent = emailContent + workDayList.get(j).toString() + " ";
                }
            }
            //ako nema salje se mejl
            if(detector != 0) sendMail(employeeList.get(i).getEmail(),emailContent + ". Please do it before end of the month.");

        }
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
                .forEach(date -> workDayList.add(date));

        return workDayList;
    }

    private List<LocalDate> findFilledDates(List<TimeSheetDay> timeSheetDays){

        List<LocalDate> filledDates = new ArrayList<>();

        //treba proci kroz listu timeSheetDays i povaditi datume u filledDates
        for(TimeSheetDay timeSheetDay : timeSheetDays) {
            filledDates.add(timeSheetDay.getDate());
        }

        return filledDates;
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
