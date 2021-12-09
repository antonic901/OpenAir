package openair.service.interfaces;

import openair.dto.ProjectDTO;
import openair.model.Admin;
import openair.model.Project;

import java.util.List;

public interface IProjectService {
    Project findProjectById(Long projectId);
    Project findProjectByName(String name);
    Project addProject(ProjectDTO projectDTO, Admin admin);
    Project addEmployeeToProject(Long employeeId, Long projectId);
    List<Project> findAllByUserId(Long employeeId);
}
