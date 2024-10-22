package com.example.JournalApp.Controllers;

import com.example.JournalApp.Entity.User;
import com.example.JournalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public")
public class PublicController {
    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck()
    {
        return "ok";
    }
    @PostMapping
    public void createUser(@RequestBody User user)
    {
        userService.saveNewEntry(user);
    }
}
