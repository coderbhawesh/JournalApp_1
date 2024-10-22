package com.example.JournalApp.Services;

import com.example.JournalApp.Entity.JournalEntry;
import com.example.JournalApp.Entity.User;
import com.example.JournalApp.Repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;
    @Transactional
    public void saveEntry(JournalEntry journalEntity, String username)
    {
        try {
            User user = userService.findByUserName(username);
            JournalEntry saved = journalEntryRepository.save(journalEntity);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        }
        catch (Exception e)
        {
            log.error("Error occurred");
            throw new RuntimeException("Error occurred in saveEntry of JournalEntry Service",e);
        }

    }

    public void saveEntry(JournalEntry journalEntity)
    {

        journalEntryRepository.save(journalEntity);

    }



    public List<JournalEntry> getAll()
    {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id)
    {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username)
    {

        try {
            User user = userService.findByUserName(username);
            boolean flag = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            userService.saveEntry(user);
            if (flag) journalEntryRepository.deleteById(id);
            return flag;
        }
        catch (Exception e)
        {
            System.out.print(e);
            throw new RuntimeException("An error occured n deleteById method ",e);
        }


    }
}
