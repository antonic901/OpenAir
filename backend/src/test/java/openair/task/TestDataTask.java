package openair.task;

import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.absence.TestData;
import openair.model.enums.ProjectType;

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
}
