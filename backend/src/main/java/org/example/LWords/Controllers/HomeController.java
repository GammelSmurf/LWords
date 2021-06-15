package org.example.LWords.Controllers;

import org.example.LWords.Models.Record;
import org.example.LWords.Models.Greeting;
import org.example.LWords.repos.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private RecordRepository recordRepository;

    @GetMapping
    public Iterable<Record> hello(){
        return recordRepository.findAll();
    }
}
