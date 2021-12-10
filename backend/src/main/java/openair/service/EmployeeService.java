package openair.service;

import liquibase.pro.packaged.A;
import openair.dto.RegisterEmployeeDTO;
import openair.model.Employee;
import openair.model.Project;
import openair.model.User;
import openair.repository.EmployeeRepository;
import openair.service.interfaces.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
    public Employee findEmployeeById(Long employeeID) {
        return employeeRepository.findById(employeeID).get();
    }

    //Svakog prvog u mesecu se poveca broj slobodnih dana za 2
    //At 00:00:00am, on the 1st day, every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public void increaseEmployeeFreeDays(){
        List<Employee> employeeList = employeeRepository.findAll();

        for(int i=0; i< employeeList.size(); i++){
            employeeList.get(i).setFreeDays(employeeList.get(i).getFreeDays()+2);
        }

        employeeRepository.saveAll(employeeList);
    }
}
