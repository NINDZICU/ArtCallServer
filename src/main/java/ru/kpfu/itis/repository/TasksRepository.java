package ru.kpfu.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.entities.Tasks;

import java.util.Set;


/**
 * Created by Anatoly on 08.07.2017.
 */
@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    Set<Tasks> findTasksByAddress(String address);
}
