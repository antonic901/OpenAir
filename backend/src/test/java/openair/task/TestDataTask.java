package openair.task;

import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.absence.TestData;
import openair.model.enums.ProjectType;

import java.util.ArrayList;
import java.util.List;

public class TestDataTask {
    public static Project createProject1() {
        Project project = new Project();

        project.setId(1L);
        project.setAdmin(TestData.createAdmin());
        project.setProjectType(ProjectType.INTERN);

        return project;
    }

    public static Project createProject2() {
        Project project = new Project();

        project.setId(2L);
        project.setAdmin(TestData.createAdmin());
        project.setProjectType(ProjectType.INTERN);

        return project;
    }

    public static Task createTask() {
        Task task = new Task();
        Project project = createProject1();
        Employee employee = TestData.createEmployee();

        task.setProject(project);
        task.setId(1L);
        task.setEmployee(employee);
        task.setName("refactoring");

        return task;
    }

    public static List<Task> createTaskList() {
        List<Task> taskList = new ArrayList<Task>();

        Task t1 = new Task();
        t1.setId(1L);
        Task t2 = new Task();
        t2.setId(2L);
        Task t3 = new Task();
        t3.setId(3L);

        taskList.add(t1);
        taskList.add(t2);
        taskList.add(t3);

        return taskList;
    }
}
