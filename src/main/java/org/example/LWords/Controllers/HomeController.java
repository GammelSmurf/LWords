package org.example.LWords.Controllers;

import org.example.LWords.Models.Record;

import org.example.LWords.Security.MyUserDetails;
import org.example.LWords.Services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private RecordService recordService;

    @GetMapping
    public Iterable<Record> home(@AuthenticationPrincipal MyUserDetails myUserDetails){
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return recordService.getRecordsByUser(myUserDetails.getUser());
    }
}
