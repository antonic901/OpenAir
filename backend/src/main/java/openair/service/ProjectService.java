package openair.service;

import liquibase.pro.packaged.P;
import openair.dto.ProjectDTO;
import openair.model.Admin;
import openair.model.Project;
import openair.repository.ProjectRepository;
import openair.service.interfaces.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService implements IProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @Override
    public Project findProjectByName(String name) {
        return projectRepository.findByName(name);
    }

    @Override
    public Project addProject(ProjectDTO projectDTO, Admin admin){
        Project project = new Project();

        project.setAdmin(admin);
        project.setName(projectDTO.getName());

        return projectRepository.save(project);
    }

    public Project findProjectByID(Long ID) {
        return projectRepository.findByID(ID);
    }
}
