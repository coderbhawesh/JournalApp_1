package com.example.JournalApp.Repository;

import com.example.JournalApp.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;
    public List<User> getUsers(String username)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));

        return mongoTemplate.find(query,User.class);

    }
}
