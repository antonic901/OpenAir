package openair.service;

import liquibase.pro.packaged.A;
import liquibase.pro.packaged.E;
import openair.dto.RegisterEmployeeDTO;
import openair.model.*;
import openair.model.enums.Department;
import openair.model.enums.UserType;

import java.util.ArrayList;
import java.util.List;

public class AdminTestData {

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

    public static Employee createEmployee1() {
        Employee employee = new Employee();
        employee.setName("Admin");
        employee.setSurname("Adminovic");
        employee.setEmail("admin@gmail.com");
        employee.setUsername("admin");
        employee.setPassword("admin");
        employee.setPhone("0613824291");
        employee.setUserType(UserType.ROLE_ADMIN);
        employee.setDepartment(Department.JAVA);
        employee.setSalary(0);
        Admin admin = new Admin();
        admin.setId(1L);
        employee.setAdmin(admin);
        return employee;
    }

    public static Employee createEmployee2() {
        Employee employee = new Employee();
        employee.setName("Admin");
        employee.setSurname("Adminovic");
        employee.setEmail("admin@gmail.com");
        employee.setUsername("admin");
        employee.setPassword("admin");
        employee.setPhone("0613824291");
        employee.setUserType(UserType.ROLE_ADMIN);
        employee.setDepartment(Department.JAVA);
        employee.setSalary(0);
        Admin admin = new Admin();
        admin.setId(2L);
        employee.setAdmin(admin);

        return employee;
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
