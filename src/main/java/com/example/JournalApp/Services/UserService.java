package com.example.JournalApp.Services;

import com.example.JournalApp.Entity.JournalEntry;
import com.example.JournalApp.Entity.User;
import com.example.JournalApp.Repository.JournalEntryRepository;
import com.example.JournalApp.Repository.UserRepository;
import com.example.JournalApp.Repository.UserRepositoryImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveNewEntry(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepository.save(user);
    }

    public void saveEntry(User user)
    {
        userRepository.save(user);
    }

    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id)
    {
        return userRepository.findById(id);
    }

    public boolean deleteById(ObjectId id)
    {
        userRepository.deleteById(id);
        return true;
    }

    public User findByUserName(String username){

        return userRepository.findByUsername(username);
    }

    public void deleteByUsername(String username)
    {
        userRepository.deleteByUsername(username);
    }

    public void saveAdmin(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER","ADMIN"));
        userRepository.save(user);

    }

    public List<User> findByAllUserName(String username){

        return userRepositoryImpl.getUsers(username);
    }


}
