package edu.phystech.terekhov_na.stickers;

import edu.phystech.terekhov_na.stickers.model.TabRepository;
import edu.phystech.terekhov_na.stickers.model.TaskRepository;
import edu.phystech.terekhov_na.stickers.model.UserDataRepository;
import edu.phystech.terekhov_na.stickers.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final TabRepository tabRepository;
    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public void run(String... strings) {
        taskRepository.findAll().forEach(System.out::println);
        tabRepository.findAll().forEach(System.out::println);
        userDataRepository.findAll().forEach(System.out::println);
        userRepository.findAll().forEach(System.out::println);
    }
}
