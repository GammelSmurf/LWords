package org.example.LWords.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.LWords.Entities.AuthRequest;
import org.example.LWords.Entities.NewRecordRequest;
import org.example.LWords.Entities.Record;

import org.example.LWords.Security.MyUserDetails;
import org.example.LWords.Services.RecordService;
import org.example.LWords.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final RecordService recordService;

    @GetMapping
    public Iterable<Record> home(@AuthenticationPrincipal MyUserDetails myUserDetails){
        return recordService.getRecordsByUser(myUserDetails.getUser());
    }

    @PostMapping
    public MessageResponse addRecord(@RequestBody NewRecordRequest newRecordRequest){
        return recordService.addRecord(newRecordRequest.getNewPhrase());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> removeRecord(@PathVariable("id") long id){
        try {
            recordService.removeRecord(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
