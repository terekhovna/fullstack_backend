package edu.phystech.terekhov_na.stickers.dao;

import edu.phystech.terekhov_na.stickers.model.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class TabDaoTest {

    @Test
    public void testAddTask() {
        var tabRep = Mockito.mock(TabRepository.class);
        Tab tab = Tab.builder().id(1L).build();
        Mockito.when(tabRep.getOne(1L)).thenReturn(tab);

        var tabDao = new TabDao(tabRep);
        Task task = Task.builder().description("test todos").build();
        tabDao.addTask(tab, task);

        Mockito.verify(tabRep, Mockito.times(1)).save(
                Mockito.argThat(x -> {
                    assertThat(x.getTasks().get(0).getDescription(), equalTo(task.getDescription()));
                    return true;
                })
        );
    }

    @Test
    public void testAddTask2() {
        var tabRep = Mockito.mock(TabRepository.class);
        Tab tab = Tab.builder().id(1L).build();

        var tasks = new ArrayList<Task>();
        tasks.add(Task.builder().description("aaaaaa").build());
        tab.setTasks(tasks);

        Mockito.when(tabRep.getOne(1L)).thenReturn(tab);

        var tabDao = new TabDao(tabRep);
        Task task = Task.builder().description("test todos").build();
        tabDao.addTask(tab, task);

        Mockito.verify(tabRep, Mockito.times(1)).save(
                Mockito.argThat(x -> {
                    assertThat(x.getTasks().stream().anyMatch(t -> t.getDescription().equals(task.getDescription())),
                        is(true));
                    return true;
                })
        );
    }
}
