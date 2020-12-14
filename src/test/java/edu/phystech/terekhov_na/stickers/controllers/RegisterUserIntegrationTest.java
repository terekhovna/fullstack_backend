package edu.phystech.terekhov_na.stickers.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.phystech.terekhov_na.stickers.model.User;
import edu.phystech.terekhov_na.stickers.model.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class RegisterUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void registrationWorks() throws Exception {
        User user = User.builder().login("userForTest").email("userForTest@mail.ru").password("passkeyword").build();
        userRepository.findByLogin(user.getLogin()).ifPresent(value -> userRepository.deleteById(value.getId()));
        userRepository.findByEmail(user.getEmail()).ifPresent(value -> userRepository.deleteById(value.getId()));

        mockMvc.perform(post("/api/sign_up")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        var userRegistered = userRepository.findByLogin(user.getLogin());
        assertThat(userRegistered.isPresent(), is(true));
        assertThat(userRegistered.get().getEmail(), equalTo(user.getEmail()));

        userRepository.findByLogin(user.getLogin()).ifPresent(value -> userRepository.deleteById(value.getId()));
    }
}
