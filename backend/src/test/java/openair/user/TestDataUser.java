package openair.user;
import openair.model.User;
import openair.model.enums.UserType;

public class TestDataUser {
    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Strahinja");
        user.setUserType(UserType.ROLE_EMPLOYEE);
        user.setEmail("straleb132@gmail.com");
        user.setPassword("password");
        user.setPhone("0616102069");
        user.setSurname("Bozovic");
        user.setUsername("strahinja123");

        return user;
    }
}
