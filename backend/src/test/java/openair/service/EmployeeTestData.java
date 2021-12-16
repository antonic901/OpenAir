package openair.service;

import openair.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTestData {

    public static Employee createEmployee(){
        Employee employee = new Employee();

        employee.setId(1L);
        employee.setUsername("anjica99");

        return employee;
    }

    public static List<Employee> createEmployeeList() {
        Employee employee1 = new Employee();
        employee1.setId(1L);

        Employee employee2 = new Employee();
        employee2.setId(2L);

        List<Employee> employees = new ArrayList<>();

        employees.add(employee1);
        employees.add(employee2);

        return employees;
    }
}
