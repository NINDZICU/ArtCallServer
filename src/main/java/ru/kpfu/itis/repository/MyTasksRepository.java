package ru.kpfu.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.entities.MyTasks;

import java.util.Set;

/**
 * Created by Anatoly on 10.07.2017.
 */
@Repository
public interface MyTasksRepository extends JpaRepository<MyTasks, Integer> {

    MyTasks findOneByTaskId(Integer taskId);
}
