package openair.service.interfaces;

import openair.model.Employee;
import openair.model.Admin;

import java.util.List;

public interface IAdminService {
    Admin findByUsername(String username);
}
