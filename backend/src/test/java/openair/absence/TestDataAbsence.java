package openair.absence;

import openair.model.*;
import openair.model.enums.Status;
import openair.model.enums.UserType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestDataAbsence {

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

    public static List<Absence> createAbsences() {
        List<Absence> absences = new ArrayList<Absence>();
        absences.add(new Absence(1L, new Period(LocalDateTime.of(2021, 11,10,0,0,0), LocalDateTime.of(2021, 11,15,0,0,0)), Status.INPROCESS, createEmployee(), createAdmin()));
        absences.add(new Absence(2L, new Period(LocalDateTime.of(2021, 11,20,0,0,0), LocalDateTime.of(2021, 11,25,0,0,0)), Status.INPROCESS, createEmployee(), createAdmin()));
        absences.add(new Absence(3L, new Period(LocalDateTime.of(2021, 12,5,0,0,0), LocalDateTime.of(2021, 12,10,0,0,0)), Status.INPROCESS, createEmployee(), createAdmin()));
        absences.add(new Absence(4L, new Period(LocalDateTime.of(2021, 12,10,0,0,0), LocalDateTime.of(2021, 12,15,0,0,0)), Status.INPROCESS, createEmployee(), createAdmin()));
        return absences;
    }

}
