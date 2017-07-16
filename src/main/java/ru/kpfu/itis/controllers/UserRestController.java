package ru.kpfu.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.entities.User;
import ru.kpfu.itis.repository.UserRepository;

import java.util.List;
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

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    @PreAuthorize("isAnonymous()")
    public void addUser(@RequestParam(value="login") String login,@RequestParam(value="name") String name, @RequestParam(value="city") String city) {
        userRepository.save(new User(login, name, city,1, 0));
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
