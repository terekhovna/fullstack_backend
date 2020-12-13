package edu.phystech.terekhov_na.stickers.dao;

import edu.phystech.terekhov_na.stickers.model.Tab;
import edu.phystech.terekhov_na.stickers.model.TabRepository;
import edu.phystech.terekhov_na.stickers.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
@Transactional
@AllArgsConstructor
public class TabDao {
    private final TabRepository tabRepository;

    public void addTask(Tab tab, Task task) {
        task.setId(null);
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
