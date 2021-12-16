package openair.service;

import openair.dto.ProjectDTO;
import openair.model.*;
import openair.model.enums.Status;
import openair.model.enums.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectTestData {

    public static Project createProject() {
        Project project = new Project();

        project.setId(1L);
        project.setName("OpenAir");
        project.setEmployeeList(EmployeeTestData.createEmployeeList());

        return project;
    }

    public static ProjectDTO createProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setName("OpenAir");

        return projectDTO;
    }

    public static User createUser1() {
        User user = new User();

        user.setId(1L);
        user.setUserType(UserType.ROLE_ADMIN);

        return user;
    }

    public static User createUser2() {
        User user = new User();

        user.setId(2L);
        user.setUserType(UserType.ROLE_EMPLOYEE);

        return user;
    }

    public static List<Project> createProjectList() {
        Project project1 = new Project();
        project1.setId(1L);

        Project project2 = new Project();
        project2.setId(2L);

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);

        return projectList;
    }

    public static Admin createAdmin() {
        Admin admin = new Admin();

        admin.setId(1L);
        admin.setProjects(createProjectList());

        return admin;
    }

    public static Employee createEmployee() {
        Employee employee = new Employee();

        employee.setId(2L);
        employee.setProjects(createProjectList());

        return employee;
    }

    public static Employee createEmployee1() {
        Employee employee = new Employee();

        employee.setId(4L);
        employee.setProjects(createProjectList());

        return employee;
    }

    public static ExpenseReport createReport() {

        ExpenseReport report1 = new ExpenseReport();

        Project project = createProject();
        report1.setProject(project);
        Employee employee = createEmployee1();
        report1.setEmployee(employee);

        report1.setStatus(Status.APPROVED);

        return report1;
    }
}
