package edu.phystech.terekhov_na.stickers.dao;

import edu.phystech.terekhov_na.stickers.controllers.TaskController;
import edu.phystech.terekhov_na.stickers.model.*;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TabDao {
    private final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TabRepository tabRepository;

    @Transactional
    public void addTask(Tab tab, Task task) {
        tab = tabRepository.getOne(tab.getId());
        var tasks = tab.getTasks();
        if(tasks == null) {
            tab.setTasks(new ArrayList<>());
            tasks = tab.getTasks();
        }
        task.setTab(tab);
        tasks.add(task);
        tabRepository.save(tab);
    }
}
