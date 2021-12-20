package openair.repository;

import openair.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String name);

    @Query(value = "Select p.id, p.name, p.project_type, p.admin_id" +
            " From projects p, employee_project ep " +
            "where p.id = ep.project_id " +
            "and ep.employee_id = ?1",
            nativeQuery = true
    )
    List<Project> findAllByEmployeeId(Long id);
    List<Project> findAllByAdminId(Long id);
}
