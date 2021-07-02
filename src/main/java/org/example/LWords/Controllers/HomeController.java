package org.example.LWords.Controllers;

import org.example.LWords.Entities.Record;

import org.example.LWords.Security.MyUserDetails;
import org.example.LWords.Services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private RecordService recordService;

    @GetMapping
    public Iterable<Record> home(@AuthenticationPrincipal MyUserDetails myUserDetails){
        return recordService.getRecordsByUser(myUserDetails.getUser());
    }
}
