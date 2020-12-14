package edu.phystech.terekhov_na.stickers.service;

import edu.phystech.terekhov_na.stickers.model.User;
import edu.phystech.terekhov_na.stickers.model.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class NewUserValidatorTest {

    @Test
    public void checkUserLoginExists() {
        var userRep = Mockito.mock(UserRepository.class);
        Mockito.when(userRep.findByLogin("user1")).thenReturn(Optional.of(new User()));
        Mockito.when(userRep.findByEmail("k@mail.ru")).thenReturn(Optional.empty());
        var validator = new NewUserValidator(userRep);
        var result = validator.validateNewUser(User.builder()
                .login("user1")
                .email("k@mail.ru")
                .password("df").build());
        assertThat(result.get(), equalTo("login exists"));
    }

    @Test
    public void checkUserEmailExists() {
        var userRep = Mockito.mock(UserRepository.class);
        Mockito.when(userRep.findByLogin("user1")).thenReturn(Optional.empty());
        Mockito.when(userRep.findByEmail("k@mail.ru")).thenReturn(Optional.of(new User()));
        var validator = new NewUserValidator(userRep);
        var result = validator.validateNewUser(User.builder()
                .login("user1")
                .email("k@mail.ru")
                .password("df").build());
        assertThat(result.get(), equalTo("email exists"));
    }

    @Test
    public void checkCorrectEmail() {
        var userRep = Mockito.mock(UserRepository.class);
        Mockito.when(userRep.findByLogin("user1")).thenReturn(Optional.empty());
        Mockito.when(userRep.findByEmail("k@mail.ru")).thenReturn(Optional.empty());
        var validator = new NewUserValidator(userRep);
        var result = validator.validateNewUser(User.builder()
                .login("user1")
                .email("kmail.ru")
                .password("df").build());
        assertThat(result.get(), equalTo("wrong email address"));

        result = validator.validateNewUser(User.builder()
                .login("user1")
                .email("k@mail.ru")
                .password("df").build());
        assertThat(result.isEmpty(), is(true));
    }
}