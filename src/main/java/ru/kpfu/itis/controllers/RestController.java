package ru.kpfu.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.entities.Authority;
import ru.kpfu.itis.entities.MyTasks;
import ru.kpfu.itis.entities.Tasks;
import ru.kpfu.itis.entities.User;
import ru.kpfu.itis.repository.MyTasksRepository;
import ru.kpfu.itis.repository.TasksRepository;
import ru.kpfu.itis.repository.UserRepository;

import java.util.Iterator;
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
            for (Authority authority : tasks.getCustomer().getAuthorities()) {
                authority.setUsers(null);
            }
        }
        Iterator<MyTasks> iterator = myTasks.iterator();
        while (iterator.hasNext()) {
            MyTasks tasks1 = iterator.next();
                if (!tasks1.getState().equals("0")) {
                    iterator.remove();
            }
        }
        return myTasks;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Set<Tasks> getAllTasks(@RequestParam(value = "city") String city) {
        Set<Tasks> myTasks = this.tasksRepository.findTasksByCity(city);
        for (Tasks tasks : myTasks) {
            tasks.getCustomer().setMyTasks(null);
            for (Authority authority : tasks.getCustomer().getAuthorities()) {
                authority.setUsers(null);
            }

        }
        ;
        return myTasks;
    }

    //TODO сделать эту проверку на стороне клиента и сделать ссылку в myTasks на Tasks
    @RequestMapping(value = "/getAllAndr", method = RequestMethod.GET)
    public Set<Tasks> getAllTasksWithoutRepeat(@RequestParam(value = "city") String city, @RequestParam(value = "login") String login) {
        Set<MyTasks> myTasks = this.userRepository.findUserByLogin(login).getMyTasks();
        Set<Tasks> tasks = this.tasksRepository.findTasksByCity(city);
//        Set<Tasks> copyTasks = tasks;
        Iterator<Tasks> iterator = tasks.iterator();
        while (iterator.hasNext()){
            Tasks tasks1 = iterator.next();
            for (MyTasks myTasks1 : myTasks) {
                if(tasks1.getId().equals(myTasks1.getTaskId())){
                    iterator.remove();
                }
            }

            for (Tasks task : tasks) {
                task.getCustomer().setMyTasks(null);
                task.setUsers(null);
                for (Authority authority : task.getCustomer().getAuthorities()) {
                    authority.setUsers(null);
                }
            }
        }
        return tasks;
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

        if(myTasksRepository.findOneByTaskId(id)== null) {
            myTasksRepository.save(new MyTasks(task.getName(), task.getDescription(), task.getDateFinish(), task.getDifficulty(),
                    customer, "0", user, task.getLatitude(), task.getLongitude(), task.getId(), task.getAddress()));
            return 1;
        } else {
            return  0;
        }



    }

    @RequestMapping(value = "/successTask", method = RequestMethod.GET)
    public Integer successTask(@RequestParam(value = "login") String login, @RequestParam(value = "idTask") Integer id) {
        User user = userRepository.findUserByLogin(login);
        MyTasks myTasks = myTasksRepository.findOne(id);
        switch (myTasks.getDifficulty()) {
            case "HARD": user.setExp(user.getExp()+30);
            case "MIDDLE": user.setExp(user.getExp()+20);
            case "EASY": user.setExp(user.getExp()+10);
        }
        if(user.getLevel()!=user.getExp()/50){
            user.setLevel(user.getExp()/50);
        }
        myTasks.setState("2");
        userRepository.save(user);
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