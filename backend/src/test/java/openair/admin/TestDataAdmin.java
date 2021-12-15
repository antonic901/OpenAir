package openair.admin;

import liquibase.pro.packaged.E;
import openair.dto.RegisterEmployeeDTO;
import openair.model.*;
import openair.model.enums.Department;
import openair.model.enums.Status;
import openair.model.enums.UserType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestDataAdmin {

    public static Admin createAdmin() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Admin");
        admin.setSurname("Adminovic");
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setPhone("0613824291");
        admin.setUserType(UserType.ROLE_ADMIN);
        admin.getRoles().add(new Role(1L, UserType.ROLE_ADMIN));
        admin.setEmployeeList(createEmployees());
        return admin;
    }

    public static List<Employee> createEmployees() {
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(new Employee());
        employees.add(new Employee());
        employees.add(new Employee());
        employees.add(new Employee());
        return employees;
    }


    public static Employee createEmployee() {
        Employee employee = new Employee();
        employee.setId(2L);
        employee.setName("Nikola");
        employee.setSurname("Antonic");
        employee.setEmail("antonicnikola6@gmail.com");
        employee.setUsername("antonic901");
        employee.setPassword("kinzo");
        employee.setPhone("0613238249");
        employee.setUserType(UserType.ROLE_EMPLOYEE);
        employee.getRoles().add(new Role(2L, UserType.ROLE_EMPLOYEE));
        return employee;
    }

    public static RegisterEmployeeDTO createRegisterEmployeeDTO1() {
        RegisterEmployeeDTO dto = new RegisterEmployeeDTO();
        dto.setName("Admin");
        dto.setSurname("Adminovic");
        dto.setEmail("admin@gmail.com");
        dto.setUsername("admin");
        dto.setPassword("admin");
        dto.setPhone("0613824291");
        dto.setUserType(UserType.ROLE_ADMIN);
        dto.setDepartment(Department.JAVA);
        dto.setSalary(0);
        dto.setAdminId(1L);
        return dto;
    }

    public static RegisterEmployeeDTO createRegisterEmployeeDTO2() {
        RegisterEmployeeDTO dto = new RegisterEmployeeDTO();
        dto.setName("Admin");
        dto.setSurname("Adminovic");
        dto.setEmail("admin@gmail.com");
        dto.setUsername("admin");
        dto.setPassword("admin");
        dto.setPhone("0613824291");
        dto.setUserType(UserType.ROLE_ADMIN);
        dto.setDepartment(Department.JAVA);
        dto.setSalary(0);
        dto.setAdminId(2L);
        return dto;
    }

    public static RegisterEmployeeDTO createRegisterEmployeeDTO3() {
        RegisterEmployeeDTO dto = new RegisterEmployeeDTO();
        dto.setName("Admin");
        dto.setSurname("Adminovic");
        dto.setEmail("admin@gmail.com");
        dto.setUsername("admin");
        dto.setPassword("admin");
        dto.setPhone("0613824291");
        dto.setDepartment(Department.JAVA);
        dto.setSalary(0);
        dto.setAdminId(1L);
        return dto;
    }

}
