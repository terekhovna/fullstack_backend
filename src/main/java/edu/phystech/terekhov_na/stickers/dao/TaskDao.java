package edu.phystech.terekhov_na.stickers.dao;

import edu.phystech.terekhov_na.stickers.controllers.TaskController;
import edu.phystech.terekhov_na.stickers.model.TabRepository;
import edu.phystech.terekhov_na.stickers.model.TaskRepository;
import edu.phystech.terekhov_na.stickers.model.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskDao {
    private final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TabRepository tabRepository;
}
