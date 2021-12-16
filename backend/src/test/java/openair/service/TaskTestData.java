package openair.service;

import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.model.enums.ProjectType;

import java.util.ArrayList;
import java.util.List;

public class TaskTestData {
    public static Project createProject1() {
        Project project = new Project();

        project.setId(1L);
        project.setAdmin(AbsenceTestData.createAdmin());
        project.setProjectType(ProjectType.INTERN);

        return project;
    }

    public static Project createProject2() {
        Project project = new Project();

        project.setId(2L);
        project.setAdmin(AbsenceTestData.createAdmin());
        project.setProjectType(ProjectType.INTERN);

        return project;
    }

    public static Task createTask() {
        Task task = new Task();
        Project project = createProject1();
        Employee employee = AbsenceTestData.createEmployee();

        task.setProject(project);
        task.setId(1L);
        task.setEmployee(employee);
        task.setName("refactoring");

        return task;
    }

    public static List<Task> createTaskList() {
        List<Task> taskList = new ArrayList<Task>();

        Project project = new Project();
        project.setId(1L);
        Employee employee = new Employee();
        employee.setId(1L);

        Task t1 = new Task();
        t1.setId(1L);
        t1.setProject(project);
        t1.setEmployee(employee);

        Task t2 = new Task();
        t2.setId(2L);
        t2.setProject(project);
        t2.setEmployee(employee);

        Task t3 = new Task();
        t3.setId(3L);
        t3.setProject(project);
        t3.setEmployee(employee);

        taskList.add(t1);
        taskList.add(t2);
        taskList.add(t3);

        return taskList;
    }
}
