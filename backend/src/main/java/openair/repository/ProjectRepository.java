package openair.repository;

import openair.model.Project;
import openair.model.enums.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByName(String name);
}
