package ru.kpfu.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.entities.MyTasks;
import ru.kpfu.itis.entities.Tasks;
import ru.kpfu.itis.entities.User;
import ru.kpfu.itis.repository.MyTasksRepository;
import ru.kpfu.itis.repository.TasksRepository;
import ru.kpfu.itis.repository.UserRepository;

import java.util.List;
import java.util.Set;


/**
 * Created by Anatoly on 08.07.2017.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {
    @Autowired
    TasksRepository tasksRepository;
    @Autowired
    MyTasksRepository myTasksRepository;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/getMy", method = RequestMethod.GET)
    public Set<MyTasks> getMyTasks(@RequestParam(value = "login") String login) {
        Set<MyTasks> myTasks = this.userRepository.findUserByLogin(login).getMyTasks();
        for (MyTasks tasks : myTasks) {
            tasks.setUser(null);
            tasks.getCustomer().setMyTasks(null);
        }
        return myTasks;
//        return new MyTasks("sadasd","kekek", "12.04.2017", "1", "Tolya" );
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Set<Tasks> getAllTasks(@RequestParam(value = "city") String city) {
        Set<Tasks> myTasks = this.tasksRepository.findTasksByAddress(city);
        for(Tasks tasks:myTasks){
            tasks.getCustomer().setMyTasks(null);
        }
        return myTasks;
//        return new MyTasks("sadasd","kekek", "12.04.2017", "1", "Tolya" );
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Integer addTask(@RequestParam(value = "address") String address, @RequestParam(value = "login") String loginCustomer,
                           @RequestParam(value = "dateFinish") String dateFinish, @RequestParam(value = "name") String name,
                           @RequestParam(value = "description") String description, @RequestParam(value = "difficulty") String difficulty) {
        tasksRepository.save(new Tasks(name, description, dateFinish, difficulty, address,
                "0", "0", userRepository.findUserByLogin(loginCustomer)));
        return 1; //TODO доделать 1 - успешно добавилось, 0 - не добавилось
//        return new MyTasks("sadasd","kekek", "12.04.2017", "1", "Tolya" );
    }


}
