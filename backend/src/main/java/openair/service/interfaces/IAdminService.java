package openair.service.interfaces;

import openair.model.Admin;


public interface IAdminService {
    Admin findByUsername(String username);
}
