package com.example.JournalApp.Controllers;

import com.example.JournalApp.Entity.User;
import com.example.JournalApp.Services.UserService;
import com.example.JournalApp.Services.WeatherService;
import com.example.JournalApp.api.response.WeatherResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;
    //to get all the users in User document
//    @GetMapping()
//    public List<User> getAllUser(){
//        return userService.getAll();
//    }
//
//    @PostMapping
//    public void createUser(@RequestBody @Valid User user)
//    {
//        userService.saveEntry(user);
//    }
    @GetMapping
    public ResponseEntity<?> greetings()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
       WeatherResponse weatherResponse = weatherService.getWeather("Kolkata");
       String greeting = " ";
       if(weatherResponse!=null)
       {
           int temp = weatherResponse.getCurrent().getTemperature();
           greeting+="Today's temperature is ";
           greeting+=Integer.toString(temp);

       }
        return  new ResponseEntity<>("Hi "+username+greeting,HttpStatus.OK);
    }

    @GetMapping("/all/{username}")
    public List<User> getAllUser(@PathVariable String username)
    {
        return userService.findByAllUserName(username);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUserName(username);
        if(userInDb!=null)
        {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.saveNewEntry(userInDb);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userService.deleteByUsername(username);


        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }










}
