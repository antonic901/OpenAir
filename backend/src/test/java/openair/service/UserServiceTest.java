package openair.service;

import openair.model.User;
import openair.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserService userService;

    @Test
    void testAddUser() {
        User user = UserTestData.createUser();

        when(userRepository.save(any(User.class))).thenReturn(new User());

        assertThat(userService.addUser(user)).isNotNull();
    }

    @Test
    void testFindByUsername() {
        //  given
        User user = new User();
        String username1 = "strahinja";
        String username2 = "nesto";
        user.setUsername(username1);

        //  when
        when(userRepository.findByUsername(username1)).thenReturn(user);
        when(userRepository.findByUsername(username2)).thenReturn(null);

        //  then
        User userIsNotNull = userService.findByUsername(username1);
        User userIsNull = userService.findByUsername(username2);

        assertThat(userIsNotNull).isNotNull();
        assertThat(userIsNull).isNull();
    }
}
