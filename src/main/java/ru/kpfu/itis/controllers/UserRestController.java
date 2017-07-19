package ru.kpfu.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.entities.Authority;
import ru.kpfu.itis.entities.MyTasks;
import ru.kpfu.itis.entities.User;
import ru.kpfu.itis.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anatoly on 10.07.2017.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {
    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public Set<User> getFriends(@RequestParam(value = "login") String login){
        return userRepository.findUserByLogin(login).getFriends();
    }

    @RequestMapping(value = "/getByID", method = RequestMethod.GET)
    public User getById(@RequestParam(value = "login") String login){
        User user = userRepository.findUserByLogin(login);
        for(MyTasks myTasks: user.getMyTasks()){
            myTasks.setCustomer(null);
            myTasks.setUser(null);
        }
        for(Authority authority: user.getAuthorities()){
            authority.setUsers(null);
        }
        return user;
    }

    @RequestMapping(value = "/getByListID", method = RequestMethod.GET)
    public Set<User> getByListId(@RequestParam(value = "login") String[] arr){
        Set<User> userSet = new HashSet();
        for (String id: arr) {
            userSet.add(userRepository.findUserByLogin(id));
        }
        return userSet;
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    @PreAuthorize("isAnonymous()")
    public void addUser(@RequestParam(value="login") String login,@RequestParam(value="name") String name, @RequestParam(value="city") String city) {
        if(userRepository.findUserByLogin(login)== null) {
            userRepository.save(new User(login, name, city, 1, 50));
        }
    }
//    @RequestMapping(value = "/aut", method = RequestMethod.POST)
//    @PreAuthorize("isAnonymous()")
//    public Integer autUser(@RequestBody User user){
//        User userFromDatabase = (User) userService.loadUserByUsername(user.getLogin());
//        if(userFromDatabase.getLogin().equals(user.getLogin()) && userFromDatabase.getPassword().equals(user.getPassword())){
//            return 1;
//        }
//        else return 0;
//    }
}
