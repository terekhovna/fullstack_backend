package edu.phystech.terekhov_na.stickers;

import edu.phystech.terekhov_na.stickers.model.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class Initializer implements CommandLineRunner {

    private final TabRepository tabRepository;
    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public void run(String... strings) {
        Task task = new Task();
//        task.setDeadline(Instant.now());
        task.setWorkHours(5.5);

        UserData userData = UserData.builder()
                .activeTabId(3L)
                .tabs(List.of()).build();
        User user = User.builder().login("admin").email("admin@mail.com")
                .password("key")
                .userData(userData).build();

//        userRepository.saveAndFlush(user);

        taskRepository.findAll().forEach(System.out::println);
        tabRepository.findAll().forEach(System.out::println);
        userDataRepository.findAll().forEach(System.out::println);
        userRepository.findAll().forEach(System.out::println);

//        UserData userData2 = UserData.builder()
//                .activeTab(null).build();
//        user.setUserData(userData2);
//        userRepository.save(user);
//
//        taskRepository.findAll().forEach(System.out::println);
//        tabRepository.findAll().forEach(System.out::println);
//        userDataRepository.findAll().forEach(System.out::println);
//        userRepository.findAll().forEach(System.out::println);
    }
}
