package openair.service;

import openair.email.model.Mail;
import openair.email.service.MailService;
import openair.model.Employee;
import openair.model.Project;
import openair.model.TimeSheetDay;
import openair.repository.EmployeeRepository;
import openair.service.interfaces.IEmployeeService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;

    private ProjectService projectService;

    private MailService mailService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, ProjectService projectService, MailService mailService) {
        this.employeeRepository = employeeRepository;
        this.projectService = projectService;
        this.mailService = mailService;
    }

    @Override
    public Employee add(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllByProjectId(Long projectId) {
        Project project =  projectService.findProjectById(projectId);
        return project.getEmployeeList();
    }

    @Override
    public Employee findByUsername(String name) {
        return employeeRepository.findByUsername(name);
    }

    @Override
    public Employee findEmployeeById(Long employeeID) {
        return employeeRepository.findById(employeeID).get();
    }

    //Svakog prvog u mesecu se poveca broj slobodnih dana za 2, +1 na svakih 5 godina zaposlenja
    //At 00:00:00am, on the 1st day, every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public void increaseEmployeeFreeDays() {
        List<Employee> employeeList = employeeRepository.findAll();

        long numOfYearsInCompany = 0;
        long increaseBy = 2;

        for (int i = 0; i < employeeList.size(); i++) {
            //koliko je godina u firmi
            numOfYearsInCompany = java.time.temporal.ChronoUnit.YEARS.between(employeeList.get(i).getDateOfHiring(), LocalDate.now());

            //ako je tu 5,10,15... godina vec dobija dodatne dane na svakih 5 godina jedan vise
            if (numOfYearsInCompany % 5 == 0) {
                increaseBy += numOfYearsInCompany / 5;
            }
            employeeList.get(i).setFreeDays((int) (employeeList.get(i).getFreeDays() + increaseBy));
        }

        employeeRepository.saveAll(employeeList);

    }

    //Every month on the last weekday, at noon
    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendReminder() {

        //pronadjem sve radne dane u mesecu
        List<LocalDate> workDayList = new ArrayList<>();
        workDayList = findWorkDaysOfMonth();

        //lista svih radnika
        List<Employee> employeeList = employeeRepository.findAll();

        for (int i = 0; i < employeeList.size(); i++) {

            //za svakog radnika pronadjem listu timeSheetDays
            List<TimeSheetDay> timeSheetDays = employeeList.get(i).getTimeSheetDays();

            //povadim datume za koje je kreirao timeSheetDays
            List<LocalDate> filledDates = new ArrayList<>();

//            for(TimeSheetDay td: timeSheetDays){
//                filledDates.add(td.getDate());
//            }

//            for(int k = 0; k < timeSheetDays; k++){
//                //System.out.println("Usla u for petlju");
//                filledDates.add(timeSheetDays.get(k).getDate());
//               // System.out.println("Zaglavila se");
//            }
            //System.out.println("Pronasao popunjene datume");

            //proverim da li za svaki radni dan u mesecu ima kreiran timeSheetDay
            for(int j=0; j < workDayList.size(); j++){
                if(!filledDates.contains(workDayList.get(j))){
                    //ako nema salje se mejl
//                    Mail mail = new Mail();
//                    mail.setMailFrom("ursaminor1777@gmail.com");
//                    mail.setContentType("REMINDER");
//                    mail.setMailSubject("Monthly reminder to log your working hours");
//                    mail.setMailContent("Dear, you forgot to fill in your working hours for " + workDayList.get(j).toString() +
//                            ". Please do it before end of the month.");
//                    mail.setMailTo(employeeList.get(i).getEmail());
//                    mailService.sendMail(mail);
                    System.out.println("Odradio slanje mejla");
                }

            }
        }
    }

    public List<LocalDate> findWorkDaysOfMonth(){
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
}
