package com.example.JournalApp.Controllers;

import com.example.JournalApp.Entity.User;
import com.example.JournalApp.Services.UserService;
import com.example.JournalApp.Utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("public")
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwt;



    @GetMapping("/health-check")
    public String healthCheck()
    {
        log.info("Health is ok");
        return "ok";
    }
    @PostMapping("/signup")
    public void createUser(@RequestBody User user)
    {
        userService.saveNewEntry(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody User user)
    {
        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String s = jwt.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(s, HttpStatus.OK);



        }catch(Exception e)
        {
            log.error("Exception occured while creating jwttoken",e);
            return new ResponseEntity<>("Incorrect username or password",HttpStatus.NOT_FOUND);

        }

    }


}
