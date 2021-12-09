package openair.service.interfaces;


import openair.model.User;

public interface IUserService {
    User findByUsername(String username);
    User addUser(User user);
    Boolean checkIfUsernameIsAvailable(String username);
}
