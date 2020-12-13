package edu.phystech.terekhov_na.stickers.dao;

import edu.phystech.terekhov_na.stickers.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class TaskDao {
    private final TaskRepository taskRepository;
    private final TabRepository tabRepository;

    public Optional<String> deleteTask(Long taskId) {
        return taskRepository.findById(taskId).<Optional<String>>map(task -> {
            Tab tab = tabRepository.getOne(task.getTab().getId());
            tab.getTasks().removeIf(t -> t.getId().equals(taskId));
            tabRepository.save(tab);
            return Optional.empty();
        }).orElse(Optional.of("no such task"));
    }

    public Optional<String> updateTask(Task taskToUpdate) {
        return taskRepository.findById(taskToUpdate.getId()).<Optional<String>>map(task -> {
            taskToUpdate.setTab(task.getTab());
            taskRepository.save(task);
            return Optional.empty();
        }).orElse(Optional.of("no such task"));
    }
}
