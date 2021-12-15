package openair.user;

import openair.repository.UserRepository;
import openair.service.UserService;
import openair.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserTest {
    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserService userService;

    //  Nesto se cudno desava sa User klasom
    //      problem: kaze da je this.userReporsitory null ????

//    @Test
//    public void testAddUser() {
//        User user = TestDataUser.createUser();
//
//        when(userRepository.save(any(User.class))).thenReturn(new User());
//
//        assertThat(userService.addUser(user)).isNotNull();
//    }


//    @Test
//    public void testFindByUsername() {
//        //  given
//        User user = new User();
//        String username1 = "strahinja";
//        String username2 = "nesto";
//        user.setUsername(username1);
//
//        //  when
//        when(userRepository.findByUsername(username1)).thenReturn(Optional.of(user));
//        when(userRepository.findByUsername(username2)).thenReturn(Optional.empty());
//
//        //then
//        User notNull = userService.findByUsername(username1);
//        User isNull = userService.findByUsername(username2);
//
//        assertThat(notNull).isNotNull();
//        assertThat(isNull).isNull();
//    }
}
