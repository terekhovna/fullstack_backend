package edu.phystech.terekhov_na.stickers.controllers;

import edu.phystech.terekhov_na.stickers.dao.TabDao;
import edu.phystech.terekhov_na.stickers.dao.TaskDao;
import edu.phystech.terekhov_na.stickers.dao.UserDao;
import edu.phystech.terekhov_na.stickers.model.*;
import edu.phystech.terekhov_na.stickers.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {
    private final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final UserDao userDao;
    private final TabDao tabDao;
    private final TaskDao taskDao;

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
            return ResponseUtils.makeOk();
        }).getOrElseGet(ResponseUtils::buildError);
    }

    @PutMapping("/tasks")
    public ResponseEntity<?> updateTask(@RequestBody Task task) {
        log.info("Request to update task: {}", task);
        return ResponseUtils.makeResponse(taskDao.updateTask(task));
    }

    @DeleteMapping("/tasks")
    public ResponseEntity<?> deleteTask(@RequestBody Task taskToDel) {
        log.info("Request to delete task: {}", taskToDel);
        return ResponseUtils.makeResponse(taskDao.deleteTask(taskToDel.getId()));
    }
}
