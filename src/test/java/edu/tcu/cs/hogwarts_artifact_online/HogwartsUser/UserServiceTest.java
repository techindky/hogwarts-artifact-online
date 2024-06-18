package edu.tcu.cs.hogwarts_artifact_online.HogwartsUser;

import edu.tcu.cs.hogwarts_artifact_online.System.Exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    List<HogwartsUser> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("tony");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        users.add(u1);

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");
        users.add(u2);

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");
        users.add(u3);
    }
    @AfterEach
    void tearDown() {}

    @Test
    void findAllUsers() {
        given(userRepository.findAll()).willReturn(users);
        List<HogwartsUser> actualUsers = userService.findAllUsers();
        assertThat(actualUsers.size()).isEqualTo(users.size());
        verify(userRepository, times(1)).findAll();
    }
    @Test
    void findUserById() {
        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        given(userRepository.findById(2)).willReturn(Optional.of(u2));
        HogwartsUser returnedUser = userService.findUserById(2);
        assertThat(returnedUser.getId()).isEqualTo(u2.getId());
        assertThat(returnedUser.getUsername()).isEqualTo(u2.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(u2.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(u2.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(u2.getRoles());
        verify(userRepository, times(1)).findById(2);
    }
    @Test
    void findUserNotFound() {
        given(userRepository.findById(5)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> userService.findUserById(5));
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with id 5 :(");
        verify(userRepository, times(1)).findById(5);
    }
    @Test
    void addUser() {
        HogwartsUser u2 = new HogwartsUser();
        u2.setUsername("david");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");
        given(passwordEncoder.encode(u2.getPassword())).willReturn("Encoded Password");
        given(userRepository.save(u2)).willReturn(u2);
        HogwartsUser savedUser = userService.addUser(u2);
        assertThat(savedUser.getUsername()).isEqualTo(u2.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(u2.getPassword());
        assertThat(savedUser.isEnabled()).isEqualTo(u2.isEnabled());
        assertThat(savedUser.getRoles()).isEqualTo(u2.getRoles());
        verify(userRepository, times(1)).save(u2);
    }
    @Test
    void updateUser() {
        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser updated = new HogwartsUser();
        updated.setUsername("david");
        updated.setPassword("654321");
        updated.setEnabled(true);
        updated.setRoles("user");

        given(userRepository.findById(2)).willReturn(Optional.of(u2));
        given(userRepository.save(u2)).willReturn(u2);

        HogwartsUser updatedUser = userService.update(2, updated);
        assertThat(updatedUser.getId()).isEqualTo(2);
        assertThat(updatedUser.getUsername()).isEqualTo(updated.getUsername());
        verify(userRepository, times(1)).findById(2);
        verify(userRepository, times(1)).save(u2);
    }
    @Test
    void updateUserNotFound() {
        HogwartsUser updated = new HogwartsUser();
        updated.setUsername("david");
        updated.setPassword("654321");
        updated.setEnabled(true);
        updated.setRoles("user");

        given(userRepository.findById(2)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.update(2, updated));
        verify(userRepository, times(1)).findById(2);
    }
    @Test
    void deleteUser() {
        HogwartsUser updated = new HogwartsUser();
        updated.setId(2);
        updated.setUsername("eric");
        updated.setPassword("654321");
        updated.setEnabled(true);
        updated.setRoles("user");

        given(userRepository.findById(2)).willReturn(Optional.of(updated));
        doNothing().when(userRepository).deleteById(2);
        userService.delete(2);
        verify(userRepository, times(1)).deleteById(2);
    }
    @Test
    void deleteUserNotFound() {
        given(userRepository.findById(5)).willReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> userService.delete(5));
        verify(userRepository, times(1)).findById(5);
    }
}
