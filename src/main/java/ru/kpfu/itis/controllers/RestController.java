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

import java.util.HashSet;
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
        Set<Tasks> myTasks = this.tasksRepository.findTasksByCity(city);
        return myTasks;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Integer addTask(@RequestParam(value = "address") String address, @RequestParam(value = "login") String loginCustomer,
                           @RequestParam(value = "dateFinish") String dateFinish, @RequestParam(value = "name") String name,
                           @RequestParam(value = "description") String description, @RequestParam(value = "difficulty") String difficulty,
                           @RequestParam(value = "city") String city) {
        tasksRepository.save(new Tasks(name, description, dateFinish, difficulty, address,
                "0", "0", userRepository.findUserByLogin(loginCustomer), city));
        return 1; //TODO доделать 1 - успешно добавилось, 0 - не добавилось
//        return new MyTasks("sadasd","kekek", "12.04.2017", "1", "Tolya" );
    }

    @RequestMapping(value = "/acceptTask", method = RequestMethod.POST)
    public Integer acceptTask(@RequestParam(value = "login") String login, @RequestParam(value = "idTask") Integer id) {
        Tasks task = tasksRepository.findOne(id);
        User user = userRepository.findUserByLogin(login);
        User customer = task.getCustomer();
        myTasksRepository.save(new MyTasks(task.getName(), task.getDescription(), task.getDateFinish(), task.getDifficulty(),
                customer, "0", user));
        return 1;
    }

    @RequestMapping(value = "/successTask", method = RequestMethod.GET)
    public Integer successTask(@RequestParam(value = "login") String login, @RequestParam(value = "idTask") Integer id) {
        MyTasks myTasks = myTasksRepository.findOne(id);
        myTasks.setState("2");
        myTasksRepository.save(myTasks);
        return 1;
    }

    @RequestMapping(value = "/failTask", method = RequestMethod.GET)
    public Integer failTask(@RequestParam(value = "login") String login, @RequestParam(value = "idTask") Integer id) {
        MyTasks myTasks = myTasksRepository.findOne(id);
        myTasks.setState("1");
        myTasksRepository.save(myTasks);
        return 1;
    }

}