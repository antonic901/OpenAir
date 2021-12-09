package openair.service;

import openair.model.Role;
import openair.model.enums.UserType;
import openair.repository.RoleRepository;
import openair.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role findByName(UserType name) {
        return roleRepository.findByName(name);
    }
}
