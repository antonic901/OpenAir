package openair.service;

import openair.model.Role;
import openair.model.enums.UserType;
import openair.repository.RoleRepository;
import openair.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        Role role;
        if(optionalRole.isPresent()) {
            role = optionalRole.get();
        }
        else {
            throw new RuntimeException("Role with ID: " + id + " doesn't exist");
        }
        return role;
    }

    @Override
    public Role findByName(UserType name) {
        return roleRepository.findByName(name);
    }
}
