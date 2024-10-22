package com.example.JournalApp.Controllers;

import com.example.JournalApp.Entity.JournalEntry;
import com.example.JournalApp.Entity.User;
import com.example.JournalApp.Services.JournalEntryService;
import com.example.JournalApp.Services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<JournalEntry> all = user.getJournalEntries();
        if(all!=null && !all.isEmpty())
        {
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> saveEntry(@RequestBody JournalEntry journalEntry)
    {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            journalEntry.setDate(LocalDateTime.now());

            journalEntryService.saveEntry(journalEntry,username);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);

        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myid}")
    public ResponseEntity<?> getJournalById(@PathVariable ObjectId myid)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries()
                .stream()
                .filter(x->x.getId().equals(myid))
                .toList();
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myid);
            return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("id/{myid}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myid)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean flag = journalEntryService.deleteById(myid,username);
        if(flag)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId id,
                                               @RequestBody JournalEntry journalEntry)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries()
                .stream()
                .filter(x->x.getId().equals(id))
                .toList();
        if(collect.isEmpty()) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JournalEntry myEntry = journalEntryService.findById(id).orElse(null);
        if(myEntry!=null)
        {
            myEntry.setTitle(journalEntry.getTitle()!=null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : myEntry.getTitle());
            myEntry.setContent(journalEntry.getContent()!=null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent():myEntry.getContent());
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry,HttpStatus.OK);
        }
        else
        {
            journalEntryService.saveEntry(journalEntry, username);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }



    }
}
