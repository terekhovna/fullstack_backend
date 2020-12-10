package edu.phystech.terekhov_na.stickers.controllers;

import edu.phystech.terekhov_na.stickers.dao.TabDao;
import edu.phystech.terekhov_na.stickers.dao.UserDao;
import edu.phystech.terekhov_na.stickers.model.*;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@AllArgsConstructor
@RequestMapping("/api")
public class TaskController {
    private final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TabRepository tabRepository;
    private final UserDao userDao;
    private final TabDao tabDao;

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasksFromActiveTab(@AuthenticationPrincipal String userId) {
        log.info("Request to get tasks for user {}", userId);
        return userDao.getActiveTab(userId)
                .<ResponseEntity<?>>map(tab -> ResponseEntity.ok(tab.getTasks()))
                .getOrElseGet(ResponseUtils::buildError);
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> addTask(@RequestBody Task task, @AuthenticationPrincipal String userId) {
        log.info("Request to add for user {} task: {}", userId, task);
        return userDao.getActiveTab(userId).<ResponseEntity<?>>map(tab -> {
            tabDao.addTask(tab, task);
            userRepository.findAll().forEach(System.out::println);
            return ResponseUtils.makeOk();
        }).getOrElseGet(ResponseUtils::buildError);
    }

    @PutMapping("/tasks")
    public ResponseEntity<?> updateTask(@RequestBody Task task) {
        log.info("Request to update task: {}", task);
        task.setTab(taskRepository.getOne(task.getId()).getTab());
        Task result = taskRepository.save(task);
        return ResponseUtils.makeOk();
    }

    @DeleteMapping("/tasks")
    public ResponseEntity<?> deleteTask(@RequestBody Task taskToDel) {
        log.info("Delete task: {}", taskToDel);
        Long id = taskToDel.getId();
        return taskRepository.findById(id).<ResponseEntity<?>>map(task -> {
            Tab tab = tabRepository.getOne(task.getTab().getId());
            tab.getTasks().removeIf(t -> t.getId().equals(id));
//            Session session = entityManager.unwrap(Session.class);
//            session.update(tab);
            tabRepository.save(tab);
            return ResponseUtils.makeOk();
        }).orElse(ResponseUtils.buildError("not such a task"));
    }
}
