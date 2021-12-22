package openair.controller;

import openair.model.enums.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@RestController
@RequestMapping(value = "/enums", produces = MediaType.APPLICATION_JSON_VALUE)
public class EnumController {

    @GetMapping("/currencies")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public List<Currency> getCurrency() {
        return new ArrayList<>(EnumSet.allOf(Currency.class));
    }

    @GetMapping("/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Department> getDepartment() {
        return new ArrayList<>(EnumSet.allOf(Department.class));
    }

    @GetMapping("/project-types")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProjectType> getProjectType() {
        return new ArrayList<>(EnumSet.allOf(ProjectType.class));
    }

    @GetMapping("/statuses")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Status> getStatus() {
        return new ArrayList<>(EnumSet.allOf(Status.class));
    }

    @GetMapping("/user-types")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserType> getUserType() {
        return new ArrayList<>(EnumSet.allOf(UserType.class));
    }

}
